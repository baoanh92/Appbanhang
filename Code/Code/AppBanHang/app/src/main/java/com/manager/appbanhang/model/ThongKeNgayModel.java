package com.manager.appbanhang.model;

import java.util.List;

public class ThongKeNgayModel {
    boolean success;
    String message;
    List<ThongTinDonHang> result;

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

    public List<ThongTinDonHang> getResult() {
        return result;
    }

    public void setResult(List<ThongTinDonHang> result) {
        this.result = result;
    }
}
