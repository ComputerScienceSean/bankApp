package com.example.bankapp.entities;

import android.os.Parcel;
import android.os.Parcelable;

public class BankAccount implements Parcelable {

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


    protected BankAccount(Parcel in) {
        title = in.readString();
        balance = in.readInt();
        accNumber = in.readString();
    }

    public static final Creator<BankAccount> CREATOR = new Creator<BankAccount>() {
        @Override
        public BankAccount createFromParcel(Parcel in) {
            return new BankAccount(in);
        }

        @Override
        public BankAccount[] newArray(int size) {
            return new BankAccount[size];
        }
    };

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
        return title + "   " + accNumber;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeInt(balance);
        dest.writeString(accNumber);
    }
}