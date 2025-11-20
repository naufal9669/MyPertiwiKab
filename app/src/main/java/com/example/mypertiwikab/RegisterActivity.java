package com.example.mypertiwikab;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class RegisterActivity extends AppCompatActivity {

    MaterialButton buttonRegister;
    TextView textLogin;
    EditText editTextEmail, editTextPassword, editTextFullname;
    LinearLayout cardRegister;
    ImageView logoRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.halamanregister);

        buttonRegister = findViewById(R.id.buttonRegister);
        textLogin = findViewById(R.id.textViewLogin);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextFullname = findViewById(R.id.editTextFullname);

        cardRegister = findViewById(R.id.cardRegister);
        logoRegister = findViewById(R.id.logoRegister);

        // ANIMASI
        Animation fade = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        Animation slide = AnimationUtils.loadAnimation(this, R.anim.slide_up);

        logoRegister.startAnimation(fade);
        cardRegister.startAnimation(slide);

        buttonRegister.setOnClickListener(v -> {
            String fullname = editTextFullname.getText().toString().trim();
            String email = editTextEmail.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();

            if (fullname.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(RegisterActivity.this, "Semua field harus diisi", Toast.LENGTH_SHORT).show();
                return;
            }

            SharedPreferences prefs = getSharedPreferences("userPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("fullname", fullname);
            editor.putString("email", email);
            editor.putString("password", password);
            editor.putBoolean("isLoggedIn", false);
            editor.apply();

            Toast.makeText(RegisterActivity.this, "Registrasi berhasil! Silakan login", Toast.LENGTH_SHORT).show();

            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();
        });

        textLogin.setOnClickListener(v ->
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class)));
    }
}
