package com.example.bankapp.Database.ViewModel;

import android.app.Activity;
import android.app.Application;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.example.bankapp.Database.AppDatabase;
import com.example.bankapp.Database.DAO.ChatDAO;
import com.example.bankapp.Database.DAO.LoanDataDAO;
import com.example.bankapp.Database.DAO.MessageDAO;
import com.example.bankapp.Database.DAO.NotificationDAO;
import com.example.bankapp.Database.DAO.UserDAO;
import com.example.bankapp.Database.Room.Chat;
import com.example.bankapp.Database.Room.LoanData;
import com.example.bankapp.Database.Room.Message;
import com.example.bankapp.Database.Room.Notification;
import com.example.bankapp.Database.Room.User;
import com.example.bankapp.R;

import java.util.concurrent.Executors;

public class LoanDataViewModel extends AppViewModel {

    private final LoanDataDAO loanDataDAO;
    private final UserDAO userDAO;
    private final NotificationDAO notificationDAO;
    private final ChatDAO chatDAO;
    private final MessageDAO messageDAO;

    public LoanDataViewModel(@NonNull Application application) {
        super(application);
        userDAO = AppDatabase.getDatabase(application).getUserDAO();
        loanDataDAO = AppDatabase.getDatabase(application).getLoanDataDAO();
        notificationDAO = AppDatabase.getDatabase(application).getNotificationDAO();
        chatDAO = AppDatabase.getDatabase(application).getChatDAO();
        messageDAO = AppDatabase.getDatabase(application).getMessageDAO();
    }

    public LoanDataDAO getLoanDataDAO() {
        return loanDataDAO;
    }

    public static LoanDataViewModel of(Activity activity){
        return new ViewModelProvider.AndroidViewModelFactory(activity.getApplication()).create(LoanDataViewModel.class);
    }

    public void applyLoan(Activity activity, String agentId, String email, String loanAmt, String value, String period, CompletionListener<Pair<User, LoanData>> listener) {
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                User user = userDAO.findByEmail(email);


                /*
                double bsrBal = user.getBsrBal();

                if (Double.parseDouble(loanAmt) > bsrBal) {
                    CompletionListener.dispatchErrorOnUIThread(activity, listener, new Exception("Insufficient Balance"));
                    return;
                }

                //*/

                //user.setBsrBal(user.getBsrBal() + Double.parseDouble(loanAmt));
                LoanData loanData = new LoanData();
                loanData.setLoanAmt(Double.parseDouble(loanAmt));
                loanData.setLoanType(value);
                loanData.setPeriod(Integer.parseInt(period));
                loanData.setUserId(user.getId());
                loanData.setAgentId(agentId);
                loanData.setState(LoanData.STATE_APPLIED);

                loanDataDAO.createOrUpdate(loanData);
                userDAO.createOrUpdate(user);

                Notification notification = new Notification(activity.getString(R.string.new_loan_application), loanData.getAgentId(), Notification.TYPE_LOAN);
                notification.setReference(loanData.getId());
                notificationDAO.createOrUpdate(notification);

                CompletionListener.dispatchSuccessOnUIThread(activity, listener, Pair.create(user, loanData));
            }catch (Throwable throwable){
                CompletionListener.dispatchErrorOnUIThread(activity, listener, throwable);
            }
        });
    }

    public void update(Activity activity, LoanData item, CompletionListener<LoanData> listener) {
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                loanDataDAO.createOrUpdate(item);
                if(item.getState() == LoanData.STATE_REJECTED){
                    Notification notification = new Notification(activity.getString(R.string.loan_application_rejected), item.getUserId(), Notification.TYPE_INSURANCE);
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
                int returnVal = loanDataDAO.approve(id, value);

                if(returnVal == LoanDataDAO.RESPONSE_CODE_SUCCESS){
                    LoanData item = loanDataDAO._findById(id);
                    Notification notification = new Notification(activity.getString(R.string.loan_application_approved), item.getUserId(), Notification.TYPE_INSURANCE);
                    notification.setReference(item.getId());
                    notificationDAO.createOrUpdate(notification);

                    Notification notification2 = new Notification(activity.getString(R.string.loan_application_commission), item.getAgentId(), Notification.TYPE_INSURANCE);
                    notification2.setReference(item.getId());
                    notificationDAO.createOrUpdate(notification2);
                }

                CompletionListener.dispatchSuccessOnUIThread(activity, listener, returnVal);
            }catch (Throwable throwable){
                CompletionListener.dispatchErrorOnUIThread(activity, listener, throwable);
            }
        });
    }

    public void inquiryApplication(Activity activity, LoanData loanData, CompletionListener<Chat> listener) {
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                String chatId = loanData.getChatId();
                Chat chat;
                if(chatId==null){
                    chat = new Chat("L", loanData.getUserId(), loanData.getAgentId());
                    chatId = chat.getId();
                    Message message = new Message(chatId, activity.getString(R.string.loan_inquiry_request, loanData.getId()), loanData.getUserId());
                    loanData.setChatId(chatId);

                    chatDAO.createOrUpdate(chat);
                    messageDAO.createOrUpdate(message);
                    loanDataDAO.createOrUpdate(loanData);
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
