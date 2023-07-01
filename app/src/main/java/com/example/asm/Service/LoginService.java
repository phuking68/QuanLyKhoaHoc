package com.example.asm.Service;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.example.asm.DAO.UserDAO;

public class LoginService extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Bundle bundle = intent.getExtras();
        String user = bundle.getString("user");
        String pass = bundle.getString("pass");
        boolean isRemember = bundle.getBoolean("isRemember");

        UserDAO userDAO = new UserDAO(this);
        boolean check = userDAO.kiemTraDangNhap(user, pass);

        //gửi data
        Intent intentBR = new Intent();
        Bundle bundleBR = new Bundle();
        bundleBR.putBoolean("check", check);
        bundleBR.putBoolean("isRemember", isRemember);

        intentBR.setAction("kiemTraDangNhap");
        intentBR.putExtras(bundleBR);
        sendBroadcast(intentBR);

        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
