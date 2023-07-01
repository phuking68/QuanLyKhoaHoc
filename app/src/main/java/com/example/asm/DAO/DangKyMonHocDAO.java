package com.example.asm.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.asm.Database.DbHelper;
import com.example.asm.Model.MonHoc;

import java.util.ArrayList;

public class DangKyMonHocDAO {

    DbHelper dbHelper;

    public DangKyMonHocDAO (Context context){
        dbHelper = new DbHelper(context);
    }

    //lấy danh sách môn học
    public ArrayList<MonHoc> getDSMonHoc(int id, boolean isAll){
        ArrayList<MonHoc> list = new ArrayList<>();

        SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();
        Cursor cursor;
//        cursor = sqLiteDatabase.rawQuery("SELECT mh.code, mh.nameMonHoc, mh.teacher, dk.id " +
//                "FROM MONHOC mh LEFT JOIN DANGKY dk ON mh.code = dk.code AND dk.id= ?",
//                new String[]{String.valueOf(id)});
        if (isAll) {
            cursor = sqLiteDatabase.rawQuery("SELECT mh.code, mh.nameMonHoc, mh.teacher, dk.id " +
                    "FROM MONHOC mh LEFT JOIN DANGKY dk ON mh.code = dk.code AND dk.id= ?",
                    new String[]{String.valueOf(id)});
        }else {
            cursor = sqLiteDatabase.rawQuery("SELECT mh.code, mh.nameMonHoc, mh.teacher, dk.id " +
                    "FROM MONHOC mh INNER JOIN DANGKY dk ON mh.code = dk.code WHERE dk.id= ?",
                    new String[]{String.valueOf(id)});
        }

        if (cursor.getCount() != 0){
            cursor.moveToFirst();
            do {
                list.add(new MonHoc(cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getInt(3)));
            }while (cursor.moveToNext());
        }

        return list;
    }

    //hàm DAO đăng ký môn học
    public boolean DangKyMonHoc(int id, String code){
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", id);
        contentValues.put("code", code);
        long check = sqLiteDatabase.insert("DANGKY", null, contentValues);

        // check == -1 thất bại
        if (check == -1)
            return false;
        return true;
    }

    //hàm DAO Hủy đăng ký môn học
    public boolean HuyDangKyMonHoc(int id, String code){
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();

        long check = sqLiteDatabase.delete("DANGKY", "id = ? and code =?", new String[]{String.valueOf(id), code});

        if (check == -1)
            return false;
        return true;
    }
}
