package com.example.bankapp.Database.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.bankapp.Database.Room.Transaction;

import java.util.List;

@Dao
public abstract class TransactionDAO {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void createOrUpdate(Transaction data);

    @Query("select * from transactions where sender=:id or receiver=:id")
    public abstract LiveData<List<Transaction>> findBySenderReceiver(String id);

    @Query("select * from transactions where receiver=:id")
    public abstract LiveData<List<Transaction>> findByReceiver(String id);



//    @Query("select * from `Transaction` where receiver=id")
//    public abstract ArrayList<Transaction> findByReceiver(String id) ;
}
