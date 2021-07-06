package com.example.bankapp.Database.ViewModel;

import android.app.Activity;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.example.bankapp.Database.AppDatabase;
import com.example.bankapp.Database.DAO.TransactionDAO;
import com.google.firebase.database.FirebaseDatabase;

public class TransactionViewModel extends AppViewModel {

    private final TransactionDAO transactionDAO;


    public TransactionViewModel(@NonNull Application application) {
        super(application);
        transactionDAO = AppDatabase.getDatabase(application).getTransactionDAO();

    }

    public static TransactionViewModel of(Activity activity){
        return new ViewModelProvider.AndroidViewModelFactory(activity.getApplication()).create(TransactionViewModel.class);
    }
}
