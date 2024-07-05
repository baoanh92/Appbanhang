package com.manager.appbanhang.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.appbanhang.R;
import com.manager.appbanhang.adapter.AccountAdapter;
import com.manager.appbanhang.model.Account;
import com.manager.appbanhang.model.AccountModel;
import com.manager.appbanhang.retrofit.ApiBanHang;
import com.manager.appbanhang.retrofit.RetrofitClient;
import com.manager.appbanhang.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class QuanLyKhachHang extends AppCompatActivity {
    Toolbar toolbar;
    Toolbar tbback;
    ListView listKhachHang;
    ApiBanHang apiBanHang;
    EditText editTextSearch;
    List<Account> list;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    int page = 1;
    AccountAdapter adapterKhachHang;
    boolean isLoading = false;
    ImageView imgsearch;
    int positionSelected = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly_khach_hang);
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        Anhxa();
        getData();
        initToolbar();
        listKhachHang.setOnItemClickListener((parent, view, position, id) -> {
            PopupMenu popupMenu = new PopupMenu(QuanLyKhachHang.this, view);

            popupMenu.getMenuInflater().inflate(R.menu.popupmenu, popupMenu.getMenu());
            positionSelected = position;

            popupMenu.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.xoa:
                        int customerId = list.get(position).getId();
                        showDeleteCustomerPopup(customerId);
                        return true;
                    case R.id.sua:
                        showAlertDialogButtonClicked(view);
                        return true;
                    default:
                        return false;
                }
            });

            popupMenu.show();
        });
        imgsearch.setOnClickListener(v -> {
            String phoneNumber = editTextSearch.getText().toString().trim();
            if (!phoneNumber.isEmpty()) {
                performSearch(phoneNumber);
            } else {
                adapterKhachHang.setData(list);
                adapterKhachHang.notifyDataSetChanged();
                Toast.makeText(QuanLyKhachHang.this, "Nhập số điện thoại tìm kiếm", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initToolbar() {
            tbback = findViewById(R.id.tbback);
            setSupportActionBar(tbback);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            tbback.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }


    private void performSearch(String phoneNumber) {
        List<Account> searchResults = new ArrayList<>();
        for (Account customer : list) {
            if (customer.getMobile().equals(phoneNumber)) {
                searchResults.add(customer);
            }
        }
        if (!searchResults.isEmpty()) {
            adapterKhachHang.setData(searchResults);
            adapterKhachHang.notifyDataSetChanged();
        } else {
            Toast.makeText(QuanLyKhachHang.this, "Không tìm thấy khách hàng với số điện thoại này", Toast.LENGTH_SHORT).show();
        }
    }

    private void getData() {
        compositeDisposable.add(apiBanHang.getKhachHang()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleResponse, this::handleError)
        );
    }

    private void handleResponse(AccountModel accountModel) {
        if (accountModel != null && accountModel.isSuccess()) {
            if (adapterKhachHang == null) {
                list = accountModel.getResult();
                adapterKhachHang = new AccountAdapter(getApplicationContext(), list);
                listKhachHang.setAdapter(adapterKhachHang);
            }
        } else {
            Toast.makeText(getApplicationContext(), "Hết dữ liệu", Toast.LENGTH_LONG).show();
            isLoading = true;
        }
    }

    private void handleError(Throwable throwable) {
        Log.d("mylog", throwable.getMessage());
        Toast.makeText(getApplicationContext(), "Không kết nối được server", Toast.LENGTH_LONG).show();
    }
    public void showAlertDialogButtonClicked(View view) {
        if (list == null || positionSelected < 0 || positionSelected >= list.size()) {
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sửa thông tin khách hàng");

        final View customLayout = getLayoutInflater().inflate(R.layout.update_khach_hang, null);
        builder.setView(customLayout);

        EditText id = customLayout.findViewById(R.id.editTextId);
        EditText email = customLayout.findViewById(R.id.editTextEmail);
        EditText numberPhone = customLayout.findViewById(R.id.editTextPhoneNumber);
        EditText address = customLayout.findViewById(R.id.editTextAddress);
        Button click = customLayout.findViewById(R.id.buttonSave);
        Account customer = list.get(positionSelected);
        if (customer != null) {
            id.setText(String.valueOf(customer.getId()));
            email.setText(customer.getEmail());
            numberPhone.setText(customer.getMobile());
            address.setText(customer.getAddress());
        }

        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String idUpdate = id.getText().toString();
                    String emailUpdate = email.getText().toString();
                    String phoneNumberUpdate = numberPhone.getText().toString();
                    String addressUpdate = address.getText().toString();


                    updateKhachHang(emailUpdate, phoneNumberUpdate, addressUpdate, Integer.parseInt(idUpdate));

                    Toast.makeText(QuanLyKhachHang.this, "Cập nhật thông tin thành công", Toast.LENGTH_SHORT).show();

                    list.get(positionSelected).setEmail(emailUpdate);
                    list.get(positionSelected).setMobile(phoneNumberUpdate);
                    list.get(positionSelected).setAddress(addressUpdate);
                    adapterKhachHang.notifyDataSetChanged();

                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    Toast.makeText(QuanLyKhachHang.this, "Lỗi: ID không hợp lệ", Toast.LENGTH_SHORT).show();
                }
            }
        });


        builder.setPositiveButton("Hủy bỏ", (dialog, which) -> {
            dialog.dismiss();
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void showDeleteCustomerPopup(int id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Xác nhận xóa khách hàng");
        builder.setMessage("Bạn có muốn xóa khách hàng này không?");
        builder.setPositiveButton("Đồng ý", (dialog, which) -> {
            deleteCustomer(id);
        });
        builder.setNegativeButton("Hủy bỏ", (dialog, which) -> {

            dialog.dismiss();
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void updateKhachHang(String email,String mobile,String address,int id) {
        apiBanHang.updateKhachHang(email,mobile,address,id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    adapterKhachHang.notifyDataSetChanged();
                }, throwable -> {

                });
    }
    private void ActionToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void deleteCustomer(int id) {
        apiBanHang.xoaKhachHang(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {

                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).getId() == id) {
                            list.remove(i);
                            break;
                        }
                    }

                    adapterKhachHang.notifyDataSetChanged();
                }, throwable -> {

                });
    }


    private void Anhxa() {
        toolbar = findViewById(R.id.toobar);
        listKhachHang = findViewById(R.id.listkhachhang);
        imgsearch = findViewById(R.id.img_search);
        list = new ArrayList<>();
        editTextSearch = findViewById(R.id.editTextSearch);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}
