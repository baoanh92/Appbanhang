package com.manager.appbanhang.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.appbanhang.R;

public class MenuQuanLy extends AppCompatActivity {

    private Button btnManageUserAccount;
    private Button btnManageProduct;
    private Button btnManageAdmin;
    private Button btnManageCategory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly);


        btnManageUserAccount = findViewById(R.id.btnManageUserAccount);
        btnManageProduct = findViewById(R.id.btnManageProduct);
        btnManageAdmin = findViewById(R.id.btnManageAdmin);
        btnManageCategory = findViewById(R.id.btnManageDanhMuc);
        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        String sharedData = sharedPreferences.getString("permission", ""); // Provide a default value if the key is not found


        btnManageProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!sharedData.equals("Admin")){
                    Toast.makeText(MenuQuanLy.this, "Bạn không có quyền phần này", Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent intent = new Intent(MenuQuanLy.this, QuanLiActivity.class);
                    startActivity(intent);
                }

            }
        });
        btnManageUserAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent intent = new Intent(MenuQuanLy.this, QuanLyKhachHang.class);
                    startActivity(intent);
                

            }
        });
        btnManageAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!sharedData.equals("Admin")){
                    Toast.makeText(MenuQuanLy.this, "Bạn không có quyền phần này", Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent intent = new Intent(MenuQuanLy.this, QuanLiTaiKhoanAdmin.class);
                    startActivity(intent);
                }
            }
        });
        btnManageCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!sharedData.equals("Admin")){
                    Toast.makeText(MenuQuanLy.this, "Bạn không có quyền phần này", Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent intent = new Intent(MenuQuanLy.this,DanhMucSanPham.class);
                    startActivity(intent);
                }
            }
        });
    }

    public void cLICKbACKhOME(View view) {
        Intent intent = new Intent(MenuQuanLy.this,MainActivity.class);
        startActivity(intent);
    }
}