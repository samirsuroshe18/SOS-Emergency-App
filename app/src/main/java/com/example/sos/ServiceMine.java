package com.example.sos;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.telephony.SmsManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.CancellationTokenSource;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;

public class ServiceMine extends Service implements SensorEventListener {

    boolean isRunning = false;
    private Vibrator vibrator;
    DatabaseHelper db;
    FusedLocationProviderClient fusedLocationClient;
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private long lastShakeTime;
    private static final int SHAKE_THRESHOLD = 90;
    // How long an alert waits for a fresh position before it goes out with the last known one
    private static final long LOCATION_WAIT = 10000;
    private static final String NO_LOCATION = "Unable to Find Location :(";
    SmsManager manager = SmsManager.getDefault();
    String myLocation = NO_LOCATION;
    private final Handler handler = new Handler(Looper.getMainLooper());

    @Override
    public void onCreate() {
        super.onCreate();

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        //Get the current location of user, kept as a fallback for an alert that cannot get a fresh one
        fusedLocationClient.getCurrentLocation(Priority.PRIORITY_BALANCED_POWER_ACCURACY, new CancellationTokenSource().getToken())
                .addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    myLocation = mapLink(location);
                }
            }
        });
    }

    private static String mapLink(Location location) {
        return "https://maps.google.com/maps?q=" + location.getLatitude() + "," + location.getLongitude();
    }

    // Sends the alert with the position at the moment of the shake, not the one from when the service started
    private void sendAlert() {
        final boolean[] sent = {false};
        Runnable send = () -> {
            if (sent[0]) return;
            sent[0] = true;
            sendMessages();
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            send.run();
            return;
        }

        // The alert must go out even when no fresh position arrives
        handler.postDelayed(send, LOCATION_WAIT);
        fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, new CancellationTokenSource().getToken())
                .addOnSuccessListener(location -> {
                    if (location != null && !sent[0]) {
                        myLocation = mapLink(location);
                    }
                    send.run();
                })
                .addOnFailureListener(e -> send.run());
    }

    private void sendMessages() {
        db = new DatabaseHelper(ServiceMine.this);
        ArrayList<ContactModel> list = db.fetchData();
        SharedPreferences sp = getSharedPreferences("message", MODE_PRIVATE);
        String msg = sp.getString("msg", null);
        if (msg == null) {
            msg = "I am in DANGER, i need help. Please urgently reach me out.";
        }
        for (ContactModel c : list) {
            String message = "Hey, " + c.getName() + " " + msg + "\n\nHere are my coordinates :\n" + myLocation;
            try {
                // A text longer than one SMS is refused unless it is sent in parts
                manager.sendMultipartTextMessage(c.getNumber(), null, manager.divideMessage(message), null, null);
            } catch (Exception e) {
                // One bad number must not stop the alert for the other contacts
                Log.e("ServiceMine", "Could not send the alert to " + c.getName(), e);
            }
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent,flags,startId);
        if (accelerometer != null) {
            // Register the sensor listener for the accelerometer sensor
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
        }

        String action = intent != null ? intent.getAction() : null;
        if ("STOP".equalsIgnoreCase(action)) {
            if(isRunning) {
                sensorManager.unregisterListener(this);
                this.stopForeground(true);
                this.stopSelf();
            }
        } else {

            Intent notificationIntent = new Intent(this, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel("MYID", "CHANNELFOREGROUND", NotificationManager.IMPORTANCE_DEFAULT);

                NotificationManager m = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                m.createNotificationChannel(channel);

                Notification notification = new Notification.Builder(this, "MYID")
                        .setContentTitle("You are protected")
                        .setContentText("We are there for you")
                        .setSmallIcon(R.drawable.siren)
                        .setContentIntent(pendingIntent)
                        .build();
                this.startForeground(115, notification);
                isRunning = true;
                return START_NOT_STICKY;
            }
        }
        return START_STICKY;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            long currentTime = System.currentTimeMillis();
            if ((currentTime - lastShakeTime) > 5000) { // Add a time gap between two shakes
                float x = event.values[0];
                float y = event.values[1];
                float z = event.values[2];

                double acceleration = Math.sqrt(x * x + y * y + z * z);

                if (acceleration > SHAKE_THRESHOLD) {

                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                    }



                    lastShakeTime = currentTime;
                    sendAlert();
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Unregister the sensor listener when the service is destroyed
        sensorManager.unregisterListener(this);
    }
}