package com.example.bankapp.Database.DAO;


import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.bankapp.Database.Room.Chat;


@Dao
public abstract class ChatDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void createOrUpdate(Chat chat);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    public abstract void update(Chat chat);

    @Query("select * from chats where id=:chatId")
    public abstract Chat findById(String chatId);

    @Query("select id, iconText, (select content from messages where chatId=c.id order by createdDate desc limit 1) as content, user1Id, user2Id, createdDate, (select count(*) from messages where chatId=c.id and not read and not sentBy=:userId) as messageCount from chats c where user1Id=:userId or user2Id=:userId")
    public abstract DataSource.Factory<Integer, Chat> paginateByUserId(String userId);

    @Query("select count(*) from chats")
    public abstract int count();

}
