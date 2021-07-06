package com.example.bankapp.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Rahul Rai on 27,June,2021.
 * rahul.rai955@gmail.com
 */
public class UsersBean {

    @SerializedName("email")
    String email;

    @SerializedName("password")
    String password;

    @SerializedName("account_no")
    Long account_no;

    @SerializedName("balance")
    int balance;

    @SerializedName("name")
    String name;


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

    public Long getAccount_no() {
        return account_no;
    }

    public void setAccount_no(Long account_no) {
        this.account_no = account_no;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "UsersBean{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", account_no='" + account_no + '\'' +
                ", balance='" + balance + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
