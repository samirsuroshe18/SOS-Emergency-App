package com.example.sos;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class EditMessageActivity extends AppCompatActivity {
    TextInputEditText etMessage;
    MaterialButton btnSave, btnReset;
    String editMsg;
    MaterialToolbar appBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_message);
        etMessage = findViewById(R.id.etMessage);
        btnSave = findViewById(R.id.btnSave);
        btnReset = findViewById(R.id.btnReset);
        appBar = findViewById(R.id.topAppBar);
        setSupportActionBar(appBar);

        SharedPreferences sp = getSharedPreferences("message", MODE_PRIVATE);
        editMsg = sp.getString("msg", null);

        if (editMsg!=null){
            showMessage();
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etMessage.getText().toString().isEmpty()){
                    etMessage.setError("Please enter your message");
                }
                else{
                    String msg = etMessage.getText().toString();
                    SharedPreferences sp = getSharedPreferences("message", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("msg", msg);
                    editor.apply();
                    Toast.makeText(EditMessageActivity.this, "Message save successfully", Toast.LENGTH_SHORT).show();
                    showMessage();
                }
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etMessage.setText("I am in DANGER, i need help. Please urgently reach me out.");
            }
        });
    }
    public void showMessage(){
        SharedPreferences sp = getSharedPreferences("message", MODE_PRIVATE);
        String msg = sp.getString("msg", null);
        etMessage.setText(msg);
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
