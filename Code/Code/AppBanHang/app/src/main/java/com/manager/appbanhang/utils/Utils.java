package com.manager.appbanhang.utils;

import com.manager.appbanhang.model.GioHang;
import com.manager.appbanhang.model.SanPhamMoi;
import com.manager.appbanhang.model.User;

import java.util.ArrayList;
import java.util.List;

public class Utils {
    public static final  String BASE_URL="http://192.168.0.101:85/banhang/";
    public static List<GioHang> manggiohang;
    public static int isadmin=0;
    public static List<GioHang> mangmuahang = new ArrayList<>();
    public static List<SanPhamMoi> SpMoi = new ArrayList<>();
    public static User user_current = new User();
}
