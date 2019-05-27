package com.example.bankapp;

public class BankAccount {

    private String title;
    private int balance;
    private String accNumber;

    public BankAccount() {

    }

    public BankAccount(String title, int balance) {
        this.title = title;
        this.balance = balance;
    }

    public BankAccount(String title, int balance, String accNumber) {
        this.title = title;
        this.balance = balance;
        this.accNumber = accNumber;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public String getAccNumber() {
        return accNumber;
    }

    public void setAccNumber(String accNumber) {
        this.accNumber = accNumber;
    }

    @Override
    public String toString() {
        return title + "--- " +accNumber + "  " + balance ;
    }
}