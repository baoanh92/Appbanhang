package com.manager.appbanhang.model;

import java.util.List;

public class LienHeModel {
    boolean success;
    String message;
    List<LienHe> result;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<LienHe> getResult() {
        return result;
    }

    public void setResult(List<LienHe> result) {
        this.result = result;
    }
}
