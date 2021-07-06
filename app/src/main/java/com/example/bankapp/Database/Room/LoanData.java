package com.example.bankapp.Database.Room;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;
import java.util.UUID;

@Entity(tableName = "loan_data")
public class LoanData {

    public static final String[] STATE_ARRAY = new String[]{"APPLIED", "PENDING", "APPROVED", "REJECTED"};
    public static final int STATE_APPLIED = 0;
    public static final int STATE_PENDING = 1;
    public static final int STATE_APPROVED = 2;
    public static final int STATE_REJECTED = 3;

    @NonNull
    @PrimaryKey
    private String id = UUID.randomUUID().toString();
    private String userId;
    private double loanAmt;
    private String loanType;
    private Date applDate = new Date();
    private double interest = 5.0;
    private int period;
    private Date updatedDate = new Date();
    private String agentId;
    private int state;
    private int commissionRate;


    @Nullable
    private String chatId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLoanType() {
        return loanType;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }

    public Date getApplDate() {
        return applDate;
    }

    public void setApplDate(Date applDate) {
        this.applDate = applDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public double getLoanAmt() {
        return loanAmt;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public void setLoanAmt(double loanAmt) {
        this.loanAmt = loanAmt;
    }

    public double getInterest() {
        return interest;
    }

    public void setInterest(double interest) {
        this.interest = interest;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getCommissionRate() {
        return commissionRate;
    }

    public void setCommissionRate(int commissionRate) {
        this.commissionRate = commissionRate;
    }

    @Nullable
    public String getChatId() {
        return chatId;
    }

    public void setChatId(@Nullable String chatId) {
        this.chatId = chatId;
    }
}
