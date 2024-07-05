package com.manager.appbanhang.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Spinner;
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

public class QuanLiTaiKhoanAdmin extends AppCompatActivity {
    Toolbar toolbar;
    Toolbar tbback;
    ListView listAdmin;
    ApiBanHang apiBanHang;
    EditText editTextSearch;
    List<Account> list;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    int page = 1;
    AccountAdapter adapterAdmin;
    boolean isLoading = false;
    ImageView imgsearch;
    Button buttonAdd;
    int positionSelected = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_li_tai_khoan_admin);
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        Anhxa();
        getData();
        initToolbar();
        listAdmin.setOnItemClickListener((parent, view, position, id) -> {
            PopupMenu popupMenu = new PopupMenu(QuanLiTaiKhoanAdmin.this, view);

            popupMenu.getMenuInflater().inflate(R.menu.popomenuadmin, popupMenu.getMenu());
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
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlertDialogAdd(view);
            }
        });
        imgsearch.setOnClickListener(v -> {
            String phoneNumber = editTextSearch.getText().toString().trim();
            if (!phoneNumber.isEmpty()) {
                performSearch(phoneNumber);
            } else {
                adapterAdmin.setData(list);
                adapterAdmin.notifyDataSetChanged();
                Toast.makeText(QuanLiTaiKhoanAdmin.this, "Nhập tên tìm kiếm", Toast.LENGTH_SHORT).show();
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
            adapterAdmin.setData(searchResults);
            adapterAdmin.notifyDataSetChanged();
        } else {
            Toast.makeText(QuanLiTaiKhoanAdmin.this, "Không tìm thấy tên thành viên này", Toast.LENGTH_SHORT).show();
        }
    }

    private void getData() {
        compositeDisposable.add(apiBanHang.getAdmin()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleResponse, this::handleError)
        );
    }

    private void handleResponse(AccountModel accountModel) {
        if (accountModel != null && accountModel.isSuccess()) {
            list = accountModel.getResult();
            adapterAdmin = new AccountAdapter(getApplicationContext(), list);
            listAdmin.setAdapter(adapterAdmin);

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
        builder.setTitle("Sửa thông tin admin");

        final View customLayout = getLayoutInflater().inflate(R.layout.update_admin, null);
        builder.setView(customLayout);

        EditText id = customLayout.findViewById(R.id.editTextId);
        EditText email = customLayout.findViewById(R.id.editTextEmail);
        EditText numberPhone = customLayout.findViewById(R.id.editTextPhone);
        EditText address = customLayout.findViewById(R.id.editTextAddress);
        EditText pq = customLayout.findViewById(R.id.editTextPermission);
        Button click = customLayout.findViewById(R.id.buttonSave);
        Account customer = list.get(positionSelected);
        if (customer != null) {
            id.setText(String.valueOf(customer.getId()));
            email.setText(customer.getEmail());
            numberPhone.setText(customer.getMobile());
            address.setText(customer.getAddress());
            pq.setText(customer.getDecentralization());
        }

        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String idUpdate = id.getText().toString();
                    String emailUpdate = email.getText().toString();
                    String phoneNumberUpdate = numberPhone.getText().toString();
                    String addressUpdate = address.getText().toString();
                    String decentral = pq.getText().toString();

                    updateAdmin(emailUpdate, phoneNumberUpdate, addressUpdate, Integer.parseInt(idUpdate),decentral);

                    Toast.makeText(QuanLiTaiKhoanAdmin.this, "Cập nhật thông tin thành công", Toast.LENGTH_SHORT).show();

                    list.get(positionSelected).setEmail(emailUpdate);
                    list.get(positionSelected).setMobile(phoneNumberUpdate);
                    list.get(positionSelected).setAddress(addressUpdate);
                    adapterAdmin.notifyDataSetChanged();

                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    Toast.makeText(QuanLiTaiKhoanAdmin.this, "Lỗi: ID không hợp lệ", Toast.LENGTH_SHORT).show();
                }
            }
        });


        builder.setPositiveButton("Hủy bỏ", (dialog, which) -> {
            dialog.dismiss();
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    public void showAlertDialogAdd(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View customLayout = getLayoutInflater().inflate(R.layout.addadmin, null);
        builder.setView(customLayout);

        EditText email = customLayout.findViewById(R.id.email);
        EditText username = customLayout.findViewById(R.id.usernname);
        EditText pass = customLayout.findViewById(R.id.pass);
        EditText mobile = customLayout.findViewById(R.id.mobile);
        EditText address = customLayout.findViewById(R.id.address);
        Spinner permission =customLayout.findViewById(R.id.permission);
        Button btnAdd = customLayout.findViewById(R.id.btnadd);
        List<String> spinner = new ArrayList();
        spinner.add("Admin");
        spinner.add("Nhân Viên");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,
                spinner);
        permission.setAdapter(adapter);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str_email = email.getText().toString().trim();
                String str_user = username.getText().toString().trim();
                String str_pass = pass.getText().toString().trim();
                String str_mobile = mobile.getText().toString().trim();
                String str_address = address.getText().toString().trim();
                String selectedItem = permission.getSelectedItem().toString();
                if (TextUtils.isEmpty(str_email)) {
                    Toast.makeText(getApplicationContext(), "Bạn chưa nhập Email", Toast.LENGTH_LONG).show();
                } else if (TextUtils.isEmpty(str_pass)) {
                    Toast.makeText(getApplicationContext(), "Bạn chưa nhập Pass", Toast.LENGTH_LONG).show();
                } else if (TextUtils.isEmpty(str_mobile)) {
                    Toast.makeText(getApplicationContext(), "Bạn chưa nhập Mobile", Toast.LENGTH_LONG).show();
                } else if (TextUtils.isEmpty(str_user)) {
                    Toast.makeText(getApplicationContext(), "Bạn chưa nhập Username", Toast.LENGTH_LONG).show();
                } else if (TextUtils.isEmpty(str_address)) {
                    Toast.makeText(getApplicationContext(), "Bạn chưa nhập Address", Toast.LENGTH_LONG).show();
                } else {
                    //post data

                    compositeDisposable.add(apiBanHang.dangkiadmin(str_email, str_pass, str_user, str_mobile, str_address,selectedItem)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    userModel -> {
                                        if (userModel.isSuccess()) {
                                            Toast.makeText(QuanLiTaiKhoanAdmin.this, "Thêm nhân viên thành công", Toast.LENGTH_SHORT).show();
                                            getData();
                                            adapterAdmin.notifyDataSetChanged();
                                        } else {
                                            Toast.makeText(getApplicationContext(), userModel.getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    },
                                    throwable -> {
                                        Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                            ));
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
        builder.setTitle("Xác nhận xóa Admin");
        builder.setMessage("Bạn có muốn xóa Admin?");
        builder.setPositiveButton("Đồng ý", (dialog, which) -> {
            deleteCustomer(id);
        });
        builder.setNegativeButton("Không", (dialog, which) -> {

            dialog.dismiss();
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void updateAdmin(String email,String mobile,String address,int id,String decent) {
        apiBanHang.updateAdmin(email,mobile,address,id,decent)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    adapterAdmin.notifyDataSetChanged();
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
        apiBanHang.xoaAdmin(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {

                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).getId() == id) {
                            list.remove(i);
                            break;
                        }
                    }

                    adapterAdmin.notifyDataSetChanged();
                }, throwable -> {

                });
    }


    private void Anhxa() {
        toolbar = findViewById(R.id.toobar);
        listAdmin = findViewById(R.id.listadmin);
        imgsearch = findViewById(R.id.img_search);
        list = new ArrayList<>();
        editTextSearch = findViewById(R.id.editTextSearch);
        buttonAdd = findViewById(R.id.btn_bottom);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}
