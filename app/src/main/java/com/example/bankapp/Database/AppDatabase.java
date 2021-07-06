package com.example.bankapp.Database;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.bankapp.Database.DAO.ChatDAO;
import com.example.bankapp.Database.DAO.InsuranceDataDAO;
import com.example.bankapp.Database.DAO.LoanDataDAO;
import com.example.bankapp.Database.DAO.MessageDAO;
import com.example.bankapp.Database.DAO.NotificationDAO;
import com.example.bankapp.Database.DAO.TransactionDAO;
import com.example.bankapp.Database.DAO.UserDAO;
import com.example.bankapp.Database.Room.Chat;
import com.example.bankapp.Database.Room.InsuranceData;
import com.example.bankapp.Database.Room.LoanData;
import com.example.bankapp.Database.Room.Message;
import com.example.bankapp.Database.Room.Notification;
import com.example.bankapp.Database.Room.Transaction;
import com.example.bankapp.Database.Room.User;

@androidx.room.Database(entities = {
        User.class,
        InsuranceData.class,
        LoanData.class,
        Transaction.class,
        Notification.class,
        Chat.class,
        Message.class
}, version = 1, exportSchema = false)
@TypeConverters(RoomConverter.class)
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase appDatabaseInstance;

    public static AppDatabase getDatabase(final Context context) {
        if (appDatabaseInstance == null) {
            synchronized (AppDatabase.class) {
                if (appDatabaseInstance == null) {
                    appDatabaseInstance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "database")
                            //.addTypeConverter(roomConverter)
                            .fallbackToDestructiveMigration()
                            .fallbackToDestructiveMigrationOnDowngrade()
                            .build();
                }
            }
        }
        return appDatabaseInstance;
    }

    public abstract UserDAO getUserDAO();

    public abstract TransactionDAO getTransactionDAO();

    public abstract LoanDataDAO getLoanDataDAO();

    public abstract InsuranceDataDAO getInsuranceDataDAO();


    public abstract NotificationDAO getNotificationDAO();

    public abstract MessageDAO getMessageDAO();

    public abstract ChatDAO getChatDAO();
}
