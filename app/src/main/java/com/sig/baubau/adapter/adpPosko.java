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
import com.sig.baubau.model.modelPosko;

import java.util.ArrayList;

public class adpPosko extends BaseAdapter {
    private Context mContext;
    private ArrayList<modelPosko> list = new ArrayList<>();

    public void setList(ArrayList<modelPosko> list) {
        this.list = list;
    }

    public adpPosko(Context mContext) {
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
            itemView = LayoutInflater.from(mContext).inflate(R.layout.item_posko, viewGroup, false);
        }

        ViewHolder viewHolder = new ViewHolder(itemView);
        modelPosko model = (modelPosko) getItem(i);
        viewHolder.bind(model);
        return itemView;
    }

    private class ViewHolder {

        TextView kode, nama, lat, lng;

        ViewHolder(View view) {
            kode = view.findViewById(R.id.itemPosko_kode);
            nama = view.findViewById(R.id.itemPosko_nama);
            lat = view.findViewById(R.id.itemPosko_lat);
            lng = view.findViewById(R.id.itemPosko_lng);
        }

        void bind(modelPosko model) {
            kode.setText(model.getKode());
            nama.setText(model.getNama());
            lat.setText(model.getLat());
            lng.setText(model.getLng());
        }
    }
}
