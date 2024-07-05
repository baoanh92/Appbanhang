package com.manager.appbanhang.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.appbanhang.R;
import com.manager.appbanhang.adapter.LoaiSpAdapter;
import com.manager.appbanhang.model.LoaiSp;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class AdminActivity extends AppCompatActivity {

    ListView listViewManHinhChinh;
    LoaiSpAdapter loaiSpAdapter;
    List<LoaiSp> mangloaisp;
    String permission;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        Anhxa();
        getLoaiSanPham();
        chonItemlistview();
        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
         permission = sharedPreferences.getString("permission", "");

    }

    private void chonItemlistview() {
        listViewManHinhChinh.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                switch (mangloaisp.get(i).getTensanpham()) {
                    case "Trang chủ":
                        Intent intent=new Intent(AdminActivity.this, MenuQuanLy.class);
                        startActivity(intent);
                        break;
                    case "Quản lý danh mục":
                        if(permission.equals("Admin")){
                            Intent ip=new Intent(AdminActivity.this, DanhMucSanPham.class);
                            ip.putExtra("loai",1);
                            startActivity(ip);
                        }
                        else{
                            Toast.makeText(AdminActivity.this, "Ban khong du quyen", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case "Quản lý khách hàng":
                        Intent ss=new Intent(AdminActivity.this, QuanLyKhachHang.class);
                        ss.putExtra("loai",2);
                        startActivity(ss);
                        break;
                    case "Quản lý sản phẩm":
                        if(permission.equals("Admin")){
                            Intent oppo=new Intent(AdminActivity.this, QuanLiActivity.class);
                            oppo.putExtra("loai",3);
                            startActivity(oppo);
                        }
                        else{
                            Toast.makeText(AdminActivity.this, "Ban khong du quyen", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case "Quản lý thành viên":
                        if(permission.equals("Admin")){
                            Intent xiaomi=new Intent(AdminActivity.this, QuanLiTaiKhoanAdmin.class);
                            xiaomi.putExtra("loai",4);
                            startActivity(xiaomi);
                        }
                        else{
                            Toast.makeText(AdminActivity.this, "Ban khong du quyen", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case "Thông tin":
                        Intent thongtin =new Intent(AdminActivity.this, ThongTinActivity.class);
                        startActivity(thongtin);
                        break;
                    case "Quản lý đơn hàng":
                    if(permission.equals("Admin")){
                        Intent donhang =new Intent(AdminActivity.this, XemDonHangActivity.class);
                        startActivity(donhang);

                    }
                    else{
                        Toast.makeText(AdminActivity.this, "Ban khong du quyen", Toast.LENGTH_SHORT).show();
                    }
                        break;
                    case "Thống kê":
                    if(permission.equals("Admin")){
                        Intent thongke =new Intent(AdminActivity.this, ThongKeActivity.class);
                        startActivity(thongke);
                    }
                    else{
                        Toast.makeText(AdminActivity.this, "Ban khong du quyen", Toast.LENGTH_SHORT).show();
                    }
                    case "Đăng xuất":
                        Paper.book().delete("user");
                        Intent dangnhap =new Intent(AdminActivity.this, DangNhapActivity.class);
                        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.clear();
                        editor.apply();
                        startActivity(dangnhap);
                        finish();
                        break;
                    default:
                        Toast.makeText(AdminActivity.this, "Chức năng chưa được cài đặt", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    private void getLoaiSanPham() {
        mangloaisp = new ArrayList<>();
        mangloaisp.add(new LoaiSp("Quản lý danh mục", "https://happy.live/wp-content/uploads/2019/09/ban-co-dang-quan-ly-danh-muc-dau-tu-hieu-qua-happy-live1-768x568.jpg"));
        mangloaisp.add(new LoaiSp("Quản lý khách hàng", "https://bizflyportal.mediacdn.vn/thumb_wm/1000,100/bizflyportal/images/qua16393876766418.jpeg"));
        mangloaisp.add(new LoaiSp("Quản lý sản phẩm", "https://trustsales.vn/image/quan-ly-san-pham.jpg"));
        mangloaisp.add(new LoaiSp("Quản lý thành viên", "https://tse3.mm.bing.net/th?id=OIP.0_WGaIuicPhecADU0fT7fAHaHa&pid=Api&P=0"));
        mangloaisp.add(new LoaiSp("Thống kê", "https://tse3.mm.bing.net/th?id=OIP.PWAttRDtQyfSAyS8dUi_DQAAAA&pid=Api&P=0&h=180"));
        mangloaisp.add(new LoaiSp("Quản lý đơn hàng", "https://tse2.mm.bing.net/th?id=OIP.6XBvyykZoIIPPS98ASkCfQHaHk&pid=Api&P=0&h=220"));
        mangloaisp.add(new LoaiSp("Quản lý ho tro", "data:image/jpeg;base64,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"));
        mangloaisp.add(new LoaiSp("Đăng xuất", "https://tse1.mm.bing.net/th?id=OIP.YYdy7rUi0LH3aQ_gjvHjTgHaHa&pid=Api&P=0"));
        // Thêm các mục còn lại vào danh sách mangloaisp ở đây
        loaiSpAdapter = new LoaiSpAdapter(getApplicationContext(), mangloaisp);
        listViewManHinhChinh.setAdapter(loaiSpAdapter);
        loaiSpAdapter.notifyDataSetChanged();
    }
    private View getViewByPosition(int position, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (position < firstListItemPosition || position > lastListItemPosition) {
            return listView.getAdapter().getView(position, null, listView);
        } else {
            final int childIndex = position - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }
    private void Anhxa() {
        listViewManHinhChinh = findViewById(R.id.listviewmanhinhchinh);
    }
}
