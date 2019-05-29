package com.example.bankapp.entities;

public class User {
    private String firstname;
    private String lastname;
    private String cpr;
    private String phonenumber;
    private String email;
    private String password;
    private String address;

    public User(String firstname, String lastname, String cpr, String phonenumber, String email, String password, String address) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.cpr = cpr;
        this.phonenumber = phonenumber;
        this.email = email;
        this.password = password;
        this.address = address;
    }

    public User() {
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getCpr() {
        return cpr;
    }

    public void setCpr(String cpr) {
        this.cpr = cpr;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
