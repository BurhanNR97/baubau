package com.sig.baubau.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.sig.baubau.R;
import com.sig.baubau.fragments.BanjirFragment;
import com.sig.baubau.fragments.LongsorFragment;

public class Bencana extends AppCompatActivity {
    RadioButton rbBanjir, rbLongsor;
    ImageView tmbl;

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bencana);

        rbBanjir = findViewById(R.id.rb_banjir);
        rbLongsor = findViewById(R.id.rb_longsor);
        tmbl = findViewById(R.id.backFromPeta);
        tmbl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        Fragment fragment = new BanjirFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.framePeta, fragment);
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(Bencana.this, MainActivity.class));
        finish();
    }

    public void onRadioButtinCLicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        Fragment fragment = null;
        Bundle data = new Bundle();

        switch (view.getId()) {
            case R.id.rb_banjir:
                if (checked) {
                    fragment = new BanjirFragment();
                    fragment.setArguments(data);
                }
                break;
            case R.id.rb_longsor:
                if (checked) {
                    fragment = new LongsorFragment();
                    fragment.setArguments(data);
                }
                break;
        }

        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.framePeta, fragment);
            ft.commit();
        }
    }
}