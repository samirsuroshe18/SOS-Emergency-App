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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.button.MaterialButton;

public class RegisterNumberActivity extends AppCompatActivity {
    private static final int PICK_CONTACT = 1;
    private static final int REQUEST_READ_CONTACTS_PERMISSION = 100;
    EditText contactName, contactNumber;
    AppCompatButton btnAddContact, btnContactBook;
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

                if (c != null && c.getCount() > 0) {
                    c.moveToFirst();
                    int nameColIndex = c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
                    int numColIndex = c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER);
                    int idColIndex = c.getColumnIndex(ContactsContract.Contacts._ID);

                    if (nameColIndex >= 0 && idColIndex >= 0 && numColIndex >= 0) {
                        String contactName = c.getString(nameColIndex);
                        String contactId = c.getString(idColIndex);
                        String contactNum = c.getString(numColIndex);
                        String phone="";

                        if (contactNum.equalsIgnoreCase("1")) {
                            Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
                            if (phones != null && phones.getCount() > 0){
                                phones.moveToFirst();
                                int index = phones.getColumnIndex("data1");
                                if (index>=0){
                                    phone = phones.getString(index);
                                }

                            }

                        }
                        int a = databaseHelper.count();
                        if (a < 5) {
                            boolean checkData = databaseHelper.insertDataFunc(contactName, phone);

                            if (checkData) {
                                Toast.makeText(RegisterNumberActivity.this, "Contact is registered", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(RegisterNumberActivity.this, "Contact doesn't registered", Toast.LENGTH_SHORT).show();
                            }
                        }
                        Log.i("content_provider", "Name : " + contactName + " Number : " + phone);
                    } else {
                        Toast.makeText(this, "Column not found", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }


    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
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