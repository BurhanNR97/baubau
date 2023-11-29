package com.sig.baubau.component;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.sig.baubau.R;

public class CustomInfoTooltip implements GoogleMap.InfoWindowAdapter {

    private final View mView;
    private Context mContext;

    public CustomInfoTooltip(Context context){
        mContext = context;
        mView = LayoutInflater.from(context).inflate(R.layout.tooltip, null);
    }

    private void rendowWindowsInfo(Marker marker, View view){
        String kec = marker.getTitle();
        String indeks = marker.getSnippet();
        TextView tvKec = view.findViewById(R.id.tooltip_kec);
        TextView tvIndeks = view.findViewById(R.id.tooltip_index);
        tvKec.setText(kec);
        tvIndeks.setText(indeks);
    }

    @Override
    public View getInfoWindow(Marker marker){
        rendowWindowsInfo(marker, mView);
        return mView;

    };

    @Override
    public View getInfoContents(Marker marker) {
        rendowWindowsInfo(marker, mView);
        return mView;
    }
}
