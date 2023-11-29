package com.sig.baubau.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sig.baubau.R;
import com.sig.baubau.adapter.adpBanjir;
import com.sig.baubau.model.modelBanjir;
import com.sig.baubau.model.modelKec;
import com.sig.baubau.model.modelKel;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Banjir extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private adpBanjir adapter;
    private ArrayList<modelBanjir> myList;
    private ArrayList<String> list;
    DatabaseReference db;
    Spinner tKode;
    ListView layout;
    TextView kosong;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banjir);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.hijau));
        }

        Toolbar toolbar = findViewById(R.id.toolbar_banjir);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Data Banjir");

        adapter = new adpBanjir(Banjir.this);
        myList = new ArrayList<>();
        db = FirebaseDatabase.getInstance().getReference("banjir");
        layout = findViewById(R.id.rvBanjir);
        layout.setOnItemClickListener(Banjir.this);
        kosong = findViewById(R.id.kosongBanjir);
        list = new ArrayList<>();
    }

    @Override
    protected void onResume() {
        super.onResume();

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                myList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    modelBanjir model = new modelBanjir();
                    model.setTidak(Double.parseDouble(ds.child("tidak").getValue().toString()));
                    model.setTinggi(Double.parseDouble(ds.child("tinggi").getValue().toString()));
                    model.setRendah(Double.parseDouble(ds.child("rendah").getValue().toString()));
                    model.setSedang(Double.parseDouble(ds.child("sedang").getValue().toString()));
                    model.setKode(ds.getKey());
                    myList.add(model);
                }

                if (myList.size() > 0) {
                    layout.setVisibility(View.VISIBLE);
                    kosong.setVisibility(View.GONE);
                } else {
                    layout.setVisibility(View.GONE);
                    kosong.setVisibility(View.VISIBLE);
                }

                adapter.setList(myList);
                layout.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("kecamatan");
        DatabaseReference dbref = FirebaseDatabase.getInstance().getReference("kelurahan");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    list.clear();
                    list.add("- Kode Kelurahan -");
                    modelKec model = ds.getValue(modelKec.class);
                    dbref.child(model.getKode()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot snap : snapshot.getChildren()) {
                                modelKel mod = snap.getValue(modelKel.class);
                                list.add(mod.getKode() + " - " + mod.getNama());
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Banjir.this, AdminActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_banjir, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.add_banjir) {
            AlertDialog.Builder builder = new AlertDialog.Builder(Banjir.this, R.style.AlertDialogTheme);
            builder.setCancelable(false);
            View v = LayoutInflater.from(Banjir.this).inflate(R.layout.popup_banjir, (RelativeLayout) findViewById(R.id.relativeBanjir));
            builder.setView(v);
            final AlertDialog alertDialog = builder.create();

            TextView teksInfo = v.findViewById(R.id.banjir_info);
            teksInfo.setText("TAMBAH DATA\nBANJIR");

            spinnerItems(v);

            TextView teksNama = v.findViewById(R.id.banjir_nama);
            TextView teksTinggi = v.findViewById(R.id.banjir_tinggi);
            TextView teksSedang = v.findViewById(R.id.banjir_sedang);
            TextView teksRendah = v.findViewById(R.id.banjir_rendah);
            TextView teksTidak = v.findViewById(R.id.banjir_tidak);

            teksNama.setFocusable(false);

            Button btnHapus = v.findViewById(R.id.banjir_hapus);
            btnHapus.setVisibility(View.GONE);


            v.findViewById(R.id.banjir_simpan).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (tKode.getSelectedItemPosition() == 0 || teksTinggi.getText().toString().isEmpty()
                        || teksSedang.getText().toString().isEmpty() ||
                        teksRendah.getText().toString().isEmpty() || teksTidak.getText().toString().isEmpty()) {
                        Toast.makeText(Banjir.this, "Data tidak boleh kosong", Toast.LENGTH_SHORT).show();
                    } else {
                        DatabaseReference rf = FirebaseDatabase.getInstance().getReference("banjir");
                        rf.child(tKode.getSelectedItem().toString().substring(0,10)).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.getChildrenCount() <= 0) {
                                    modelBanjir modelBanjir = new modelBanjir();
                                    modelBanjir.setTinggi(Double.parseDouble(teksTinggi.getText().toString().trim()));
                                    modelBanjir.setSedang(Double.parseDouble(teksSedang.getText().toString().trim()));
                                    modelBanjir.setRendah(Double.parseDouble(teksRendah.getText().toString().trim()));
                                    modelBanjir.setTidak(Double.parseDouble(teksTidak.getText().toString().trim()));
                                    db.child(tKode.getSelectedItem().toString().substring(0,10)).setValue(modelBanjir).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(Banjir.this, "Data berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                                            alertDialog.cancel();
                                        }
                                    });
                                } else {
                                    Toast.makeText(Banjir.this, "Data sudah ada", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
            });

            v.findViewById(R.id.banjir_batal).setOnClickListener(new View.OnClickListener() {
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
        return super.onOptionsItemSelected(item);
    }

    private void spinnerItems (View v ) {
        ArrayAdapter<String> adpSpin = new ArrayAdapter<String>(Banjir.this,
                android.R.layout.simple_spinner_item, list);
        adpSpin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        tKode = v.findViewById(R.id.banjir_kode);
        tKode.setAdapter(adpSpin);
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        TextView getId = (TextView)view.findViewById(R.id.itemBanjir_kode);
        final String id = getId.getText().toString();

        AlertDialog.Builder builder = new AlertDialog.Builder(Banjir.this, R.style.AlertDialogTheme);
        builder.setCancelable(false);
        View v = LayoutInflater.from(Banjir.this).inflate(R.layout.popup_banjir, (RelativeLayout) findViewById(R.id.relativeBanjir));
        builder.setView(v);
        final AlertDialog alertDialog = builder.create();

        TextView teksInfo = v.findViewById(R.id.banjir_info);
        teksInfo.setText("UBAH DATA\nBANJIR");

        Spinner edSpin = v.findViewById(R.id.banjir_kode);
        edSpin.setVisibility(View.GONE);
        TextView edKode = v.findViewById(R.id.banjir_nama);
        edKode.setVisibility(View.VISIBLE);
        edKode.setFocusable(false);
        TextView edTinggi = v.findViewById(R.id.banjir_tinggi);
        TextView edSedang = v.findViewById(R.id.banjir_sedang);
        TextView edRendah = v.findViewById(R.id.banjir_rendah);
        TextView edAman = v.findViewById(R.id.banjir_tidak);

        db.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot ds) {
                edTinggi.setText(ds.child("tinggi").getValue().toString());
                edRendah.setText(ds.child("rendah").getValue().toString());
                edSedang.setText(ds.child("sedang").getValue().toString());
                edAman.setText(ds.child("tidak").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        v.findViewById(R.id.banjir_simpan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edTinggi.getText().toString().trim().isEmpty() || edSedang.getText().toString().trim().isEmpty() ||
                    edRendah.getText().toString().trim().isEmpty() || edAman.getText().toString().trim().isEmpty()) {
                    Toast.makeText(Banjir.this, "Data tidak boleh kosong", Toast.LENGTH_SHORT).show();
                } else {
                    Map<String, Object> data = new HashMap<>();
                    data.put("rendah", edRendah.getText().toString().trim());
                    data.put("sedang", edSedang.getText().toString().trim());
                    data.put("tidak", edAman.getText().toString().trim());
                    data.put("tinggi", edTinggi.getText().toString().trim());
                    db.child(id).updateChildren(data)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(Banjir.this, "Data berhasil diubah", Toast.LENGTH_SHORT).show();
                                alertDialog.cancel();
                            }
                        }
                    });
                }
            }
        });

        v.findViewById(R.id.banjir_hapus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hapusData(v, id);
                alertDialog.cancel();
            }
        });

        v.findViewById(R.id.banjir_batal).setOnClickListener(new View.OnClickListener() {
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

    private void hapusData(View v, String path) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Banjir.this, R.style.AlertDialogTheme);
        builder.setCancelable(false);
        v = LayoutInflater.from(Banjir.this).inflate(R.layout.dialog_hapus, (ConstraintLayout) findViewById(R.id.layoutDialogContainerHapus));
        builder.setView(v);
        final AlertDialog alertDialog = builder.create();

        v.findViewById(R.id.btnYaHapus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.child(path).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Banjir.this, "Data berhasil dihapus", Toast.LENGTH_SHORT).show();
                            alertDialog.cancel();
                        }
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
}