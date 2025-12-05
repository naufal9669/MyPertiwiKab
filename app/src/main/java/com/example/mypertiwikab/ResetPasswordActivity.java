package com.example.mypertiwikab;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ResetPasswordActivity extends AppCompatActivity {

    EditText etPassBaru, etKonfirmasi;
    Button btnKirim, btnOkDone, btnBack2;
    View popupSuccess;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reset_password);

        etPassBaru = findViewById(R.id.etPassBaru);
        etKonfirmasi = findViewById(R.id.etKonfirmasi);
        btnKirim = findViewById(R.id.btnKirim);
        popupSuccess = findViewById(R.id.popupSuccess);
        btnOkDone = findViewById(R.id.btnOkDone);
        btnBack2 = findViewById(R.id.btnBack2);

        // Debugging / Info
        String emailFromIntent = getIntent().getStringExtra("email");

        btnBack2.setOnClickListener(v -> onBackPressed());

        btnKirim.setOnClickListener(v -> {
            String pass1 = etPassBaru.getText().toString();
            String pass2 = etKonfirmasi.getText().toString();

            if (pass1.isEmpty() || pass2.isEmpty()) {
                Toast.makeText(this, "Isi semua field!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!pass1.equals(pass2)) {
                etKonfirmasi.setError("Password tidak cocok");
                return;
            }

            // Simpan password baru ke SharedPreferences (simulasi berhasil ganti di server)
            getSharedPreferences("userPrefs", MODE_PRIVATE)
                    .edit()
                    .putString("password", pass1)
                    .apply();

            // Tampilkan popup sukses
            popupSuccess.setVisibility(View.VISIBLE);
        });

        // Tombol OK di Popup Success
        btnOkDone.setOnClickListener(this::onClickDone);
    }

    // Fungsi pindah ke Login setelah sukses
    private void onClickDone(View v) {
        popupSuccess.setVisibility(View.GONE);
        Intent intent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
        // Flag ini penting biar user tidak bisa kembali ke halaman reset password
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}