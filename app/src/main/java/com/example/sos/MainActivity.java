package com.example.sos;

import android.Manifest;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 11;
    int contactList;
    DatabaseHelper databaseHelper;
    private static final int REQUEST_CHECK_SETTING = 10;
    String[] permissions = {
            Manifest.permission.SEND_SMS,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    @Override
    protected void onStart() {
        super.onStart();

        databaseHelper = new DatabaseHelper(this);
        contactList = databaseHelper.count();

        if(contactList==0){
            Intent intent = new Intent(this, RegisterNumberActivity.class);
            startActivity(intent);
            Toast.makeText(this, "Register at least one contact", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("MYID", "CHANNELFOREGROUND", NotificationManager.IMPORTANCE_DEFAULT);

            NotificationManager m = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            m.createNotificationChannel(channel);
        }

    }

    public void stopServiceV(View view) {

        Intent notificationIntent = new Intent(this,ServiceMine.class);
        notificationIntent.setAction("stop");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getApplicationContext().startForegroundService(notificationIntent);
            Snackbar.make(findViewById(android.R.id.content),"Service Stopped!", Snackbar.LENGTH_LONG).show();
        }
    }

    public void startServiceV(View view) {

        List<String> permissionList = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(permission);
            }
        }

        if (!permissionList.isEmpty()) {
            //Request permission
            ActivityCompat.requestPermissions(this, permissionList.toArray(new String[0]), REQUEST_CODE);
        } else {
            // All permissions are already granted, you can proceed with your tasks.
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            boolean isLocationEnabled = locationManager != null && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            if (!isLocationEnabled) {
                // Location settings are not enabled, show a dialog to prompt the user to enable them
                LocationRequest locationRequest = LocationRequest.create();
                locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                locationRequest.setInterval(10000);
                locationRequest.setFastestInterval(10000 / 2);

                LocationSettingsRequest.Builder locationSettingRequestBuilder = new LocationSettingsRequest.Builder();

                locationSettingRequestBuilder.addLocationRequest(locationRequest);
                locationSettingRequestBuilder.setAlwaysShow(true);

                SettingsClient settingsClient = LocationServices.getSettingsClient(MainActivity.this);

                Task<LocationSettingsResponse> task = settingsClient.checkLocationSettings(locationSettingRequestBuilder.build());

                task.addOnFailureListener(MainActivity.this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (e instanceof ResolvableApiException) {

                            try {
                                ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                                resolvableApiException.startResolutionForResult(MainActivity.this, REQUEST_CHECK_SETTING);
                            } catch (IntentSender.SendIntentException ex) {
                                throw new RuntimeException(ex);
                            }
                        }
                    }
                });
            }else {
                Intent notificationIntent = new Intent(this,ServiceMine.class);
                notificationIntent.setAction("Start");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    getApplicationContext().startForegroundService(notificationIntent);
                    Snackbar.make(findViewById(android.R.id.content),"Service Started!", Snackbar.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            // Check if all permissions are granted
            boolean allPermissionsGranted = true;
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    allPermissionsGranted = false;
                    break;
                }
            }

            if (allPermissionsGranted) {
                // All permissions are granted, you can proceed with your tasks.
                // For example, start your main activity or perform other operations.

            } else {
                // Some or all permissions were denied. Handle this situation.
                Toast.makeText(this, "Some permissions were denied.", Toast.LENGTH_SHORT).show();

                new AlertDialog.Builder(this).setTitle("Permission denied").setMessage("If you reject permission, You can't use this service\n\n" +
                                "Please turn on SMS and Location permission at [Setting] > [Permission]")
                        .setPositiveButton("PROCEED", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                openAppSettings();
                            }
                        }).setNegativeButton("CLOSE", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Toast.makeText(MainActivity.this, "SMS Permission denied", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        }).show();
            }
        }
    }
    private void openAppSettings() {
        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivity(intent);
    }
}