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

// API Imports WAJIB
import com.example.mypertiwikab.api.ApiClient;
import com.example.mypertiwikab.api.ApiService;
import com.example.mypertiwikab.model.DefaultResponse;
import com.example.mypertiwikab.model.OtpResponse;
import com.example.mypertiwikab.utils.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordActivity extends AppCompatActivity {

    EditText etEmail;
    Button btnNext, btnBack, btnOk;
    View popupEmail;

    private static final String PREFS_OTP = "userPrefsOTP";
    private static final String KEY_PENDING_EMAIL = "pending_email";
    private static final String KEY_PENDING_GURU_ID = "pending_guru_id";

    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password);

        apiService = ApiClient.getClient().create(ApiService.class);

        etEmail = findViewById(R.id.etEmail);
        btnNext = findViewById(R.id.btnNext);
        popupEmail = findViewById(R.id.popupEmail);
        btnOk = findViewById(R.id.btnOk);
        btnBack = findViewById(R.id.btnBack);

        btnNext.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();

            if (email.isEmpty()) {
                etEmail.setError("Email tidak boleh kosong");
                return;
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                etEmail.setError("Format email salah");
                return;
            }

            sendOtpApi(email);
        });

        btnBack.setOnClickListener(v -> onBackPressed());

        // Popup OK -> buka OTP verification activity
        btnOk.setOnClickListener(v -> {
            popupEmail.setVisibility(View.GONE);

            SharedPreferences prefs = getSharedPreferences(PREFS_OTP, MODE_PRIVATE);
            String email = prefs.getString(KEY_PENDING_EMAIL, "");
            String guruId = prefs.getString(KEY_PENDING_GURU_ID, "0");

            Intent intent = new Intent(ForgotPasswordActivity.this, OTPVerificationActivity.class);
            intent.putExtra("email", email);
            intent.putExtra("guru_id", guruId);
            startActivity(intent);
        });
    }

    private void sendOtpApi(String email) {
        apiService.sendOtpRequest(email).enqueue(new Callback<OtpResponse>() {
            @Override
            public void onResponse(Call<OtpResponse> call, Response<OtpResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {

                    OtpResponse otpResponse = response.body();

                    // SIMPAN ID GURU YANG DIKEMBALIKAN SERVER
                    SharedPreferences prefs = getSharedPreferences(PREFS_OTP, MODE_PRIVATE);
                    prefs.edit()
                            .putString(KEY_PENDING_EMAIL, email)
                            .putString(KEY_PENDING_GURU_ID, otpResponse.getGuruId()) // <-- FIX: Ambil ID dari OtpResponse
                            .apply();

                    Toast.makeText(ForgotPasswordActivity.this, otpResponse.getMessage(), Toast.LENGTH_LONG).show();
                    popupEmail.setVisibility(View.VISIBLE);
                } else {
                    String msg = response.body() != null ? response.body().getMessage() : "Email tidak terdaftar/Server error.";
                    Toast.makeText(ForgotPasswordActivity.this, "Gagal: " + msg, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<OtpResponse> call, Throwable t) {
                Toast.makeText(ForgotPasswordActivity.this, "Koneksi Gagal ke Server OTP.", Toast.LENGTH_LONG).show();
            }
        });
    }
}