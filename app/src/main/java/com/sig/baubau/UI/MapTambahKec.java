package com.sig.baubau.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sig.baubau.R;

import java.util.ArrayList;
import java.util.List;

public class MapTambahKec extends AppCompatActivity implements OnMapReadyCallback {
    SupportMapFragment supportMapFragment;
    GoogleMap googleMap;
    String latlang = "";
    Marker marker;
    MarkerOptions markerOptions;
    Button keluar, simpan;
    List<Marker> markerList = new ArrayList<>();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_tambah_kec);

        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_addkec);
        supportMapFragment.getMapAsync(this);

        keluar = findViewById(R.id.keluar_mapaddkec);
        simpan = findViewById(R.id.save_addkec);

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
                    Toast.makeText(MapTambahKec.this, "Titik koordinat belum ada", Toast.LENGTH_SHORT).show();
                } else {
                    String[] point = latlang.split(",");
                    Intent intent = new Intent(MapTambahKec.this, TambahKec.class);
                    intent.putExtra("a", "1");
                    intent.putExtra("kode", getIntent().getStringExtra("kode"));
                    intent.putExtra("nama", getIntent().getStringExtra("nama"));
                    intent.putExtra("lat", point[0]);
                    intent.putExtra("lng", point[1]);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap gMap) {
        googleMap = gMap;
        LatLng kota = new LatLng(-5.434039, 122.667554);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(kota));
        CameraPosition position = CameraPosition.builder()
                .target(kota)
                .zoom(11.5f)
                .build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), null);

        if (!getIntent().getStringExtra("lat").isEmpty()) {
            LatLng titik = new LatLng(Double.parseDouble(getIntent().getStringExtra("lat")),
                                      Double.parseDouble(getIntent().getStringExtra("lng")));
            markerOptions = new MarkerOptions().position(titik);
            marker = googleMap.addMarker(markerOptions);
        }

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                markerOptions = new MarkerOptions().position(latLng);

                if (marker != null) {
                    marker.remove();
                }
                marker = googleMap.addMarker(markerOptions);
                latlang = latLng.toString().substring(10, latLng.toString().length() - 1);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(MapTambahKec.this, TambahKec.class);
        intent.putExtra("a", "0");
        intent.putExtra("kode", getIntent().getStringExtra("kode"));
        intent.putExtra("nama", getIntent().getStringExtra("nama"));
        intent.putExtra("lat", getIntent().getStringExtra("lat"));
        intent.putExtra("lng", getIntent().getStringExtra("lng"));
        startActivity(intent);
        finish();
    }
}