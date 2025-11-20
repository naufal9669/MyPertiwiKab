package com.example.mypertiwikab;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class LoginActivity extends AppCompatActivity {

    MaterialButton buttonLogin;
    TextView textRegister, textLupaPassword;
    EditText editTextEmail, editTextPassword;
    View cardLogin;    // Card putih
    ImageView logoImage; // Logo animasi

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.halamanlogin);  // pastikan nama layout sama

        // --- INISIALISASI VIEW ---
        buttonLogin = findViewById(R.id.buttonLogin);
        textRegister = findViewById(R.id.textRegister);
        textLupaPassword = findViewById(R.id.textForgotPassword);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);

        // Tambahan untuk animasi
        cardLogin = findViewById(R.id.cardLogin);
        logoImage = findViewById(R.id.logoImage);

        // --- ANIMASI ---
        Animation fade = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        Animation slide = AnimationUtils.loadAnimation(this, R.anim.slide_up);

        logoImage.startAnimation(fade);
        cardLogin.startAnimation(slide);

        // --- LOGIN LOGIC ---
        SharedPreferences prefs = getSharedPreferences("userPrefs", MODE_PRIVATE);

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

                startActivity(new Intent(LoginActivity.this, FiturActivity.class));
                finish();
            } else {
                Toast.makeText(LoginActivity.this, "Email atau Password salah!", Toast.LENGTH_SHORT).show();
            }
        });

        // --- REGISTER ---
        textRegister.setOnClickListener(v ->
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));

        // --- LUPA PASSWORD ---
        textLupaPassword.setOnClickListener(v ->
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class)));
    }
}
