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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sig.baubau.R;
import com.sig.baubau.adapter.adpKec;
import com.sig.baubau.adapter.adpKel;
import com.sig.baubau.model.modelKec;
import com.sig.baubau.model.modelKel;

import java.util.ArrayList;

public class Kelurahan extends AppCompatActivity implements AdapterView.OnItemClickListener {

    DatabaseReference db;
    ListView layout;
    TextView kosong;
    adpKel adapters;
    ArrayList<modelKel> myList;
    String idku;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kelurahan);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.hijau));
        }

        idku = getIntent().getStringExtra("id");

        Toolbar toolbar = findViewById(R.id.toolbar_kelurahan);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Data Kelurahan");

        db = FirebaseDatabase.getInstance().getReference("kelurahan").child(idku);
        layout = findViewById(R.id.rvKel);
        layout.setOnItemClickListener(this);
        myList = new ArrayList<>();
        adapters = new adpKel(Kelurahan.this);
        kosong = (TextView) findViewById(R.id.kosongKel);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        TextView getId = (TextView)view.findViewById(R.id.itemKel_kode);
        TextView getNama = (TextView)view.findViewById(R.id.itemKel_nama);
        TextView getL = (TextView)view.findViewById(R.id.itemKel_lat);
        TextView getS = (TextView)view.findViewById(R.id.itemKel_lng);
        final String nid = getId.getText().toString();
        final String nNama = getNama.getText().toString();
        final String nL = getL.getText().toString();
        final String nS = getS.getText().toString();

        AlertDialog.Builder builder = new AlertDialog.Builder(Kelurahan.this, R.style.AlertDialogTheme);
        builder.setCancelable(false);
        View v = LayoutInflater.from(Kelurahan.this).inflate(R.layout.dialog_items, (ConstraintLayout) findViewById(R.id.containerItems));
        builder.setView(v);
        final AlertDialog alertDialog = builder.create();

        v.findViewById(R.id.btnUbahh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Kelurahan.this, UbahKel.class);
                intent.putExtra("kode", nid);
                intent.putExtra("nama", nNama);
                intent.putExtra("lat", nL);
                intent.putExtra("lng", nS);
                startActivity(intent);
                finish();
            }
        });

        v.findViewById(R.id.btnHapuss).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hapusData(nid);
                alertDialog.dismiss();
            }
        });

        v.findViewById(R.id.btnBatall).setOnClickListener(new View.OnClickListener() {
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

    public void setListView() {
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                myList.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    modelKel model = dataSnapshot.getValue(modelKel.class);
                    myList.add(model);
                }

                adapters.setList(myList);
                layout.setAdapter(adapters);

                if (myList.size() == 0) {
                    layout.setVisibility(View.GONE);
                    kosong.setVisibility(View.VISIBLE);
                } else {
                    layout.setVisibility(View.VISIBLE);
                    kosong.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setListView();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Kelurahan.this, Kecamatan.class);
        intent.putExtra("a", "0");
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_kel, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.add_kel) {
            Intent intent = new Intent(Kelurahan.this, TambahKel.class);
            intent.putExtra("a", "0");
            intent.putExtra("id", idku);
            intent.putExtra("l", getIntent().getStringExtra("l").trim());
            intent.putExtra("s", getIntent().getStringExtra("s").trim());
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void hapusData (String id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Kelurahan.this, R.style.AlertDialogTheme);
        builder.setCancelable(false);
        View v = LayoutInflater.from(Kelurahan.this).inflate(R.layout.dialog_hapus, (ConstraintLayout) findViewById(R.id.layoutDialogContainerHapus));
        builder.setView(v);
        final AlertDialog alertDialog = builder.create();

        v.findViewById(R.id.btnYaHapus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.child(id).removeValue();
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("kelurahan").child(idku);
                ref.child(id).removeValue();
                Toast.makeText(Kelurahan.this, "Data berhasil dihapus", Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
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