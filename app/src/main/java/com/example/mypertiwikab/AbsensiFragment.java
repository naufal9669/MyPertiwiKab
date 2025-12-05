package com.example.mypertiwikab;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.mypertiwikab.api.ApiClient;
import com.example.mypertiwikab.api.ApiService;
import com.example.mypertiwikab.model.GuruProfile;
import com.example.mypertiwikab.model.IzinStatusResponse;
import com.example.mypertiwikab.model.ProfileResponse;
import com.example.mypertiwikab.model.StatusAbsenResponse;
import com.example.mypertiwikab.utils.SessionManager;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AbsensiFragment extends Fragment {

    private TextView tvNamaGuru, tvJabatan, tvStatusDatang, tvStatusPulang, tvInfoKehadiran;
    private Button btnAbsenDatang, btnAbsenPulang;
    private ImageView imgProfil;
    private SessionManager sessionManager;
    private int guruId;

    private String statusDatangRaw = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.halamanabsensi, container, false);

        sessionManager = new SessionManager(requireContext());
        HashMap<String, String> user = sessionManager.getUserDetails();

        try {
            String idStr = user.get(SessionManager.KEY_GURU_ID);
            if (idStr != null) guruId = Integer.parseInt(idStr);
        } catch (NumberFormatException e) { e.printStackTrace(); }

        // INISIALISASI SESUAI XML YANG BARU
        tvNamaGuru = view.findViewById(R.id.tvNamaGuru);
        tvJabatan = view.findViewById(R.id.tvJabatan);
        tvStatusDatang = view.findViewById(R.id.tvStatusDatang);
        tvStatusPulang = view.findViewById(R.id.tvStatusPulang);
        tvInfoKehadiran = view.findViewById(R.id.tvInfoKehadiran);
        btnAbsenDatang = view.findViewById(R.id.btnAbsenDatang);
        btnAbsenPulang = view.findViewById(R.id.btnAbsenPulang);
        imgProfil = view.findViewById(R.id.imgProfil);

        // Set Data Awal
        if(tvNamaGuru != null) tvNamaGuru.setText(user.get(SessionManager.KEY_NAMA));
        if(tvJabatan != null) tvJabatan.setText(user.get(SessionManager.KEY_JABATAN));

        // --- FIX NAVIGASI ---
        // Pakai startActivity karena tujuannya adalah Activity
        btnAbsenDatang.setOnClickListener(v -> checkButtonAction("datang"));
        btnAbsenPulang.setOnClickListener(v -> checkButtonAction("pulang"));

        // Load Data
        fetchProfileData();
        fetchStatusHariIni();

        return view;
    }

    private void checkButtonAction(String tipe) {
        if (statusDatangRaw != null && statusDatangRaw.toUpperCase().contains("ALFA") && !statusDatangRaw.contains("IZIN")) {
            Toast.makeText(requireContext(), "Anda melewati jam masuk (13:00). Status otomatis ALFA.", Toast.LENGTH_LONG).show();
        } else {
            if (tipe.equals("datang")) {
                startActivity(new Intent(getActivity(), AbsensiDatangActivity.class));
            } else {
                startActivity(new Intent(getActivity(), AbsensiPulangActivity.class));
            }
        }
    }

    private void fetchProfileData() {
        ApiClient.getClient().create(ApiService.class).getProfile(guruId).enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getGuruProfile() != null) {
                    GuruProfile profile = response.body().getGuruProfile();
                    if(tvNamaGuru != null) tvNamaGuru.setText(profile.getFullname());
                    if(tvJabatan != null) tvJabatan.setText(profile.getJabatan());

                    if (profile.getFotoUrl() != null && !profile.getFotoUrl().isEmpty() && imgProfil != null) {
                        Glide.with(requireContext()).load(profile.getFotoUrl()).circleCrop().into(imgProfil);
                    }
                }
            }
            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {}
        });
    }

    private void fetchStatusHariIni() {
        ApiClient.getClient().create(ApiService.class).getStatusHariIni(guruId).enqueue(new Callback<StatusAbsenResponse>() {
            @Override
            public void onResponse(Call<StatusAbsenResponse> call, Response<StatusAbsenResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    StatusAbsenResponse status = response.body();
                    String stDatang = status.getStatusDatang();
                    String stPulang = status.getStatusPulang();
                    statusDatangRaw = stDatang;

                    String displayPulang = stPulang;
                    if (stDatang.toUpperCase().contains("ALFA")) displayPulang = "Menunggu Pulang";

                    if (tvStatusDatang != null) tvStatusDatang.setText("Status Datang: " + stDatang);
                    if (tvStatusPulang != null) tvStatusPulang.setText("Status Pulang: " + displayPulang);

                    updateInfoBawah(stDatang);

                    boolean isHadir = stDatang.toLowerCase().contains("hadir") || stDatang.toLowerCase().contains("terlambat");
                    if (btnAbsenDatang != null) {
                        btnAbsenDatang.setEnabled(!isHadir);
                        btnAbsenDatang.setAlpha(isHadir ? 0.5f : 1.0f);
                    }
                    boolean isSudahPulang = stPulang.toLowerCase().contains("sudah") || (stPulang.toLowerCase().contains("pulang") && !stPulang.contains("Menunggu"));
                    if (btnAbsenPulang != null) {
                        btnAbsenPulang.setEnabled(!isSudahPulang);
                        btnAbsenPulang.setAlpha(isSudahPulang ? 0.5f : 1.0f);
                    }

                    fetchIzinStatus(stDatang);
                }
            }
            @Override
            public void onFailure(Call<StatusAbsenResponse> call, Throwable t) {}
        });
    }

    private void fetchIzinStatus(String statusDatangFinal) {
        ApiClient.getClient().create(ApiService.class).getIzinStatus(guruId).enqueue(new Callback<IzinStatusResponse>() {
            @Override
            public void onResponse(Call<IzinStatusResponse> call, Response<IzinStatusResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String izinStatus = response.body().getIzinStatus();
                    if ("Diterima".equalsIgnoreCase(izinStatus) || "Menunggu".equalsIgnoreCase(izinStatus)) {
                        statusDatangRaw = "IZIN";
                        if (tvStatusDatang != null) tvStatusDatang.setText("IZIN (" + izinStatus + ")");
                        if (tvStatusPulang != null) tvStatusPulang.setText("IZIN (" + izinStatus + ")");
                        updateInfoBawah("IZIN");
                        if (btnAbsenDatang != null) btnAbsenDatang.setEnabled(false);
                        if (btnAbsenPulang != null) btnAbsenPulang.setEnabled(false);
                    } else {
                        updateInfoBawah(statusDatangFinal);
                    }
                }
            }
            @Override
            public void onFailure(Call<IzinStatusResponse> call, Throwable t) {}
        });
    }

    private void updateInfoBawah(String status) {
        if (tvInfoKehadiran == null) return;
        String lowerSt = status.toLowerCase();
        String text = "BELUM ABSEN";
        int color = 0xFF000000;

        if (lowerSt.contains("izin")) { text = "IZIN"; color = 0xFF0000FF; }
        else if (lowerSt.contains("alfa")) { text = "ALFA"; color = 0xFFFF0000; }
        else if (lowerSt.contains("terlambat")) { text = "TERLAMBAT"; color = 0xFFFF8C00; }
        else if (lowerSt.contains("hadir")) { text = "HADIR"; color = 0xFF008000; }
        else if (lowerSt.contains("sakit")) { text = "SAKIT"; color = 0xFF800080; }

        tvInfoKehadiran.setText(text);
        tvInfoKehadiran.setTextColor(color);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (guruId != 0) fetchStatusHariIni();
    }
}