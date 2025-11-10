package com.example.mypertiwikab;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class PengajuanIzinActivity extends AppCompatActivity {

    private ImageButton btnBack, btnPilihTanggal;
    private TextView textTanggal;
    private Spinner spinnerJenisIzin;
    private EditText editTextDeskripsi;
    private Button buttonAjukanIzin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.halamanpengajuanizin);

        // Hubungkan view dengan id di XML
        btnBack = findViewById(R.id.btnBack);
        btnPilihTanggal = findViewById(R.id.btnPilihTanggal);
        textTanggal = findViewById(R.id.textTanggal);
        spinnerJenisIzin = findViewById(R.id.spinnerJenisIzin);
        editTextDeskripsi = findViewById(R.id.editTextDeskripsi);
        buttonAjukanIzin = findViewById(R.id.buttonajukanIzin);

        // 🔙 Tombol kembali
        btnBack.setOnClickListener(v -> onBackPressed());

        // 🗓️ Pilih tanggal
        btnPilihTanggal.setOnClickListener(v -> showDatePicker());

        // 🪣 Isi spinner dengan array dari resources
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.arraysizin, // pastikan array ini ada di res/values/arrays.xml
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerJenisIzin.setAdapter(adapter);

        // 🚀 Tombol Ajukan izin
        buttonAjukanIzin.setOnClickListener(v -> ajukanIzin());
    }

    // Fungsi menampilkan DatePicker
    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String selectedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                    textTanggal.setText(selectedDate);
                    textTanggal.setTextColor(getResources().getColor(android.R.color.black));
                },
                year, month, day
        );
        datePickerDialog.show();
    }

    // Fungsi Ajukan izin
    private void ajukanIzin() {
        String tanggal = textTanggal.getText().toString();
        String jenisIzin = spinnerJenisIzin.getSelectedItem().toString();
        String deskripsi = editTextDeskripsi.getText().toString();

        if (tanggal.equals("Pilih Tanggal") || deskripsi.isEmpty()) {
            Toast.makeText(this, "Harap isi semua data izin!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Nanti bisa diganti simpan ke Firebase atau database
        String pesan = "Pengajuan Berhasil!";
        Toast.makeText(this, "\n" + pesan, Toast.LENGTH_LONG).show();

        Intent intent = new Intent(PengajuanIzinActivity.this, FiturActivity.class);
        intent.putExtra("openFragment", "absensi");
        startActivity(intent);
    }
}
