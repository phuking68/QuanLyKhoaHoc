package com.example.asm.Model;

import java.io.Serializable;

public class MonHoc implements Serializable {
    private String code;
    private String namemonhoc;
    private String teacher;
    private int isRegister;

    public MonHoc(String code, String namemonhoc, String teacher) {
        this.code = code;
        this.namemonhoc = namemonhoc;
        this.teacher = teacher;
    }

    public MonHoc(String code, String namemonhoc, String teacher, int isRegister) {
        this.code = code;
        this.namemonhoc = namemonhoc;
        this.teacher = teacher;
        this.isRegister = isRegister;
    }

    public int getIsRegister() {
        return isRegister;
    }

    public void setIsRegister(int isRegister) {
        this.isRegister = isRegister;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNamemonhoc() {
        return namemonhoc;
    }

    public void setNamemonhoc(String namemonhoc) {
        this.namemonhoc = namemonhoc;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }
}
