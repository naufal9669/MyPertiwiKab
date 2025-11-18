package com.example.mypertiwikab;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ResetPasswordActivity extends AppCompatActivity {

    EditText etPassBaru, etKonfirmasi;
    Button btnKirim, btnOkDone;
    View popupSuccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reset_password);

        etPassBaru = findViewById(R.id.etPassBaru);
        etKonfirmasi = findViewById(R.id.etKonfirmasi);
        btnKirim = findViewById(R.id.btnKirim);
        popupSuccess = findViewById(R.id.popupSuccess);
        btnOkDone = findViewById(R.id.btnOkDone);

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

            popupSuccess.setVisibility(View.VISIBLE);
        });

        btnOkDone.setOnClickListener(v -> {
            popupSuccess.setVisibility(View.GONE);
            finish(); // kembali ke login
        });
    }
}
