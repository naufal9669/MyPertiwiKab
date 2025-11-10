package com.example.mypertiwikab;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AbsensiPulangActivity extends AppCompatActivity {

    private EditText inputTanggal, inputNamaGuru, inputWaktuPulang;
    private Button btnAbsen;
    private ImageView btnBack, btnPilihTanggal;
    private final Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.halamanabsensipulang);

        inputTanggal = findViewById(R.id.inputTanggal);
        inputNamaGuru = findViewById(R.id.inputNamaGuru);
        inputWaktuPulang = findViewById(R.id.inputWaktuPulang);
        btnAbsen = findViewById(R.id.btnAbsen);
        btnBack = findViewById(R.id.btnBack);
        btnPilihTanggal = findViewById(R.id.btnPilihTanggal);

        // Pilih tanggal
        btnPilihTanggal.setOnClickListener(v -> showDatePicker());
        inputTanggal.setOnClickListener(v -> showDatePicker());

        // Tombol kembali
        btnBack.setOnClickListener(v -> onBackPressed());

        // Tombol Absen ditekan
        btnAbsen.setOnClickListener(v -> {
            String nama = inputNamaGuru.getText().toString().trim();
            String tanggal = inputTanggal.getText().toString().trim();
            String waktu = inputWaktuPulang.getText().toString().trim();

            if (nama.isEmpty() || tanggal.isEmpty() || waktu.isEmpty()) {
                Toast.makeText(this, "Lengkapi semua data!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Absen pulang berhasil disimpan!", Toast.LENGTH_SHORT).show();
            }
            Intent intent = new Intent(AbsensiPulangActivity.this, FiturActivity.class);
            intent.putExtra("openFragment", "absensi");
            startActivity(intent);
        });
    }

    private void showDatePicker() {
        DatePickerDialog datePicker = new DatePickerDialog(
                this,
                (DatePicker view, int year, int month, int dayOfMonth) -> {
                    calendar.set(year, month, dayOfMonth);
                    SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", new Locale("id", "ID"));
                    inputTanggal.setText(sdf.format(calendar.getTime()));
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePicker.show();
    }
}
