package com.manager.appbanhang.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.example.appbanhang.R;
import com.manager.appbanhang.adapter.LoaiSpAdapter;
import com.manager.appbanhang.adapter.SanPhamMoiAdapter;
import com.manager.appbanhang.model.LoaiSp;
import com.manager.appbanhang.model.SanPhamMoi;
import com.manager.appbanhang.model.User;
import com.manager.appbanhang.retrofit.ApiBanHang;
import com.manager.appbanhang.retrofit.RetrofitClient;
import com.manager.appbanhang.utils.Utils;
import com.google.android.material.navigation.NavigationView;
import com.nex3z.notificationbadge.NotificationBadge;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    ViewFlipper viewFlipper;
    RecyclerView recyclerViewManHinhChinh;
    NavigationView navigationView;
    ListView listViewManHinhChinh;
    DrawerLayout drawerLayout;
    LoaiSpAdapter loaiSpAdapter;
    List<LoaiSp> mangloaisp;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiBanHang apiBanHang;
    List<SanPhamMoi> mangSpMoi;
    SanPhamMoiAdapter spAdapter;
    NotificationBadge badge;
    FrameLayout frameLayout;
    ImageView imgsearch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        Paper.init(this);
        if (Paper.book().read("user") != null) {
            User user = Paper.book().read("user");
            Utils.user_current = user;
        }

        Anhxa();
        ActionBar();

        if (isConnected(this)) {

            ActionViewFlipper();
            getLoaiSanPham();
            getSpMoi();
            chonItemlistview();

        } else {
            Toast.makeText(getApplicationContext(), "Không có Internet, vui lòng kết nối", Toast.LENGTH_LONG).show();
        }

    }

    private void chonItemlistview() {
        listViewManHinhChinh.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (mangloaisp.get(i).getTensanpham()) {
                    case "Trang chủ":
                        Intent intent = new Intent(MainActivity.this, MainActivity.class);
                        startActivity(intent);
                        break;
                    case "Thông tin":
                        Intent thongtin = new Intent(MainActivity.this, ThongTinActivity.class);
                        startActivity(thongtin);
                        break;
                    case "Đơn hàng":
                        Intent donhang = new Intent(MainActivity.this, DonHangKhachHang.class);
                        startActivity(donhang);
                        break;
                    case "Đăng xuất":
                        Paper.book().delete("user");
                        Intent dangnhap = new Intent(MainActivity.this, DangNhapActivity.class);
                        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.clear();
                        editor.apply();
                        startActivity(dangnhap);
                        finish();
                        break;
                    default:
                        Intent ss = new Intent(MainActivity.this, ActivityThongTinCacSanPham.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt("loai", mangloaisp.get(i).getId());
                        bundle.putString("ten", mangloaisp.get(i).getTensanpham());
                        ss.putExtras(bundle);
                        startActivity(ss);
                        break;
                }
            }
        });
    }


    private void getSpMoi() {
        compositeDisposable.add(apiBanHang.getSpMoi().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(sanPhamMoiModel -> {
            if (sanPhamMoiModel.isSuccess()) {
                mangSpMoi = sanPhamMoiModel.getResult();
                spAdapter = new SanPhamMoiAdapter(getApplicationContext(), mangSpMoi);
                recyclerViewManHinhChinh.setAdapter(spAdapter);
            }
        }, throwable -> {
            Toast.makeText(getApplicationContext(), "Không kết nối được server" + throwable.getMessage(), Toast.LENGTH_LONG).show();
        }));
    }

    private void getLoaiSanPham() {
        compositeDisposable.add(apiBanHang.getLoaiSp().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(loaiSpModel -> {
            if (loaiSpModel.isSuccess()) {
                mangloaisp.clear(); // Xóa dữ liệu cũ
                mangloaisp.add(new LoaiSp("Trang chủ", "https://prohome.com.vn/wp-content/uploads/2021/08/vector-icon-bieu-tuong-ngoi-nha-3d-dep-54.png"));
                mangloaisp.addAll(loaiSpModel.getResult());
                mangloaisp.add(new LoaiSp("Thông tin", "https://thegloucesterroad.co.uk/wp-content/uploads/2021/04/the-phone-shop-9381094.jpg"));
                mangloaisp.add(new LoaiSp("Đơn hàng", "https://tse2.mm.bing.net/th?id=OIP.6XBvyykZoIIPPS98ASkCfQHaHk&pid=Api&P=0&h=220"));
                mangloaisp.add(new LoaiSp("Đăng xuất", "https://tse1.mm.bing.net/th?id=OIP.YYdy7rUi0LH3aQ_gjvHjTgHaHa&pid=Api&P=0"));
                //khoi tao adapter
                loaiSpAdapter = new LoaiSpAdapter(getApplicationContext(), mangloaisp);
                listViewManHinhChinh.setAdapter(loaiSpAdapter);
                loaiSpAdapter.notifyDataSetChanged();
            }
        }));
    }


    private void ActionViewFlipper() {
        List<String> mangquangcao = new ArrayList<>();
        mangquangcao.add("https://images.search.yahoo.com/images/view;_ylt=AwrjW6WynHdmvUM5mAGJzbkF;_ylu=c2VjA3NyBHNsawNpbWcEb2lkA2EwYTllMzk2NDZjOGUzM2RlODJlNTRjMGJkOWZlODBiBGdwb3MDMQRpdANiaW5n?back=https%3A%2F%2Fimages.search.yahoo.com%2Fsearch%2Fimages%3Fp%3Dshop%2Bc%25E1%25BB%25ADa%2Bh%25C3%25A0ng%2Bb%25C3%25A1n%2Bgi%25C3%25A0y%26type%3DE210US91215G0%26fr%3Dmcafee%26fr2%3Dpiv-web%26tab%3Dorganic%26ri%3D1&w=800&h=600&imgurl=www.deco-crystal.com%2Fwp-content%2Fuploads%2F2021%2F08%2Fthiet-ke-shop-giay-dep-nho.jpg&rurl=https%3A%2F%2Fnoithatpmax.com%2Fthiet-ke-cua-hang-giay-dep%2F&size=149.3KB&p=shop+c%E1%BB%ADa+h%C3%A0ng+b%C3%A1n+gi%C3%A0y&oid=a0a9e39646c8e33de82e54c0bd9fe80b&fr2=piv-web&fr=mcafee&tt=C%E1%BB%B1c+Hot%3A+20+m%E1%BA%ABu+thi%E1%BA%BFt+k%E1%BA%BF+shop+gi%C3%A0y+d%C3%A9p+%C4%91%E1%BA%B9p%2C+%E1%BA%A5n+t%C6%B0%E1%BB%A3ng+nh%E1%BA%A5t+-+N%E1%BB%99i+Th%E1%BA%A5t+Pmax&b=0&ni=21&no=1&ts=&tab=organic&sigr=kUkkX1vMb4Gm&sigb=Uh2f06yPDCFn&sigi=C9aLe_7FJIpV&sigt=x__dXRm51P5H&.crumb=qkA2YDuQWEg&fr=mcafee&fr2=piv-web&type=E210US91215G0");
        mangquangcao.add("https://images.search.yahoo.com/images/view;_ylt=AwrjW6WynHdmvUM5owGJzbkF;_ylu=c2VjA3NyBHNsawNpbWcEb2lkAzE3NjgzNDkzYmQ4MzlhNDhhYmIxNzdjZGU2OWMxMjYwBGdwb3MDMTIEaXQDYmluZw--?back=https%3A%2F%2Fimages.search.yahoo.com%2Fsearch%2Fimages%3Fp%3Dshop%2Bc%25E1%25BB%25ADa%2Bh%25C3%25A0ng%2Bb%25C3%25A1n%2Bgi%25C3%25A0y%26type%3DE210US91215G0%26fr%3Dmcafee%26fr2%3Dpiv-web%26tab%3Dorganic%26ri%3D12&w=1024&h=683&imgurl=topnlist.com%2Fwp-content%2Fuploads%2F2020%2F08%2Fshop-ban-giay-dep-o-ha-noi-2.jpg&rurl=https%3A%2F%2Ftopnlist.com%2Fshop-ban-giay-dep-nhat-o-ha-noi%2F&size=54.2KB&p=shop+c%E1%BB%ADa+h%C3%A0ng+b%C3%A1n+gi%C3%A0y&oid=17683493bd839a48abb177cde69c1260&fr2=piv-web&fr=mcafee&tt=Top+10+Shop+B%C3%A1n+Gi%C3%A0y+%C4%90%E1%BA%B9p%2C+Uy+T%C3%ADn+%E1%BB%9E+H%C3%A0+N%E1%BB%99i&b=0&ni=21&no=12&ts=&tab=organic&sigr=mkHrQ6aEDp4X&sigb=Gq3JJXiT1J8h&sigi=fpncsnhEUq.l&sigt=aeaHb3u3IFBV&.crumb=qkA2YDuQWEg&fr=mcafee&fr2=piv-web&type=E210US91215G0");
        mangquangcao.add("https://images.search.yahoo.com/images/view;_ylt=AwrjW6WynHdmvUM5nAGJzbkF;_ylu=c2VjA3NyBHNsawNpbWcEb2lkA2Q0MjYyYmY3OWRlMWMwZWRlZGZiNzNlYmUzNGYzYmFmBGdwb3MDNQRpdANiaW5n?back=https%3A%2F%2Fimages.search.yahoo.com%2Fsearch%2Fimages%3Fp%3Dshop%2Bc%25E1%25BB%25ADa%2Bh%25C3%25A0ng%2Bb%25C3%25A1n%2Bgi%25C3%25A0y%26type%3DE210US91215G0%26fr%3Dmcafee%26fr2%3Dpiv-web%26tab%3Dorganic%26ri%3D5&w=960&h=540&imgurl=kenhz.net%2Fwp-content%2Fuploads%2F2021%2F06%2Fgiay-dep-nam-vung-tau.jpg&rurl=https%3A%2F%2Fkenhz.net%2Ftop-cac-shop-ban-giay-dep-nam-cao-cap-tot-nhat-o-vung-tau%2F&size=100.3KB&p=shop+c%E1%BB%ADa+h%C3%A0ng+b%C3%A1n+gi%C3%A0y&oid=d4262bf79de1c0ededfb73ebe34f3baf&fr2=piv-web&fr=mcafee&tt=TOP+c%C3%A1c+shop+b%C3%A1n+gi%C3%A0y+d%C3%A9p+Nam+%C4%91%E1%BA%B9p+t%E1%BB%91t+nh%E1%BA%A5t+%E1%BB%9F+V%C5%A9ng+T%C3%A0u+-+K%C3%AAnh+Z&b=0&ni=21&no=5&ts=&tab=organic&sigr=D9soa4bc0LB6&sigb=oDCO_R20o.LV&sigi=w5x2s_e09WHX&sigt=aaR5XYmLtCXy&.crumb=qkA2YDuQWEg&fr=mcafee&fr2=piv-web&type=E210US91215G0");
        for (int i = 0; i < mangquangcao.size(); i++) {
            ImageView imageView = new ImageView(getApplicationContext());
            Glide.with(getApplicationContext()).load(mangquangcao.get(i)).into(imageView);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            viewFlipper.addView(imageView);
        }
        viewFlipper.setFlipInterval(3000);
        viewFlipper.setAutoStart(true);
        Animation slide_in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_right);
        Animation slide_out = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out_rigth);
        viewFlipper.setInAnimation(slide_in);
        viewFlipper.setOutAnimation(slide_out);
    }

    private void ActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(android.R.drawable.ic_menu_sort_by_size);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    private void Anhxa() {
        imgsearch = findViewById(R.id.imgsearch);
        toolbar = findViewById(R.id.toobarmanhinhchinh);
        viewFlipper = findViewById(R.id.viewlipper);
        recyclerViewManHinhChinh = findViewById(R.id.recycleview);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerViewManHinhChinh.setLayoutManager(layoutManager);
        recyclerViewManHinhChinh.setHasFixedSize(true);
        listViewManHinhChinh = findViewById(R.id.listviewmanhinhchinh);
        navigationView = findViewById(R.id.navigationview);
        drawerLayout = findViewById(R.id.drawerlayout);
        badge = findViewById(R.id.menu_sl);
        frameLayout = findViewById(R.id.framegiohang);
        //khoi tao list
        mangloaisp = new ArrayList<>();

        mangSpMoi = new ArrayList<>();
        if (Paper.book().read("giohang") != null) {
            Utils.manggiohang = Paper.book().read("giohang");
        }
        if (Utils.manggiohang == null) {
            Utils.manggiohang = new ArrayList<>();
        } else {
            int totalItem = 0;
            for (int i = 0; i < Utils.manggiohang.size(); i++) {
                totalItem = totalItem + Utils.manggiohang.get(i).getSoluong();
            }
            badge.setText(String.valueOf(Utils.manggiohang.size()));
        }
        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent giohang = new Intent(getApplicationContext(), GioHangActivity.class);
                startActivity(giohang);
            }
        });
        imgsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        int totalItem = 0;
        for (int i = 0; i < Utils.manggiohang.size(); i++) {
            totalItem = totalItem + Utils.manggiohang.get(i).getSoluong();
        }
        badge.setText(String.valueOf(Utils.manggiohang.size()));
    }

    private boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo moblie = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if ((wifi != null && wifi.isConnected()) || (moblie != null && moblie.isConnected())) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}