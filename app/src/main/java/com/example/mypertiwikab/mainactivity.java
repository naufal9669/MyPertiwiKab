package com.example.mypertiwikab;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mypertiwikab.R;
import com.google.android.material.button.MaterialButton;

public class mainactivity extends AppCompatActivity {

    MaterialButton buttonWebsite, buttonDial, buttonEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainactivity);

        buttonWebsite = findViewById(R.id.buttonWebsite);
        buttonDial = findViewById(R.id.buttonDial);
        buttonEmail = findViewById(R.id.buttonEmail);

        // Implicit Intent -> Buka Website
        buttonWebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openWebsite = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com"));
                startActivity(openWebsite);



            }
        });

        // Implicit Intent -> Panggil Nomor Telepon
        buttonDial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dialPhone = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:08123456789"));
                startActivity(dialPhone);
            }
        });

        // Implicit Intent -> Kirim Email
        buttonEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendEmail = new Intent(Intent.ACTION_SENDTO);
                sendEmail.setData(Uri.parse("mailto:admin@myapp.com"));
                sendEmail.putExtra(Intent.EXTRA_SUBJECT, "Test Email");
                sendEmail.putExtra(Intent.EXTRA_TEXT, "Halo ini percobaan implicit intent di Android.");
                startActivity(sendEmail);
            }
        });
    }
}
