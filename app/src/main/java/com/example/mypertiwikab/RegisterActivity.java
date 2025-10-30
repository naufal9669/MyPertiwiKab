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

public class RegisterActivity extends AppCompatActivity {

    MaterialButton buttonRegister;
    TextView textLogin;
    EditText editTextEmail, editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.halamanregister);

        buttonRegister = findViewById(R.id.buttonRegister);
        textLogin = findViewById(R.id.textViewLogin);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Email/Password tidak boleh kosong", Toast.LENGTH_SHORT).show();
                } else {
                    SharedPreferences prefs = getSharedPreferences("userPrefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("email", email);
                    editor.putString("password", password);
                    editor.putBoolean("isLoggedIn", false);
                    editor.apply();

                    Toast.makeText(RegisterActivity.this, "Registrasi berhasil! Silakan login", Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    finish();
                }
            }
        });

        textLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });
    }
}
