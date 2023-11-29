package com.sig.baubau.adapter;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sig.baubau.R;
import com.sig.baubau.model.modelList;

import java.util.List;

public class adpList extends RecyclerView.Adapter{
    private List<modelList> dataList;

    public adpList(List<modelList> dataList) {
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.tabel_list, parent, false);

        return new RowViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        RowViewHolder rowViewHolder = (RowViewHolder) holder;

        int rowPos = rowViewHolder.getAdapterPosition();

        if (rowPos == 0) {
            rowViewHolder.kelurahan.setVisibility(View.GONE);
        } else {
            modelList data = dataList.get(rowPos - 1);

            rowViewHolder.kelurahan.setText(data.getKel() + "");
            rowViewHolder.kelurahan.setTextSize(18f);

            if (data.getHasil().equals("Aman")) {
                rowViewHolder.kelurahan.setTextColor(Color.WHITE);
            } else
            if (data.getHasil().equals("Rendah")) {
                rowViewHolder.kelurahan.setTextColor(Color.GREEN);
            } else
            if (data.getHasil().equals("Sedang")) {
                rowViewHolder.kelurahan.setTextColor(Color.YELLOW);
            } else
            if (data.getHasil().equals("Tinggi")) {
                rowViewHolder.kelurahan.setTextColor(Color.RED);
            }
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size() + 1;
    }

    public class RowViewHolder extends RecyclerView.ViewHolder {
        TextView kelurahan;

        RowViewHolder(View itemView) {
            super(itemView);
            kelurahan = itemView.findViewById(R.id.tb_kel);
        }
    }
}
