package com.example.asm.DAO;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.asm.Database.DbHelper;

public class UserDAO {
    DbHelper dbHelper;
    SharedPreferences sharedPreferences;

    public UserDAO(Context context){
        dbHelper = new DbHelper(context);
        sharedPreferences = context.getSharedPreferences("THONGTIN", Context.MODE_PRIVATE);
    }

    //kiểm tra thông tin đăng nhập
    public boolean kiemTraDangNhap(String user, String pass){
        SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("Select * from USER Where userName =? AND pass=?", new String[]{user, pass});
        if (cursor.getCount() != 0){
            cursor.moveToFirst();
            //lưu data để lấy id của user khi đăng ký môn học hoặc hủy
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("id", cursor.getInt(0));
            editor.apply();
            return true;
        }
        return false;
    }

}
