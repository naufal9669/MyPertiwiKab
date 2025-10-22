package com.example.mypertiwikab;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class home extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.halamanhome, container, false);

        Button btnIzin = view.findViewById(R.id.btnIzin);
        Button btnRapor = view.findViewById(R.id.btnRapor);

        btnIzin.setOnClickListener(v ->
                Toast.makeText(getActivity(), "Menu Pengajuan Izin diklik", Toast.LENGTH_SHORT).show());

        btnRapor.setOnClickListener(v ->
                Toast.makeText(getActivity(), "Menu E-Rapor diklik", Toast.LENGTH_SHORT).show());

        return view;
    }
}
