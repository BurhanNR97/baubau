package com.sig.baubau.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sig.baubau.R;
import com.sig.baubau.adapter.adpPosko;
import com.sig.baubau.model.modelPosko;

import java.util.ArrayList;

public class DataPosko extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private adpPosko adapter;
    private ArrayList<modelPosko> myList;
    private ArrayList<String> list;
    DatabaseReference db;
    Spinner tKode;
    ListView layout;
    TextView kosong;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_posko);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.hijau));
        }

        Toolbar toolbar = findViewById(R.id.toolbar_posko);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Posko Pengungsian");

        adapter = new adpPosko(DataPosko.this);
        myList = new ArrayList<>();
        db = FirebaseDatabase.getInstance().getReference("posko");
        layout = findViewById(R.id.rvPosko);
        layout.setOnItemClickListener(DataPosko.this);
        kosong = findViewById(R.id.kosongPosko);
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
                    modelPosko model = ds.getValue(modelPosko.class);
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
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        TextView id = view.findViewById(R.id.itemPosko_kode);
        final String getID = id.getText().toString().trim();

        db.child(getID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                modelPosko model = snapshot.getValue(modelPosko.class);
                Intent intent = new Intent(DataPosko.this, UbahPosko.class);
                intent.putExtra("kode", snapshot.getKey());
                intent.putExtra("nama", model.getNama());
                intent.putExtra("lat", model.getLat());
                intent.putExtra("lng", model.getLng());
                startActivity(intent);
                finish();
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
        Intent intent = new Intent(DataPosko.this, AdminActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_posko, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.add_posko) {
            Intent intent = new Intent(DataPosko.this, TambahPosko.class);
            intent.putExtra("kode", "");
            intent.putExtra("nama", "");
            intent.putExtra("lat", "");
            intent.putExtra("lng", "");
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}