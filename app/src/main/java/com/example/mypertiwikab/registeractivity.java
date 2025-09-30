package com.example.mypertiwikab;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mypertiwikab.R;
import com.google.android.material.button.MaterialButton;

public class registeractivity extends AppCompatActivity {

    MaterialButton buttonRegister;
    TextView textLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.halamanregister);

        buttonRegister = findViewById(R.id.buttonRegister);
        textLogin = findViewById(R.id.textViewLogin);

        // Explicit Intent -> Pindah ke MainActivity setelah register
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent explicitIntent = new Intent(registeractivity.this, mainactivity.class);
                startActivity(explicitIntent);
            }
        });

        // Explicit Intent -> Pindah ke LoginActivity
        textLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent explicitIntent = new Intent(registeractivity.this, loginactivity.class);
                startActivity(explicitIntent);
            }
        });

        // Contoh Implicit Intent -> Kirim email (opsional)
        buttonRegister.setOnLongClickListener(v -> {
            Intent implicitIntent = new Intent(Intent.ACTION_SEND);
            implicitIntent.setType("message/rfc822");
            implicitIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"admin@myapp.com"});
            implicitIntent.putExtra(Intent.EXTRA_SUBJECT, "Register Request");
            implicitIntent.putExtra(Intent.EXTRA_TEXT, "Saya ingin daftar akun baru.");
            startActivity(Intent.createChooser(implicitIntent, "Kirim Email via:"));
            return true;
        });
    }
}
