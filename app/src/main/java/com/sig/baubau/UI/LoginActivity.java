package com.sig.baubau.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sig.baubau.R;
import com.sig.baubau.model.Users;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    EditText edUser, edPass;
    Button login;
    private FirebaseAuth mAuth;
    TextView reset;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.bg_login));
        }

        mAuth = FirebaseAuth.getInstance();
        edUser = findViewById(R.id.editTextEmail);
        edPass = findViewById(R.id.editTextPassword);
        login = findViewById(R.id.btnLogin);
        reset = findViewById(R.id.lupa);

        login.setOnClickListener(this);

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this, R.style.AlertDialogTheme);
                builder.setCancelable(false);
                View v = LayoutInflater.from(LoginActivity.this).inflate(R.layout.lupa_pass, (RelativeLayout) findViewById(R.id.containerLupa));
                builder.setView(v);
                final AlertDialog alertDialog = builder.create();

                EditText edEmail = v.findViewById(R.id.editTextEmaill);
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

                edEmail.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (edEmail.getText().toString().matches(emailPattern) && s.length() > 0)
                        {
                            edEmail.setError("Format email salah");
                        }
                    }
                });

                v.findViewById(R.id.btnReset).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String email = edEmail.getText().toString();
                        if (email.isEmpty()) {
                            edEmail.setError("Harap diisi");
                        } else {
                            mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(LoginActivity.this, "Link reset password telah dikirim", Toast.LENGTH_SHORT).show();
                                        alertDialog.cancel();
                                    } else {
                                        Toast.makeText(LoginActivity.this, "Gagal mengirim link", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }
                });

                v.findViewById(R.id.btnBt).setOnClickListener(new View.OnClickListener() {
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
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            reload();
        }
    }

    private void reload() {
        FirebaseAuth.getInstance().signOut();
    }

    private void updateUI(FirebaseUser user) {
        startActivity(new Intent(LoginActivity.this, AdminActivity.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }

    @Override
    public void onClick(View view) {
        String username = edUser.getText().toString();
        String password = edPass.getText().toString();

        if (username.isEmpty() && password.isEmpty()) {
            edUser.setError("Harap diisi");
            edPass.setError("Harap diisi");
        } else if (username.isEmpty()) {
            edUser.setError("Harap diisi");
        } else if (password.isEmpty()) {
            edPass.setError("Harap diisi");
        } else {
            /*mAuth.signInWithEmailAndPassword(username, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                updateUI(user);
                            } else {
                                Toast.makeText(LoginActivity.this, "Username dan Password salah.",
                                        Toast.LENGTH_SHORT).show();
                                updateUI(null);
                            }
                        }
                    });*/
            ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
            progressDialog.setMessage("Autentikasi pengguna");
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();

            DatabaseReference db = FirebaseDatabase.getInstance().getReference("users");
            db.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String u = ds.child("username").getValue().toString();
                        String p = ds.child("password").getValue().toString();

                        if (u.equals(username) && p.equals(password)) {
                            progressDialog.cancel();
                            startActivity(new Intent(LoginActivity.this, AdminActivity.class));
                            finish();
                        } else {
                            progressDialog.cancel();
                            Toast.makeText(LoginActivity.this, "Username atau password salah", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}