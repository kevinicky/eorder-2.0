package com.niki.eorder.model;

public class Users {
    private long eBalance;
    private String email;
    private String name;

    public long geteBalance() {
        return eBalance;
    }

    public void seteBalance(long eBalance) {
        this.eBalance = eBalance;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
