package com.manager.appbanhang.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.appbanhang.R;
import com.manager.appbanhang.adapter.AccountAdapter;
import com.manager.appbanhang.adapter.ListSanPham;
import com.manager.appbanhang.model.Account;
import com.manager.appbanhang.model.AccountModel;
import com.manager.appbanhang.model.LoaiSp;
import com.manager.appbanhang.model.LoaiSpModel;
import com.manager.appbanhang.retrofit.ApiBanHang;
import com.manager.appbanhang.retrofit.RetrofitClient;
import com.manager.appbanhang.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DanhMucSanPham extends AppCompatActivity {
    ListView listcategory;
    ApiBanHang apiBanHang;
    List<LoaiSp> list;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ListSanPham adapterKhachHang;
    boolean isLoading = false;
    int positionSelected = -1;
    Button button;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_muc_san_pham);
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        Anhxa();
        getData();
        initToolbar();
        listcategory.setOnItemClickListener((parent, view, position, id) -> {
            PopupMenu popupMenu = new PopupMenu(DanhMucSanPham.this, view);

            popupMenu.getMenuInflater().inflate(R.menu.popupcategory, popupMenu.getMenu());
            positionSelected = position;

            popupMenu.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.xoa:
                        int idsp = list.get(position).getId();
                        showDeleteCustomerPopup(idsp);
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
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlertDialogAdd(view);
            }
        });
    }

    private void initToolbar() {
        toolbar = findViewById(R.id.toobar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getData() {
        compositeDisposable.add(apiBanHang.getLoaiSpSell()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleResponse, this::handleError)
        );
    }

    private void handleResponse(LoaiSpModel loaiSpModel) {
        if (loaiSpModel != null && loaiSpModel.isSuccess()) {
            list = loaiSpModel.getResult();
            adapterKhachHang = new ListSanPham(getApplicationContext(), list);
            listcategory.setAdapter(adapterKhachHang);

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
        builder.setTitle("Sửa thông tin danh mục");

        final View customLayout = getLayoutInflater().inflate(R.layout.updatesanpham, null);
        builder.setView(customLayout);

        EditText name = customLayout.findViewById(R.id.name);
        EditText imageView = customLayout.findViewById(R.id.iamgeLink);
        EditText id = customLayout.findViewById(R.id.id);
        Button click = customLayout.findViewById(R.id.addSp);
        LoaiSp loaisp = list.get(positionSelected);
        if (loaisp != null) {
            name.setText(loaisp.getTensanpham());
            imageView.setText(loaisp.getHinhanh());
            id.setText(String.valueOf(loaisp.getId()));
        }

        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String newName = name.getText().toString();
                    String linkImage = imageView.getText().toString();
                    int idNew = Integer.parseInt(id.getText().toString());
                    updateCategory(newName, linkImage, idNew);
                    list.get(positionSelected).setTensanpham(newName);
                    Toast.makeText(DanhMucSanPham.this, "Cập nhật thông tin thành công", Toast.LENGTH_SHORT).show();
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    Toast.makeText(DanhMucSanPham.this, "Lỗi: ID không hợp lệ", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setPositiveButton("Hủy bỏ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }



    private void showDeleteCustomerPopup(int id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Xác nhận xóa danh mục");
        builder.setMessage("Bạn có muốn xóa danh mục này không?");
        builder.setPositiveButton("Đồng ý", (dialog, which) -> {
            deleteDanhMuc(id);
        });
        builder.setNegativeButton("Hủy bỏ", (dialog, which) -> {

            dialog.dismiss();
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    public void showAlertDialogAdd(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View customLayout = getLayoutInflater().inflate(R.layout.themsanpham, null);
        builder.setView(customLayout);

        EditText imageLink = customLayout.findViewById(R.id.iamgeLink);
        EditText name = customLayout.findViewById(R.id.name);

        builder.setPositiveButton("Thêm", null); // We'll set the click listener later

        builder.setNegativeButton("Hủy bỏ", (dialog, which) -> {
            dialog.dismiss(); // Dismiss the dialog if "Hủy bỏ" is clicked
        });

        AlertDialog dialog = builder.create();

        // Override the positive button listener to handle custom logic
        dialog.setOnShowListener(dialogInterface -> {
            Button btnAdd = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            btnAdd.setOnClickListener(v -> {
                String link = imageLink.getText().toString().trim();
                String nameNew = name.getText().toString().trim();

                if (TextUtils.isEmpty(link)) {
                    Toast.makeText(getApplicationContext(), "Bạn chưa nhập ảnh", Toast.LENGTH_LONG).show();
                } else if (TextUtils.isEmpty(nameNew)) {
                    Toast.makeText(getApplicationContext(), "Bạn chưa nhập tên", Toast.LENGTH_LONG).show();
                } else {
                    // Post data
                    compositeDisposable.add(apiBanHang.addCatogory(nameNew, link)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    userModel -> {
                                        if (userModel.isSuccess()) {
                                            Toast.makeText(DanhMucSanPham.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                                            getData();
                                            adapterKhachHang.notifyDataSetChanged();
                                            dialog.dismiss(); // Dismiss dialog on success
                                        } else {
                                            Toast.makeText(getApplicationContext(), userModel.getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    },
                                    throwable -> {
                                        Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                            ));
                }
            });
        });

        dialog.show();
    }



    private void updateCategory(String name,String hinhanh,int id) {
        apiBanHang.updateCategory(id,name,hinhanh)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    adapterKhachHang.notifyDataSetChanged();
                }, throwable -> {

                });
    }
    private void deleteDanhMuc(int id) {
        apiBanHang.xoaDanhMuc(id)
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
        listcategory = findViewById(R.id.listcategory);
        list = new ArrayList<>();
        button = findViewById(R.id.btn_bottom);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}
