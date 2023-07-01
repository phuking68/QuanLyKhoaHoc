package com.example.asm.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.asm.Model.Home;
import com.example.asm.Model.MonHoc;
import com.example.asm.R;
import com.example.asm.Service.RegisterMonHocService;

import java.util.ArrayList;

public class DangKyMonHocAdapter extends RecyclerView.Adapter<DangKyMonHocAdapter.DangKyViewHolder> {

    private Context context;
    private ArrayList<MonHoc> list;
    private int id;
    private boolean isAll;

    public DangKyMonHocAdapter(Context context, ArrayList<MonHoc> list, int id, boolean isAll) {
        this.context = context;
        this.list = list;
        this.id = id;
        this.isAll = isAll;
    }

    @NonNull
    @Override
    public DangKyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        View view = inflater.inflate(R.layout.item_dangkymonhoc, parent, false);
        return new DangKyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DangKyViewHolder holder, int position) {
        holder.edtTenMonHoc.setText((list.get(position).getNamemonhoc()));
        holder.edtCode.setText(String.valueOf(list.get(position).getCode()));
        holder.edtTeacher.setText(list.get(position).getTeacher());

        //so sánh id người dùng hiện tại có bằng = isResgister
        if (list.get(position).getIsRegister() == id){
            holder.btnTrangThaiDangKy.setText("Huỷ đăng ký môn học");
            holder.btnTrangThaiDangKy.setBackgroundColor(Color.RED);
        }else {
            holder.btnTrangThaiDangKy.setText("Đăng ký môn học");
            holder.btnTrangThaiDangKy.setBackgroundColor(Color.BLUE);
        }

        //đăng ký môn học hoặc hủy môn học
        holder.btnTrangThaiDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RegisterMonHocService.class);
                Bundle bundle = new Bundle();
                bundle.putInt("id", id);
                bundle.putString("code", list.get(holder.getAbsoluteAdapterPosition()).getCode());
                bundle.putInt("isRegister", list.get(holder.getAbsoluteAdapterPosition()).getIsRegister());
                bundle.putBoolean("isAll", isAll);
                intent.putExtras(bundle);
                context.startService(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class DangKyViewHolder extends RecyclerView.ViewHolder {
        TextView edtTenMonHoc, edtCode, edtTeacher;
        Button btnTrangThaiDangKy;

        public DangKyViewHolder(@NonNull View itemView) {
            super(itemView);

            edtTenMonHoc = itemView.findViewById(R.id.edtTenMonHoc);
            edtCode = itemView.findViewById(R.id.edtCode);
            edtTeacher = itemView.findViewById(R.id.edtTeacher);
            btnTrangThaiDangKy= itemView.findViewById(R.id.btnTrangThaiDangKy);

        }
    }
}
