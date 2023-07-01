package com.example.asm.Adapter;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.asm.Model.Item;
import com.example.asm.R;
import com.example.asm.WebviewActivity;

import java.util.ArrayList;

public class TinTucAdapter extends RecyclerView.Adapter<TinTucAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Item> list;
    private String linkImg;

    public TinTucAdapter(Context context, ArrayList<Item> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View view = inflater.inflate(R.layout.item_tintuc, parent, false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtTitleNews.setText(list.get(position).getTitle());
        holder.txtPubDate.setText(list.get(position).getPubDate());
        holder.txtCmt.setText("Comment: " + list.get(position).getComments());

        linkImg = list.get(position).getDescription().get__cdata();
        int indexBegin = linkImg.indexOf("<img src=");
        if (indexBegin > 0) {
            indexBegin += 10;
            int indexEnd = linkImg.indexOf("\" ></a>", indexBegin);
            if (indexEnd > 0 && indexBegin > 0) {
                linkImg = linkImg.substring(indexBegin, indexEnd);
                Glide
                        .with(context)
                        .load(linkImg)
                        .into(holder.ivTinTuc);
            }
        }

        holder.layout_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, WebviewActivity.class);
                intent.putExtra("linkNews", list.get(holder.getAbsoluteAdapterPosition()).getLink());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitleNews, txtPubDate, txtCmt;
        CardView layout_item;
        ImageView ivTinTuc;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitleNews = itemView.findViewById(R.id.txtTitleNews);
            ivTinTuc = itemView.findViewById(R.id.ivTinTuc);
            txtPubDate = itemView.findViewById(R.id.txtPubDate);
            txtCmt = itemView.findViewById(R.id.txtCmt);
            layout_item = itemView.findViewById(R.id.layout_item);
        }
    }
}
