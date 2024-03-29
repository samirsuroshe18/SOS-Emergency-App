package com.example.sos;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class RegisterNumberActivity extends AppCompatActivity {
    private static final int PICK_CONTACT = 1;
    private static final int REQUEST_READ_CONTACTS_PERMISSION = 100;
    EditText contactName, contactNumber;
    Button btnAddContact, btnContactBook;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_number);

        contactNumber = findViewById(R.id.contactNumber);
        contactName = findViewById(R.id.contactName);
        btnAddContact = findViewById(R.id.btnAddContact);
        btnContactBook = findViewById(R.id.btnContactBook);
        databaseHelper = new DatabaseHelper(this);

       btnAddContact.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               String name = contactName.getText().toString();
               String number = contactNumber.getText().toString();
                int a = databaseHelper.count();

               if ( a < 5) {
                   if (number.length()!=10){
                       contactNumber.setError("Enter valid number");
                       return;
                   }
                   boolean checkData = databaseHelper.insertDataFunc(name, number);

                   if (checkData) {
                       Toast.makeText(RegisterNumberActivity.this, "Contact is registered", Toast.LENGTH_SHORT).show();
                       contactName.setText("");
                       contactNumber.setText("");
                   }
                   else {
                       Toast.makeText(RegisterNumberActivity.this, "Contact doesn't registered", Toast.LENGTH_SHORT).show();
                   }
               }
               else {
                       Toast.makeText(RegisterNumberActivity.this, "Can't Add more than 5 Contacts", Toast.LENGTH_SHORT).show();
                   }


           }
       });

        btnContactBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(RegisterNumberActivity.this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
                    int a = databaseHelper.count();
                    if (a < 5) {
                        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                        startActivityForResult(intent, PICK_CONTACT);
                    } else {
                        Toast.makeText(RegisterNumberActivity.this, "Can't Add more than 5 Contacts", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, 100);
                    }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_CONTACT) {
            if (resultCode == Activity.RESULT_OK) {

                Uri contactData = data.getData();
                Cursor c = managedQuery(contactData, null, null, null, null);
                if (c.moveToFirst()) {

                    String id = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));
                    String hasPhone = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                    String phone = null;
                    try {
                        if (hasPhone.equalsIgnoreCase("1")) {
                            Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id, null, null);
                            phones.moveToFirst();
                            phone = phones.getString(phones.getColumnIndex("data1"));
                        }
                        String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                        int a = databaseHelper.count();
                        if (a < 5) {
                            boolean checkData = databaseHelper.insertDataFunc(name, phone);

                            if (checkData) {
                                Toast.makeText(RegisterNumberActivity.this, "Contact is registered", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(RegisterNumberActivity.this, "Contact doesn't registered", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } catch (Exception ex) {
                    }
                }
            }
        }


    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_READ_CONTACTS_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, make the phone call
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, PICK_CONTACT);
                Toast.makeText(this, "Call permission granted", Toast.LENGTH_SHORT).show();
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS)) {

                    new AlertDialog.Builder(this).setTitle("Permission required").setMessage("We need permission to register the contact")
                            .setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ActivityCompat.requestPermissions(RegisterNumberActivity.this, new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_READ_CONTACTS_PERMISSION);
                                }
                            }).show();
                } else {

                    new AlertDialog.Builder(this).setTitle("Permission denied").setMessage("If you reject permission, you can't register the number\n\n" +
                                    "Please turn on Contacts permission at [Setting] > [Permission]")
                            .setPositiveButton("PROCEED", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    openAppSettings();
                                }
                            }).setNegativeButton("CLOSE", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(RegisterNumberActivity.this, "Contact permission denied", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                            }).show();
                }
            }
        }
    }
    private void openAppSettings() {
        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivity(intent);
    }
}