package com.example.mypertiwikab;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class dashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);

        // Tampilkan halaman home default saat pertama buka
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.nav_host_fragment, new home())
                .commit();

        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int id = item.getItemId();

            // Gunakan if-else alih-alih switch-case
            if (id == R.id.nav_absensi) {
                selectedFragment = new absensi(); // nanti buat class halamannotes
            } else if (id == R.id.nav_izin) {
                selectedFragment = new izin(); // nanti buat class halamancalendar
            } else if (id == R.id.nav_home) {
                selectedFragment = new home();
            } else if (id == R.id.nav_raport) {
                selectedFragment = new raport(); // nanti buat class halamanmood
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