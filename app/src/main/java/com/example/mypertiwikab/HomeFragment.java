package com.example.mypertiwikab;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.mypertiwikab.api.ApiClient;
import com.example.mypertiwikab.api.ApiService;
import com.example.mypertiwikab.model.GuruProfile;
import com.example.mypertiwikab.model.ProfileResponse;
import com.example.mypertiwikab.utils.SessionManager;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private TextView tvNamaGuru, tvJabatanGuru;
    private CircleImageView ivProfilePhoto;
    private SessionManager sessionManager;
    private int guruId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.halamanhome, container, false);

        sessionManager = new SessionManager(requireContext());
        sessionManager.checkLogin();

        // --- INISIALISASI VIEW (Asumsi ID sama dengan yang kita tambahkan sebelumnya) ---
        tvNamaGuru = view.findViewById(R.id.tvNamaGuruHome);
        tvJabatanGuru = view.findViewById(R.id.tvJabatanGuruHome);
        ivProfilePhoto = view.findViewById(R.id.ivProfilePhotoHome);

        // --- Dapatkan Guru ID ---
        HashMap<String, String> user = sessionManager.getUserDetails();
        try {
            String idStr = user.get(SessionManager.KEY_GURU_ID);
            if (idStr == null) throw new NumberFormatException("ID Guru is null");
            guruId = Integer.parseInt(idStr);

            // FIX SINKRONISASI DATA SESSION (Fix Flicker)
            if (tvNamaGuru != null) tvNamaGuru.setText(user.get(SessionManager.KEY_NAMA));
            if (tvJabatanGuru != null) tvJabatanGuru.setText(user.get(SessionManager.KEY_JABATAN));

            fetchProfileData(guruId); // Panggil API untuk data foto
        } catch (NumberFormatException e) {
            Log.e("HOME_FRAGMENT", "ID Guru Error: " + e.getMessage());
        }

        return view;
    }

    // Dipanggil saat Fragment di-resume (misalnya saat kembali dari Profil atau Absensi)
    @Override
    public void onResume() {
        super.onResume();
        if (guruId != 0) {
            fetchProfileData(guruId);
        }
    }

    private void fetchProfileData(int guruId) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<ProfileResponse> call = apiService.getProfile(guruId);

        call.enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(@NonNull Call<ProfileResponse> call, @NonNull Response<ProfileResponse> response) {
                if (!isAdded()) return;

                if (response.isSuccessful() && response.body() != null && response.body().getGuruProfile() != null) {
                    GuruProfile profile = response.body().getGuruProfile();

                    // Tampilkan data (menimpa data session jika API sukses)
                    if (tvNamaGuru != null) tvNamaGuru.setText(profile.getFullname());
                    if (tvJabatanGuru != null) tvJabatanGuru.setText(profile.getJabatan());

                    // Load Foto Profil menggunakan Glide
                    if (profile.getFotoUrl() != null && ivProfilePhoto != null && requireContext() != null) {
                        Glide.with(requireContext())
                                .load(profile.getFotoUrl())
                                .placeholder(R.drawable.teacher)
                                .error(R.drawable.teacher)
                                .into(ivProfilePhoto);
                    }
                } else {
                    // Jika API Gagal, data Session tetap tampil
                }
            }

            @Override
            public void onFailure(@NonNull Call<ProfileResponse> call, @NonNull Throwable t) {
                // Jika koneksi gagal, data Session tetap tampil
            }
        });
    }
}