package com.example.asm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity {

    Toolbar toolbar;
    LinearLayout courseItem, mapItem, mediaItem, newsItem, logoutItem, socialItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        courseItem = findViewById(R.id.courseItem);
        mapItem = findViewById(R.id.mapItem);
        newsItem = findViewById(R.id.newsItem);
        socialItem = findViewById(R.id.socialItem);
        logoutItem = findViewById(R.id.logoutItem);
        mediaItem = findViewById(R.id.mediaItem);
        TextView txtShowUser = findViewById(R.id.txtShowUser);
        TextView txtDate = findViewById(R.id.txtDate);
        TextClock texrClockTime = findViewById(R.id.texrClockTime);

        //show user
        SharedPreferences sharedPreferences = getSharedPreferences("dataUser", MODE_PRIVATE);
        String username = sharedPreferences.getString("user", "");
        txtShowUser.setText(username + "");

        //show date
        //lấy ngày hiện tại
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("E, dd/MM/yyyy", Locale.getDefault());
        String ngay = simpleDateFormat.format(currentTime);
        txtDate.setText(ngay);

        //show time
        texrClockTime.setFormat12Hour("hh:mm:ss a");

//        toolbar = findViewById(R.id.toolBar);
//        setSupportActionBar(toolbar);

//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setTitle("");
//        actionBar.setDisplayShowHomeEnabled(true);
//        actionBar.setDisplayHomeAsUpEnabled(true);

        courseItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, QuanLyMonHocActivity.class));
            }
        });

        mapItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, MapsActivity.class));
            }
        });

        mediaItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, MediaActivity.class));
            }
        });

        newsItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, NewsActivity.class));
            }
        });

        socialItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, SocialActivity.class));
            }
        });

        logoutItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();


                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

    }
    //jbkjnkn

}