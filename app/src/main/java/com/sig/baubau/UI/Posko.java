package com.sig.baubau.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sig.baubau.R;
import com.sig.baubau.databinding.ActivityPoskoBinding;
import com.sig.baubau.model.modelPosko;

import java.util.ArrayList;

public class Posko extends AppCompatActivity implements OnMapReadyCallback {

    ArrayList<Marker> markerList = new ArrayList<>();
    private ActivityPoskoBinding binding;
    GoogleMap gMap;
    Toolbar kembali;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPoskoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapPosko);
        mapFragment.getMapAsync(this);

        DatabaseReference db = FirebaseDatabase.getInstance().getReference("posko");
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                markerList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    modelPosko model = ds.getValue(modelPosko.class);
                    double lat = Double.parseDouble(model.getLat());
                    double lng = Double.parseDouble(model.getLng());
                    Marker marker = gMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)));
                    marker.setTag(model.getKode() + "\n" + model.getNama());
                    markerList.add(marker);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        kembali = findViewById(R.id.toolbar);
        kembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        LatLng kota = new LatLng(-5.434039, 122.667554);
        gMap.moveCamera(CameraUpdateFactory.newLatLng(kota));
        CameraPosition position = CameraPosition.builder()
                .target(kota)
                .zoom(11.3f)
                .build();
        gMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), null);

        gMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                Toast.makeText(Posko.this, marker.getTag().toString(), Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        startActivity(new Intent(Posko.this, MainActivity.class));
        finish();
    }
}