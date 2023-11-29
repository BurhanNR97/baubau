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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sig.baubau.R;

public class AdminActivity extends AppCompatActivity {
    LinearLayout lyKec, lyBanjir, lyLongsor;
    ConstraintLayout lyAkun, lyPosko;
    TextView tvAkun, tvPosko, tvKec, tvBanjir, tvLongsor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.hijau));
        }

        lyAkun = findViewById(R.id.layoutAkun);
        tvAkun = findViewById(R.id.jmlAkun);

        lyBanjir = findViewById(R.id.layoutBanjir);
        tvBanjir = findViewById(R.id.jmlBanjir);
        lyBanjir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminActivity.this, Banjir.class));
                finish();
            }
        });

        tvLongsor = findViewById(R.id.jmlLongsor);
        lyLongsor = findViewById(R.id.layoutLongsor);
        lyLongsor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminActivity.this, Longsor.class));
                finish();
            }
        });

        tvPosko = findViewById(R.id.jmlPosko);
        lyPosko = findViewById(R.id.layoutPosko);
        lyPosko.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminActivity.this, DataPosko.class));
                finish();
            }
        });

        lyKec = findViewById(R.id.layoutKec);
        tvKec = findViewById(R.id.jmlKec);
        lyKec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminActivity.this, Kecamatan.class);
                intent.putExtra("a", "0");
                startActivity(intent);
            }
        });
        dataKec();
        dataBanjir();
        dataLongsor();
        dataAkun();
        dataPosko();

        Button keluar = findViewById(R.id.btnLogout);
        keluar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AdminActivity.this, R.style.AlertDialogTheme);
                builder.setCancelable(false);
                View v = LayoutInflater.from(AdminActivity.this).inflate(R.layout.dialog_hapus, (ConstraintLayout) findViewById(R.id.layoutDialogContainerHapus));
                builder.setView(v);
                final AlertDialog alertDialog = builder.create();

                TextView tanya = v.findViewById(R.id.txtTanyaHapus);
                tanya.setText("Apakah anda ingin logout ?");

                v.findViewById(R.id.btnYaHapus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(AdminActivity.this, MainActivity.class));
                        finish();
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
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void dataKec() {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference("kecamatan");
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tvKec.setText(""+snapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void dataBanjir() {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference("banjir");
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tvBanjir.setText(""+snapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void dataLongsor() {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference("longsor");
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tvLongsor.setText(""+snapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void dataPosko() {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference("posko");
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tvPosko.setText(""+snapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void dataAkun() {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference("users");
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tvAkun.setText(""+snapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}