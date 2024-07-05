package com.manager.appbanhang.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.appbanhang.R;
import com.manager.appbanhang.adapter.DonHangAdapter;
import com.manager.appbanhang.model.DonHang;
import com.manager.appbanhang.model.EventBus.DonHangEvent;
import com.manager.appbanhang.retrofit.ApiBanHang;
import com.manager.appbanhang.retrofit.RetrofitClient;
import com.manager.appbanhang.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class XemDonHangActivity extends AppCompatActivity {
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiBanHang apiBanHang;
    RecyclerView redonhang;
    Toolbar toolbar;
    DonHang donHang;
    int tinhtrang;
    AlertDialog dialog;
    private SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy/MM/dd", Locale.US);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xem_don_hang);
        initView();
        initToolbar();
        getOrder();

    }

    private void getOrder() {
        compositeDisposable.add(apiBanHang.xemDonHang(0)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        donHangModel -> {
                            DonHangAdapter adapter = new DonHangAdapter(getApplicationContext(),donHangModel.getResult());
                            redonhang.setAdapter(adapter);
                        }
                ));

    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void initView() {
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        redonhang = findViewById(R.id.recycleview_donhang);
        toolbar = findViewById(R.id.toobar);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        redonhang.setLayoutManager(layoutManager);

    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }


    private void showCustumDialog() {
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_donhang,null);
        Spinner spinner = view.findViewById(R.id.spinner_dialog);
        AppCompatButton btndongy = view.findViewById(R.id.dongy_dialog);
        List<String> list = new ArrayList<>();
        list.add("Đơn hàng đang được xử lý");
        list.add("Đơn hàng đã chấp nhận");
        list.add("Đơn hàng đã giao cho đơn vị vận chuyển");
        list.add("Giao Thành công");
        list.add("Đơn hàng đã hủy");
        list.add("Thêm ngày giao hàng");
        list.add("Thêm ghi chú");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,list);
        spinner.setAdapter(adapter);
        spinner.setSelection(donHang.getTrangthai());

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
                tinhtrang = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btndongy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                capNhapDonHang();
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        dialog = builder.create();
        dialog.show();

    }

    private void capNhapDonHang() {
        if(tinhtrang < 5){
            compositeDisposable.add(apiBanHang.updateOrder(donHang.getId(),tinhtrang)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            messageModel -> {
                                getOrder();
                                dialog.dismiss();
                            },
                            throwable -> {

                            }
                    ));
        }
        else if(tinhtrang == 6){
            LayoutInflater inflater = getLayoutInflater();
            View view = inflater.inflate(R.layout.themghichu,null);
             Spinner note = view.findViewById(R.id.note);
            List<String> categories = new ArrayList<>();
            categories.add("Giao Thành công");
            categories.add(" Đơn hàng đã chấp nhận");
            categories.add("Đơn hàng đang được xử lý");
            categories.add("Chưa nhận hàng");
            categories.add("Đơn hàng đã hủy");
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            note.setAdapter(adapter);
            AppCompatButton btndongy = view.findViewById(R.id.dongy_dialog);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(view);
            dialog = builder.create();
            dialog.show();

            btndongy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        compositeDisposable.add(apiBanHang.updateNote(donHang.getId(),note.getSelectedItem().toString())
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(
                                        messageModel -> {
                                            Toast.makeText(XemDonHangActivity.this, "Thành công", Toast.LENGTH_SHORT).show();
                                            dialog.dismiss();
                                        },
                                        throwable -> {

                                        }
                                ));
                        dialog.dismiss();

                }
            });
        }
        else {
            LayoutInflater inflater = getLayoutInflater();
            View view = inflater.inflate(R.layout.themghichu_ngay,null);
            EditText date = view.findViewById(R.id.date);
            List<String> categories = new ArrayList<>();
            AppCompatButton btndongy = view.findViewById(R.id.dongy_dialog);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(view);
            dialog = builder.create();
            dialog.show();
            date.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDatePickerDialog(date);
                }
            });

            btndongy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String inputDate = date.getText().toString();
                    if (isValidDate(inputDate)) {
                        compositeDisposable.add(apiBanHang.updateNoteandDate(donHang.getId(),inputDate)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(
                                        messageModel -> {
                                            Toast.makeText(XemDonHangActivity.this, "Thành công", Toast.LENGTH_SHORT).show();
                                            dialog.dismiss();
                                        },
                                        throwable -> {

                                        }
                                ));
                        dialog.dismiss();
                    } else {
                        Toast.makeText(XemDonHangActivity.this, "Vui lòng nhập đúng định dạng là yyyy/MM.dd", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
    private boolean isValidDate(String inputDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.US);
        dateFormat.setLenient(false);
        try {
            // Parsing the input string as a date
            dateFormat.parse(inputDate);
            return true;
        } catch (ParseException e) {
            // Thrown if the input string does not match the specified format
            return false;
        }
    }

    private void showDatePickerDialog(final EditText dateEditText) {
        final Calendar newCalendar = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                dateEditText.setText(dateFormatter.format(newDate.getTime()));
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }
    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public  void evenDonHang(DonHangEvent event){
        if (event != null){
            donHang = event.getDonHang();
            showCustumDialog();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}