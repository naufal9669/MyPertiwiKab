package com.example.mypertiwikab.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import com.example.mypertiwikab.LoginActivity;
import java.util.HashMap;

public class SessionManager {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context context;

    // --- KUNCI: DIBUAT PUBLIC AGAR BISA DIAKSES DARI LUAR CLASS ---
    public static final String KEY_EMAIL = "email";
    public static final String KEY_GURU_ID = "guru_id";
    public static final String KEY_NAMA = "fullname";
    public static final String KEY_JABATAN = "jabatan";

    // PERBAIKAN UTAMA: PREF_NAME harus public static final (Memperbaiki error 'private access')
    public static final String PREF_NAME = "SesiGuruPertiwi";
    private static final String IS_LOGIN = "IsLoggedIn";


    public SessionManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, 0);
        editor = pref.edit();
    }

    // Perhatikan urutan argumennya di sini (ID, Nama, Jabatan, Email)
    public void createLoginSession(String id, String nama, String jabatan, String email) {
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_GURU_ID, id);
        editor.putString(KEY_NAMA, nama);
        editor.putString(KEY_JABATAN, jabatan);
        editor.putString(KEY_EMAIL, email);
        editor.apply();
    }

    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<>();
        user.put(KEY_GURU_ID, pref.getString(KEY_GURU_ID, null));
        user.put(KEY_NAMA, pref.getString(KEY_NAMA, null));
        user.put(KEY_JABATAN, pref.getString(KEY_JABATAN, null));
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));
        return user;
    }

    // Method ini digunakan ProfilFragment saat update nama/jabatan
    public void updateSessionNameAndJabatan(String nama, String jabatan) {
        editor.putString(KEY_NAMA, nama);
        editor.putString(KEY_JABATAN, jabatan);
        editor.apply();
    }

    public void logoutUser() {
        editor.clear();
        editor.apply();

        Intent i = new Intent(context, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }

    public void checkLogin() {
        if (!this.isLoggedIn()) {
            Intent i = new Intent(context, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
    }
}