package com.example.mypertiwikab;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView; // Import WAJIB untuk Spinner Listener
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
import androidx.fragment.app.FragmentManager;

import com.example.mypertiwikab.api.ApiClient;
import com.example.mypertiwikab.api.ApiService;
import com.example.mypertiwikab.model.DefaultResponse;
import com.example.mypertiwikab.model.IzinRequest;
import com.example.mypertiwikab.utils.SessionManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PengajuanIzinActivity extends Fragment {

    private ImageButton btnPilihTanggal;
    private TextView textTanggal;
    private Spinner spinnerJenisIzin;
    private EditText editTextDeskripsi;
    private Button buttonAjukanIzin;

    private SessionManager sessionManager;
    private int guruId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.pengajuanizin, container, false);

        sessionManager = new SessionManager(requireContext());
        sessionManager.checkLogin();

        // --- AMBIL ID GURU DARI SESSION ---
        HashMap<String, String> user = sessionManager.getUserDetails();
        try {
            String idStr = user.get(SessionManager.KEY_GURU_ID);
            if (idStr == null) throw new NumberFormatException("ID Guru is null");
            guruId = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            Toast.makeText(requireContext(), "ID Guru error, silakan login ulang.", Toast.LENGTH_LONG).show();
            sessionManager.logoutUser();
        }

        // Inisialisasi komponen UI pakai view.findViewById
        btnPilihTanggal = view.findViewById(R.id.btnPilihTanggal);
        textTanggal = view.findViewById(R.id.textTanggal);
        spinnerJenisIzin = view.findViewById(R.id.spinnerJenisIzin);
        editTextDeskripsi = view.findViewById(R.id.editTextDeskripsi);
        buttonAjukanIzin = view.findViewById(R.id.buttonajukanIzin);

        // Tombol pilih tanggal
        btnPilihTanggal.setOnClickListener(v -> showDatePicker());

        // --- PERBAIKAN SPINNER WARNA TEXT DINAMIS ---
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.arraysizin,
                R.layout.spinner_text // Menggunakan layout kustom (teks hitam)
        );

        // Menggunakan dropdown item bawaan Android untuk tampilan list yang turun
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerJenisIzin.setAdapter(adapter);

        // KUNCI FIX: Mengatur warna teks berdasarkan pilihan
        spinnerJenisIzin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Pastikan view yang dilewatkan adalah TextView
                if (view != null && view instanceof TextView) {
                    TextView selectedText = (TextView) view;

                    if (position == 0) {
                        // Item 0 adalah "Pilih jenis izin" (Placeholder)
                        // Mengatur warna teks menjadi abu-abu seperti hint (0xFF888888)
                        selectedText.setTextColor(0xFF888888);
                    } else {
                        // Item > 0 adalah pilihan sebenarnya
                        // Mengatur warna teks menjadi hitam pekat (0xFF000000)
                        selectedText.setTextColor(0xFF000000);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Tidak ada aksi
            }
        });

        // Memastikan item awal terpilih (agar listener di atas dipanggil)
        spinnerJenisIzin.setSelection(0);


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
                android.R.style.Theme_Holo_Light_Dialog,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String selectedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                    textTanggal.setText(selectedDate);
                    textTanggal.setTextColor(getResources().getColor(android.R.color.black));
                },
                year, month, day
        );

        datePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        datePickerDialog.show();
    }

    private void ajukanIzin() {
        String tanggalView = textTanggal.getText().toString();
        String jenisIzin = spinnerJenisIzin.getSelectedItem().toString();
        String deskripsi = editTextDeskripsi.getText().toString();

        // Pengecekan wajib: Pastikan bukan placeholder yang dipilih
        if (tanggalView.equals("Pilih Tanggal") || jenisIzin.equals("Pilih jenis izin") || deskripsi.isEmpty() || guruId == 0) {
            Toast.makeText(requireContext(), "Harap lengkapi semua data izin!", Toast.LENGTH_SHORT).show();
            return;
        }

        // --- KONVERSI TANGGAL ke format API (YYYY-MM-DD) ---
        String tanggalApi;
        try {
            SimpleDateFormat formatView = new SimpleDateFormat("d/M/yyyy", new Locale("id", "ID"));
            SimpleDateFormat formatApi = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            tanggalApi = formatApi.format(formatView.parse(tanggalView));
        } catch (Exception e) {
            Toast.makeText(requireContext(), "Format tanggal error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            return;
        }

        // 1. Buat Request
        IzinRequest request = new IzinRequest(guruId, tanggalApi, jenisIzin, deskripsi);

        // 2. Panggil API
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<DefaultResponse> call = apiService.submitIzin(request);

        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    DefaultResponse izinResponse = response.body();
                    Toast.makeText(requireContext(), izinResponse.getMessage(), Toast.LENGTH_LONG).show();

                    if (izinResponse.isSuccess()) {
                        // --- NAVIGASI SUKSES ---
                        if (getActivity() != null) {
                            FragmentManager fm = getActivity().getSupportFragmentManager();
                            fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                            // Ganti ke HomeFragment
                            fm.beginTransaction()
                                    .replace(R.id.nav_host_fragment, new HomeFragment())
                                    .commit();
                        }
                    }
                } else {
                    Toast.makeText(requireContext(), "Pengajuan Gagal. Cek Log: " + response.code(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
                Toast.makeText(requireContext(), "Koneksi Gagal Izin: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}