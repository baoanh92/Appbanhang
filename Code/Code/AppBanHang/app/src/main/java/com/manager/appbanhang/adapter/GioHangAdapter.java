package com.manager.appbanhang.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.manager.appbanhang.Interface.IImageClickListenner;
import com.example.appbanhang.R;
import com.manager.appbanhang.activity.MenuQuanLy;
import com.manager.appbanhang.model.EventBus.TinhTongEvent;
import com.manager.appbanhang.model.GioHang;
import com.manager.appbanhang.model.SanPhamMoi;
import com.manager.appbanhang.retrofit.ApiBanHang;
import com.manager.appbanhang.utils.Utils;

import org.greenrobot.eventbus.EventBus;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class GioHangAdapter extends RecyclerView.Adapter<GioHangAdapter.MyViewHolder> {
    private Context context;
    private List<GioHang> gioHangList;
    private ApiBanHang apiBanHang;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public GioHangAdapter(Context context, List<GioHang> gioHangList, ApiBanHang apiBanHang) {
        this.context = context;
        this.gioHangList = gioHangList;
        this.apiBanHang = apiBanHang;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_giohang, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        GioHang gioHang = gioHangList.get(position);
        holder.item_giohang_tensp.setText(gioHang.getTensp());
        holder.item_giohang_soluong.setText(gioHang.getSoluong() + " ");

        if (gioHang.getHinhsp().contains("http")) {
            Glide.with(context).load(gioHang.getHinhsp()).into(holder.item_giohang_image);
        } else {
            String hinh = Utils.BASE_URL + "images/" + gioHang.getHinhsp();
            Glide.with(context).load(hinh).into(holder.item_giohang_image);
        }

        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.item_giohang_gia.setText(decimalFormat.format(gioHang.getGiasp()) + " VNĐ");
        long gia = gioHang.getSoluong() * gioHang.getGiasp();
        holder.item_giohang_giasp2.setText(decimalFormat.format(gia) + " VNĐ");

        holder.checkBox.setOnCheckedChangeListener(null); // Clear previous listener
        holder.checkBox.setChecked(false);

        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            gioHang.setCheck(isChecked);
            if (isChecked) {
                if (!Utils.mangmuahang.contains(gioHang)) {
                    Utils.mangmuahang.add(gioHang);
                }
            } else {
                Utils.mangmuahang.remove(gioHang);
            }
            EventBus.getDefault().postSticky(new TinhTongEvent());
        });

        holder.setListener((view, pos, giatri) -> {
            if (giatri == 1) {
                if (gioHangList.get(pos).getSoluong() > 1) {
                    int soluongmoi = gioHangList.get(pos).getSoluong() - 1;
                    gioHangList.get(pos).setSoluong(soluongmoi);
                } else if (gioHangList.get(pos).getSoluong() == 1) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    builder.setTitle("Thông báo");
                    builder.setMessage("Bạn có muốn xóa sản phẩm khỏi giỏ hàng");
                    builder.setPositiveButton("Đồng ý", (dialogInterface, i) -> {
                        gioHangList.remove(pos);
                        Utils.mangmuahang.remove(gioHang);
                        Paper.book().write("giohang", Utils.manggiohang);
                        notifyDataSetChanged();
                        EventBus.getDefault().postSticky(new TinhTongEvent());
                    });
                    builder.setNegativeButton("Hủy", (dialogInterface, which) -> dialogInterface.dismiss());

                    // Hiển thị Dialog bằng context của itemView
                    view.post(() -> builder.show());


                }
            } else if (giatri == 2) {
                SanPhamMoi gioHangItem = null;
                for (SanPhamMoi item : Utils.SpMoi) {
                    if (item.getId() == gioHangList.get(pos).getIdsp()) {
                        gioHangItem = item;
                        break;
                    }
                }
                if (gioHangItem != null && gioHangList.get(pos).getSoluong() >= gioHangItem.getQuantity()) {
                    Toast.makeText(context, "Sản phẩm đã hết hàng", Toast.LENGTH_SHORT).show();
                } else {
                    compositeDisposable.add(getSoLuong(gioHangList.get(pos).getIdsp())
                            .subscribe(
                                    quantity -> {
                                        GioHang gioHang1 = gioHangList.get(pos);
                                        if (gioHang1.getSoluong() < quantity) {
                                            int soluongmoi = gioHang1.getSoluong() + 1;
                                            gioHang1.setSoluong(soluongmoi);
                                        } else {
                                            Toast.makeText(context, "Sản phẩm " + gioHang1.getTensp() + "đã hết trong kho hàng ", Toast.LENGTH_SHORT).show();
                                        }
                                    },
                                    throwable -> {
                                        // Xử lý nếu có lỗi xảy ra khi lấy số lượng
                                    }
                            )
                    );
                }
            }
            notifyDataSetChanged();
            EventBus.getDefault().postSticky(new TinhTongEvent());
        });
    }

    @Override
    public int getItemCount() {
        return gioHangList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView item_giohang_image, imgtru, imgcong;
        TextView item_giohang_tensp, item_giohang_gia, item_giohang_soluong, item_giohang_giasp2;
        IImageClickListenner listener;
        CheckBox checkBox;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            item_giohang_image = itemView.findViewById(R.id.item_giohang_image);
            item_giohang_tensp = itemView.findViewById(R.id.item_giohang_tensp);
            item_giohang_gia = itemView.findViewById(R.id.item_giohang_gia);
            item_giohang_soluong = itemView.findViewById(R.id.item_giohang_soluong);
            item_giohang_giasp2 = itemView.findViewById(R.id.item_giohang_giasp2);
            imgtru = itemView.findViewById(R.id.item_giohang_tru);
            imgcong = itemView.findViewById(R.id.item_giohang_cong);
            checkBox = itemView.findViewById(R.id.item_giohang_check);

            imgcong.setOnClickListener(this);
            imgtru.setOnClickListener(this);
        }

        public void setListener(IImageClickListenner listener) {
            this.listener = listener;
        }

        @Override
        public void onClick(View view) {
            if (view == imgtru) {
                listener.onImageClick(view, getAdapterPosition(), 1);
            } else if (view == imgcong) {
                listener.onImageClick(view, getAdapterPosition(), 2);
            }
        }
    }

    public Observable<Integer> getSoLuong(int idsp) {
        return apiBanHang.getSpMoiById(idsp)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(sanPhamMoiModel -> sanPhamMoiModel.getResult().get(0).getQuantity());
    }
}
          