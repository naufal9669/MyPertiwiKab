package com.example.mypertiwikab;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class AbsensiFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // Hubungkan fragment dengan layout halamanabsensi.xml
        View view = inflater.inflate(R.layout.halamanabsensi, container, false);

        // Ambil tombol dari layout
        Button btnPengajuanIzin = view.findViewById(R.id.btnPengajuanIzin);
        Button btnAbsenDatang = view.findViewById(R.id.btnAbsenDatang);
        Button btnAbsenPulang = view.findViewById(R.id.btnAbsenPulang);

        // Tombol Pengajuan Izin → buka PengajuanIzinActivity
        btnPengajuanIzin.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), PengajuanIzinActivity.class);
            startActivity(intent);
        });

        // Tombol Absen Datang → buka AbsensiDatangActivity
        btnAbsenDatang.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AbsensiDatangActivity.class);
            startActivity(intent);
        });

        // Tombol Absen Pulang (opsional: nanti bisa diarahkan ke AbsensiPulangActivity)
        btnAbsenPulang.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), com.example.mypertiwikab.AbsensiPulangActivity.class);
            startActivity(intent);
        });

        return view;
    }
}
