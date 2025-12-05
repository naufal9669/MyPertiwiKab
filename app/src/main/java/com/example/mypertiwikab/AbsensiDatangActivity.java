package com.example.mypertiwikab;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull; // <--- WAJIB DITAMBAHKAN

import com.example.mypertiwikab.api.ApiClient; // <--- WAJIB DITAMBAHKAN
import com.example.mypertiwikab.api.ApiService; // <--- WAJIB DITAMBAHKAN
import com.example.mypertiwikab.model.AbsenRequest; // <--- WAJIB DITAMBAHKAN
import com.example.mypertiwikab.model.DefaultResponse; // <--- WAJIB DITAMBAHKAN
import com.example.mypertiwikab.utils.SessionManager; // <--- WAJIB DITAMBAHKAN

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import retrofit2.Call; // <--- WAJIB DITAMBAHKAN
import retrofit2.Callback; // <--- WAJIB DITAMBAHKAN
import retrofit2.Response; // <--- WAJIB DITAMBAHKAN

public class AbsensiDatangActivity extends AppCompatActivity {

    private EditText inputTanggal, inputNamaGuru, inputWaktuDatang;
    private Button btnAbsen;
    private ImageView btnBack;
    private final Calendar calendar = Calendar.getInstance();

    private SessionManager sessionManager;
    private int guruId;
    private String namaGuru;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.halamanabsensidatang);

        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();

        inputTanggal = findViewById(R.id.inputTanggal);
        inputNamaGuru = findViewById(R.id.inputNamaGuru);
        inputWaktuDatang = findViewById(R.id.inputWaktuDatang);
        btnAbsen = findViewById(R.id.btnAbsen);
        btnBack = findViewById(R.id.btnBack);

        // --- AMBIL DATA DARI SESSION ---
        HashMap<String, String> user = sessionManager.getUserDetails();
        try {
            String idStr = user.get(SessionManager.KEY_GURU_ID);
            if (idStr == null) throw new NumberFormatException("ID Guru is null");
            guruId = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "ID Guru tidak valid. Silakan login ulang.", Toast.LENGTH_LONG).show();
            sessionManager.logoutUser();
            finish();
            return;
        }
        namaGuru = user.get(SessionManager.KEY_NAMA);

        // 🟦 AUTO-FILL DATA
        inputNamaGuru.setText(namaGuru);
        inputNamaGuru.setEnabled(false);

        String tanggalNow = new SimpleDateFormat("dd MMMM yyyy", new Locale("id", "ID")).format(calendar.getTime());
        inputTanggal.setText(tanggalNow);
        inputTanggal.setEnabled(false);

        String waktuNow = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(calendar.getTime());
        inputWaktuDatang.setText(waktuNow);
        inputWaktuDatang.setEnabled(false);

        // Tombol kembali
        btnBack.setOnClickListener(v -> onBackPressed());

        // Tombol Absen ditekan
        btnAbsen.setOnClickListener(v -> {
            postAbsen("datang"); // Panggil API dengan tipe "datang"
        });
    }

    // --- FUNGSI CALL API ABSENSI ---
    private void postAbsen(String tipeAbsen) {
        if (guruId == 0) {
            Toast.makeText(this, "Error: ID Guru tidak ditemukan. Silakan login ulang.", Toast.LENGTH_LONG).show();
            return;
        }

        // 1. Buat request object
        AbsenRequest request = new AbsenRequest(guruId, tipeAbsen);

        // 2. Panggil API Service
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<DefaultResponse> call = apiService.postAbsen(request);

        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(@NonNull Call<DefaultResponse> call, @NonNull Response<DefaultResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    DefaultResponse absenResponse = response.body();
                    Toast.makeText(AbsensiDatangActivity.this, absenResponse.getMessage(), Toast.LENGTH_LONG).show();

                    if (absenResponse.isSuccess()) {
                        // Kembali ke fragment absensi jika sukses
                        Intent intent = new Intent(AbsensiDatangActivity.this, FiturActivity.class);
                        intent.putExtra("openFragment", "absensi");
                        startActivity(intent);
                        finish();
                    }
                } else {
                    // Tampilkan pesan error spesifik jika API gagal (misal code 409: Sudah Absen)
                    Toast.makeText(AbsensiDatangActivity.this, "Absen Gagal. Status: " + response.code(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<DefaultResponse> call, @NonNull Throwable t) {
                Toast.makeText(AbsensiDatangActivity.this, "Koneksi Gagal: Cek API Absensi.", Toast.LENGTH_LONG).show();
            }
        });
    }
}