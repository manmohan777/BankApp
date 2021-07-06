package com.example.bankapp.Database.Room;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;
import java.util.UUID;

@Entity(tableName = "insurance_data")
public class InsuranceData {
    public static final String[] STATE_ARRAY = new String[]{"APPLIED", "PENDING", "APPROVED", "REJECTED"};
    public static final int STATE_APPLIED = 0;
    public static final int STATE_PENDING = 1;
    public static final int STATE_APPROVED = 2;
    public static final int STATE_REJECTED = 3;

    @NonNull
    @PrimaryKey
    private String id = UUID.randomUUID().toString();

    @NonNull
    private Date applDate = new Date();

    @NonNull
    private Date updatedDate = new Date();

    @NonNull
    private String userId;

    @Nullable
    private String chatId;

    private double insuranceAmt;

    @NonNull
    private String insuranceType;

    @NonNull
    private String nominee;

    @NonNull
    private String agentId;
    private int state;
    private int commissionRate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setApplDate(@NonNull Date applDate) {
        this.applDate = applDate;
    }

    public void setUpdatedDate(@NonNull Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public Date getApplDate() {
        return applDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public double getInsuranceAmt() {
        return insuranceAmt;
    }

    public void setInsuranceAmt(double insuranceAmt) {
        this.insuranceAmt = insuranceAmt;
    }

    public String getInsuranceType() {
        return insuranceType;
    }

    public void setInsuranceType(String insuranceType) {
        this.insuranceType = insuranceType;
    }

    public String getNominee() {
        return nominee;
    }

    public void setNominee(String nominee) {
        this.nominee = nominee;
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
