package com.example.sos;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.appbar.MaterialToolbar;

public class Guide extends AppCompatActivity {
    MaterialToolbar appBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        appBar = findViewById(R.id.toolbar);
        setSupportActionBar(appBar);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Handle back button press
            onBackPressed(); // or finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}