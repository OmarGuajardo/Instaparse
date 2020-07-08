package com.example.instaparse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.instaparse.databinding.ActivityProfileBinding;
import com.google.android.material.appbar.MaterialToolbar;


public class ProfileActivity extends AppCompatActivity {

    MaterialToolbar toolbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        toolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("");


    }
    //When user hits the back button we simply finish the intent
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }
}