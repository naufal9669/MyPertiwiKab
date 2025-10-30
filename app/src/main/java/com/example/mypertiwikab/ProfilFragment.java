package com.example.mypertiwikab;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ProfilFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.halamanprofil, container, false);

        ImageButton btnBack = view.findViewById(R.id.btnBackProfil);
        btnBack.setOnClickListener(v -> requireActivity().onBackPressed());

        return view;
    }
}
