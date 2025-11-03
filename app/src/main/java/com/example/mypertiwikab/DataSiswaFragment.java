package com.example.mypertiwikab;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class DataSiswaFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // Inflate layout
        View view = inflater.inflate(R.layout.halamandatasiswa, container, false);

        // Ambil tombol dari layout
        Button btnInfo1 = view.findViewById(R.id.buttonInfo1);
        Button btnInfo2 = view.findViewById(R.id.buttonInfo2);
        Button btnInfo3 = view.findViewById(R.id.buttonInfo3);

        // Listener untuk semua tombol
        View.OnClickListener listener = v -> {
            Fragment infoFragment = new halamaninformasi(); // gunakan nama class fragment informasi kamu
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.nav_host_fragment, infoFragment) // gunakan nav_host_fragment
                    .addToBackStack(null)
                    .commit();
        };

        btnInfo1.setOnClickListener(listener);
        btnInfo2.setOnClickListener(listener);
        btnInfo3.setOnClickListener(listener);

        return view;
    }
}
