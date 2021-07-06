package com.example.bankapp.Database.DAO;


import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.bankapp.Database.Room.Notification;


@Dao
public abstract class NotificationDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract Long createOrUpdate(Notification notification);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    public abstract void update(Notification notification);

    @Query("select * from notifications where userId = :userId order by read")
    public abstract DataSource.Factory<Integer, Notification> paginateByUserId(String userId);

    @Query("select count(*) from notifications")
    public abstract int count();

    @Query("select count(*) from notifications where not read and userId = :userId")
    public abstract LiveData<Integer> countNotReadByUserId(String userId);


    @Query("update notifications set read = 1")
    public abstract void markAllAsRead();
}
