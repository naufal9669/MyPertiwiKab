package com.example.mypertiwikab;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/**
 * ForgotPasswordActivity:
 * - Validasi email
 * - Generate OTP (4-digit) dan simpan di SharedPreferences
 * - Tampilkan popup bahwa OTP dikirim (simulasi) -> btnOk menuju OTPVerificationActivity
 */
public class ForgotPasswordActivity extends AppCompatActivity {

    EditText etEmail;
    Button btnNext, btnBack, btnOk;
    View popupEmail;

    private static final String PREFS = "userPrefs";
    private static final String KEY_PENDING_OTP = "pending_otp";
    private static final String KEY_PENDING_EMAIL = "pending_email";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password);

        etEmail = findViewById(R.id.etEmail);
        btnNext = findViewById(R.id.btnNext);
        popupEmail = findViewById(R.id.popupEmail);
        btnOk = findViewById(R.id.btnOk);
        btnBack = findViewById(R.id.btnBack);

        // Next -> validate email, generate OTP, show popup
        btnNext.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                etEmail.setError("Format email salah");
                return;
            }

            // Generate 4-digit OTP
            String otp = generateOtp4();

            // Save OTP & email to SharedPreferences (simulation of server)
            SharedPreferences prefs = getSharedPreferences(PREFS, MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(KEY_PENDING_OTP, otp);
            editor.putString(KEY_PENDING_EMAIL, email);
            editor.apply();

            // Simulate sending email (for dev: show toast and popup)
            Toast.makeText(this, "OTP dikirim ke: " + email + " (kode: " + otp + ")", Toast.LENGTH_LONG).show();

            // Show popup overlay
            popupEmail.setVisibility(View.VISIBLE);
        });

        // Back button
        btnBack.setOnClickListener(v -> onBackPressed());

        // Popup OK -> buka OTP verification activity
        btnOk.setOnClickListener(v -> {
            popupEmail.setVisibility(View.GONE);

            // Ambil email dari prefs (yang baru saja disimpan)
            SharedPreferences prefs = getSharedPreferences(PREFS, MODE_PRIVATE);
            String email = prefs.getString(KEY_PENDING_EMAIL, "");

            Intent intent = new Intent(ForgotPasswordActivity.this, OTPVerificationActivity.class);
            intent.putExtra("email", email);
            startActivity(intent);
            // jangan finish, biar user bisa kembali kalau ingin
        });
    }

    private String generateOtp4() {
        int random = (int) (Math.random() * 9000) + 1000; // 1000..9999
        return String.valueOf(random);
    }
}
