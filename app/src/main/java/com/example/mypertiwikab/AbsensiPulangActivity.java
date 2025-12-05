package com.example.mypertiwikab;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;

import com.example.mypertiwikab.api.ApiClient;
import com.example.mypertiwikab.api.ApiService;
import com.example.mypertiwikab.model.AbsenRequest;
import com.example.mypertiwikab.model.DefaultResponse;
import com.example.mypertiwikab.utils.SessionManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AbsensiPulangActivity extends AppCompatActivity {

    private EditText inputTanggal, inputNamaGuru, inputWaktuPulang;
    private Button btnAbsen;
    private ImageView btnBack;
    private final Calendar calendar = Calendar.getInstance();

    private SessionManager sessionManager;
    private int guruId;
    private String namaGuru;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.halamanabsensipulang);

        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();

        inputTanggal = findViewById(R.id.inputTanggal);
        inputNamaGuru = findViewById(R.id.inputNamaGuru);
        inputWaktuPulang = findViewById(R.id.inputWaktuPulang);
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

        // 🟦 AUTO-FILL DATA (Nama Guru, Tanggal Hari Ini, Waktu Saat Ini)
        inputNamaGuru.setText(namaGuru);
        inputNamaGuru.setEnabled(false); // Tidak bisa diedit

        String tanggalNow = new SimpleDateFormat("dd MMMM yyyy", new Locale("id", "ID")).format(calendar.getTime());
        inputTanggal.setText(tanggalNow);
        inputTanggal.setEnabled(false); // Tidak bisa diedit

        String waktuNow = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(calendar.getTime());
        inputWaktuPulang.setText(waktuNow);
        inputWaktuPulang.setEnabled(false); // Tidak bisa diedit (Waktu Server yang menentukan)

        // Tombol kembali
        btnBack.setOnClickListener(v -> onBackPressed());

        // Tombol Absen ditekan
        btnAbsen.setOnClickListener(v -> {
            postAbsen("pulang"); // Panggil API dengan tipe "pulang"
        });
    }

    // --- FUNGSI CALL API ABSENSI (untuk Pulang) ---
    private void postAbsen(String tipeAbsen) {
        if (guruId == 0) {
            Toast.makeText(this, "Error: ID Guru tidak ditemukan. Silakan login ulang.", Toast.LENGTH_LONG).show();
            return;
        }

        AbsenRequest request = new AbsenRequest(guruId, tipeAbsen);
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<DefaultResponse> call = apiService.postAbsen(request);

        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(@NonNull Call<DefaultResponse> call, @NonNull Response<DefaultResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    DefaultResponse absenResponse = response.body();
                    Toast.makeText(AbsensiPulangActivity.this, absenResponse.getMessage(), Toast.LENGTH_LONG).show();

                    if (absenResponse.isSuccess()) {
                        // Kembali ke fragment absensi jika sukses
                        Intent intent = new Intent(AbsensiPulangActivity.this, FiturActivity.class);
                        intent.putExtra("openFragment", "absensi");
                        startActivity(intent);
                        finish();
                    }
                } else {
                    Toast.makeText(AbsensiPulangActivity.this, "Absen Pulang Gagal. Status: " + response.code(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<DefaultResponse> call, @NonNull Throwable t) {
                Toast.makeText(AbsensiPulangActivity.this, "Koneksi Gagal: Cek API Absensi.", Toast.LENGTH_LONG).show();
            }
        });
    }
}