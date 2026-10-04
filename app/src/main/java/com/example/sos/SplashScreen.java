package com.example.sos;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // The splash was closed before the wait ended
                if (isFinishing() || isDestroyed()) {
                    return;
                }
                Intent I = new Intent(SplashScreen.this, HomeActivity.class);
                startActivity(I);
                finish();
            }
        },1000);
    }
}