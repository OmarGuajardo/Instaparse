package com.example.instaparse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.instaparse.databinding.ActivityProfileBinding;
import com.google.android.material.appbar.MaterialToolbar;
import com.parse.FindCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


public class ProfileActivity extends AppCompatActivity {

    public String TAG = "ProfileActivity";
    MaterialToolbar toolbar;
    TextView tvUserNameDetails;
    RecyclerView rvProfilePics;
    ProfileAdapter adapter;
    List<Post> posts;
    ParseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //Toolbar
        toolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("");

        //Finding View
        tvUserNameDetails = findViewById(R.id.tvUserNameDetails);
        rvProfilePics = findViewById(R.id.rvProfilePics);

        //Populating Views and Vars
        tvUserNameDetails.setText(ParseUser.getCurrentUser().getUsername());
        posts = new ArrayList<>();
        user = ParseUser.getCurrentUser();
        adapter = new ProfileAdapter(getApplicationContext(),posts);

        //Recycler View set up
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),3);
        rvProfilePics.setLayoutManager(gridLayoutManager);
        rvProfilePics.setAdapter(adapter);

        //Get User Posts
        queryPosts();
    }

    protected void queryPosts() {
        // Specify which class to query
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.whereEqualTo(Post.KEY_USER,user);
        query.addDescendingOrder(Post.KEY_CREATED_KEY);
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> listPosts, ParseException e) {
                if(e != null){
                    Log.e(TAG, "error with querying posts", e );
                    return;
                }
                posts.clear();
                posts.addAll(listPosts);
                adapter.notifyDataSetChanged();
                Log.d(TAG, "retrieved posts size + " +listPosts.size());

            }
        });
    }
    //When user hits the back button we simply finish the intent
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }


}