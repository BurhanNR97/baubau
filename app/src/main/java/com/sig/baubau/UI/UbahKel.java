package com.sig.baubau.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sig.baubau.R;

import java.util.HashMap;

public class UbahKel extends AppCompatActivity implements View.OnClickListener {
    Button keluar, simpan;
    EditText edKode, edNama, edLat, edLng;
    ImageView map;
    String idku;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubah_kel);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.hijau));
        }

        idku = getIntent().getStringExtra("id");

        keluar = findViewById(R.id.editKel_keluar);
        simpan = findViewById(R.id.editKel_simpan);
        edKode = findViewById(R.id.editKel_kode);
        edNama = findViewById(R.id.editKel_nama);
        edLat = findViewById(R.id.editKel_Lat);
        edLng = findViewById(R.id.editKel_Lng);
        map = findViewById(R.id.editKel_map);

        edKode.setText(getIntent().getStringExtra("kode"));
        edNama.setText(getIntent().getStringExtra("nama"));
        edLat.setText(getIntent().getStringExtra("lat"));
        edLng.setText(getIntent().getStringExtra("lng"));

        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UbahKel.this, MapUbahKel.class);
                intent.putExtra("a", "1");
                intent.putExtra("kode", edKode.getText().toString());
                intent.putExtra("nama", edNama.getText().toString());
                intent.putExtra("lat", edLat.getText().toString());
                intent.putExtra("lng", edLng.getText().toString());
                //startActivity(intent);
                //finish();
            }
        });

        keluar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        simpan.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(UbahKel.this, Kelurahan.class);
        intent.putExtra("kode", getIntent().getStringExtra("kode"));
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View view) {
        String kode = edKode.getText().toString();
        String nama = edNama.getText().toString();
        String lat = edLat.getText().toString();
        String lng = edLng.getText().toString();

        if (nama.isEmpty()) {
            edNama.setError("Harus diisi");
            edNama.requestFocus();
        } else
        if (lat.isEmpty()) {
            edLat.setError("Pilih koordinat dahulu");
        } else
        if (lng.isEmpty()) {
            edLng.setError("Pilih koordinat dahulu");
        } else {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("kelurahan").child(idku);
            DatabaseReference reff = FirebaseDatabase.getInstance().getReference("kel");
            HashMap map = new HashMap();
            map.put("kode", kode);
            map.put("nama", nama);
            map.put("lat", lat);
            map.put("lng", lng);
            reff.child(kode).updateChildren(map);
            ref.child(kode).updateChildren(map).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(UbahKel.this, "Data berhasil diubah", Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }
                }
            });
        }
    }
}