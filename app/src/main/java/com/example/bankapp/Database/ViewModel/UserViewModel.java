package com.example.bankapp.Database.ViewModel;

import android.app.Activity;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.example.bankapp.Database.AppDatabase;
import com.example.bankapp.Database.DAO.NotificationDAO;
import com.example.bankapp.Database.DAO.TransactionDAO;
import com.example.bankapp.Database.DAO.UserDAO;
import com.example.bankapp.Database.Room.Notification;
import com.example.bankapp.Database.Room.Transaction;
import com.example.bankapp.Database.Room.User;
import com.example.bankapp.Database.SharedPreferences.AdminPreferences;
import com.example.bankapp.R;

import java.util.concurrent.Executors;

public class UserViewModel extends AppViewModel {

    private final UserDAO userDAO;
    private final TransactionDAO transactionDAO;
    private final NotificationDAO notificationDAO;


    public UserViewModel(@NonNull Application application) {
        super(application);
        userDAO = AppDatabase.getDatabase(application).getUserDAO();
        transactionDAO = AppDatabase.getDatabase(application).getTransactionDAO();
        notificationDAO = AppDatabase.getDatabase(application).getNotificationDAO();
    }

//    public void mockDate(Activity activity, @NonNull CompletionListener<Void> listener){
//        Executors.newSingleThreadExecutor().execute(() -> {
//            try {
//                userDAO.mockData();
//                CompletionListener.dispatchSuccessOnUIThread(activity, listener);
//            }catch (Throwable throwable){
//                CompletionListener.dispatchErrorOnUIThread(activity, listener, throwable);
//            }
//        });
//    }


    public void addOrDeduceMoney(Activity activity, User user, double amt, @NonNull CompletionListener<User> listener){
        Executors.newSingleThreadExecutor().execute(() -> {
            User receiver = userDAO.findByAccount(user.getAccountNo());
            if (receiver == null) {
                CompletionListener.dispatchErrorOnUIThread(activity, listener, new Exception("Receiver account not valid"));
                return;
            }
            try {
                double newBsr = user.getBsrBal() + amt;

                if(newBsr<200){
                    CompletionListener.dispatchErrorOnUIThread(activity, listener, new Exception("Insufficient Balance"));
                    return;
                }

                user.setBsrBal(newBsr);
                AdminPreferences.getInstance().setBal(AdminPreferences.getInstance().getBal() + amt);
                Transaction transaction = new Transaction();
                if(amt>0){
                    transaction.setSender(user.getId());
                    transaction.setReceiver("ADMIN");
                    transaction.setSenderName(user.getName());
                    transaction.setSenderAccNo(user.getAccountNo());
                    transaction.setReceiverName("ADMIN");
                    transaction.setReceiverAccNo("ADMIN");
                    transaction.setAmount(amt);
                }else {
                    transaction.setSender("ADMIN");
                    transaction.setReceiverName(user.getName());
                    transaction.setSenderName("ADMIN");
                    transaction.setReceiver(user.getId());
                    transaction.setReceiverAccNo(user.getAccountNo());
                    transaction.setSenderAccNo("ADMIN");
                    transaction.setAmount(amt);
                }
                transaction.setSuccessful(true);

                transactionDAO.createOrUpdate(transaction);
                userDAO.createOrUpdate(user);

                String transactionInfo = amt>0?activity.getString(R.string.new_money_reception):activity.getString(R.string.new_money_deduction);
                Notification notification = new Notification(transactionInfo, user.getId(), Notification.TYPE_TRANSACTION);
                notification.setReference(transaction.getId());
                notificationDAO.createOrUpdate(notification);

                CompletionListener.dispatchSuccessOnUIThread(activity, listener, user);
            }catch (Throwable throwable){
                CompletionListener.dispatchErrorOnUIThread(activity, listener, throwable);
            }
        });
    }

    public void sendMoney(Activity activity, User user, String acc, String amt, @NonNull CompletionListener<User> listener){
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                User receiver = userDAO.findByAccount(acc);
                if (receiver == null) {
                    CompletionListener.dispatchErrorOnUIThread(activity, listener, new Exception("Receiver account not valid"));
                    return;
                }
                receiver.setBsrBal(receiver.getBsrBal() + Double.parseDouble(amt));
                user.setBsrBal(user.getBsrBal() - Double.parseDouble(amt) - 0.5);
                AdminPreferences.getInstance().setBal(AdminPreferences.getInstance().getBal() + 0.5);
                Transaction transaction = new Transaction();
                transaction.setAmount(Double.parseDouble(amt));
                transaction.setReceiver(receiver.getId());
                transaction.setSender(user.getId());
                transaction.setSuccessful(true);

                transaction.setSenderAccNo(user.getAccountNo());
                transaction.setReceiverAccNo(receiver.getAccountNo());
                transaction.setSenderName(user.getName());
                transaction.setReceiverName(receiver.getName());

                transactionDAO.createOrUpdate(transaction);
                userDAO.createOrUpdate(user);
                userDAO.createOrUpdate(receiver);

                Notification notification = new Notification(activity.getString(R.string.new_money_reception), receiver.getId(), Notification.TYPE_TRANSACTION);
                notification.setReference(transaction.getId());
                notificationDAO.createOrUpdate(notification);

                CompletionListener.dispatchSuccessOnUIThread(activity, listener, user);
            }catch (Throwable throwable){
                CompletionListener.dispatchErrorOnUIThread(activity, listener, throwable);
            }
        });
    }

    public void userByEmail(Activity activity, String email, @NonNull CompletionListener<User> listener){
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                User user = userDAO.findByEmail(email);
                CompletionListener.dispatchSuccessOnUIThread(activity, listener, user);
            }catch (Throwable throwable){
                CompletionListener.dispatchErrorOnUIThread(activity, listener, throwable);
            }
        });
    }

    public void userByAccount(Activity activity, String acc, @NonNull CompletionListener<User> listener){
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                User user = userDAO.findByAccount(acc);
                CompletionListener.dispatchSuccessOnUIThread(activity, listener, user);
            }catch (Throwable throwable){
                CompletionListener.dispatchErrorOnUIThread(activity, listener, throwable);
            }
        });
    }

    public void createOrUpdate(Activity activity, User user, @NonNull CompletionListener<Void> listener){
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                userDAO.createOrUpdate(user);
                CompletionListener.dispatchSuccessOnUIThread(activity, listener);
            }catch (Throwable throwable){
                CompletionListener.dispatchErrorOnUIThread(activity, listener, throwable);
            }
        });
    }

    public UserDAO getUserDAO() {
        return userDAO;
    }

    public static UserViewModel of(Activity activity){
        return new ViewModelProvider.AndroidViewModelFactory(activity.getApplication()).create(UserViewModel.class);
    }
}
