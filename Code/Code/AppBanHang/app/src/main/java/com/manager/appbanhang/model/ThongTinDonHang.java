package com.manager.appbanhang.model;

public class ThongTinDonHang {
    private String hinhanh;
    private String sodienthoai;
    private String tongtien;
    private String tensp;
    private String email;
    private int id;
    private String NgayGiao;
    private String GhiChu;

    public ThongTinDonHang(String hinhanh, String sodienthoai, String tongtien, String tensp, String email, int id, String NgayGiao, String GhiChu) {
        this.hinhanh = hinhanh;
        this.sodienthoai = sodienthoai;
        this.tongtien = tongtien;
        this.tensp = tensp;
        this.email = email;
        this.id = id;
        this.NgayGiao = NgayGiao;
        this.GhiChu = GhiChu;
    }

    public String getNgayGiao() {
        return NgayGiao;
    }

    public void setNgayGiao(String ngayGiao) {
        NgayGiao = ngayGiao;
    }

    public String getGhiChu() {
        return GhiChu;
    }

    public void setGhiChu(String ghiChu) {
        GhiChu = ghiChu;
    }

    public String getHinhanh() {
        return hinhanh;
    }

    public void setHinhanh(String hinhanh) {
        this.hinhanh = hinhanh;
    }

    public String getSodienthoai() {
        return sodienthoai;
    }

    public void setSodienthoai(String sodienthoai) {
        this.sodienthoai = sodienthoai;
    }

    public String getTongtien() {
        return tongtien;
    }

    public void setTongtien(String tongtien) {
        this.tongtien = tongtien;
    }

    public String getTensp() {
        return tensp;
    }

    public void setTensp(String tensp) {
        this.tensp = tensp;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
