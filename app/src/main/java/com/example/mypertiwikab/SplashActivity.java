package com.example.mypertiwikab;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.halamansplash);

        ImageView logo = findViewById(R.id.logoSplash);
        TextView text = findViewById(R.id.textSplash);

        Animation slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        slideUp.setFillAfter(true);

        logo.setAlpha(1f);
        text.setAlpha(1f);

        logo.startAnimation(slideUp);
        text.startAnimation(slideUp);

        new Handler().postDelayed(() -> {
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            finish();
        }, 2300);
    }
}
