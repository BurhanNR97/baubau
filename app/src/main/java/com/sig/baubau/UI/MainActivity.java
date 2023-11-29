package com.sig.baubau.UI;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.sig.baubau.R;

public class MainActivity extends AppCompatActivity {
    private CardView cvBencana, cvPosko, cvTentang, cvLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.primary));
        }

        cvBencana = findViewById(R.id.cvBencana);
        cvPosko = findViewById(R.id.cvPosko);
        cvTentang = findViewById(R.id.cvTentang);
        cvLogin = findViewById(R.id.cvLogin);

        cvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            }
        });

        cvBencana.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, Bencana.class));
                finish();
            }
        });

        cvTentang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.AlertDialogTheme);
                builder.setCancelable(false);
                View v = LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_tentang, (ConstraintLayout) findViewById(R.id.layoutDialogContainerInfo));
                builder.setView(v);
                final AlertDialog alertDialog = builder.create();

                v.findViewById(R.id.btnOK).setOnClickListener(new View.OnClickListener() {
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

        cvPosko.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, Posko.class));
                finish();
            }
        });
    }
}