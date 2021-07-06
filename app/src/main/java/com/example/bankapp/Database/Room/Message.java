package com.example.bankapp.Database.Room;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Entity(tableName = "messages")
public class Message {

    @NonNull
    @PrimaryKey
    private String id = UUID.randomUUID().toString();

    @NonNull
    private String content;

    @NonNull
    private String chatId;

    @NonNull
    private String sentBy;

    @NonNull
    private boolean read = false;


    @NonNull
    private Date createdDate = new Date();


    public Message() {
    }

    @Ignore
    public Message(@NonNull String chatId, @NonNull String content, @NonNull String sentBy) {
        this.content = content;
        this.sentBy = sentBy;
        this.chatId = chatId;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    @NonNull
    public String getContent() {
        return content;
    }

    public void setContent(@NonNull String content) {
        this.content = content;
    }

    @NonNull
    public String getSentBy() {
        return sentBy;
    }

    public void setSentBy(@NonNull String sentBy) {
        this.sentBy = sentBy;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    @NonNull
    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(@NonNull Date createdDate) {
        this.createdDate = createdDate;
    }

    @NonNull
    public String getChatId() {
        return chatId;
    }

    public void setChatId(@NonNull String chatId) {
        this.chatId = chatId;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id='" + id + '\'' +
                ", content='" + content + '\'' +
                ", chatId='" + chatId + '\'' +
                ", sentBy='" + sentBy + '\'' +
                ", read1=" + read +
                ", createdDate=" + createdDate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return read == message.read &&
                id.equals(message.id) &&
                content.equals(message.content) &&
                chatId.equals(message.chatId) &&
                sentBy.equals(message.sentBy) &&
                createdDate.equals(message.createdDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, content, chatId, sentBy, read, createdDate);
    }

    public static final DiffUtil.ItemCallback<Message> DIFF_UTIL = new DiffUtil.ItemCallback<Message>() {
        @Override
        public boolean areItemsTheSame(@NonNull Message oldItem, @NonNull Message newItem) {
            return oldItem.id.equals(newItem.id);
        }

        @Override
        public boolean areContentsTheSame(@NonNull Message oldItem, @NonNull Message newItem) {
            return oldItem.equals(newItem);
        }
    };
}
