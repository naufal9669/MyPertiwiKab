package com.example.mypertiwikab;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class OTPVerificationActivity extends AppCompatActivity {

    ImageView btnBack;
    EditText otp1, otp2, otp3, otp4;
    Button btnVerify, btnResend;
    TextView tvTimer;

    String correctOtp = "1234"; // contoh OTP dummy

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.otp_verification);

        btnBack = findViewById(R.id.btnBack);
        otp1 = findViewById(R.id.otp1);
        otp2 = findViewById(R.id.otp2);
        otp3 = findViewById(R.id.otp3);
        otp4 = findViewById(R.id.otp4);
        btnVerify = findViewById(R.id.btnVerify);
        btnResend = findViewById(R.id.btnResend);
        tvTimer = findViewById(R.id.tvTimer);

        // Back button
        btnBack.setOnClickListener(v -> onBackPressed());

        // Timer 30 detik
        startTimer();

        btnVerify.setOnClickListener(v -> {
            String code = otp1.getText().toString() +
                    otp2.getText().toString() +
                    otp3.getText().toString() +
                    otp4.getText().toString();

            if (code.length() < 4) {
                Toast.makeText(this, "Masukkan semua digit OTP!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!code.equals(correctOtp)) {
                Toast.makeText(this, "OTP salah!", Toast.LENGTH_SHORT).show();
                return;
            }

            startActivity(new Intent(OTPVerificationActivity.this, ResetPasswordActivity.class));
            finish();
        });

        btnResend.setOnClickListener(v -> {
            Toast.makeText(this, "Kode OTP dikirim ulang!", Toast.LENGTH_SHORT).show();
            btnResend.setEnabled(false);
            startTimer();
        });
    }

    private void startTimer() {
        new CountDownTimer(30000, 1000) { // 30 detik
            @Override
            public void onTick(long millisUntilFinished) {
                tvTimer.setText("00:" + (millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                tvTimer.setText("Kirim ulang kode");
                btnResend.setEnabled(true);
            }
        }.start();
    }
}
