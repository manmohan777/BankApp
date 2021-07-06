package com.example.bankapp.Database.Room;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;
import java.util.UUID;

@Entity(tableName = "users")
public class User {

    @NonNull
    @PrimaryKey
    private String id = UUID.randomUUID().toString();

    @NonNull
    private String name;

    @NonNull
    private double bsrBal = 0;

    @NonNull
    private String email;

    @NonNull
    private String password;

    @NonNull
    private String phoneNumber;

    @NonNull
    private String accountNo;

    @NonNull
    private Date createdDate = new Date();

    @NonNull
    private Date updatedDate = new Date();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getBsrBal() {
        return bsrBal;
    }

    public void setBsrBal(double bsrBal) {
        this.bsrBal = bsrBal;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
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

    public boolean isAgent() {
        return id != null && id.startsWith("A");
    }

    public static class Info{
        private String id;
        private String name;
        private String email;
        private String phoneNumber;
        private String accountNo;

        public Info(String id, String name, String email, String phoneNumber, String accountNo) {
            this.id = id;
            this.name = name;
            this.email = email;
            this.phoneNumber = phoneNumber;
            this.accountNo = accountNo;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public String getAccountNo() {
            return accountNo;
        }

        public void setAccountNo(String accountNo) {
            this.accountNo = accountNo;
        }

        public boolean isAgent() {
            return id != null && id.startsWith("A");
        }
    }

}
