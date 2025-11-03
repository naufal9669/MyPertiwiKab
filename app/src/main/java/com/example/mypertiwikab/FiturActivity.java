package com.example.mypertiwikab;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class FiturActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.halamanfitur);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);

        // Tampilkan halaman home default saat pertama buka
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.nav_host_fragment, new HomeFragment())
                .commit();

        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int id = item.getItemId();

            // Gunakan if-else alih-alih switch-case
            if (id == R.id.nav_Datasiswa) {
                selectedFragment = new DataSiswaFragment(); // nanti buat class halamannotes
            } else if (id == R.id.nav_Absensi) {
                selectedFragment = new AbsensiFragment(); // nanti buat class halamancalendar
            } else if (id == R.id.nav_Dashboard) {
                selectedFragment = new HomeFragment();
            } else if (id == R.id.nav_profil) {
                selectedFragment = new ProfilFragment(); // nanti buat class halamanmood
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.nav_host_fragment, selectedFragment)
                        .commit();
            }

            return true;
        });
    }
}