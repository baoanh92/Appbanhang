package com.manager.appbanhang.adapter;


import static android.text.method.TextKeyListener.clear;
import static java.util.Collections.addAll;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.appbanhang.R;
import com.manager.appbanhang.activity.ThongKeDonHangHomNay;
import com.manager.appbanhang.model.ThongTinDonHang;

import java.util.ArrayList;
import java.util.List;

public class ThongTinDonHangAdapter extends BaseAdapter {
    List<ThongTinDonHang> array;
    List<ThongTinDonHang> filteredList;
    Context context;
    public void updateList(List<ThongTinDonHang> newList) {
        addAll(newList);
        notifyDataSetChanged();
    }
    public ThongTinDonHangAdapter(Context context, List<ThongTinDonHang> array) {
        this.array = array;
        this.filteredList = new ArrayList<>(array);
        this.context = context;
    }

    public void filter(String keyword) {
        filteredList.clear();
        if (keyword.isEmpty()) {
            filteredList.addAll(array);
        } else {
            for (ThongTinDonHang donHang : array) {
                if (donHang.getGhiChu() != null && donHang.getGhiChu().toLowerCase().contains(keyword.toLowerCase())) {
                    filteredList.add(donHang);
                }

            }
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return filteredList.size();
    }

    @Override
    public Object getItem(int i) {
        return filteredList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public class ViewHolder{
        TextView id;
        TextView email;
        ImageView imghinhanh;
        TextView total;
        TextView numberPhone;
        TextView tensp;
        TextView ghichu;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            view = layoutInflater.inflate(R.layout.item_dat, viewGroup, false);
            viewHolder.id = view.findViewById(R.id.itemdt_mota);
            viewHolder.email = view.findViewById(R.id.emailbuy);
            viewHolder.tensp = view.findViewById(R.id.itemdt_ten);
            viewHolder.imghinhanh = view.findViewById(R.id.itemdt_image);
            viewHolder.total = view.findViewById(R.id.total);
            viewHolder.numberPhone = view.findViewById(R.id.sdt);
            viewHolder.ghichu = view.findViewById(R.id.ghichu);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        ThongTinDonHang donHang = filteredList.get(i);

        viewHolder.id.setText("ID: " + donHang.getId());
        viewHolder.email.setText("Email: " + donHang.getEmail());
        Glide.with(context).load(donHang.getHinhanh()).into(viewHolder.imghinhanh);
        viewHolder.total.setText("Tổng tiền: " + donHang.getTongtien());
        viewHolder.numberPhone.setText("Số điện thoại: " + donHang.getSodienthoai());
        viewHolder.tensp.setText("Tên Sản Phẩm: " + donHang.getTensp());
        if (donHang.getGhiChu() == null || donHang.getGhiChu().isEmpty()) {
            viewHolder.ghichu.setText("Vui lòng thêm ở quản lý đơn hàng");
        } else {
            viewHolder.ghichu.setText("Ghi chú: " + donHang.getGhiChu());
        }
        return view;
    }
}

