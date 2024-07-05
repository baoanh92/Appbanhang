package com.manager.appbanhang.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appbanhang.R;
import com.manager.appbanhang.retrofit.ApiBanHang;
import com.manager.appbanhang.retrofit.RetrofitClient;
import com.manager.appbanhang.utils.Utils;

import io.paperdb.Paper;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DangNhapActivity extends AppCompatActivity {
    TextView txtdangki, txtresetpass;
    EditText email, pass;
    AppCompatButton btndangnhap;
    ApiBanHang apiBanHang;
    CompositeDisposable  compositeDisposable =  new CompositeDisposable();
    boolean isLogin = false;
    CheckBox checkAdmin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_nhap);
        initView();
        initControll();
    }

    private void initControll() {
        txtdangki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DangKiActivity.class);
                startActivity(intent);
            }
        });

        btndangnhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str_email = email.getText().toString().trim();
                String str_pass = pass.getText().toString().trim();
                if (TextUtils.isEmpty(str_email)) {
                    Toast.makeText(getApplicationContext(), "Bạn chưa nhập Email", Toast.LENGTH_LONG).show();
                } else if (TextUtils.isEmpty(str_pass)) {
                    Toast.makeText(getApplicationContext(), "Bạn chưa nhập Pass", Toast.LENGTH_LONG).show();
                }else {
                    //lưu thông tin lần đăng nhập sau
                    Paper.book().write("email", str_email);
                    Paper.book().write("pass", str_pass);
                    dangNhap(str_email, str_pass);

                }
            }
        });

    }

    private void initView() {
        Paper.init(this);
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        txtdangki =findViewById(R.id.txtdangki);
        email =findViewById(R.id.email);
        pass =findViewById(R.id.pass);
        checkAdmin = findViewById(R.id.admin_checkbox);
        btndangnhap =findViewById(R.id.btndangnhap);

        if (Paper.book().read("email") != null && Paper.book().read("pass") != null){
            email.setText(Paper.book().read("email"));
            pass.setText(Paper.book().read("pass"));
            if (Paper.book().read("islogin") != null){
                boolean flag = Paper.book().read("islogin");
                if (flag){
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
//                            dangNhap(Paper.book().read("email"),Paper.book().read("pass"));

                        }
                    },1200);
                }
            }
        }

    }

    private void dangNhap(String email, String pass) {
        if(checkAdmin.isChecked()) {
            compositeDisposable.add(apiBanHang.dangnhapadmin(email, pass)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            userModel -> {
                                if (userModel.isSuccess()) {
                                    isLogin = true;
                                    Paper.book().write("islogin", isLogin);
                                    Utils.user_current = userModel.getResult().get(0);
                                    String permission = userModel.getResult().get(0).getDecentralization();
                                    Paper.book().write("user", userModel.getResult().get(0));
                                    Intent intent = new Intent(getApplicationContext(), AdminActivity.class);
                                    startActivity(intent);
                                    finish();
                                    SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("email", email);
                                    editor.putString("permission",permission);
                                    editor.apply();
                                }
                                else {
                                    Toast.makeText(getApplicationContext(), "Email hoặc password sai", Toast.LENGTH_LONG).show();
                                }
                            },
                            throwable -> {
                                Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_LONG).show();
                            }
                    ));
        }
        else{
            compositeDisposable.add(apiBanHang.dangnhap(email,pass)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            userModel -> {
                                if(userModel.isSuccess()){
                                    isLogin = true;
                                    Paper.book().write("islogin", isLogin);
                                    Utils.user_current = userModel.getResult().get(0);
                                    //Save thoong tin khách hàng
                                    SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putInt("id", userModel.getResult().get(0).getId());
                                    editor.apply();
                                    SharedPreferences sharedPreferences1 = getSharedPreferences("MySharedPref1", MODE_PRIVATE);
                                    SharedPreferences.Editor editor1 = sharedPreferences1.edit();
                                    editor1.putString("email", email);
                                    editor1.apply();
                                    Paper.book().write("user", userModel.getResult().get(0));
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                                else {
                                    Toast.makeText(getApplicationContext(), "Email hoặc password sai", Toast.LENGTH_LONG).show();
                                }
                            },
                            throwable -> {
                                Toast.makeText(getApplicationContext(), "Lỗi đăng nhập: " + throwable.getMessage(), Toast.LENGTH_LONG).show();
                            }
                    ));
        }
}


    @Override
    protected void onResume() {
        super.onResume();
        if(Utils.user_current.getEmail() != null && Utils.user_current.getPass() != null){
            email.setText(Utils.user_current.getEmail());
            pass.setText(Utils.user_current.getPass());
        }
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}