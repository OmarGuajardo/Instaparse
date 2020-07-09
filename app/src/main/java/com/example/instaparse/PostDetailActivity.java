package com.example.instaparse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.ParcelUuid;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.instaparse.models.Comment;
import com.example.instaparse.models.Post;
import com.google.android.material.appbar.MaterialToolbar;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.parceler.Parcels;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PostDetailActivity extends AppCompatActivity {

    private static final String TAG = "PostDetailsActivity";
    MaterialToolbar toolbar;
    Post post;
    TextView tvUsername;
    ImageView ivPostPictureFeed;
    TextView tvLikesCounter;
    ImageButton btnLike;
    ImageButton btnComment;
    TextView tvPostDescription;
    TextView tvTimeStamp;
    ImageView ivProfilePicture;
    CommentsAdapter adapter;
    RecyclerView rvComments;
    List<Comment>comments;



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
        ivProfilePicture = findViewById(R.id.ivProfilePicture);

        tvLikesCounter = findViewById(R.id.tvLikesCounter);
        btnLike = findViewById(R.id.btnLike);
        btnComment = findViewById(R.id.btnComment);
        tvPostDescription = findViewById(R.id.tvPostDescription);
        tvTimeStamp = findViewById(R.id.tvTimeStamp);

        //Unpacking the Extras
        post = Parcels.unwrap(getIntent().getParcelableExtra("postSelected"));
        setValues();

        //Setting up the recycler view
        comments = post.getCommentList();
        Log.d(TAG, "onCreate: comments size " + comments.size());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        adapter = new CommentsAdapter(comments,getApplicationContext());
        rvComments = findViewById(R.id.rvComments);
        rvComments.setLayoutManager(linearLayoutManager);
        rvComments.setAdapter(adapter);







    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

    public void setValues(){
        //Getting the List of Users that have liked the post
        final ArrayList<ParseUser> likeList = (ArrayList<ParseUser>)post.get("likeList");
        tvLikesCounter.setText(String.valueOf(likeList.size()) + " Likes");
        if(likeList.size() > 0){
            if(isInList(likeList) > -1){
                btnLike.setSelected(true);
            }
        }else {
            btnLike.setSelected(false);
        }
        //On click listener for the btn like
        btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int index = isInList(likeList);
                if(index > - 1){
                    likeList.remove(index);
                    post.put("likeList",likeList);
                    btnLike.setSelected(false);
                }
                else{

                    likeList.add(ParseUser.getCurrentUser());
                    post.add("likeList",ParseUser.getCurrentUser());
                    btnLike.setSelected(true);

                }
                post.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        tvLikesCounter.setText(String.valueOf(likeList.size()) + " Likes");
                    }
                });
            }
        });
        //Populating fields
        try {
            tvUsername.setText(post.getUser().fetchIfNeeded().getUsername());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        tvPostDescription.setText(post.getDescription());
        ParseFile image = post.getImage();
        if(image != null) {
            Glide.with(getApplicationContext())
                    .load(post.getImage().getUrl())
                    .centerCrop()
                    .into(ivPostPictureFeed);
        }

        if(post.getUser().getParseFile("profilePicture") != null){
            Glide.with(getApplicationContext())
                    .load(post.getUser().getParseFile("profilePicture").getUrl())
                    .circleCrop()
                    .into(ivProfilePicture);
        }
        Date date = post.getCreatedAt();
        SimpleDateFormat DateFor = new SimpleDateFormat("dd MMMM yyyy");
        String stringDate = DateFor.format(date);
        tvTimeStamp.setText(stringDate);
    }
    public int isInList(ArrayList<ParseUser> likeList){
        for(ParseUser user: likeList){
            if(user.getObjectId().equals(ParseUser.getCurrentUser().getObjectId())){
                Log.d(TAG, "isInList: ");
                return likeList.indexOf(user);
            }
        }
        return -1;
    }
}