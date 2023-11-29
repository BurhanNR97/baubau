package com.sig.baubau.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sig.baubau.R;
import com.sig.baubau.databinding.ActivityMapTambahPoskoBinding;
import com.sig.baubau.databinding.ActivityMapUbahPoskoBinding;

import java.util.ArrayList;

public class MapUbahPosko extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    Button simpan, kembali;
    ArrayList<LatLng> titik = new ArrayList<>();
    String latlang = "";
    Marker marker;
    String kode = "";  String nama = "";  String lat = "";   String lng = "";

    private ActivityMapUbahPoskoBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapUbahPoskoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_editposko);
        mapFragment.getMapAsync(this);

        kode = getIntent().getStringExtra("kode").trim();
        nama = getIntent().getStringExtra("nama").trim();
        lat = getIntent().getStringExtra("lat").trim();
        lng = getIntent().getStringExtra("lng").trim();
        kembali = findViewById(R.id.back_editposko);
        simpan = findViewById(R.id.save_editposko);

        kembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng kota = new LatLng(-5.434039, 122.667554);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(kota));
        CameraPosition position = CameraPosition.builder()
                .target(kota)
                .zoom(11.3f)
                .build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), null);

        if (!lat.isEmpty()) {
            LatLng latLng = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
            titik.add(latLng);
            marker = googleMap.addMarker(new MarkerOptions().position(latLng));
        }

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (titik.size() > 0) {
                    marker.remove();
                }
                titik.add(latLng);
                LatLng koordinat = latLng;
                MarkerOptions markerOptions = new MarkerOptions().position(koordinat);
                marker = googleMap.addMarker(markerOptions);
                latlang = latLng.toString().substring(10, latLng.toString().length() - 1);
                String[] titik = latlang.split(",");
                lat = titik[0].trim();
                lng = titik[1].trim();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(MapUbahPosko.this, UbahPosko.class);
        intent.putExtra("kode", kode);
        intent.putExtra("nama", nama);
        intent.putExtra("lat", lat);
        intent.putExtra("lng", lng);
        startActivity(intent);
        finish();
    }
}