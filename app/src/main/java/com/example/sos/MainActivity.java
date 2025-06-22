//package com.example.sos;
//
//import android.Manifest;
//import android.app.AlertDialog;
//import android.app.NotificationChannel;
//import android.app.NotificationManager;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.IntentSender;
//import android.content.pm.PackageManager;
//import android.location.LocationManager;
//import android.net.Uri;
//import android.os.Build;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.app.ActivityCompat;
//import androidx.core.content.ContextCompat;
//
//import com.google.android.gms.common.api.ResolvableApiException;
//import com.google.android.gms.location.LocationRequest;
//import com.google.android.gms.location.LocationServices;
//import com.google.android.gms.location.LocationSettingsRequest;
//import com.google.android.gms.location.LocationSettingsResponse;
//import com.google.android.gms.location.SettingsClient;
//import com.google.android.gms.tasks.OnFailureListener;
//import com.google.android.gms.tasks.Task;
//import com.google.android.material.snackbar.Snackbar;
//
//import java.util.ArrayList;
//import java.util.List;
//
//
//public class MainActivity extends AppCompatActivity {
//
//    private static final int REQUEST_CODE = 11;
//    int contactList;
//    DatabaseHelper databaseHelper;
//    private static final int REQUEST_CHECK_SETTING = 10;
//    String[] permissions = {
//            Manifest.permission.SEND_SMS,
//            Manifest.permission.ACCESS_COARSE_LOCATION,
//            Manifest.permission.ACCESS_FINE_LOCATION
//    };
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//
//        databaseHelper = new DatabaseHelper(this);
//        contactList = databaseHelper.count();
//
//        if(contactList==0){
//            Intent intent = new Intent(this, RegisterNumberActivity.class);
//            startActivity(intent);
//            Toast.makeText(this, "Register at least one contact", Toast.LENGTH_LONG).show();
//            finish();
//        }
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel channel = new NotificationChannel("MYID", "CHANNELFOREGROUND", NotificationManager.IMPORTANCE_DEFAULT);
//
//            NotificationManager m = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//            m.createNotificationChannel(channel);
//        }
//
//    }
//
//    public void stopServiceV(View view) {
//
//        Intent notificationIntent = new Intent(this,ServiceMine.class);
//        notificationIntent.setAction("stop");
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            getApplicationContext().startForegroundService(notificationIntent);
//            Snackbar.make(findViewById(android.R.id.content),"Service Stopped!", Snackbar.LENGTH_LONG).show();
//        }
//    }
//
//    public void startServiceV(View view) {
//
//        List<String> permissionList = new ArrayList<>();
//        for (String permission : permissions) {
//            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
//                permissionList.add(permission);
//            }
//        }
//
//        if (!permissionList.isEmpty()) {
//            //Request permission
//            ActivityCompat.requestPermissions(this, permissionList.toArray(new String[0]), REQUEST_CODE);
//        } else {
//            // All permissions are already granted, you can proceed with your tasks.
//            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
//            boolean isLocationEnabled = locationManager != null && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
//
//            if (!isLocationEnabled) {
//                // Location settings are not enabled, show a dialog to prompt the user to enable them
//                LocationRequest locationRequest = LocationRequest.create();
//                locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//                locationRequest.setInterval(10000);
//                locationRequest.setFastestInterval(10000 / 2);
//
//                LocationSettingsRequest.Builder locationSettingRequestBuilder = new LocationSettingsRequest.Builder();
//
//                locationSettingRequestBuilder.addLocationRequest(locationRequest);
//                locationSettingRequestBuilder.setAlwaysShow(true);
//
//                SettingsClient settingsClient = LocationServices.getSettingsClient(MainActivity.this);
//
//                Task<LocationSettingsResponse> task = settingsClient.checkLocationSettings(locationSettingRequestBuilder.build());
//
//                task.addOnFailureListener(MainActivity.this, new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        if (e instanceof ResolvableApiException) {
//
//                            try {
//                                ResolvableApiException resolvableApiException = (ResolvableApiException) e;
//                                resolvableApiException.startResolutionForResult(MainActivity.this, REQUEST_CHECK_SETTING);
//                            } catch (IntentSender.SendIntentException ex) {
//                                throw new RuntimeException(ex);
//                            }
//                        }
//                    }
//                });
//            }else {
//                Intent notificationIntent = new Intent(this,ServiceMine.class);
//                notificationIntent.setAction("Start");
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                    getApplicationContext().startForegroundService(notificationIntent);
//                    Snackbar.make(findViewById(android.R.id.content),"Service Started!", Snackbar.LENGTH_LONG).show();
//                }
//            }
//        }
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == REQUEST_CODE) {
//            // Check if all permissions are granted
//            boolean allPermissionsGranted = true;
//            for (int result : grantResults) {
//                if (result != PackageManager.PERMISSION_GRANTED) {
//                    allPermissionsGranted = false;
//                    break;
//                }
//            }
//
//            if (allPermissionsGranted) {
//                // All permissions are granted, you can proceed with your tasks.
//                // For example, start your main activity or perform other operations.
//
//            } else {
//                // Some or all permissions were denied. Handle this situation.
//                Toast.makeText(this, "Some permissions were denied.", Toast.LENGTH_SHORT).show();
//
//                new AlertDialog.Builder(this).setTitle("Permission denied").setMessage("If you reject permission, You can't use this service\n\n" +
//                                "Please turn on SMS and Location permission at [Setting] > [Permission]")
//                        .setPositiveButton("PROCEED", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                openAppSettings();
//                            }
//                        }).setNegativeButton("CLOSE", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//
//                                Toast.makeText(MainActivity.this, "SMS Permission denied", Toast.LENGTH_SHORT).show();
//                                dialog.dismiss();
//                            }
//                        }).show();
//            }
//        }
//    }
//    private void openAppSettings() {
//        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//        intent.setData(Uri.parse("package:" + getPackageName()));
//        startActivity(intent);
//    }
//}

package com.example.sos;

import android.Manifest;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
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
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
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

    private ImageView statusIcon;
    private TextView statusText;
    private MaterialButton startButton, stopButton;
    private MaterialCardView statusCard;
    private boolean isServiceActive = false;
    MaterialToolbar appBar;

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
    protected void onResume() {
        super.onResume();
        // Optionally fetch real service status here if needed
        updateServiceStatus();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        appBar = findViewById(R.id.topAppBar);
        setSupportActionBar(appBar);
        initializeViews();
        isServiceActive = isMyServiceRunning(ServiceMine.class);
        Log.d("ManiActivity", String.valueOf(isServiceActive));
        updateServiceStatus();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("MYID", "CHANNELFOREGROUND", NotificationManager.IMPORTANCE_DEFAULT);

            NotificationManager m = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            m.createNotificationChannel(channel);
        }

    }

    private void initializeViews() {
        statusIcon = findViewById(R.id.statusIcon);
        statusText = findViewById(R.id.statusText);
        startButton = findViewById(R.id.start);
        stopButton = findViewById(R.id.stop);
        statusCard = findViewById(R.id.statusCard);
    }

    public void stopServiceV(View view) {

        Intent notificationIntent = new Intent(this,ServiceMine.class);
        notificationIntent.setAction("stop");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getApplicationContext().startForegroundService(notificationIntent);
            Snackbar.make(findViewById(android.R.id.content),"Service Stopped!", Snackbar.LENGTH_LONG).show();
            isServiceActive = false;
            updateServiceStatus();
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
                    isServiceActive = true;
                    updateServiceStatus();
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

    private void updateServiceStatus() {
        updateStatusCard();
        updateStartButton();
        updateStopButton();

        // Animate the status change
        animateStatusChange();
    }

    // Animate status changes for better UX
    private void animateStatusChange() {
        // Scale animation for status icon
        statusIcon.animate()
                .scaleX(1.1f)
                .scaleY(1.1f)
                .setDuration(200)
                .withEndAction(() -> {
                    statusIcon.animate()
                            .scaleX(1.0f)
                            .scaleY(1.0f)
                            .setDuration(200)
                            .start();
                })
                .start();

        // Fade animation for status text
        statusText.animate()
                .alpha(0.5f)
                .setDuration(150)
                .withEndAction(() -> {
                    statusText.animate()
                            .alpha(1.0f)
                            .setDuration(150)
                            .start();
                })
                .start();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed(); // or finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateStatusCard(){
        TypedValue colorPrimaryContainerValue = new TypedValue();
        getTheme().resolveAttribute(com.google.android.material.R.attr.colorPrimaryContainer, colorPrimaryContainerValue, true);
        int colorPrimaryContainer = ContextCompat.getColor(this, colorPrimaryContainerValue.resourceId);

        TypedValue colorOnPrimaryContainerValue = new TypedValue();
        getTheme().resolveAttribute(com.google.android.material.R.attr.colorOnPrimaryContainer, colorOnPrimaryContainerValue, true);
        int colorOnPrimaryContainer = ContextCompat.getColor(this, colorOnPrimaryContainerValue.resourceId);

        TypedValue colorSurfaceVariantValue = new TypedValue();
        getTheme().resolveAttribute(com.google.android.material.R.attr.colorSurfaceVariant, colorSurfaceVariantValue, true);
        int colorSurfaceVariant = ContextCompat.getColor(this, colorSurfaceVariantValue.resourceId);

        TypedValue colorOnSurfaceVariantValue = new TypedValue();
        getTheme().resolveAttribute(com.google.android.material.R.attr.colorOnSurfaceVariant, colorOnSurfaceVariantValue, true);
        int colorOnSurfaceVariant = ContextCompat.getColor(this, colorOnSurfaceVariantValue.resourceId);

        TypedValue strokeColorValue = new TypedValue();
        getTheme().resolveAttribute(com.google.android.material.R.attr.colorOutline, strokeColorValue, true);
        int strokeColor = ContextCompat.getColor(this, strokeColorValue.resourceId);

        if(isServiceActive){
            statusCard.setCardBackgroundColor(ColorStateList.valueOf(colorPrimaryContainer));
            statusCard.setStrokeWidth(0);
            statusIcon.setImageResource(R.drawable.safety_check);
            statusIcon.setColorFilter(colorOnPrimaryContainer);
            statusText.setText("Service Active");
            statusText.setTextColor(colorOnPrimaryContainer);
        } else{
            statusCard.setCardBackgroundColor(ColorStateList.valueOf(colorSurfaceVariant));
            statusCard.setStrokeWidth(0);
            statusCard.setStrokeColor(strokeColor);
            statusIcon.setImageResource(R.drawable.safety_check_off);
            statusIcon.setColorFilter(colorOnSurfaceVariant);
            statusText.setText("Service Inactive");
            statusText.setTextColor(colorOnSurfaceVariant);
        }
    }

    private void updateStartButton(){
        TypedValue colorPrimaryValue = new TypedValue();
        getTheme().resolveAttribute(com.google.android.material.R.attr.colorPrimary, colorPrimaryValue, true);
        int colorPrimary = ContextCompat.getColor(this, colorPrimaryValue.resourceId);

        TypedValue colorOnPrimaryValue = new TypedValue();
        getTheme().resolveAttribute(com.google.android.material.R.attr.colorOnPrimary, colorOnPrimaryValue, true);
        int colorOnPrimary = ContextCompat.getColor(this, colorOnPrimaryValue.resourceId);

        TypedValue colorSurfaceVariantValue = new TypedValue();
        getTheme().resolveAttribute(com.google.android.material.R.attr.colorSurfaceVariant, colorSurfaceVariantValue, true);
        int colorSurfaceVariant = ContextCompat.getColor(this, colorSurfaceVariantValue.resourceId);

        TypedValue colorOnSurfaceVariantValue = new TypedValue();
        getTheme().resolveAttribute(com.google.android.material.R.attr.colorOnSurfaceVariant, colorOnSurfaceVariantValue, true);
        int colorOnSurfaceVariant = ContextCompat.getColor(this, colorOnSurfaceVariantValue.resourceId);

        TypedValue strokeColorValue = new TypedValue();
        getTheme().resolveAttribute(com.google.android.material.R.attr.colorOutline, strokeColorValue, true);
        int strokeColor = ContextCompat.getColor(this, strokeColorValue.resourceId);

        if(isServiceActive){
            startButton.setEnabled(false);
            startButton.setStrokeWidth(1);
            startButton.setStrokeColor(ColorStateList.valueOf(strokeColor));
            startButton.setBackgroundTintList(ColorStateList.valueOf(colorSurfaceVariant));
            startButton.setIconTint(ColorStateList.valueOf(colorOnSurfaceVariant));
            startButton.setTextColor(colorOnSurfaceVariant);
            startButton.setText("Monitoring Active");
        } else{
            startButton.setEnabled(true);
            startButton.setStrokeWidth(0);
            startButton.setBackgroundTintList(ColorStateList.valueOf(colorPrimary));
            startButton.setIconTint(ColorStateList.valueOf(colorOnPrimary));
            startButton.setTextColor(colorOnPrimary);
            startButton.setText("Start Monitoring");
        }
    }

    private void updateStopButton(){
        TypedValue colorSurfaceVariantValue = new TypedValue();
        getTheme().resolveAttribute(com.google.android.material.R.attr.colorSurfaceVariant, colorSurfaceVariantValue, true);
        int colorSurfaceVariant = ContextCompat.getColor(this, colorSurfaceVariantValue.resourceId);

        TypedValue colorOnSurfaceVariantValue = new TypedValue();
        getTheme().resolveAttribute(com.google.android.material.R.attr.colorOnSurfaceVariant, colorOnSurfaceVariantValue, true);
        int colorOnSurfaceVariant = ContextCompat.getColor(this, colorOnSurfaceVariantValue.resourceId);

        TypedValue colorErrorValue = new TypedValue();
        getTheme().resolveAttribute(com.google.android.material.R.attr.colorError, colorErrorValue, true);
        int colorError = ContextCompat.getColor(this, colorErrorValue.resourceId);

        TypedValue colorOnErrorValue = new TypedValue();
        getTheme().resolveAttribute(com.google.android.material.R.attr.colorOnError, colorOnErrorValue, true);
        int colorOnError = ContextCompat.getColor(this, colorOnErrorValue.resourceId);

        TypedValue strokeColorValue = new TypedValue();
        getTheme().resolveAttribute(com.google.android.material.R.attr.colorOutline, strokeColorValue, true);
        int strokeColor = ContextCompat.getColor(this, strokeColorValue.resourceId);

        if(isServiceActive){
            stopButton.setEnabled(true);
            stopButton.setStrokeWidth(0);
            stopButton.setBackgroundTintList(ColorStateList.valueOf(colorError));
            stopButton.setIconTint(ColorStateList.valueOf(colorOnError));
            stopButton.setTextColor(colorOnError);
            stopButton.setText("Stop Monitoring");
        } else{
            stopButton.setEnabled(false);
            stopButton.setStrokeWidth(1);
            stopButton.setStrokeColor(ColorStateList.valueOf(strokeColor));
            stopButton.setBackgroundTintList(ColorStateList.valueOf(colorSurfaceVariant));
            stopButton.setIconTint(ColorStateList.valueOf(colorOnSurfaceVariant));
            stopButton.setTextColor(colorOnSurfaceVariant);
            stopButton.setText("Monitoring Deactive");
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}