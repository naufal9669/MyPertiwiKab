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

public class LoginActivity extends AppCompatActivity {

    MaterialButton buttonLogin;
    TextView textRegister, textLupaPassword;
    EditText editTextEmail, editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.halamanlogin);

        buttonLogin = findViewById(R.id.buttonLogin);
        textRegister = findViewById(R.id.textRegister);
        textLupaPassword = findViewById(R.id.textForgotPassword); // ⬅ Tambahin ini
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);

        SharedPreferences prefs = getSharedPreferences("userPrefs", MODE_PRIVATE);

        // Tombol Login
        buttonLogin.setOnClickListener(v -> {
            String inputEmail = editTextEmail.getText().toString().trim();
            String inputPassword = editTextPassword.getText().toString().trim();

            String savedEmail = prefs.getString("email", "admin@gmail.com");
            String savedPassword = prefs.getString("password", "12345");

            if (inputEmail.equals(savedEmail) && inputPassword.equals(savedPassword)) {
                Toast.makeText(LoginActivity.this, "Login Berhasil", Toast.LENGTH_SHORT).show();

                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("isLoggedIn", true);
                editor.apply();

                Intent explicitIntent = new Intent(LoginActivity.this, FiturActivity.class);
                startActivity(explicitIntent);
                finish();
            } else {
                Toast.makeText(LoginActivity.this, "Email atau Password salah!", Toast.LENGTH_SHORT).show();
            }
        });

        // Tombol Register
        textRegister.setOnClickListener(v -> {
            Intent explicitIntent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(explicitIntent);
        });

        // Tombol Lupa Password  ⬅ Wajib Ada!
        textLupaPassword.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
            startActivity(intent);
        });
    }
}
