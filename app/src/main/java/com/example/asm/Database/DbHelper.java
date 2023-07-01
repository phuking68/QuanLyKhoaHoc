package com.example.asm.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {

    public DbHelper (Context context){
        super(context, "DANGKYMONHOC",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String dbUser = "create table USER(id integer primary key autoincrement, userName text, pass text, ten text)";
        db.execSQL(dbUser);

        String dbMonHoc = "create table MONHOC(code text primary key, nameMonHoc text, teacher text )";
        db.execSQL(dbMonHoc);

        String dbDangKy = "create table DANGKY(id integer , code text)";
        db.execSQL(dbDangKy);

        String dbThongTin = "create table THONGTIN(idThongTin integer primary key autoincrement, code text, date text, adrress text)";
        db.execSQL(dbThongTin);

        //data người dùng (user)
        db.execSQL("INSERT INTO USER VALUES (1,'phuking','123456','Thiên Phú'),(2,'hotuyen','123','Tuyển')");

        //data môn học (course)
        db.execSQL("INSERT INTO MONHOC VALUES('MOB201','Android Nâng Cao','Nguyễn Trí Định'),('MOB306','React Native','Trần Anh Hùng')," +
                "('MOB2041','Dự Án Mẫu','Nguyễn Trí Định')");

        //data các môn học mà người dùng đã đăng ký
        db.execSQL("INSERT INTO DANGKY VALUES(1,'MOB201'),(1,'MOB306'),(2,'MOB306')");


        //data thông tin lịch học từng môn (info)
        db.execSQL("INSERT INTO THONGTIN VALUES(1, 'MOB201', 'Ca 2 - 19/09/2022', 'T1011'),(2, 'MOB201', 'Ca 2 - 21/09/2022', 'T1011')," +
                "(3, 'MOB201', 'Ca 2 - 23/09/2022', 'T1011'),(4, 'MOB306', 'Ca 5 - 20/09/2022', 'F204')," +
                "(5, 'MOB306', 'Ca 5 - 22/09/2022', 'F204'),(6, 'MOB2041', 'Ca 1 - 20/09/2022', 'Online - https://meet.google.com/rku-beuk-wqu')");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion){
            db.execSQL("DROP TABLE IF EXISTS USER");
            db.execSQL("DROP TABLE IF EXISTS MONHOC");
            db.execSQL("DROP TABLE IF EXISTS DANGKY");
            db.execSQL("DROP TABLE IF EXISTS THONGTIN");

            onCreate(db);
        }
    }
}
