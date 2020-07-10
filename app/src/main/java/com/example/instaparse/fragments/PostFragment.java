package com.example.instaparse.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.instaparse.EndlessRecyclerViewScrollListener;
import com.example.instaparse.models.Post;
import com.example.instaparse.adapter.PostAdapter;
import com.example.instaparse.R;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;


public class PostFragment extends Fragment {

    public String TAG = "PostFragment";
    public int KEY_REQUEST_CODE = 42;
    protected RecyclerView rvPosts;
    protected PostAdapter adapter;
    protected List<Post> listPosts;
    protected SwipeRefreshLayout swipeContainer;
    protected EndlessRecyclerViewScrollListener scrollListener;



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvPosts = view.findViewById(R.id.rvPost);
        listPosts = new ArrayList<>();
        adapter = new PostAdapter(listPosts,getContext());
        rvPosts.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());

        rvPosts.setLayoutManager(linearLayoutManager);
        rvPosts.addItemDecoration(new DividerItemDecoration(rvPosts.getContext(), DividerItemDecoration.VERTICAL));


        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                //TODO: Modify load more data to retrieve past posts
                Log.d(TAG, "onLoadMore: loading....");
                loadMoreData();
            }
        };

        swipeContainer = view.findViewById(R.id.swipeContainer);
        queryPosts();


        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                queryPosts();
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        rvPosts.addOnScrollListener(scrollListener);

    }

    public PostFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        queryPosts();
        rvPosts.addOnScrollListener(scrollListener);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_post, container, false);
    }


    protected void queryPosts() {
        // Specify which class to query
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.include(Post.KEY_COMMENT_LIST);
        query.setLimit(5);
        query.addDescendingOrder(Post.KEY_CREATED_KEY);
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if(e != null){
                    Log.e(TAG, "error with querying posts", e );
                    return;
                }
                listPosts.clear();
                listPosts.addAll(posts);
                adapter.notifyDataSetChanged();
                Log.d(TAG, "retrieved posts ");
                swipeContainer.setRefreshing(false);
            }
        });

    }
    protected void loadMoreData() {
        // Specify which class to query
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.whereLessThan(Post.KEY_CREATED_KEY,listPosts.get(listPosts.size()-1).getCreatedAt());
        query.setLimit(5);
        query.addDescendingOrder(Post.KEY_CREATED_KEY);
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if(e != null){
                    Log.e(TAG, "error with querying posts", e );
                    return;
                }
                listPosts.addAll(posts);
                adapter.notifyDataSetChanged();
                Log.d(TAG, "retrieved posts ");
                swipeContainer.setRefreshing(false);
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, ";ALKSDFJAL;SDKJFA;LSKDJFAS;LDKFJASDFS");

//        Log.d(TAG, "onActivityResult: ");
//        final int position = data.getExtras().getInt("position");
//        listPosts.get(position).fetchInBackground(new GetCallback<Post>() {
//            @Override
//            public void done(Post object, ParseException e) {
//                Log.d(TAG, "done: pos "+ position);
//                Log.d(TAG, "done: obj "+object.getDescription());
//                listPosts.set(position,object);
//                adapter.notifyDataSetChanged();
//
//            }
//        });
    }

}