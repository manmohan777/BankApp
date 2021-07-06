package com.example.bankapp.model;


public class AgentListData {


    Long account_no;

    int balance;

    String name;

    public AgentListData() {
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
        return "AgentListData{" +
                "account_no='" + account_no + '\'' +
                ", balance='" + balance + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
