package com.sig.baubau.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sig.baubau.R;
import com.sig.baubau.model.modelPosko;

public class TambahPosko extends AppCompatActivity {
    ImageView peta;
    EditText edKode, edNama, edLat, edLng;
    Button simpan, batal;
    DatabaseReference db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_posko);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.hijau));
        }

        edKode = findViewById(R.id.posko_kode);
        edNama = findViewById(R.id.posko_nama);
        edLat = findViewById(R.id.posko_lat);
        edLng = findViewById(R.id.posko_lng);
        simpan = findViewById(R.id.posko_simpan);
        batal = findViewById(R.id.posko_batal);
        peta = findViewById(R.id.posko_map);
        db = FirebaseDatabase.getInstance().getReference("posko");

        edKode.setText(getIntent().getStringExtra("kode"));
        edNama.setText(getIntent().getStringExtra("nama"));
        edLat.setText(getIntent().getStringExtra("lat"));
        edLng.setText(getIntent().getStringExtra("lng"));

        batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        peta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TambahPosko.this, MapTambahPosko.class);
                intent.putExtra("kode", edKode.getText().toString().trim());
                intent.putExtra("nama", edNama.getText().toString().trim());
                intent.putExtra("lat", edLat.getText().toString().trim());
                intent.putExtra("lng", edLng.getText().toString().trim());
                startActivity(intent);
                finish();
            }
        });

        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edKode.getText().toString().isEmpty() || edNama.getText().toString().isEmpty() ||
                    edLat.getText().toString().isEmpty() || edLng.getText().toString().isEmpty()) {
                    Toast.makeText(TambahPosko.this, "Data tidak boleh kosong", Toast.LENGTH_SHORT).show();
                } else {
                    modelPosko model = new modelPosko();
                    model.setKode(edKode.getText().toString().trim());
                    model.setNama(edNama.getText().toString().trim());
                    model.setLat(edLat.getText().toString().trim());
                    model.setLng(edLng.getText().toString().trim());
                    db.child(model.getKode()).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(TambahPosko.this, "Data berhasil ditambahkan",
                                        Toast.LENGTH_SHORT).show();
                                onBackPressed();
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        startActivity(new Intent(TambahPosko.this, DataPosko.class));
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();

        db.orderByKey().limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getChildrenCount() < 1) {
                    edKode.setText("POS00001");
                } else
                if (snapshot.getChildrenCount() >= 1) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        int k = Integer.parseInt(ds.getKey().substring(3));
                        int n = k + 1;
                        if (ds.getKey().trim().substring(3).length() == 1) {
                            edKode.setText("POS0000" + n);
                        } else
                        if (ds.getKey().trim().substring(3).length() == 2) {
                            edKode.setText("POS000" + n);
                        } else
                        if (ds.getKey().trim().substring(3).length() == 3) {
                            edKode.setText("POS00" + n);
                        } else
                        if (ds.getKey().trim().substring(3).length() == 4) {
                            edKode.setText("POS0" + n);
                        } else
                        if (ds.getKey().trim().substring(3).length() > 4) {
                            edKode.setText("POS0000" + n);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}