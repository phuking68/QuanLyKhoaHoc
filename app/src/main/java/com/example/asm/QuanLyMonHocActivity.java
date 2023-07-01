package com.example.asm;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;



public class QuanLyMonHocActivity extends AppCompatActivity {

    Toolbar toolbar;
    private CardView item_KH_DaDangKy, item_DangKy_KhoaHoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly_mon_hoc);

        item_DangKy_KhoaHoc = findViewById(R.id.item_DangKy_KhoaHoc);
        item_KH_DaDangKy = findViewById(R.id.item_KH_DaDangKy);

        toolbar = findViewById(R.id.toolBar);

        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        item_DangKy_KhoaHoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuanLyMonHocActivity.this,DangKyMonHocActivity.class);
                Bundle bundle = new Bundle();
                bundle.putBoolean("isAll", true);
                intent.putExtras(bundle);

                startActivity(intent);
            }
        });

        item_KH_DaDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuanLyMonHocActivity.this,DangKyMonHocActivity.class);
                Bundle bundle = new Bundle();
                bundle.putBoolean("isAll", false);
                intent.putExtras(bundle);

                startActivity(intent);
            }
        });


    }
}