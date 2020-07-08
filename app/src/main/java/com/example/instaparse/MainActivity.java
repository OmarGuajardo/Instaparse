package com.example.instaparse;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.instaparse.databinding.ActivityMainBinding;
import com.example.instaparse.fragments.ComposeFragment;
import com.example.instaparse.fragments.PostFragment;
import com.example.instaparse.ui.login.LoginActivity;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public String TAG = "MainActivity";


    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        final FragmentManager fragmentManager = getSupportFragmentManager();
        MaterialToolbar topAppBar = findViewById(R.id.topAppBar);
        
        topAppBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId() == R.id.btnLogout){
                    ParseUser.logOut();
                    Intent i = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                }
                return true;
            };
        });


        binding.bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = new ComposeFragment();
                switch (item.getItemId()) {
                    case R.id.action_home:
                        fragment = new PostFragment();
                        // do something here
                        break;
                    case R.id.action_compose:
                        // do something here
                        fragment = new ComposeFragment();
                        break;
                    case R.id.action_profile:
                    default:
                        fragment = new ProfileFragment();
                        break;
                }
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
                return true;
            }
        });
        // Set default selection
        binding.bottomNavigation.setSelectedItemId(R.id.action_home);
    }








}