package com.sig.baubau.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
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
import com.sig.baubau.model.modelPosko;

import java.util.HashMap;
import java.util.Map;

public class UbahPosko extends AppCompatActivity {
    ImageView peta;
    EditText edKode, edNama, edLat, edLng;
    Button simpan, batal, hapus;
    DatabaseReference db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubah_posko);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.hijau));
        }

        edKode = findViewById(R.id.posko_kode1);
        edNama = findViewById(R.id.posko_nama1);
        edLat = findViewById(R.id.posko_lat1);
        edLng = findViewById(R.id.posko_lng1);
        simpan = findViewById(R.id.posko_simpan1);
        batal = findViewById(R.id.posko_batal1);
        peta = findViewById(R.id.posko_map1);
        hapus = findViewById(R.id.posko_hapus);
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
                Intent intent = new Intent(UbahPosko.this, MapUbahPosko.class);
                intent.putExtra("kode", edKode.getText().toString().trim());
                intent.putExtra("nama", edNama.getText().toString().trim());
                intent.putExtra("lat", edLat.getText().toString().trim());
                intent.putExtra("lng", edLng.getText().toString().trim());
                startActivity(intent);
                finish();
            }
        });

        hapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(UbahPosko.this, R.style.AlertDialogTheme);
                builder.setCancelable(false);
                View v = LayoutInflater.from(UbahPosko.this).inflate(R.layout.dialog_hapus, (ConstraintLayout) findViewById(R.id.layoutDialogContainerHapus));
                builder.setView(v);
                final AlertDialog alertDialog = builder.create();

                v.findViewById(R.id.btnYaHapus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        db.child(edKode.getText().toString().trim()).removeValue()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(UbahPosko.this, "Data berhasil dihapus", Toast.LENGTH_SHORT).show();
                                onBackPressed();
                            }
                        });
                    }
                });

                v.findViewById(R.id.btnTidakHapus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.cancel();
                    }
                });

                if (alertDialog.getWindow()!=null) {
                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                }
                alertDialog.show();
            }
        });

        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edKode.getText().toString().isEmpty() || edNama.getText().toString().isEmpty() ||
                        edLat.getText().toString().isEmpty() || edLng.getText().toString().isEmpty()) {
                    Toast.makeText(UbahPosko.this, "Data tidak boleh kosong", Toast.LENGTH_SHORT).show();
                } else {
                    Map<String, Object> map = new HashMap<>();
                    modelPosko model = new modelPosko();
                    map.put("kode", edKode.getText().toString().trim());
                    map.put("nama", edNama.getText().toString().trim());
                    map.put("lat", edLat.getText().toString().trim());
                    map.put("lng", edLng.getText().toString().trim());
                    db.child(edKode.getText().toString().trim()).updateChildren(map)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(UbahPosko.this, "Data berhasil diubah",
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
        startActivity(new Intent(UbahPosko.this, DataPosko.class));
        finish();
    }
}