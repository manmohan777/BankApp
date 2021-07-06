package com.example.bankapp.Database.Room;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;
import java.util.UUID;

@Entity(tableName = "transactions")
public class Transaction {

    @NonNull
    @PrimaryKey
    private String id = UUID.randomUUID().toString();

    @NonNull
    private String sender;

    @NonNull
    private String receiver;


    private String senderName;


    private String receiverName;

    private String senderAccNo;


    private String receiverAccNo;

    @NonNull
    private double amount;

    @NonNull
    private boolean successful = false;

    @NonNull
    private Date createdDate = new Date();

    @NonNull
    private Date updatedDate = new Date();


    public Transaction() {
    }

    @Ignore
    public Transaction(String sender, String receiver, double amount, boolean successful) {
        this.sender = sender;
        this.receiver = receiver;
        this.amount = amount;
        this.successful = successful;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getSenderAccNo() {
        return senderAccNo;
    }

    public void setSenderAccNo(String senderAccNo) {
        this.senderAccNo = senderAccNo;
    }

    public String getReceiverAccNo() {
        return receiverAccNo;
    }

    public void setReceiverAccNo(String receiverAccNo) {
        this.receiverAccNo = receiverAccNo;
    }

    public String getReceiver() {
        return receiver;
    }

    @NonNull
    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(@NonNull String senderName) {
        this.senderName = senderName;
    }

    @NonNull
    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(@NonNull String receiverName) {
        this.receiverName = receiverName;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public static Transaction of(String sender, String receiver, double amount, boolean successful ){
        return new Transaction(sender, receiver, amount, successful);
    }
}
