package com.example.asm.Service;

import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.example.asm.DAO.DangKyMonHocDAO;
import com.example.asm.Model.MonHoc;

import java.util.ArrayList;

public class RegisterMonHocService extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle bundle = intent.getExtras();
        int id = bundle.getInt("id", -1);
        String code = bundle.getString("code", "");
        int isRegister = bundle.getInt("isRegister", -1);
        boolean isAll = bundle.getBoolean("isAll");

        boolean check;
        DangKyMonHocDAO dangKyMonHocDAO = new DangKyMonHocDAO(this);
        if (isRegister == id){
            check = dangKyMonHocDAO.HuyDangKyMonHoc(id, code);
        }else {
            check = dangKyMonHocDAO.DangKyMonHoc(id, code);
        }

        ArrayList<MonHoc> list = new ArrayList<>();
        if (check){
            list = dangKyMonHocDAO.getDSMonHoc(id, isAll);
        }

        Intent intentBR = new Intent();
        Bundle bundleBR = new Bundle();
        bundleBR.putBoolean("check", check);
        bundleBR.putSerializable("list", list);
        intentBR.putExtras(bundleBR);
        intentBR.setAction("dangkymonhoc");
        sendBroadcast(intentBR);

        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
