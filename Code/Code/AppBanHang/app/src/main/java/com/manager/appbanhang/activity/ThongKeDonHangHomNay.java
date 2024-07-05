package com.manager.appbanhang.activity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.appbanhang.R;
import com.manager.appbanhang.adapter.ThongTinDonHangAdapter;
import com.manager.appbanhang.model.ThongKeNgayModel;
import com.manager.appbanhang.model.ThongTinDonHang;
import com.manager.appbanhang.retrofit.ApiBanHang;
import com.manager.appbanhang.retrofit.RetrofitClient;
import com.manager.appbanhang.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ThongKeDonHangHomNay extends AppCompatActivity {

    private ListView list;
    private ApiBanHang apiBanHang;
    private List<ThongTinDonHang> listDonHang;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private ThongTinDonHangAdapter adapter;
    private EditText searchEditText;
    private EditText dateEditText;
    private Button buttonDate;
    private Toolbar tbback;
    private Calendar calendar;
    private Spinner spinner;
    private String selectedItem;
    private List<ThongTinDonHang> filteredList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_ke_don_hang_hom_nay);
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);

        list = findViewById(R.id.list_item_sell);
        searchEditText = findViewById(R.id.search);
        dateEditText = findViewById(R.id.date);
        buttonDate = findViewById(R.id.buttonDate);

        calendar = Calendar.getInstance();
        updateDateEditText();
        getData();
        initToolbar();
        setupSearchListener();
        buttonDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });
        setupSpinner();
    }

    private void setupSpinner() {
        spinner = findViewById(R.id.spinner);
        List<String> categories = new ArrayList<>();
        categories.add("Xem tất cả đơn hàng");
        categories.add("Giao Thành công");
        categories.add(" Đơn hàng đã chấp nhận");
        categories.add("Đơn hàng đang được xử lý");
        categories.add("Chưa nhận hàng");
        categories.add("Đơn hàng đã hủy");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedItem = parent.getItemAtPosition(position).toString();
                if(selectedItem.equals("Xem tất cả đơn hàng")){
                    filterList(null);
                }
                else{
                    filterList(selectedItem);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                filterList(null);
            }
        });

    }

    private void filterList(String keyword) {
        if (keyword == null) {
            getData();
        } else {
            if (filteredList == null) {
                filteredList = new ArrayList<>();
            } else {
                filteredList.clear();
            }
            if (listDonHang != null) {
                for (ThongTinDonHang donHang : listDonHang) {
                    if (donHang != null && donHang.getGhiChu() != null && donHang.getGhiChu().equals(keyword)) {
                        filteredList.add(donHang);
                    }
                }
                adapter = new ThongTinDonHangAdapter(getApplicationContext(), filteredList);
                list.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        }
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

    private void setupSearchListener() {
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String keyword = editable.toString();
                if (adapter != null) {
                    adapter.filter(keyword);
                }
            }
        });
    }

    public void getData() {
        String selectedDate = dateEditText.getText().toString();
        if (selectedDate.isEmpty()) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
            selectedDate = sdf.format(Calendar.getInstance().getTime());
        }
        compositeDisposable.add(apiBanHang.getSanPhamSellByDate(selectedDate)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleResponse, this::handleError)
        );
    }

    private void handleResponse(ThongKeNgayModel thongKeNgayModel) {
        if (thongKeNgayModel != null && thongKeNgayModel.isSuccess()) {
            listDonHang = thongKeNgayModel.getResult();
            adapter = new ThongTinDonHangAdapter(getApplicationContext(), listDonHang);
            list.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        } else {
            listDonHang = new ArrayList<>();
            adapter = new ThongTinDonHangAdapter(getApplicationContext(), listDonHang);
            list.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

    private void handleError(Throwable throwable) {
        Log.d("mylog", throwable.getMessage());
        Toast.makeText(getApplicationContext(), "Không kết nối được server", Toast.LENGTH_LONG).show();
    }

    private void showDatePickerDialog() {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int selectedYear,
                                          int selectedMonth, int selectedDay) {
                        calendar.set(Calendar.YEAR, selectedYear);
                        calendar.set(Calendar.MONTH, selectedMonth);
                        calendar.set(Calendar.DAY_OF_MONTH, selectedDay);
                        updateDateEditText();
                        getData(); // Cập nhật dữ liệu sau khi người dùng chọn ngày
                    }
                }, year, month, day);

        datePickerDialog.show();
    }

    private void updateDateEditText() {
        String myFormat = "yyyy/MM/dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
        dateEditText.setText(sdf.format(calendar.getTime()));
    }
}
