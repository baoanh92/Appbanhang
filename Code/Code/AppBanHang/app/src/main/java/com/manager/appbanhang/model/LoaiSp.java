package com.manager.appbanhang.model;

public class LoaiSp {
    int id;
    String loaisp;
    String hinhanh;

    public LoaiSp(String loaisp, String hinhanh) {
        this.loaisp = loaisp;
        this.hinhanh = hinhanh;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTensanpham() {
        return loaisp;
    }

    public void setTensanpham(String loaisp) {
        this.loaisp = loaisp;
    }

    public String getHinhanh() {
        return hinhanh;
    }

    public void setHinhanh(String hinhanh) {
        this.hinhanh = hinhanh;
    }
}
