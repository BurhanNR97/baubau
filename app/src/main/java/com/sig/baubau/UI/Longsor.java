package com.sig.baubau.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
import com.sig.baubau.adapter.adpLongsor;
import com.sig.baubau.model.modelBanjir;
import com.sig.baubau.model.modelKec;
import com.sig.baubau.model.modelKel;
import com.sig.baubau.model.modelLongsor;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Longsor extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private adpLongsor adapter;
    private ArrayList<modelLongsor> myList;
    private ArrayList<String> list;
    DatabaseReference db;
    ListView layout;
    TextView kosong;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_longsor);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.hijau));
        }

        Toolbar toolbar = findViewById(R.id.toolbar_longsor);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Data Longsor");

        adapter = new adpLongsor(Longsor.this);
        myList = new ArrayList<>();
        db = FirebaseDatabase.getInstance().getReference("longsor");
        layout = findViewById(R.id.rvLongsor);
        kosong = findViewById(R.id.kosongLongsor);
        list = new ArrayList<>();

        layout.setOnItemClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                myList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    modelLongsor model = new modelLongsor();
                    model.setTidak(Double.parseDouble(ds.child("tidak").getValue().toString()));
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
        Intent intent = new Intent(Longsor.this, AdminActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_longsor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.add_longsor) {
            AlertDialog.Builder builder = new AlertDialog.Builder(Longsor.this, R.style.AlertDialogTheme);
            builder.setCancelable(false);
            View v = LayoutInflater.from(Longsor.this).inflate(R.layout.popup_longsor, (RelativeLayout) findViewById(R.id.relativeLongsor));
            builder.setView(v);
            final AlertDialog alertDialog = builder.create();

            ArrayAdapter<String> adpSpin = new ArrayAdapter<String>(Longsor.this,
                    android.R.layout.simple_spinner_item, list);
            adpSpin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            TextView teksInfo = v.findViewById(R.id.longsor_info);
            teksInfo.setText("TAMBAH DATA\nLONGSOR");

            Spinner tKode = v.findViewById(R.id.longsor_kode);
            tKode.setAdapter(adpSpin);

            TextView teksSedang = v.findViewById(R.id.longsor_sedang);
            TextView teksRendah = v.findViewById(R.id.longsor_rendah);
            TextView teksTidak = v.findViewById(R.id.longsor_tidak);

            Button btnHapus = v.findViewById(R.id.longsor_hapus);
            btnHapus.setVisibility(View.GONE);

            v.findViewById(R.id.longsor_simpan).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (tKode.getSelectedItemPosition() == 0 ||
                            teksSedang.getText().toString().isEmpty() ||
                            teksRendah.getText().toString().isEmpty() || teksTidak.getText().toString().isEmpty()) {
                        Toast.makeText(Longsor.this, "Data tidak boleh kosong", Toast.LENGTH_SHORT).show();
                    } else {
                        db.child(tKode.getSelectedItem().toString().substring(0,10))
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.getChildrenCount() <= 0) {
                                    modelLongsor modelLongsor = new modelLongsor();
                                    modelLongsor.setKode(tKode.getSelectedItem().toString().substring(0,10));
                                    modelLongsor.setSedang(Double.parseDouble(teksSedang.getText().toString().trim()));
                                    modelLongsor.setRendah(Double.parseDouble(teksRendah.getText().toString().trim()));
                                    modelLongsor.setTidak(Double.parseDouble(teksTidak.getText().toString().trim()));
                                    db.child(tKode.getSelectedItem().toString().substring(0,10))
                                            .setValue(modelLongsor).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(Longsor.this, "Data berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                                            alertDialog.cancel();
                                        }
                                    });
                                } else {
                                    Toast.makeText(Longsor.this, "Data sudah ada", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
            });

            v.findViewById(R.id.longsor_batal).setOnClickListener(new View.OnClickListener() {
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

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        TextView getId = (TextView)view.findViewById(R.id.itemLongsor_kode);
        final String id = getId.getText().toString();

        AlertDialog.Builder builder = new AlertDialog.Builder(Longsor.this, R.style.AlertDialogTheme);
        builder.setCancelable(false);
        View v = LayoutInflater.from(Longsor.this).inflate(R.layout.popup_longsor, (RelativeLayout) findViewById(R.id.relativeLongsor));
        builder.setView(v);
        final AlertDialog alertDialog = builder.create();

        TextView teksInfo = v.findViewById(R.id.longsor_info);
        teksInfo.setText("UBAH DATA\nLONGSOR");

        Spinner edSpin = v.findViewById(R.id.longsor_kode);
        edSpin.setVisibility(View.GONE);
        TextView edSedang = v.findViewById(R.id.longsor_sedang);
        TextView edRendah = v.findViewById(R.id.longsor_rendah);
        TextView edTidak = v.findViewById(R.id.longsor_tidak);

        db.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot ds) {
                edRendah.setText(ds.child("rendah").getValue().toString());
                edSedang.setText(ds.child("sedang").getValue().toString());
                edTidak.setText(ds.child("tidak").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        v.findViewById(R.id.longsor_simpan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edSedang.getText().toString().trim().isEmpty() || edRendah.getText().toString().trim().isEmpty() ||
                    edTidak.getText().toString().trim().isEmpty()) {
                    Toast.makeText(Longsor.this, "Data tidak boleh kosong", Toast.LENGTH_SHORT).show();
                } else {
                    Map<String, Object> data = new HashMap<>();
                    data.put("rendah", edRendah.getText().toString().trim());
                    data.put("sedang", edSedang.getText().toString().trim());
                    data.put("tidak", edTidak.getText().toString().trim());
                    db.child(edSpin.getSelectedItem().toString().trim().substring(0,10))
                            .updateChildren(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(Longsor.this, "Data berhasil diubah", Toast.LENGTH_SHORT).show();
                                        alertDialog.cancel();
                                    }
                                }
                            });
                }
            }
        });

        v.findViewById(R.id.longsor_hapus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hapusData(v, edSpin.getSelectedItem().toString().trim());
                alertDialog.cancel();
            }
        });

        v.findViewById(R.id.longsor_batal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
            }
        });

        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
    }

    private void hapusData(View v, String path) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Longsor.this, R.style.AlertDialogTheme);
        builder.setCancelable(false);
        v = LayoutInflater.from(Longsor.this).inflate(R.layout.popup_longsor, (RelativeLayout) findViewById(R.id.relativeLongsor));
        builder.setView(v);
        final AlertDialog alertDialog = builder.create();

        v.findViewById(R.id.btnYaHapus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.child(path).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Longsor.this, "Data berhasil dihapus", Toast.LENGTH_SHORT).show();
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
    }
}