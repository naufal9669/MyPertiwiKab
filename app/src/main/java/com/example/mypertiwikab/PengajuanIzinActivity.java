package com.example.mypertiwikab;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Calendar;

public class PengajuanIzinActivity extends Fragment {

    private ImageButton btnPilihTanggal;
    private TextView textTanggal;
    private Spinner spinnerJenisIzin;
    private EditText editTextDeskripsi;
    private Button buttonAjukanIzin;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // Inflate layout fragment
        View view = inflater.inflate(R.layout.pengajuanizin, container, false);

        // Inisialisasi komponen UI pakai view.findViewById
        btnPilihTanggal = view.findViewById(R.id.btnPilihTanggal);
        textTanggal = view.findViewById(R.id.textTanggal);
        spinnerJenisIzin = view.findViewById(R.id.spinnerJenisIzin);
        editTextDeskripsi = view.findViewById(R.id.editTextDeskripsi);
        buttonAjukanIzin = view.findViewById(R.id.buttonajukanIzin);

        // Tombol pilih tanggal
        btnPilihTanggal.setOnClickListener(v -> showDatePicker());

        // Isi spinner dari resources
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.arraysizin,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerJenisIzin.setAdapter(adapter);

        // Tombol ajukan izin
        buttonAjukanIzin.setOnClickListener(v -> ajukanIzin());

        return view;
    }

    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String selectedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                    textTanggal.setText(selectedDate);
                    textTanggal.setTextColor(getResources().getColor(android.R.color.black));
                },
                year, month, day
        );
        datePickerDialog.show();
    }

    private void ajukanIzin() {
        String tanggal = textTanggal.getText().toString();
        String jenisIzin = spinnerJenisIzin.getSelectedItem().toString();
        String deskripsi = editTextDeskripsi.getText().toString();

        if (tanggal.equals("Pilih Tanggal") || deskripsi.isEmpty()) {
            Toast.makeText(requireContext(), "Harap isi semua data izin!", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(requireContext(), "Pengajuan Berhasil!", Toast.LENGTH_LONG).show();

        Intent intent = new Intent(requireActivity(), FiturActivity.class);
        intent.putExtra("openFragment", "absensi");
        startActivity(intent);
    }
}
