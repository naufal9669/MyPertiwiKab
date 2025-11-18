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
        Button btnAbsenDatang = view.findViewById(R.id.btnAbsenDatang);
        Button btnAbsenPulang = view.findViewById(R.id.btnAbsenPulang);

        // Tombol Absen Datang → buka AbsensiDatangActivity
        btnAbsenDatang.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AbsensiDatangActivity.class);
            startActivity(intent);
        });

        // ✅ Tombol Absen Pulang → buka AbsensiPulangActivity
        btnAbsenPulang.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AbsensiPulangActivity.class);
            startActivity(intent);
        });

        return view;
    }
}
