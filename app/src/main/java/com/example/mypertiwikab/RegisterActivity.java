package com.example.mypertiwikab;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log; // Ditambahkan untuk debugging
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

// Import API dan Model
import com.example.mypertiwikab.api.ApiClient;
import com.example.mypertiwikab.api.ApiService;
import com.example.mypertiwikab.model.DefaultResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


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

        // --- INISIALISASI VIEW ---
        buttonRegister = findViewById(R.id.buttonRegister);
        textLogin = findViewById(R.id.textViewLogin);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextFullname = findViewById(R.id.editTextFullname);

        cardRegister = findViewById(R.id.cardRegister);
        logoRegister = findViewById(R.id.logoRegister);

        // --- ANIMASI (Dibiarkan Sesuai Kode Asli) ---
        Animation fade = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        Animation slide = AnimationUtils.loadAnimation(this, R.anim.slide_up);

        logoRegister.startAnimation(fade);
        cardRegister.startAnimation(slide);

        // --- LOGIKA REGISTRASI BARU (PANGGIL API) ---
        buttonRegister.setOnClickListener(v -> {
            String fullname = editTextFullname.getText().toString().trim();
            String email = editTextEmail.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();

            if (fullname.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(RegisterActivity.this, "Semua field harus diisi", Toast.LENGTH_SHORT).show();
                return;
            }

            // Panggil method API
            attemptRegister(fullname, email, password);

            // Logika SharedPreferences sebelumnya DIHAPUS
        });

        // --- LOGIN ---
        textLogin.setOnClickListener(v ->
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class)));
    }

    /**
     * Method untuk memanggil API Registrasi Guru.
     */
    private void attemptRegister(String fullname, String email, String password) {

        // 1. Akses Retrofit Client
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        // 2. Panggil method registerGuru yang baru kita buat
        Call<DefaultResponse> call = apiService.registerGuru(fullname, email, password);

        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    DefaultResponse registerResponse = response.body();

                    Toast.makeText(RegisterActivity.this, registerResponse.getMessage(), Toast.LENGTH_LONG).show();

                    if (registerResponse.isSuccess()) {
                        // Jika sukses, kembali ke halaman Login
                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                        finish();
                    }
                } else {
                    String errorMsg = "Gagal Registrasi. Cek API PHP.";

                    // Coba ambil pesan error dari body jika ada (misal status 409 Conflict)
                    try {
                        if (response.errorBody() != null) {
                            // Jika API kita merespon JSON error yang valid, kita bisa ambil dari sana.
                            // Untuk saat ini, kita tampilkan kode HTTP saja.
                            errorMsg = "Gagal Registrasi (Kode: " + response.code() + "). Coba cek email sudah terdaftar.";
                        }
                    } catch (Exception e) {
                        Log.e("REGISTER_ERROR", "Failed to parse error body: " + e.getMessage());
                    }

                    Toast.makeText(RegisterActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
                // Network Error (server mati, IP salah)
                Log.e("REGISTER_API_ERROR", "Error: " + t.getMessage());
                Toast.makeText(RegisterActivity.this, "Koneksi Gagal: Cek jaringan dan server API.", Toast.LENGTH_LONG).show();
            }
        });
    }
}