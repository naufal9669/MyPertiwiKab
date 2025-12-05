package com.example.mypertiwikab;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

// API Imports
import com.example.mypertiwikab.api.ApiClient;
import com.example.mypertiwikab.api.ApiService;
import com.example.mypertiwikab.model.DefaultResponse;
import com.example.mypertiwikab.model.OtpResponse; // <-- INI WAJIB DITAMBAHKAN

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OTPVerificationActivity extends AppCompatActivity {

    ImageView btnBack;
    EditText otp1, otp2, otp3, otp4;
    Button btnVerify, btnResend;
    TextView tvTimer;

    String guruId;
    String email;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.otp_verification);

        apiService = ApiClient.getClient().create(ApiService.class);

        // Ambil data dari Intent
        email = getIntent().getStringExtra("email");
        guruId = getIntent().getStringExtra("guru_id");

        if (guruId == null || guruId.equals("0")) {
            Toast.makeText(this, "Sesi ID Guru hilang. Silakan ulangi proses lupa password.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        btnBack = findViewById(R.id.btnBack);
        otp1 = findViewById(R.id.otp1);
        otp2 = findViewById(R.id.otp2);
        otp3 = findViewById(R.id.otp3);
        otp4 = findViewById(R.id.otp4);
        btnVerify = findViewById(R.id.btnVerify);
        btnResend = findViewById(R.id.btnResend);
        tvTimer = findViewById(R.id.tvTimer);

        // Auto-move cursor
        setupOtpInput(otp1, otp2);
        setupOtpInput(otp2, otp3);
        setupOtpInput(otp3, otp4);


        // Back button
        btnBack.setOnClickListener(v -> onBackPressed());

        // Timer 30 detik
        startTimer();

        // TOMBOL VERIFIKASI
        btnVerify.setOnClickListener(v -> {
            String code = otp1.getText().toString() +
                    otp2.getText().toString() +
                    otp3.getText().toString() +
                    otp4.getText().toString();

            if (code.length() < 4) {
                Toast.makeText(this, "Masukkan semua digit OTP!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Panggil API verifikasi
            verifyOtpApi(Integer.parseInt(guruId), code);
        });

        // TOMBOL KIRIM ULANG
        btnResend.setOnClickListener(v -> {
            Toast.makeText(this, "Meminta kode baru...", Toast.LENGTH_SHORT).show();
            sendResendOtpApi(email);
        });
    }

    // --- FUNGSI API VERIFIKASI (FIX CALLBACK) ---
    private void verifyOtpApi(int id, String code) {
        apiService.verifyOtpRequest(id, code).enqueue(new Callback<OtpResponse>() { // <-- PERBAIKAN DI SINI
            @Override
            public void onResponse(Call<OtpResponse> call, Response<OtpResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {

                    // Sukses verifikasi, pindah ke ResetPassword
                    Toast.makeText(OTPVerificationActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(OTPVerificationActivity.this, ResetPasswordActivity.class);
                    // Kirim ID Guru ke halaman reset password
                    intent.putExtra("guru_id", String.valueOf(id));
                    startActivity(intent);
                    finish();

                } else {
                    String msg = response.body() != null ? response.body().getMessage() : "Verifikasi gagal/kode kadaluarsa.";
                    Toast.makeText(OTPVerificationActivity.this, msg, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<OtpResponse> call, Throwable t) {
                Toast.makeText(OTPVerificationActivity.this, "Koneksi Gagal Verifikasi.", Toast.LENGTH_LONG).show();
            }
        });
    }

    // --- FUNGSI API KIRIM ULANG (FIX CALLBACK) ---
    private void sendResendOtpApi(String email) {
        apiService.sendOtpRequest(email).enqueue(new Callback<OtpResponse>() { // <-- PERBAIKAN DI SINI
            @Override
            public void onResponse(Call<OtpResponse> call, Response<OtpResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    Toast.makeText(OTPVerificationActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                    btnResend.setEnabled(false);
                    startTimer();
                } else {
                    Toast.makeText(OTPVerificationActivity.this, "Gagal kirim ulang.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<OtpResponse> call, Throwable t) {
                Toast.makeText(OTPVerificationActivity.this, "Gagal koneksi server.", Toast.LENGTH_LONG).show();
            }
        });
    }

    // --- HELPER METHOD BAWAAN ---
    private void startTimer() {
        new CountDownTimer(30000, 1000) {
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

    private void setupOtpInput(final EditText current, final EditText next) {
        current.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 1 && next != null) {
                    next.requestFocus();
                }
            }
            @Override public void afterTextChanged(Editable s) {}
        });
    }
}