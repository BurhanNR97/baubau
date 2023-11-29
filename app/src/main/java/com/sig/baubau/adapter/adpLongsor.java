package com.sig.baubau.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sig.baubau.R;
import com.sig.baubau.model.modelLongsor;

import java.util.ArrayList;

public class adpLongsor extends BaseAdapter {
    private Context mContext;
    private ArrayList<modelLongsor> list = new ArrayList<>();

    public void setList(ArrayList<modelLongsor> list) {
        this.list = list;
    }

    public adpLongsor(Context mContext) {
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
            itemView = LayoutInflater.from(mContext).inflate(R.layout.item_longsor, viewGroup, false);
        }

        ViewHolder viewHolder = new ViewHolder(itemView);
        modelLongsor model = (modelLongsor) getItem(i);
        viewHolder.bind(model);
        return itemView;
    }

    private class ViewHolder {

        TextView kode, sedang, rendah, tidak;
        int n = 0;

        ViewHolder(View view) {
            kode = view.findViewById(R.id.itemLongsor_kode);
            sedang = view.findViewById(R.id.itemLongsor_sedang);
            rendah = view.findViewById(R.id.itemLongsor_rendah);
            tidak = view.findViewById(R.id.itemLongsor_tidak);
        }

        void bind(modelLongsor model) {
            kode.setText(model.getKode());
            sedang.setText(Double.toString(model.getSedang()));
            rendah.setText(Double.toString(model.getRendah()));
            tidak.setText(Double.toString(model.getTidak()));
        }
    }
}
