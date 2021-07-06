package com.example.bankapp.Database.ViewModel;

import android.app.Activity;
import android.app.Application;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.example.bankapp.Database.AppDatabase;
import com.example.bankapp.Database.DAO.ChatDAO;
import com.example.bankapp.Database.DAO.InsuranceDataDAO;
import com.example.bankapp.Database.DAO.MessageDAO;
import com.example.bankapp.Database.DAO.NotificationDAO;
import com.example.bankapp.Database.DAO.UserDAO;
import com.example.bankapp.Database.Room.Chat;
import com.example.bankapp.Database.Room.InsuranceData;
import com.example.bankapp.Database.Room.Message;
import com.example.bankapp.Database.Room.Notification;
import com.example.bankapp.Database.Room.User;
import com.example.bankapp.R;

import java.util.concurrent.Executors;

public class InsuranceDataViewModel extends AppViewModel {

    private final InsuranceDataDAO insuranceDataDAO;
    private final UserDAO userDAO;
    private final NotificationDAO notificationDAO;
    private final ChatDAO chatDAO;
    private final MessageDAO messageDAO;


    public InsuranceDataViewModel(@NonNull Application application) {
        super(application);
        userDAO = AppDatabase.getDatabase(application).getUserDAO();
        insuranceDataDAO = AppDatabase.getDatabase(application).getInsuranceDataDAO();
        notificationDAO = AppDatabase.getDatabase(application).getNotificationDAO();
        chatDAO = AppDatabase.getDatabase(application).getChatDAO();
        messageDAO = AppDatabase.getDatabase(application).getMessageDAO();
    }

    public InsuranceDataDAO getInsuranceDataDAO() {
        return insuranceDataDAO;
    }

    public static InsuranceDataViewModel of(Activity activity){
        return new ViewModelProvider.AndroidViewModelFactory(activity.getApplication()).create(InsuranceDataViewModel.class);
    }

    public void applyInsurance(Activity activity, String agentId, String email, String insuranceAmt, String value, String nominee, CompletionListener<Pair<User, InsuranceData>> listener) {
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                User user = userDAO.findByEmail(email);


                double bsrBal = user.getBsrBal();

                if (Double.parseDouble(insuranceAmt) > bsrBal) {
                    CompletionListener.dispatchErrorOnUIThread(activity, listener, new Exception("Insufficient Balance"));
                    return;
                }

                user.setBsrBal(user.getBsrBal() - Double.parseDouble(insuranceAmt));
                InsuranceData insuranceData = new InsuranceData();
                insuranceData.setInsuranceAmt(Double.parseDouble(insuranceAmt));
                insuranceData.setInsuranceType(value);
                insuranceData.setNominee(nominee);
                insuranceData.setUserId(user.getId());
                insuranceData.setAgentId(agentId);

                insuranceDataDAO.createOrUpdate(insuranceData);
                userDAO.createOrUpdate(user);

                Notification notification = new Notification(activity.getString(R.string.new_insurance_application), insuranceData.getAgentId(), Notification.TYPE_INSURANCE);
                notification.setReference(insuranceData.getId());
                notificationDAO.createOrUpdate(notification);

                CompletionListener.dispatchSuccessOnUIThread(activity, listener, Pair.create(user, insuranceData));
            }catch (Throwable throwable){
                CompletionListener.dispatchErrorOnUIThread(activity, listener, throwable);
            }
        });
    }

    public void update(Activity activity, InsuranceData item, CompletionListener<InsuranceData> listener) {
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                insuranceDataDAO.createOrUpdate(item);
                if(item.getState() == InsuranceData.STATE_REJECTED){
                    Notification notification = new Notification(activity.getString(R.string.insurance_application_rejected), item.getUserId(), Notification.TYPE_INSURANCE);
                    notification.setReference(item.getId());
                    notificationDAO.createOrUpdate(notification);
                }
                CompletionListener.dispatchSuccessOnUIThread(activity, listener, item);
            }catch (Throwable throwable){
                CompletionListener.dispatchErrorOnUIThread(activity, listener, throwable);
            }
        });
    }

    public void approve(Activity activity, String id, int value, CompletionListener<Integer> listener) {
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                int returnVal = insuranceDataDAO.approve(id, value);

                if(returnVal == InsuranceDataDAO.RESPONSE_CODE_SUCCESS){
                    InsuranceData item = insuranceDataDAO._findById(id);
                    Notification notification = new Notification(activity.getString(R.string.insurance_application_approved), item.getUserId(), Notification.TYPE_INSURANCE);
                    notification.setReference(item.getId());
                    notificationDAO.createOrUpdate(notification);

                    Notification notification2 = new Notification(activity.getString(R.string.insurance_application_commission), item.getAgentId(), Notification.TYPE_INSURANCE);
                    notification2.setReference(item.getId());
                    notificationDAO.createOrUpdate(notification2);
                }

                CompletionListener.dispatchSuccessOnUIThread(activity, listener, returnVal);
            }catch (Throwable throwable){
                CompletionListener.dispatchErrorOnUIThread(activity, listener, throwable);
            }
        });
    }

    public void inquiryApplication(Activity activity, InsuranceData insuranceData, CompletionListener<Chat> listener) {
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                String chatId = insuranceData.getChatId();
                Chat chat;
                if(chatId==null){
                    chat = new Chat("I", insuranceData.getUserId(), insuranceData.getAgentId());
                    chatId = chat.getId();
                    Message message = new Message(chatId, activity.getString(R.string.insurance_inquiry_request, insuranceData.getId()), insuranceData.getUserId());
                    insuranceData.setChatId(chatId);

                    chatDAO.createOrUpdate(chat);
                    messageDAO.createOrUpdate(message);
                    insuranceDataDAO.createOrUpdate(insuranceData);
                }else {
                    chat = chatDAO.findById(chatId);
                }
                CompletionListener.dispatchSuccessOnUIThread(activity, listener, chat);
            }catch (Throwable throwable){
                CompletionListener.dispatchErrorOnUIThread(activity, listener, throwable);
            }
        });
    }
}
