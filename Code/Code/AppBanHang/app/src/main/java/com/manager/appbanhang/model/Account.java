package com.manager.appbanhang.model;


public class Account {
    private int id;
    private String email;
    private String pass;
    private String username;
    private String mobile;
    private String address;
    private String Decentralization;

    public String getDecentralization() {
        return Decentralization;
    }

    public void setDecentralization(String decentralization) {
        Decentralization = decentralization;
    }
    public Account(int id, String email, String pass, String username, String mobile, String address) {
        this.id = id;
        this.email = email;
        this.pass = pass;
        this.username = username;
        this.mobile = mobile;
        this.address = address;
    }
    public Account(int id, String email, String pass, String username, String mobile, String address, String decentralization) {
        this.id = id;
        this.email = email;
        this.pass = pass;
        this.username = username;
        this.mobile = mobile;
        this.address = address;
        this.Decentralization = decentralization;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
