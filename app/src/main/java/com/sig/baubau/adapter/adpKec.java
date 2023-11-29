package com.sig.baubau.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sig.baubau.R;
import com.sig.baubau.model.modelKec;

import java.util.ArrayList;

public class adpKec extends BaseAdapter {
    private Context mContext;
    private ArrayList<modelKec> list = new ArrayList<>();

    public void setList(ArrayList<modelKec> list) {
        this.list = list;
    }

    public adpKec(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View itemView = view;

        if (itemView == null) {
            itemView = LayoutInflater.from(mContext).inflate(R.layout.item_kecamatan, viewGroup, false);
        }

        ViewHolder viewHolder = new ViewHolder(itemView);
        modelKec model = (modelKec) getItem(i);
        viewHolder.bind(model);
        return itemView;
    }

    private class ViewHolder {

        TextView kode, nama, lat, lng, jumlah;
        int n = 0;

        ViewHolder(View view) {
            kode = view.findViewById(R.id.itemKec_kode);
            nama = view.findViewById(R.id.itemKec_nama);
            lat = view.findViewById(R.id.itemKec_lat);
            lng = view.findViewById(R.id.itemKec_lng);
            jumlah = view.findViewById(R.id.itemKec_kel);
        }

        void bind(modelKec model) {
            kode.setText(model.getKode());
            nama.setText(model.getNama());
            lat.setText(model.getLat());
            lng.setText(model.getLng());

            DatabaseReference db = FirebaseDatabase.getInstance().getReference("kelurahan");
            db.child(model.getKode()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    long a = snapshot.getChildrenCount();
                    jumlah.setText(a + " Kelurahan");
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}
