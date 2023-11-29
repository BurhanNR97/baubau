package com.sig.baubau.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.sig.baubau.R;

public class TambahBanjir extends AppCompatActivity {
    TextView teksNama, teksTinggi, teksSedang, teksRendah, teksTidak;
    Button simpan, keluar;
    Spinner tKode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_banjir);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.hijau));
        }

        tKode = findViewById(R.id.banjir_kode);
        teksNama = findViewById(R.id.banjir_nama);
        teksTinggi = findViewById(R.id.banjir_tinggi);
        teksSedang = findViewById(R.id.banjir_sedang);
        teksRendah = findViewById(R.id.banjir_rendah);
        teksTidak = findViewById(R.id.banjir_tidak);
        simpan = findViewById(R.id.banjir_simpan);
        keluar = findViewById(R.id.banjir_batal);

        keluar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        tKode.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(TambahBanjir.this, tKode.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(TambahBanjir.this, Banjir.class));
        finish();
    }
}