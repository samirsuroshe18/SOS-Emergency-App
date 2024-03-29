package com.example.sos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class ShowContact extends AppCompatActivity {

    RecyclerView contactRecyclerView;
    ArrayList<ContactModel> modelArrayList;
    ContactRecyclerAdapter adapter;
    DatabaseHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_contact);

        contactRecyclerView = findViewById(R.id.contactRecylerView);
        contactRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        helper = new DatabaseHelper(this);
        modelArrayList = helper.fetchData();
        adapter = new ContactRecyclerAdapter(this, modelArrayList);
        contactRecyclerView.setAdapter(adapter);
    }
}