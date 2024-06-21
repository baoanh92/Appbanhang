package com.example.appbanhang.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbanhang.R;
import com.example.appbanhang.adapter.ChiTietAdapter;
import com.example.appbanhang.model.DonHang;
import com.example.appbanhang.model.DonHang;

import java.util.List;

public class DonHangAdapter extends RecyclerView.Adapter<DonHangAdapter.MyViewHolder> {

    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
    Context context;
    List<DonHang> list;

    public DonHangAdapter(Context context, List<DonHang> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public DonHangAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_donhang,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DonHangAdapter.MyViewHolder holder, int position) {
        DonHang donHang = list.get(position);
        holder.txtdonhang.setText("Đơn hàng: " +donHang.getId());
        holder.trangthai.setText(trangThaiDon(donHang.getTrangthai()));
        holder.diachi.setText("Địa chỉ: "+donHang.getDiachi());
        LinearLayoutManager layoutManager = new LinearLayoutManager(holder.rcChitiet.getContext(),LinearLayoutManager.VERTICAL,false);
        layoutManager.setInitialPrefetchItemCount(donHang.getItem().size());

        //adapter
        ChiTietAdapter chiTietAdapter  = new ChiTietAdapter(context,donHang.getItem());
        holder.rcChitiet.setLayoutManager(layoutManager);
        holder.rcChitiet.setAdapter(chiTietAdapter);
        holder.rcChitiet.setRecycledViewPool(viewPool);
    }

    private String trangThaiDon(int sttus){
        String result = "";
        switch (sttus){
            case 0:
                result = "Đơn hàng đang được xử lý";
                break;
            case 1:
                result = "Đơn hàng đã chấp nhận";
                break;
            case 2:
                result = "Đơn hàng đã giao cho đơn vị vận chuyển";
                break;
            case 3:
                result = "Thành công";
                break;
            case 4:
                result = "Đã hủy";
                break;
        }

        return result;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtdonhang,trangthai,diachi;
        RecyclerView rcChitiet;

        public MyViewHolder(@NonNull View itemView) {

            super(itemView);
            txtdonhang = itemView.findViewById(R.id.iddonhang);
            trangthai = itemView.findViewById(R.id.tinhtrang);
            diachi = itemView.findViewById(R.id.diachi_donhang);
            rcChitiet = itemView.findViewById(R.id.recycleview_chitiet);
        }
    }
}
