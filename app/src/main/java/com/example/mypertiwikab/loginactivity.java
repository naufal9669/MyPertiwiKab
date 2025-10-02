package com.example.mypertiwikab;

import android.content.Intent;
import android.content.SharedPreferences;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.halamanlogin);

        buttonLogin = findViewById(R.id.buttonLogin);
        textRegister = findViewById(R.id.textRegister);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);

        SharedPreferences prefs = getSharedPreferences("userPrefs", MODE_PRIVATE);

        // Kalau sudah login sebelumnya, langsung ke dashboard
        if (prefs.getBoolean("isLoggedIn", false)) {
            startActivity(new Intent(loginactivity.this, dashboard.class));
            finish();
        }

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputEmail = editTextEmail.getText().toString().trim();
                String inputPassword = editTextPassword.getText().toString().trim();

                String savedEmail = prefs.getString("email", "admin@gmail.com");
                String savedPassword = prefs.getString("password", "12345");

                if (inputEmail.equals(savedEmail) && inputPassword.equals(savedPassword)) {
                    Toast.makeText(loginactivity.this, "Login Berhasil", Toast.LENGTH_SHORT).show();

                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean("isLoggedIn", true);
                    editor.apply();

                    Intent explicitIntent = new Intent(loginactivity.this, dashboard.class);
                    startActivity(explicitIntent);
                    finish();
                } else {
                    Toast.makeText(loginactivity.this, "Email atau Password salah!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        textRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent explicitIntent = new Intent(loginactivity.this, registeractivity.class);
                startActivity(explicitIntent);
            }
        });
    }
}
