package com.example.instaparse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Parcel;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.MaterialToolbar;

import org.parceler.Parcels;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PostDetailActivity extends AppCompatActivity {

    MaterialToolbar toolbar;
    Post post;
    TextView tvUsername;
    ImageView ivPostPictureFeed;
    TextView tvLikesCounter;
    ImageButton btnLike;
    ImageButton btnComment;
    TextView tvPostDescription;
    TextView tvTimeStamp;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        toolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("");
        post = new Post();

        //Hooking up all the Views
        tvUsername = findViewById(R.id.tvUserName);
        ivPostPictureFeed = findViewById(R.id.ivPostPictureFeed);
        tvLikesCounter = findViewById(R.id.tvLikesCounter);
        btnLike = findViewById(R.id.btnLike);
        btnComment = findViewById(R.id.btnComment);
        tvPostDescription = findViewById(R.id.tvPostDescription);
        tvTimeStamp = findViewById(R.id.tvTimeStamp);

        //Unpacking the Extras
        post = Parcels.unwrap(getIntent().getParcelableExtra("postSelected"));

        //Populating fields
        tvUsername.setText(post.getUser().getUsername());
        tvPostDescription.setText(post.getDescription());
        Glide.with(getApplicationContext())
                .load(post.getImage().getUrl())
                .centerCrop()
                .into(ivPostPictureFeed);
        Date date = post.getCreatedAt();
        SimpleDateFormat DateFor = new SimpleDateFormat("dd MMMM yyyy");
        String stringDate = DateFor.format(date);
        tvTimeStamp.setText(stringDate);

    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }
}