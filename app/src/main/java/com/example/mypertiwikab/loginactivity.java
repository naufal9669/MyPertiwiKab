package com.example.mypertiwikab;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class loginactivity extends AppCompatActivity {

    MaterialButton buttonLogin;
    TextView textRegister;
    EditText editTextEmail, editTextPassword;

    // Akun default (user buatan sendiri)
    private final String EMAIL = "admin@gmail.com";
    private final String PASSWORD = "12345";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.halamanlogin);

        buttonLogin = findViewById(R.id.buttonLogin);
        textRegister = findViewById(R.id.textRegister);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);

        // Login check
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputEmail = editTextEmail.getText().toString().trim();
                String inputPassword = editTextPassword.getText().toString().trim();

                if (inputEmail.equals(EMAIL) && inputPassword.equals(PASSWORD)) {
                    Toast.makeText(loginactivity.this, "Login Berhasil", Toast.LENGTH_SHORT).show();
                    Intent explicitIntent = new Intent(loginactivity.this, mainactivity.class);
                    startActivity(explicitIntent);
                    finish(); // biar login ga bisa balik ke halaman login pakai tombol back
                } else {
                    Toast.makeText(loginactivity.this, "Email atau Password salah!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Pindah ke halaman Register
        textRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {   
                Intent explicitIntent = new Intent(loginactivity.this, registeractivity.class);
                startActivity(explicitIntent);
            }
        });
    }
}
