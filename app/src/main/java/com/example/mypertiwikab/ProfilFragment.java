package com.example.mypertiwikab;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.mypertiwikab.api.ApiClient;
import com.example.mypertiwikab.api.ApiService;
import com.example.mypertiwikab.model.DefaultResponse;
import com.example.mypertiwikab.model.GuruProfile;
import com.example.mypertiwikab.model.ProfileResponse;
import com.example.mypertiwikab.utils.FileUtils;
import com.example.mypertiwikab.utils.SessionManager;

import java.io.File;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfilFragment extends Fragment {

    private SessionManager sessionManager;
    private int guruId;

    private TextView tvNamaGuruHeader;
    private ImageView imgProfil;
    private View btnGantiFoto;
    private EditText etNamaField, etJabatan, etNisn, etAlamat, etEmail;
    private Button btnSimpanFinal;
    private android.widget.ImageButton btnLogoutFinal;

    private static final int REQUEST_GALLERY = 100;
    private Uri selectedImageUri = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.halamanprofil, container, false);

        if (getContext() == null) return view;

        sessionManager = new SessionManager(requireContext());
        sessionManager.checkLogin();

        // 1. Inisialisasi View
        tvNamaGuruHeader = view.findViewById(R.id.tvNamaGuruHeader);
        imgProfil = view.findViewById(R.id.imgProfil);
        btnGantiFoto = view.findViewById(R.id.btnGantiFoto);
        etNamaField = view.findViewById(R.id.etNamaField);
        etJabatan = view.findViewById(R.id.etJabatan);
        etNisn = view.findViewById(R.id.etNisn);
        etAlamat = view.findViewById(R.id.etAlamat);
        etEmail = view.findViewById(R.id.etEmail);
        btnLogoutFinal = view.findViewById(R.id.btnLogOutFinal);
        btnSimpanFinal = view.findViewById(R.id.btnSimpanFinal);

        // 2. Ambil Data
        HashMap<String, String> user = sessionManager.getUserDetails();
        try {
            String idStr = user.get(SessionManager.KEY_GURU_ID);
            if (idStr != null) {
                guruId = Integer.parseInt(idStr);
                displaySessionData(user);
                fetchProfileData(guruId);
            }
        } catch (NumberFormatException e) {
            sessionManager.logoutUser();
        }

        // 3. Listener
        if (btnLogoutFinal != null) btnLogoutFinal.setOnClickListener(v -> sessionManager.logoutUser());

        if (btnSimpanFinal != null) btnSimpanFinal.setOnClickListener(v -> updateProfile());

        if (btnGantiFoto != null) {
            btnGantiFoto.setOnClickListener(v -> checkPermission());
        }

        return view;
    }

    private void displaySessionData(HashMap<String, String> user) {
        String nama = user.get(SessionManager.KEY_NAMA);
        String email = user.get(SessionManager.KEY_EMAIL);
        String jabatan = user.get(SessionManager.KEY_JABATAN);

        if (tvNamaGuruHeader != null) tvNamaGuruHeader.setText(nama);
        if (etNamaField != null) etNamaField.setText(nama);
        if (etEmail != null) etEmail.setText(email);
        if (etJabatan != null) etJabatan.setText(jabatan);
    }

    private void fetchProfileData(int guruId) {
        ApiClient.getClient().create(ApiService.class).getProfile(guruId).enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(@NonNull Call<ProfileResponse> call, @NonNull Response<ProfileResponse> response) {
                if (!isAdded()) return;

                if (response.isSuccessful() && response.body() != null && response.body().getGuruProfile() != null) {
                    GuruProfile profile = response.body().getGuruProfile();

                    if (tvNamaGuruHeader != null) tvNamaGuruHeader.setText(profile.getFullname());
                    if (etNamaField != null) etNamaField.setText(profile.getFullname());
                    if (etJabatan != null) etJabatan.setText(profile.getJabatan());
                    if (etNisn != null) etNisn.setText(profile.getNisn());
                    if (etAlamat != null) etAlamat.setText(profile.getAlamat());
                    if (etEmail != null) etEmail.setText(profile.getEmail());

                    if (profile.getFotoUrl() != null && !profile.getFotoUrl().isEmpty()) {
                        Glide.with(requireContext())
                                .load(profile.getFotoUrl())
                                .placeholder(R.drawable.teacher)
                                .error(R.drawable.teacher)
                                .skipMemoryCache(true)
                                .into(imgProfil);
                    }
                }
            }
            @Override
            public void onFailure(@NonNull Call<ProfileResponse> call, @NonNull Throwable t) {}
        });
    }

    private void updateProfile() {
        if (guruId == 0) return;

        String fullname = etNamaField.getText().toString().trim();
        String jabatan = etJabatan.getText().toString().trim();
        String nisn = etNisn.getText().toString().trim();
        String alamat = etAlamat.getText().toString().trim();

        if (fullname.isEmpty()) {
            Toast.makeText(requireContext(), "Nama wajib diisi", Toast.LENGTH_SHORT).show();
            return;
        }

        RequestBody idPart = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(guruId));
        RequestBody namePart = RequestBody.create(MediaType.parse("text/plain"), fullname);
        RequestBody jabPart = RequestBody.create(MediaType.parse("text/plain"), jabatan);
        RequestBody nisnPart = RequestBody.create(MediaType.parse("text/plain"), nisn);
        RequestBody almPart = RequestBody.create(MediaType.parse("text/plain"), alamat);

        // BAGIAN UPLOAD FOTO YANG DIPERBAIKI
        MultipartBody.Part photoPart = null;

        if (selectedImageUri != null) {
            try {
                // FileUtils sekarang sudah otomatis mengompres foto!
                File file = FileUtils.getFile(requireContext(), selectedImageUri);

                if (file != null) {
                    RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
                    photoPart = MultipartBody.Part.createFormData("photo", file.getName(), requestFile);
                }
            } catch (Exception e) {
                Toast.makeText(requireContext(), "Gagal proses gambar", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        // JIKA TIDAK ADA FOTO, KIRIM TEXT KOSONG (AGAR SERVER TIDAK ERROR)
        if (photoPart == null) {
            RequestBody attachmentEmpty = RequestBody.create(MediaType.parse("text/plain"), "");
            photoPart = MultipartBody.Part.createFormData("photo", "", attachmentEmpty);
        }

        Toast.makeText(requireContext(), "Menyimpan...", Toast.LENGTH_SHORT).show();

        ApiClient.getClient().create(ApiService.class).updateProfile(
                idPart, namePart, jabPart, nisnPart, almPart, photoPart
        ).enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().isSuccess()) {
                        Toast.makeText(getContext(), "Berhasil Disimpan!", Toast.LENGTH_SHORT).show();
                        sessionManager.createLoginSession(String.valueOf(guruId), fullname, etEmail.getText().toString(), jabatan);
                        fetchProfileData(guruId);
                        selectedImageUri = null; // Reset
                    } else {
                        Toast.makeText(getContext(), "Gagal: " + response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Error Server: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Koneksi Gagal (Cek IP/Waktu)", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkPermission() {
        String permission;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permission = Manifest.permission.READ_MEDIA_IMAGES;
        } else {
            permission = Manifest.permission.READ_EXTERNAL_STORAGE;
        }

        if (ContextCompat.checkSelfPermission(requireContext(), permission) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{permission}, REQUEST_GALLERY);
        } else {
            openGallery();
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Pilih Aplikasi Galeri"), REQUEST_GALLERY);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_GALLERY) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                Toast.makeText(getContext(), "Izin ditolak", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_GALLERY && resultCode == Activity.RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            if (imgProfil != null) imgProfil.setImageURI(selectedImageUri);
            Toast.makeText(getContext(), "Foto dipilih. Klik SIMPAN.", Toast.LENGTH_LONG).show();
        }
    }
}