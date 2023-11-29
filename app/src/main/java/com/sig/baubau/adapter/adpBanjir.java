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
import com.sig.baubau.model.modelBanjir;

import java.util.ArrayList;

public class adpBanjir extends BaseAdapter {
    private Context mContext;
    private ArrayList<modelBanjir> list = new ArrayList<>();

    public void setList(ArrayList<modelBanjir> list) {
        this.list = list;
    }

    public adpBanjir(Context mContext) {
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
            itemView = LayoutInflater.from(mContext).inflate(R.layout.item_banjir, viewGroup, false);
        }

        ViewHolder viewHolder = new ViewHolder(itemView);
        modelBanjir model = (modelBanjir) getItem(i);
        viewHolder.bind(model);
        return itemView;
    }

    private class ViewHolder {

        TextView kode, nama, tinggi, sedang, rendah, tidak;
        int n = 0;

        ViewHolder(View view) {
            kode = view.findViewById(R.id.itemBanjir_kode);
            nama = view.findViewById(R.id.itemBanjir_nama);
            tinggi = view.findViewById(R.id.itemBanjir_tinggi);
            sedang = view.findViewById(R.id.itemBanjir_sedang);
            rendah = view.findViewById(R.id.itemBanjir_rendah);
            tidak = view.findViewById(R.id.itemBanjir_tidak);
        }

        void bind(modelBanjir model) {
            kode.setText(model.getKode());
            nama.setText("");
            tinggi.setText(Double.toString(model.getTinggi()));
            sedang.setText(Double.toString(model.getSedang()));
            rendah.setText(Double.toString(model.getRendah()));
            tidak.setText(Double.toString(model.getTidak()));
        }
    }
}
