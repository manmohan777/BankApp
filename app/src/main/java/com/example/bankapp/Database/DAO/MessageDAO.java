package com.example.bankapp.Database.DAO;


import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.bankapp.Database.Room.Message;


@Dao
public abstract class MessageDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract Long createOrUpdate(Message message);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    public abstract void update(Message message);

    @Query("select * from messages where chatId=:chatId")
    public abstract DataSource.Factory<Integer, Message> paginateByChat(String chatId);

    @Query("select * from messages")
    public abstract DataSource.Factory<Integer, Message> paginateChat();

    @Query("select count(*) from messages")
    public abstract int count();

    @Query("update messages set read = 1 where chatId=:chatId and not sentBy =:userId")
    public abstract void markAllAsRead(String chatId, String userId);
}
