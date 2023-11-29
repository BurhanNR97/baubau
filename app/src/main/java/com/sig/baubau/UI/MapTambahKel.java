package com.sig.baubau.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sig.baubau.R;

import java.util.ArrayList;
import java.util.List;

public class MapTambahKel extends AppCompatActivity implements OnMapReadyCallback {
    String id, kode, nama, lat, lng;
    SupportMapFragment supportMapFragment;
    GoogleMap googleMap;
    LinearLayout undo, reset;
    String latlang = "";
    LatLng kota;
    CheckBox cb;
    Button keluar, simpan;
    Polygon polygon;
    String[] latlong = null;
    StringBuffer dataLat = new StringBuffer();
    StringBuffer dataLng = new StringBuffer();
    List<String> latitude = new ArrayList<>();
    List<String> longitude = new ArrayList<>();
    List<LatLng> latLngsList = new ArrayList<>();
    List<Marker> markerList = new ArrayList<>();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_tambah_kel);

        id = getIntent().getStringExtra("id");
        kode = getIntent().getStringExtra("kode");
        nama = getIntent().getStringExtra("nama");
        lat = getIntent().getStringExtra("lat");
        lng = getIntent().getStringExtra("lng");

        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_addkel);
        supportMapFragment.getMapAsync(this);

        keluar = findViewById(R.id.keluar_mapaddkel);
        simpan = findViewById(R.id.save_addkel);
        undo = findViewById(R.id.btnundoAdd);
        reset = findViewById(R.id.btnresetAdd);
        cb = findViewById(R.id.cb_poligon);

        keluar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (latlang.isEmpty()) {
                    Toast.makeText(MapTambahKel.this, "Titik koordinat belum ada", Toast.LENGTH_SHORT).show();
                } else {
                    for (int i=0; i<latitude.size()-1; i++){
                        if (latitude.size()-1 == i){
                            dataLat.append(latitude.get(i));
                            dataLng.append(longitude.get(i));
                        } else {
                            dataLat.append(latitude.get(i)).append(',');
                            dataLng.append(longitude.get(i)).append(',');
                        }
                    }
                    Intent intent = new Intent(MapTambahKel.this, TambahKel.class);
                    intent.putExtra("a", "1");
                    intent.putExtra("id", id);
                    intent.putExtra("kode", kode);
                    intent.putExtra("nama", nama);
                    intent.putExtra("lat", dataLat.toString());
                    intent.putExtra("lng", dataLng.toString());
                    intent.putExtra("l", getIntent().getStringExtra("l").trim());
                    intent.putExtra("s", getIntent().getStringExtra("s").trim());
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap gMap) {
        googleMap = gMap;
        double l = Double.parseDouble(getIntent().getStringExtra("l").trim());
        double s = Double.parseDouble(getIntent().getStringExtra("s").trim());
        kota = new LatLng(l,s);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(kota));
        CameraPosition position = CameraPosition.builder()
                .target(kota)
                .zoom(12.0f)
                .build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), null);

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                MarkerOptions markerOptions = new MarkerOptions().position(latLng);
                Marker marker = googleMap.addMarker(markerOptions);

                latLngsList.add(latLng);
                latlang = latLng.toString().substring(10, latLng.toString().length() - 1);
                latlong = latlang.split(",");
                latitude.add(latlong[0]);
                longitude.add(latlong[1]);
                markerList.add(marker);

                //polygon = googleMap.addPolygon(polygonOptions);
                //polygon.setStrokeColor(Color.GREEN);
                /*if (markerList.size() >= 2){
                    PolygonOptions polygonOptions = new PolygonOptions().add(latLngsList.get(latLngsList.size() - 2), latLngsList.get(latLngsList.size() - 1))
                            .clickable(true);
                    polygon = googleMap.addPolygon(polygonOptions);
                    polygon.setStrokeColor(Color.GREEN);
                    if (polygon == null) return;
                }*/
            }
        });

        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (latLngsList.size() > 0){
                    if (cb.isChecked()){
                        PolygonOptions polygonOptions = new PolygonOptions().addAll(latLngsList);
                        polygon = googleMap.addPolygon(polygonOptions);
                        polygon.setStrokeColor(Color.GREEN);
                    } else {
                        polygon.setStrokeColor(Color.TRANSPARENT);
                    }
                } else {
                    cb.setChecked(false);
                }
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (polygon!=null){
                    polygon.remove();
                }
                for (Marker marker : markerList) marker.remove();
                latLngsList.clear();
                markerList.clear();
                latlong = new String[latLngsList.size()];
                latitude.clear();
                longitude.clear();
            }
        });

        undo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (polygon!=null){
                    polygon.remove();
                }
                Marker marker = markerList.get(markerList.size() - 1);
                marker.remove();
                latLngsList.remove(latLngsList.get(latLngsList.size() - 1));
                markerList.remove(markerList.get(markerList.size() - 1));
                latitude.remove(latitude.get(latitude.size() - 1));
                longitude.remove(longitude.get(longitude.size() - 1));
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(MapTambahKel.this, TambahKel.class);
        intent.putExtra("a", "0");
        intent.putExtra("id", id);
        intent.putExtra("kode", kode);
        intent.putExtra("nama", nama);
        intent.putExtra("lat", lat);
        intent.putExtra("lng", lng);
        startActivity(intent);
        finish();
    }
}