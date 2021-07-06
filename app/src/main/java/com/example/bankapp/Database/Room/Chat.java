package com.example.bankapp.Database.Room;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Entity(tableName = "chats")
public class Chat {

    @NonNull
    @PrimaryKey
    private String id = UUID.randomUUID().toString();

    @Nullable
    private String content;

    @NonNull
    private String user1Id;

    @NonNull
    private String iconText;

    @NonNull
    private String user2Id;

    @NonNull
    private Date createdDate = new Date();

    private int messageCount;

    public Chat() {
    }

    @Ignore
    public Chat(@NonNull String iconText, @NonNull String user1Id, @NonNull String user2Id) {
        this.iconText = iconText;
        this.user1Id = user1Id;
        this.user2Id = user2Id;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    @Nullable
    public String getContent() {
        return content;
    }

    public void setContent(@Nullable String content) {
        this.content = content;
    }

    @NonNull
    public String getUser1Id() {
        return user1Id;
    }

    public void setUser1Id(@NonNull String user1Id) {
        this.user1Id = user1Id;
    }

    @NonNull
    public String getUser2Id() {
        return user2Id;
    }

    public void setUser2Id(@NonNull String user2Id) {
        this.user2Id = user2Id;
    }

    @NonNull
    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(@NonNull Date createdDate) {
        this.createdDate = createdDate;
    }

    public int getMessageCount() {
        return messageCount;
    }

    public void setMessageCount(int messageCount) {
        this.messageCount = messageCount;
    }

    @NonNull
    public String getIconText() {
        return iconText;
    }

    public void setIconText(@NonNull String iconText) {
        this.iconText = iconText;
    }

    @Override
    public String toString() {
        return "Chat{" +
                "id='" + id + '\'' +
                ", content='" + content + '\'' +
                ", user1Id='" + user1Id + '\'' +
                ", user2Id='" + user2Id + '\'' +
                ", createdDate=" + createdDate +
                ", messageCount=" + messageCount +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Chat chat = (Chat) o;
        return id.equals(chat.id) &&
                user1Id.equals(chat.user1Id) &&
                user2Id.equals(chat.user2Id) &&
                createdDate.equals(chat.createdDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, content, user1Id, user2Id, createdDate);
    }

    public static final DiffUtil.ItemCallback<Chat> DIFF_UTIL = new DiffUtil.ItemCallback<Chat>() {
        @Override
        public boolean areItemsTheSame(@NonNull Chat oldItem, @NonNull Chat newItem) {
            return oldItem.id.equals(newItem.id);
        }

        @Override
        public boolean areContentsTheSame(@NonNull Chat oldItem, @NonNull Chat newItem) {
            return oldItem.equals(newItem);
        }
    };
}
