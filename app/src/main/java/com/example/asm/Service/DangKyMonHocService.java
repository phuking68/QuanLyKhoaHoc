package com.example.asm.Service;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.example.asm.DAO.DangKyMonHocDAO;
import com.example.asm.Model.MonHoc;

import java.util.ArrayList;

public class DangKyMonHocService extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle bundle = intent.getExtras();
        int id = bundle.getInt("id");
        boolean isAll = bundle.getBoolean("isAll");

        DangKyMonHocDAO dangKyMonHocDAO = new DangKyMonHocDAO(this);
        ArrayList<MonHoc> list = dangKyMonHocDAO.getDSMonHoc(id, isAll);

        Intent intentBR = new Intent();
        Bundle bundleBR = new Bundle();
        bundleBR.putSerializable("list", list);
        bundleBR.putBoolean("check", true);
        intentBR.setAction("DSMonHoc");
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
