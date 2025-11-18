package com.example.mypertiwikab;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AbsensiDatangActivity extends AppCompatActivity {

    private EditText inputTanggal, inputNamaGuru, inputWaktuDatang;
    private Button btnAbsen;
    private ImageView btnBack;
    private final Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.halamanabsensidatang);

        inputTanggal = findViewById(R.id.inputTanggal);
        inputNamaGuru = findViewById(R.id.inputNamaGuru);
        inputWaktuDatang = findViewById(R.id.inputWaktuDatang);
        btnAbsen = findViewById(R.id.btnAbsen);
        btnBack = findViewById(R.id.btnBack);

        // 🟦 AUTO-FILL TANGGAL (tidak bisa diedit)
        String tanggalNow = new SimpleDateFormat("dd MMMM yyyy", new Locale("id", "ID"))
                .format(calendar.getTime());
        inputTanggal.setText(tanggalNow);
        inputTanggal.setEnabled(false); // ❌ tidak bisa diedit

        // 🟦 AUTO-FILL WAKTU DATANG (tidak bisa diedit)
        String waktuNow = new SimpleDateFormat("HH:mm", Locale.getDefault())
                .format(calendar.getTime());
        inputWaktuDatang.setText(waktuNow);
        inputWaktuDatang.setEnabled(false); // ❌ tidak bisa diedit

        // Tombol kembali
        btnBack.setOnClickListener(v -> onBackPressed());

        // Tombol Absen ditekan
        btnAbsen.setOnClickListener(v -> {
            String nama = inputNamaGuru.getText().toString().trim();

            if (nama.isEmpty()) {
                Toast.makeText(this, "Nama guru wajib diisi!", Toast.LENGTH_SHORT).show();
                return;
            }

            Toast.makeText(this, "Absensi berhasil disimpan!", Toast.LENGTH_SHORT).show();

            // Kembali ke fragment absensi
            Intent intent = new Intent(AbsensiDatangActivity.this, FiturActivity.class);
            intent.putExtra("openFragment", "absensi");
            startActivity(intent);
            finish();
        });
    }
}
