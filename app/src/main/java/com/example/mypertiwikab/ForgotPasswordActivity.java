package com.example.mypertiwikab;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class ForgotPasswordActivity extends AppCompatActivity {

    EditText etEmail;
    Button btnNext, btnBack, btnOk;
    View popupEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password);

        etEmail = findViewById(R.id.etEmail);
        btnNext = findViewById(R.id.btnNext);
        popupEmail = findViewById(R.id.popupEmail);
        btnOk = findViewById(R.id.btnOk);
        btnBack = findViewById(R.id.btnBack);

        btnNext.setOnClickListener(v -> {
            String email = etEmail.getText().toString();

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                etEmail.setError("Format email salah");
                return;
            }

            popupEmail.setVisibility(View.VISIBLE);
        });
        btnBack.setOnClickListener(v -> onBackPressed());


        btnOk.setOnClickListener(v -> {
            popupEmail.setVisibility(View.GONE);
            startActivity(new Intent(this, ResetPasswordActivity.class));
        });
    }
}
