package com.example.sos;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.Manifest;


public class soscall extends AppCompatActivity {

    Button but1,but2,but3,but4,but5;
    private static final int REQUEST_CALL_PHONE_PERMISSION = 1;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soscall);

        but1 = findViewById(R.id.callambu);
        but2 = findViewById(R.id.callfire);
        but3 = findViewById(R.id.callpol);
        but4 = findViewById(R.id.calldis);
        but5 = findViewById(R.id.callgas);

        but1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performPhoneCall("tel:108");
            }
        });
        but2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performPhoneCall("tel:104");
            }
        });
        but3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performPhoneCall("tel:100");
            }
        });
        but4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performPhoneCall("tel:104");
            }
        });
        but5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performPhoneCall("tel:1906");
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CALL_PHONE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(this, "Call permission granted", Toast.LENGTH_SHORT).show();
            } else {
                // Permission denied, check if a rationale is needed
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE)) {

                    new AlertDialog.Builder(this).setTitle("Permission required").setMessage("We need permission for calling")
                            .setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ActivityCompat.requestPermissions(soscall.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL_PHONE_PERMISSION);
                                }
                            }).show();
                } else {

                    new AlertDialog.Builder(this).setTitle("Permission denied").setMessage("If you reject permission, you can't use this call service\n\n" +
                                    "Please turn on Phone permission at [Setting] > [Permission]")
                            .setPositiveButton("PROCEED", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    openAppSettings();
                                }
                            }).setNegativeButton("CLOSE", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(soscall.this, "Call permission denied", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                                }).show();
//
                }
            }
        }
    }

    private void performPhoneCall(String phoneNumber) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, request it
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL_PHONE_PERMISSION);
        } else {
            // Permission is already granted, make the phone call
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse(phoneNumber));
            startActivity(intent);
        }
    }

    private void openAppSettings() {
        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivity(intent);
    }
}