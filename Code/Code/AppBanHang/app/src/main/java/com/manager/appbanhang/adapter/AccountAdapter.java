package com.manager.appbanhang.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.appbanhang.R;
import com.manager.appbanhang.model.Account;

import java.util.List;

public class AccountAdapter extends BaseAdapter {
    List<Account> array;
    Context context;

    public AccountAdapter(Context context, List<Account> array) {
        this.array = array;
        this.context = context;
    }
    public void setData(List<Account> newData) {
        this.array = newData;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return array.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }
    public class ViewHolder{
        TextView id;
        TextView email;
        ImageView imghinhanh;
        TextView address;
        TextView numberPhone;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            view = layoutInflater.inflate(R.layout.customkhachang, viewGroup, false);
            viewHolder.id = view.findViewById(R.id.idKhachHang);
            viewHolder.email = view.findViewById(R.id.email);
            viewHolder.imghinhanh = view.findViewById(R.id.image);
            viewHolder.address = view.findViewById(R.id.diachi);
            viewHolder.numberPhone = view.findViewById(R.id.sodienthoai);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.id.setText("ID: " + array.get(i).getId());
        viewHolder.email.setText("Email: " + array.get(i).getEmail());
        viewHolder.imghinhanh.setImageResource(R.drawable.ic_person_24);
        viewHolder.address.setText("Địa chỉ: " + array.get(i).getAddress());
        viewHolder.numberPhone.setText("Số điện thoại: " + array.get(i).getMobile());
        return view;
    }


}
