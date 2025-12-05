package com.example.mypertiwikab;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class FiturActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.halamanfitur); // Pastikan nama layout XML benar

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);

        // --- 1. TENTUKAN FRAGMENT AWAL ---
        // Cek apakah ada perintah khusus dari Intent (misal setelah Login atau Absen)
        String fragmentToOpen = getIntent().getStringExtra("openFragment");

        Fragment initialFragment = new HomeFragment(); // Default ke Home
        int selectedMenuId = R.id.nav_Dashboard;       // Default menu Home

        if (fragmentToOpen != null) {
            if (fragmentToOpen.equals("absensi")) {
                initialFragment = new AbsensiFragment();
                selectedMenuId = R.id.nav_Absensi;
            } else if (fragmentToOpen.equals("izin")) {
                initialFragment = new IzinFragment();
                selectedMenuId = R.id.nav_pengajuanizin;
            }
        }

        if (savedInstanceState == null) {
            loadFragment(initialFragment);
            bottomNav.setSelectedItemId(selectedMenuId);
        }

        // --- 2. LOGIKA KLIK BOTTOM NAV (ANTI KEDIP) ---
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            // 🔥 LOGIKA BARU: Cek apakah tombol yang diklik SAMA dengan yang sedang aktif?
            // Jika sama, return false (JANGAN RELOAD/JANGAN KEDIP)
            if (bottomNav.getSelectedItemId() == id) {
                return false;
            }

            Fragment selectedFragment = null;

            if (id == R.id.nav_Dashboard) {
                selectedFragment = new HomeFragment();
            } else if (id == R.id.nav_Absensi) {
                selectedFragment = new AbsensiFragment();
            } else if (id == R.id.nav_pengajuanizin) {
                selectedFragment = new IzinFragment();
            } else if (id == R.id.nav_profil) {
                selectedFragment = new ProfilFragment();
            }

            if (selectedFragment != null) {
                loadFragment(selectedFragment);
                return true; // Return true artinya item terpilih dan icon berubah warna
            }

            return false;
        });
    }

    // Fungsi Helper biar kodingan rapi
    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                // PENTING: Pastikan ID FrameLayout di XML adalah 'nav_host_fragment'
                .replace(R.id.nav_host_fragment, fragment)
                .commit();
    }
}