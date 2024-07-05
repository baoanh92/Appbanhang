package com.manager.appbanhang.adapter;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appbanhang.R;
import com.manager.appbanhang.Interface.ItemClickListener;
import com.manager.appbanhang.activity.ChiTietActivity;
import com.manager.appbanhang.model.EventBus.SuaXoaEvent;
import com.manager.appbanhang.model.SanPhamMoi;
import com.manager.appbanhang.utils.Utils;


import org.greenrobot.eventbus.EventBus;

import java.text.DecimalFormat;
import java.util.List;

public class SanPhamMoiAdapter extends RecyclerView.Adapter<SanPhamMoiAdapter.MyViewHolder> {
    Context context;
    List<SanPhamMoi> array;

    public SanPhamMoiAdapter(Context context, List<SanPhamMoi> array) {
        this.context = context;
        this.array = array;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sp_moi,parent,false);

        return new MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        SanPhamMoi sanPhamMoi = array.get(position);
        holder.txtTen.setText(sanPhamMoi.getTensp());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.txtGia.setText("Giá: " + decimalFormat.format(Double.parseDouble(sanPhamMoi.getGiasp())) + " VNĐ");
        if(sanPhamMoi.getQuantity() <= 0){
            holder.txtsoluong.setText("Đã hết hàng");
        }
        else {
            holder.txtsoluong.setText("Còn: "+ sanPhamMoi.getQuantity());
        }
        //truyền hình ảnh up lên của người dùng để bán
        if (sanPhamMoi.getHinhanh().contains("http")) {
            Glide.with(context).load(sanPhamMoi.getHinhanh()).into(holder.imghinhAnh);
        }
        else {
            String hinh = Utils.BASE_URL + "images/" + sanPhamMoi.getHinhanh();
            Glide.with(context).load(hinh).into(holder.imghinhAnh);
        }

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int pos, boolean isLongClick) {
                // Kiểm tra sản phẩm đã hết hàng hay chưa
                if (sanPhamMoi.getQuantity() <= 0) {
                    // Nếu sản phẩm đã hết hàng, hiển thị thông báo
                    Toast.makeText(context, "Đã hết hàng", Toast.LENGTH_SHORT).show();
                }
                else if (!isLongClick) {
                    SharedPreferences sharedPreferences = context.getSharedPreferences("MySharedPref", MODE_PRIVATE);
                    String permission = sharedPreferences.getString("permission", "");
                    if(permission.equals("Admin")){
                    }
                    else{
                        Intent intent = new Intent(context, ChiTietActivity.class);
                        intent.putExtra("chitiet",sanPhamMoi);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                    // Nếu không phải long click, chuyển đến ChiTietActivity

                }
                else {
                    // Nếu là long click, thực hiện các thao tác khác
                    EventBus.getDefault().postSticky(new SuaXoaEvent(sanPhamMoi));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return array.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener, View.OnLongClickListener {
        TextView txtGia,txtTen , txtsoluong;
        ImageView imghinhAnh;
        private ItemClickListener itemClickListener;
        public MyViewHolder(@NonNull View itemView) {

            super(itemView);
            txtGia = itemView.findViewById(R.id.itemsp_gia);
            txtTen = itemView.findViewById(R.id.itemsp_ten);
            imghinhAnh = itemView.findViewById(R.id.itemsp_image);
            txtsoluong = itemView.findViewById(R.id.itemsp_SoLuong);
            itemView.setOnClickListener(this);
            // ánh xạ
            itemView.setOnCreateContextMenuListener(this);
            itemView.setOnLongClickListener(this);// nhấn giữ trong thời gian dài

        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View view) {
            itemClickListener.onClick(view,getAdapterPosition(),false);
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            SharedPreferences sharedPreferences = context.getSharedPreferences("MySharedPref", MODE_PRIVATE);
            String permission = sharedPreferences.getString("permission", "");
            if(permission.equals("Admin")){
                contextMenu.add(0,0,getAdapterPosition(),"Sửa");
                contextMenu.add(0,1,getAdapterPosition(),"Xóa");
            }
        }

        @Override
        public boolean onLongClick(View view) {
            itemClickListener.onClick(view,getAdapterPosition(),true);
            return false;
        }
    }

}
