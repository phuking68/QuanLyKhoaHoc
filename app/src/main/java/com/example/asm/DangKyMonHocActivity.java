package com.example.asm;

import static android.graphics.Color.rgb;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asm.Adapter.DangKyMonHocAdapter;
import com.example.asm.Model.MonHoc;
import com.example.asm.Service.DangKyMonHocService;

import java.util.ArrayList;

public class DangKyMonHocActivity extends AppCompatActivity {
    private RecyclerView recyclerDangKyMonHoc;
    private TextView txtTitleDSKhoHoc;
    int id;
    boolean isAll;
    private IntentFilter intentFilter;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ky_mon_hoc);

        recyclerDangKyMonHoc = findViewById(R.id.recyclerDangKyMonHoc);
        txtTitleDSKhoHoc = findViewById(R.id.txtTitleDSKhoHoc);
        toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        intentFilter = new IntentFilter();
        intentFilter.addAction("DSMonHoc");
        intentFilter.addAction("dangkymonhoc");

        //lấy id của user đăng nhập
        SharedPreferences sharedPreferences = getSharedPreferences("THONGTIN", MODE_PRIVATE);
        id = sharedPreferences.getInt("id", -1);

        //lấy giá trị isAll true
        Intent intentIsAll = getIntent();
        Bundle bundleIsAll = intentIsAll.getExtras();
        isAll = bundleIsAll.getBoolean("isAll");

        if(isAll){
            txtTitleDSKhoHoc.setText("DANH SÁCH KHÓA HỌC");
        }else {
            txtTitleDSKhoHoc.setText("KHOÁ HỌC ĐÃ ĐĂNG KÝ");
            //set background cho toolbar
            Drawable drawable;
            drawable = new ColorDrawable(Color.rgb(222, 138, 215));
            //drawable = new ColorDrawable(0xA0500050);

            actionBar.setBackgroundDrawable(drawable);
        }

        Intent intent = new Intent(DangKyMonHocActivity.this, DangKyMonHocService.class);
        Bundle bundle = new Bundle();
        bundle.putInt("id", id);
        bundle.putBoolean("isAll" ,isAll);
        intent.putExtras(bundle);
        startService(intent);

    }


    //hàm load data
    private void loadData(ArrayList<MonHoc> list){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerDangKyMonHoc.setLayoutManager(linearLayoutManager);
        DangKyMonHocAdapter adapter = new DangKyMonHocAdapter(this, list ,id, isAll);
        recyclerDangKyMonHoc.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(myBroadcast, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(myBroadcast);
    }

    private BroadcastReceiver myBroadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()){
                case "DSMonHoc":
                case "dangkymonhoc":
                    Bundle bundle = intent.getExtras();
                    boolean check = bundle.getBoolean("check", true);

                    if (check){
                        ArrayList<MonHoc> list = (ArrayList<MonHoc>) bundle.getSerializable("list");
                        loadData(list);
                    }else {
                        Toast.makeText(context, "Đăng ký thất bại!", Toast.LENGTH_SHORT).show();
                        break;
                    }

                    break;
            }
        }
    };
}