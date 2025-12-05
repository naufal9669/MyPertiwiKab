package com.example.mypertiwikab;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

// Import MaterialButton
import com.google.android.material.button.MaterialButton;

// Import Model dan API yang kita buat
import com.example.mypertiwikab.api.ApiClient;
import com.example.mypertiwikab.api.ApiService;
import com.example.mypertiwikab.model.GuruModel;
import com.example.mypertiwikab.model.LoginRequest;
import com.example.mypertiwikab.model.LoginResponse;
import com.example.mypertiwikab.utils.SessionManager;

// Import Retrofit
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    // DEKLARASI VIEW (Menggunakan ID yang BENAR dari XML)
    private EditText editTextEmail, editTextPassword;
    private MaterialButton buttonLogin;
    private TextView textRegister, textLupaPassword;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.halamanlogin);

        // 1. Inisialisasi Session Manager
        sessionManager = new SessionManager(this);

        // 2. Cek apakah sudah Login (Otomatis Redirect jika sudah)
        if (sessionManager.isLoggedIn()) {
            startActivity(new Intent(LoginActivity.this, FiturActivity.class));
            finish();
            return;
        }

        // 3. Inisialisasi View dengan ID yang BENAR
        try {
            // INPUT FIELD
            editTextEmail = findViewById(R.id.editTextEmail);
            editTextPassword = findViewById(R.id.editTextPassword);

            // BUTTON LOGIN (MaterialButton)
            buttonLogin = findViewById(R.id.buttonLogin);

            // TEXT VIEW
            textRegister = findViewById(R.id.textRegister);
            textLupaPassword = findViewById(R.id.textForgotPassword);

        } catch (Exception e) {
            Log.e("LoginActivity", "Error in View Initialization: Pastikan semua ID View sudah benar.");
            Toast.makeText(this, "Error in View Initialization. Check Logcat.", Toast.LENGTH_LONG).show();
        }

        // 4. Listener untuk Tombol Login
        if (buttonLogin != null) {
            buttonLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    attemptLogin();
                }
            });
        }

        // Listener untuk tombol Register (Memakai kode yang sudah kamu punya)
        if (textRegister != null) {
            textRegister.setOnClickListener(v ->
                    startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));
        }

        // Listener untuk tombol Lupa Password (Memakai kode yang sudah kamu punya)
        if (textLupaPassword != null) {
            textLupaPassword.setOnClickListener(v ->
                    startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class)));
        }
    }

    /**
     * Method untuk memproses permintaan Login Guru ke API.
     */
    private void attemptLogin() {
        // Cek kembali view utama sebelum mencoba mengambil teks
        if (editTextEmail == null || editTextPassword == null) {
            Toast.makeText(this, "Error: View Login belum siap.", Toast.LENGTH_LONG).show();
            return;
        }

        String inputEmail = editTextEmail.getText().toString().trim();
        String inputPassword = editTextPassword.getText().toString().trim();

        if (inputEmail.isEmpty() || inputPassword.isEmpty()) {
            Toast.makeText(LoginActivity.this, "Email dan Password wajib diisi!", Toast.LENGTH_SHORT).show();
            return;
        }

        // 1. Buat objek request untuk dikirim ke API
        LoginRequest request = new LoginRequest(inputEmail, inputPassword);

        // 2. Akses Retrofit Client
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<LoginResponse> call = apiService.loginGuru(request);

        // 3. Eksekusi call secara asynchronous
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse loginResponse = response.body();

                    if (loginResponse.isSuccess()) {
                        // ** LOGIN BERHASIL! **
                        GuruModel guru = loginResponse.getGuru();

                        // Simpan data Guru ke SessionManager
                        // PERBAIKAN: MENAMBAHKAN PARAMETER EMAIL
                        sessionManager.createLoginSession(
                                String.valueOf(guru.getId()), // ID Guru
                                guru.getFullname(),            // Nama Lengkap
                                guru.getJabatan(),             // Jabatan/Role
                                guru.getEmail()                // <-- EMAIL WAJIB DITAMBAHKAN
                        );

                        Toast.makeText(LoginActivity.this, "Login Berhasil: " + guru.getFullname(), Toast.LENGTH_SHORT).show();

                        // Pindah ke Activity utama
                        startActivity(new Intent(LoginActivity.this, FiturActivity.class));
                        finish();
                    } else {
                        // Respon API: Gagal (Email/Password salah)
                        Toast.makeText(LoginActivity.this, "Login Gagal: " + loginResponse.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    // HTTP Error (401, 404, 500)
                    String errorMsg = "Gagal terhubung ke server (HTTP Code: " + response.code() + ")";
                    Toast.makeText(LoginActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                // Network Error (server mati, no internet, IP salah di ApiClient)
                Toast.makeText(LoginActivity.this, "Koneksi Gagal: Cek jaringan dan server API (XAMPP/Laragon).", Toast.LENGTH_LONG).show();
            }
        });
    }
}