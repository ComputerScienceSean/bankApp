package com.example.bankapp.entities;

public class AutoPay {

    private Long amount;
    private String sendFrom;
    private String payedTo;
    private String sendDate;

    public AutoPay() {
    }

    public AutoPay(Long amount, String sendFrom, String payedTo, String sendDate) {
        this.amount = amount;
        this.sendFrom = sendFrom;
        this.payedTo = payedTo;
        this.sendDate = sendDate;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getSendFrom() {
        return sendFrom;
    }

    public void setSendFrom(String sendFrom) {
        this.sendFrom = sendFrom;
    }

    public String getPayedTo() {
        return payedTo;
    }

    public void setPayedTo(String payedTo) {
        this.payedTo = payedTo;
    }

    public String getSendDate() {
        return sendDate;
    }

    public void setSendDate(String sendDate) {
        this.sendDate = sendDate;
    }
}
