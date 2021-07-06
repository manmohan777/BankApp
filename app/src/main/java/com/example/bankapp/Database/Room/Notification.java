package com.example.bankapp.Database.Room;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Entity(tableName = "notifications")
public class Notification {

    public static final int TYPE_MESSAGE = 0;
    public static final int TYPE_LOAN = 1;
    public static final int TYPE_INSURANCE = 2;
    public static final int TYPE_TRANSACTION = 5;

    @NonNull
    @PrimaryKey
    private String id = UUID.randomUUID().toString();

    @NonNull
    private String content;

    @NonNull
    private String userId;

    @NonNull
    private String reference;


    @NonNull
    private boolean read = false;

    @NonNull
    private Date createdDate = new Date();

    int notificationType;

    public Notification() {
    }

    @Ignore
    public Notification(@NonNull String content, @NonNull String userId, int notificationType) {
        this.content = content;
        this.userId = userId;
        this.notificationType = notificationType;
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
    public String getUserId() {
        return userId;
    }

    public void setUserId(@NonNull String userId) {
        this.userId = userId;
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

    public int getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(int notificationType) {
        this.notificationType = notificationType;
    }

    @NonNull
    public String getReference() {
        return reference;
    }

    public void setReference(@NonNull String reference) {
        this.reference = reference;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "id='" + id + '\'' +
                ", content='" + content + '\'' +
                ", userId='" + userId + '\'' +
                ", resourceId='" + reference + '\'' +
                ", read=" + read +
                ", createdDate=" + createdDate +
                ", notificationType=" + notificationType +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Notification that = (Notification) o;
        return read == that.read &&
                notificationType == that.notificationType &&
                id.equals(that.id) &&
                content.equals(that.content) &&
                userId.equals(that.userId) &&
                reference.equals(that.reference) &&
                createdDate.equals(that.createdDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, content, userId, reference, read, createdDate, notificationType);
    }

    public static Notification of(String content, @NonNull String userId, int type){
        return new Notification(content, userId, type);
    }

    public static final DiffUtil.ItemCallback<Notification> DIFF_UTIL = new DiffUtil.ItemCallback<Notification>() {
        @Override
        public boolean areItemsTheSame(@NonNull Notification oldItem, @NonNull Notification newItem) {
            return oldItem.id.equals(newItem.id);
        }

        @Override
        public boolean areContentsTheSame(@NonNull Notification oldItem, @NonNull Notification newItem) {
            return oldItem.equals(newItem);
        }
    };
}
