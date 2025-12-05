package com.example.mypertiwikab;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

// --- IMPORT WAJIB BIAR GAK MERAH ---
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mypertiwikab.api.ApiClient;
import com.example.mypertiwikab.api.ApiService;
import com.example.mypertiwikab.model.DefaultResponse;
import com.example.mypertiwikab.model.IzinRequest;
import com.example.mypertiwikab.utils.SessionManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IzinFragment extends Fragment {

    // Deklarasi Variabel
    private ImageButton btnPilihTanggal;
    private TextView textTanggal;
    private Spinner spinnerJenisIzin;
    private EditText editTextDeskripsi;
    private Button buttonAjukanIzin;
    private SessionManager sessionManager;
    private int guruId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Pastikan layout XML-nya benar (halamanizin.xml)
        View view = inflater.inflate(R.layout.pengajuanizin, container, false);

        sessionManager = new SessionManager(requireContext());
        HashMap<String, String> user = sessionManager.getUserDetails();
        try {
            String idStr = user.get(SessionManager.KEY_GURU_ID);
            if (idStr != null) guruId = Integer.parseInt(idStr);
        } catch (NumberFormatException e) { e.printStackTrace(); }

        // --- INISIALISASI (JODOHKAN DENGAN XML) ---
        btnPilihTanggal = view.findViewById(R.id.btnPilihTanggal);
        textTanggal = view.findViewById(R.id.textTanggal);
        spinnerJenisIzin = view.findViewById(R.id.spinnerJenisIzin);
        editTextDeskripsi = view.findViewById(R.id.editTextDeskripsi);
        buttonAjukanIzin = view.findViewById(R.id.buttonajukanIzin);

        // Setup Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                requireContext(), R.array.arraysizin, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerJenisIzin.setAdapter(adapter);

        // Listener Warna Spinner
        spinnerJenisIzin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (view instanceof TextView) {
                    ((TextView) view).setTextColor(position == 0 ? 0xFF888888 : 0xFF000000);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Listener Tanggal (Klik Tombol atau Teks)
        btnPilihTanggal.setOnClickListener(v -> showDatePicker());
        textTanggal.setOnClickListener(v -> showDatePicker());

        // Listener Submit
        buttonAjukanIzin.setOnClickListener(v -> submitIzin());

        return view;
    }

    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Pakai Theme Light agar tombol OK/Cancel kelihatan
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                android.R.style.Theme_DeviceDefault_Light_Dialog,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String selectedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                    textTanggal.setText(selectedDate);
                    textTanggal.setTextColor(0xFF000000); // Hitam
                },
                year, month, day
        );
        datePickerDialog.show();
    }

    private void submitIzin() {
        String jenis = spinnerJenisIzin.getSelectedItem().toString();
        String deskripsi = editTextDeskripsi.getText().toString().trim();
        String tanggalView = textTanggal.getText().toString();

        if (deskripsi.isEmpty() || jenis.equals("Pilih jenis izin") || tanggalView.equals("Pilih Tanggal")) {
            Toast.makeText(requireContext(), "Lengkapi data izin!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Format Tanggal
        String tanggalApi = "";
        try {
            SimpleDateFormat sdfView = new SimpleDateFormat("d/M/yyyy", Locale.getDefault());
            SimpleDateFormat sdfApi = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            tanggalApi = sdfApi.format(sdfView.parse(tanggalView));
        } catch (Exception e) {
            tanggalApi = new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(new Date());
        }

        ApiService api = ApiClient.getClient().create(ApiService.class);
        IzinRequest req = new IzinRequest(guruId, tanggalApi, jenis, deskripsi);

        api.submitIzin(req).enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    if(response.body().isSuccess()) {
                        editTextDeskripsi.setText("");
                        textTanggal.setText("Pilih Tanggal");
                        spinnerJenisIzin.setSelection(0);

                        // Pindah ke Halaman Absensi (Sesuai request UX kamu tadi)
                        if (getActivity() != null) {
                            getActivity().getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.nav_host_fragment, new AbsensiFragment())
                                    .commit();

                            // Update icon menu bawah
                            com.google.android.material.bottomnavigation.BottomNavigationView bottomNav =
                                    getActivity().findViewById(R.id.bottom_navigation);
                            if (bottomNav != null) bottomNav.setSelectedItemId(R.id.nav_Absensi);
                        }
                    }
                } else {
                    Toast.makeText(getContext(), "Gagal: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Koneksi Gagal", Toast.LENGTH_SHORT).show();
            }
        });
    }
}