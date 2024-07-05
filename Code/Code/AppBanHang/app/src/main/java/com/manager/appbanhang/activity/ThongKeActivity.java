package com.manager.appbanhang.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.appbanhang.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.common.api.Api;
import com.manager.appbanhang.retrofit.ApiBanHang;
import com.manager.appbanhang.retrofit.RetrofitClient;
import com.manager.appbanhang.utils.Utils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ThongKeActivity extends AppCompatActivity {
    Toolbar toolbar;
    PieChart pieChart;
    BarChart barChart;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiBanHang apiBanHang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_ke);
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        initView();
        ActionToolBar();
        getdataChart();
        settingBarchart();
    }

    private void settingBarchart() {
        barChart.getDescription().setEnabled(false);
        barChart.setDrawValueAboveBar(false);
        XAxis xAxis = barChart.getXAxis();
        xAxis.setAxisMinimum(1);
        xAxis.setAxisMaximum(12);
        YAxis yAxisright = barChart.getAxisRight();
        yAxisright.setAxisMinimum(0);
        YAxis yAxisLeft = barChart.getAxisLeft();
        yAxisLeft.setAxisMinimum(0);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_thongke,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.tkthang:
                getTkthang();
                return true;
            case R.id.tktong:
                getTongDoanhThu();
                return true;
            case R.id.tkngay:
                Intent intent = new Intent(ThongKeActivity.this,ThongKeDonHangHomNay.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getTkthang() {
        barChart.setVisibility(View.VISIBLE);
        pieChart.setVisibility(View.GONE);
        compositeDisposable.add(apiBanHang.getthongkeThang()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                   thongKeModel -> {
                       if (thongKeModel.isSuccess()){
                           List<BarEntry> listdata = new ArrayList<>();
                           for (int i=0;i<thongKeModel.getResult().size();i++){
                               String tongtien = thongKeModel.getResult().get(i).getTongtienthang();
                               String thang = thongKeModel.getResult().get(i).getThang();
                               listdata.add(new BarEntry(Integer.parseInt(thang),Float.parseFloat(tongtien)));
                           }
                           BarDataSet barDataSet = new BarDataSet(listdata,"Thống kê");
                           barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
                           barDataSet.setValueTextSize(14f);
                           barDataSet.setValueTextColor(Color.RED);

                           BarData data = new BarData(barDataSet);
                           barChart.animateXY(2000,2000);
                           barChart.setData(data);
                           barChart.invalidate();
                       }
                   },
                        throwable -> {
                       Log.d("logg",throwable.getMessage());
                        }
                ));
    }
    private void getTongDoanhThu() {
        compositeDisposable.add(apiBanHang.getthongkeThang()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        thongKeModel -> {
                            if (thongKeModel.isSuccess()){
                                float tongDoanhThu = 0;
                                for (int i = 0; i < thongKeModel.getResult().size(); i++){
                                    String tongtien = thongKeModel.getResult().get(i).getTongtienthang();
                                    float tt = Float.parseFloat(tongtien);
                                    tongDoanhThu += tt;
                                }
                                showTotalRevenueDialog(tongDoanhThu);
                            }
                        },
                        throwable -> {
                            Log.d("logg", throwable.getMessage());
                        }
                ));
    }
    private void getThongKeNgay() {
        compositeDisposable.add(apiBanHang.getThongKeNgay()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        thongKeNgayModel -> {
                            if (thongKeNgayModel.isSuccess()) {
                                float tongDoanhThu = thongKeNgayModel.getTongDoanhThuNgay();
                                showTotalRevenueTodayDialog(tongDoanhThu);
                            } else {
                                Log.d("logg", "Không thành công: " + thongKeNgayModel.getMessage());
                            }
                        },
                        throwable -> {
                            Log.d("logg", "Lỗi khi lấy dữ liệu: " + throwable.getMessage());
                        }
                ));
    }

    private void showTotalRevenueDialog(float totalRevenue) {

        DecimalFormat decimalFormat = new DecimalFormat("#,###.##");
        String formattedTotalRevenue = decimalFormat.format(totalRevenue);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Tổng Doanh Thu");
        builder.setMessage("Tổng doanh thu là: " + formattedTotalRevenue + " VND");
        builder.setPositiveButton("Đóng", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void showTotalRevenueTodayDialog(float totalRevenue) {
        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();

        DecimalFormat decimalFormat = new DecimalFormat("#,###.##");
        String formattedTotalRevenue = decimalFormat.format(totalRevenue);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Tổng Doanh Thu " + currentDate);
        builder.setMessage("Tổng doanh thu hôm nay là: " + formattedTotalRevenue + " VND");
        builder.setPositiveButton("Đóng", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void getdataChart() {
        List<PieEntry> listdata = new ArrayList<>();
        compositeDisposable.add(apiBanHang.getthongke()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        thongKeModel -> {
                            if (thongKeModel.isSuccess()){
                                for (int i= 0; i<thongKeModel.getResult().size();i++){
                                    String tensp = thongKeModel.getResult().get(i).getTensp();
                                    int tong = thongKeModel.getResult().get(i).getTong();
                                    listdata.add(new PieEntry(tong, tensp));
                                }
                                PieDataSet pieDataSet = new PieDataSet(listdata,"Thống kê");
                                PieData data = new PieData();
                                data.setDataSet(pieDataSet);
                                data.setValueTextSize(12f);
                                data.setValueFormatter(new PercentFormatter(pieChart));
                                pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS);

                                pieChart.setData(data);
                                pieChart.animateXY(2000,2000);
                                pieChart.setUsePercentValues(true);
                                pieChart.getDescription().setEnabled(false);
                                pieChart.invalidate();

                            }
                        },
                        throwable -> {
                            Log.d("log", throwable.getMessage());
                        }
                ));

    }

    private void initView() {
        toolbar = findViewById(R.id.toobar);
        pieChart =findViewById(R.id.piechart);
        barChart = findViewById(R.id.barchart);
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

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}