package com.example.sos;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.color.DynamicColors;

public class HomeActivity extends AppCompatActivity {

    CardView registerContact, editMessage, sosGuide,helpline, showContact,Info, btnSosService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DynamicColors.applyToActivitiesIfAvailable(getApplication());
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        registerContact = findViewById(R.id.registerContact);
        showContact = findViewById(R.id.showContact);
        editMessage = findViewById(R.id.editMessage);
        btnSosService = findViewById(R.id.btnSosService);
        helpline = findViewById(R.id.helpline);
        Info = findViewById(R.id.Info);
        sosGuide = findViewById(R.id.sosGuide);

        registerContact.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, RegisterNumberActivity.class);
            startActivity(intent);
        });

        showContact.setOnClickListener(view -> {
            Intent intent = new Intent(HomeActivity.this, ShowContact.class);
            startActivity(intent);
        });

        editMessage.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, EditMessageActivity.class);
            startActivity(intent);
        });

        btnSosService.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, MainActivity.class);
            startActivity(intent);
        });

        helpline.setOnClickListener(view -> {
            Intent intent = new Intent(HomeActivity.this, SosCall.class);
            startActivity(intent);
        });

        Info.setOnClickListener(view -> {
            Intent intent = new Intent(HomeActivity.this, Instructions.class);
            startActivity(intent);
        });

        sosGuide.setOnClickListener(view -> {
            Intent intent = new Intent(HomeActivity.this, Guide.class);
            startActivity(intent);
        });
    }
}