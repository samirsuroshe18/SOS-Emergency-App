package com.example.sos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

public class EditMessageActivity extends AppCompatActivity {
    TextInputEditText etMessage;
    AppCompatButton btnSave;
    String editMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_message);

        etMessage = findViewById(R.id.etMessage);
        btnSave = findViewById(R.id.btnSave);

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
    }
    public void showMessage(){
        SharedPreferences sp = getSharedPreferences("message", MODE_PRIVATE);
        String msg = sp.getString("msg", null);
        etMessage.setText(msg);
    }
}
