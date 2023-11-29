package com.sig.baubau.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.sig.baubau.R;
import com.sig.baubau.model.modelKec;

import java.util.ArrayList;

public class TambahKec extends AppCompatActivity implements View.OnClickListener {
    Button keluar, simpan;
    EditText edKode, edNama, edLat, edLng;
    ImageView map;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_kec);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.hijau));
        }

        keluar = findViewById(R.id.addKec_keluar);
        simpan = findViewById(R.id.addKec_simpan);
        edKode = findViewById(R.id.addKec_kode);
        edNama = findViewById(R.id.addKec_nama);
        edLat = findViewById(R.id.addKec_Lat);
        edLng = findViewById(R.id.addKec_Lng);
        map = findViewById(R.id.addKec_map);

        if (getIntent().getStringExtra("a").equals("1")) {
            edKode.setText(getIntent().getStringExtra("kode"));
            edNama.setText(getIntent().getStringExtra("nama"));
            edLat.setText(getIntent().getStringExtra("lat"));
            edLng.setText(getIntent().getStringExtra("lng"));
        }

        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TambahKec.this, MapTambahKec.class);
                intent.putExtra("a", "1");
                intent.putExtra("kode", edKode.getText().toString());
                intent.putExtra("nama", edNama.getText().toString());
                intent.putExtra("lat", edLat.getText().toString());
                intent.putExtra("lng", edLng.getText().toString());
                startActivity(intent);
                finish();
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
    protected void onResume() {
        super.onResume();

        if (getIntent().getStringExtra("a").equals("0")) {
            DatabaseReference db = FirebaseDatabase.getInstance().getReference("kecamatan");
            Query ref = db.orderByKey().limitToLast(1);
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    long a = snapshot.getChildrenCount();

                    if (a <= 0) {
                        edKode.setText("747201");
                    } else
                    if (a > 0) {
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            int b = Integer.parseInt(ds.getKey());
                            int c = b + 1;
                            edKode.setText(String.valueOf(c));
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        startActivity(new Intent(TambahKec.this, Kecamatan.class));
        finish();
    }

    @Override
    public void onClick(View view) {
        String kode = edKode.getText().toString();
        String nama = edNama.getText().toString();
        String lat = edLat.getText().toString();
        String lng = edLng.getText().toString();

        if (kode.isEmpty()) {
            edKode.requestFocus();
            edKode.setError("Data harus diisi");
        } else if (nama.isEmpty()) {
            edNama.requestFocus();
            edNama.setError("Data harus diisi");
        } else if (lat.isEmpty()) {
            edLat.setError("Silahkan pilih koordinat");
        } else if (lng.isEmpty()) {
            edLng.setError("Silahkan pilih koordinat");
        } else {
            modelKec model = new modelKec();
            model.setKode(kode);
            model.setNama(nama);
            model.setLat(lat);
            model.setLng(lng);
            DatabaseReference db = FirebaseDatabase.getInstance().getReference("kecamatan");
            db.child(kode).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(TambahKec.this, "Data berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    } else {
                        Toast.makeText(TambahKec.this, task.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}