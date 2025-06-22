//package com.example.sos;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.app.ActivityCompat;
//import androidx.core.content.ContextCompat;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import android.Manifest;
//import android.app.AlertDialog;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.net.Uri;
//import android.os.Bundle;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.LinearLayout;
//import android.widget.Toast;
//
//import com.google.android.material.appbar.MaterialToolbar;
//import com.google.android.material.button.MaterialButton;
//
//import java.util.ArrayList;
//
//public class ShowContact extends AppCompatActivity {
//    private static final int REQUEST_CALL_PHONE_PERMISSION = 1;
//    RecyclerView contactRecyclerView;
//    ArrayList<ContactModel> modelArrayList;
//    ContactRecyclerAdapter adapter;
//    DatabaseHelper helper;
//    MaterialToolbar appBar;
//    LinearLayout emptyStateLayout;
//    MaterialButton addFirstContactButton;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_show_contact);
//        appBar = findViewById(R.id.toolbar);
//        setSupportActionBar(appBar);
//        contactRecyclerView = findViewById(R.id.contactRecylerView);
//        contactRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        helper = new DatabaseHelper(this);
//        modelArrayList = helper.fetchData();
//        adapter = new ContactRecyclerAdapter(this, modelArrayList);
//        contactRecyclerView.setAdapter(adapter);
//        emptyStateLayout = findViewById(R.id.emptyStateLayout);
//        addFirstContactButton = findViewById(R.id.addFirstContactButton);
//
//        addFirstContactButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(ShowContact.this, RegisterNumberActivity.class);
//                startActivity(intent);
//                finish();
//            }
//        });
//
//        if(modelArrayList.isEmpty()){
//            emptyStateLayout.setVisibility(View.VISIBLE);
//        } else {
//            emptyStateLayout.setVisibility(View.GONE);
//        }
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == REQUEST_CALL_PHONE_PERMISSION) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//
//                Toast.makeText(this, "Call permission granted", Toast.LENGTH_SHORT).show();
//            } else {
//                // Permission denied, check if a rationale is needed
//                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE)) {
//
//                    new AlertDialog.Builder(this).setTitle("Permission required").setMessage("We need permission for calling")
//                            .setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    ActivityCompat.requestPermissions(ShowContact.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL_PHONE_PERMISSION);
//                                }
//                            }).show();
//                } else {
//
//                    new AlertDialog.Builder(this).setTitle("Permission denied").setMessage("If you reject permission, you can't use this call service\n\n" +
//                                    "Please turn on Phone permission at [Setting] > [Permission]")
//                            .setPositiveButton("PROCEED", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    openAppSettings();
//                                }
//                            }).setNegativeButton("CLOSE", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    Toast.makeText(ShowContact.this, "Call permission denied", Toast.LENGTH_SHORT).show();
//                                    dialog.dismiss();
//                                }
//                            }).show();
////
//                }
//            }
//        }
//    }
//
//    private void performPhoneCall(String phoneNumber) {
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//            // Permission is not granted, request it
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL_PHONE_PERMISSION);
//        } else {
//            // Permission is already granted, make the phone call
//            Intent intent = new Intent(Intent.ACTION_CALL);
//            intent.setData(Uri.parse(phoneNumber));
//            startActivity(intent);
//        }
//    }
//
//    private void openAppSettings() {
//        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//        intent.setData(Uri.parse("package:" + getPackageName()));
//        startActivity(intent);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == android.R.id.home) {
//            // Handle back button press
//            onBackPressed(); // or finish();
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
//}




package com.example.sos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class ShowContact extends AppCompatActivity implements ContactRecyclerAdapter.OnContactDeleteListener {
    private static final int REQUEST_CALL_PHONE_PERMISSION = 1;
    RecyclerView contactRecyclerView;
    ArrayList<ContactModel> modelArrayList;
    ContactRecyclerAdapter adapter;
    DatabaseHelper helper;
    MaterialToolbar appBar;
    LinearLayout emptyStateLayout;
    MaterialButton addFirstContactButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_contact);
        appBar = findViewById(R.id.toolbar);
        setSupportActionBar(appBar);
        contactRecyclerView = findViewById(R.id.contactRecylerView);
        contactRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        helper = new DatabaseHelper(this);
        modelArrayList = helper.fetchData();

        // Pass the delete listener to the adapter
        adapter = new ContactRecyclerAdapter(this, modelArrayList, this);
        contactRecyclerView.setAdapter(adapter);

        emptyStateLayout = findViewById(R.id.emptyStateLayout);
        addFirstContactButton = findViewById(R.id.addFirstContactButton);

        addFirstContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowContact.this, RegisterNumberActivity.class);
                startActivity(intent);
                finish();
            }
        });

        updateEmptyState();
    }

    // Method to update empty state visibility
    private void updateEmptyState() {
        if (modelArrayList.isEmpty()) {
            emptyStateLayout.setVisibility(View.VISIBLE);
            contactRecyclerView.setVisibility(View.GONE);
        } else {
            emptyStateLayout.setVisibility(View.GONE);
            contactRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    // Implement the callback method from adapter
    @Override
    public void onContactDeleted() {
        updateEmptyState();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh data when returning from RegisterNumberActivity
        modelArrayList.clear();
        modelArrayList.addAll(helper.fetchData());
        adapter.notifyDataSetChanged();
        updateEmptyState();
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
                                    ActivityCompat.requestPermissions(ShowContact.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL_PHONE_PERMISSION);
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
                                    Toast.makeText(ShowContact.this, "Call permission denied", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                            }).show();
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