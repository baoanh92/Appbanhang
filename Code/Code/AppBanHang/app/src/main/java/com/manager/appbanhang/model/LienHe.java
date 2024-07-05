package com.manager.appbanhang.model;

public class LienHe {
    private int id;
    private String message;
    private String user;
    private String date;
    private String admin;

    public LienHe(int id, String message, String user, String date, String admin) {
        this.id = id;
        this.message = message;
        this.user = user;
        this.date = date;
        this.admin = admin;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }
}
