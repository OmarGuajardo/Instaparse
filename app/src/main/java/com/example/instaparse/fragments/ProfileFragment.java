package com.example.instaparse.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.example.instaparse.EndlessRecyclerViewScrollListener;
import com.example.instaparse.adapter.ProfileAdapter;
import com.example.instaparse.R;
import com.example.instaparse.models.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {

    //Views
    public String TAG = "PostFragment";
    protected RecyclerView rvPosts;
    protected ProfileAdapter adapter;
    protected List<Post> posts;
    protected ImageView ivProfilePictureDetails;
    protected TextView tvUserNameDetails;
    protected RecyclerView rvProfilePics;

    //Vars necessary for swiping actions
    protected SwipeRefreshLayout swipeContainer;
    protected EndlessRecyclerViewScrollListener scrollListener;
    File photoFile;

    //Vars necessary for picture taking
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 42;
    public String photoFileName = "photo.jpg";
    public ParseUser currentUser;




    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Setting the Global User
        currentUser = ParseUser.getCurrentUser();

        //View Setting,Finding and Vars Setting
        ivProfilePictureDetails = view.findViewById(R.id.ivProfilePictureDetails);
        tvUserNameDetails = view.findViewById(R.id.tvUserNameDetails);
        rvProfilePics = view.findViewById(R.id.rvProfilePics);
        tvUserNameDetails.setText(currentUser.getUsername());
        posts = new ArrayList<>();
        TextView tvBio = view.findViewById(R.id.tvBio);
        tvBio.setText("Hi welcome to my page \uD83D\uDC2A\uD83E\uDD5F\uD83C\uDF71☺\uD83D\uDE02");

        //RecyclerView Setup
        adapter = new ProfileAdapter(getContext(),posts);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),3);
        rvProfilePics.setLayoutManager(gridLayoutManager);
        rvProfilePics.setAdapter(adapter);

        //Fetching posts
        queryPosts();

        ivProfilePictureDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchCamera();
            }
        });

        if(currentUser.getParseFile("profilePicture") != null){
            Glide.with(getContext())
                    .load(currentUser.getParseFile("profilePicture").getUrl())
                    .circleCrop()
                    .into(ivProfilePictureDetails);
        }

    }

    public ProfileFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }


    protected void queryPosts() {
        // Specify which class to query
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.whereEqualTo(Post.KEY_USER,ParseUser.getCurrentUser());
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
            }
        });
    }



    //Will make a new object of Post and set the data accordingly
    //Then will save it to the DB
    private void saveProfilePicture(File photoFile) {
        //TODO: put picture received into a new Parse Class Profile Picture with a pointer to the current User
        currentUser.put("profilePicture",new ParseFile(photoFile));
        currentUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                Toast.makeText(getContext(), "Profile Picture Saved", Toast.LENGTH_SHORT).show();
                Glide.with(getContext())
                        .load(currentUser.getParseFile("profilePicture").getUrl())
                        .circleCrop()
                        .into(ivProfilePictureDetails);
            }
        });

    }

    //This methods calls and intent to launch the camera, take the picture and save the picture as a file that
    //we can then use to put in the ImageView
    private void launchCamera() {
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Create a File reference for future access
        photoFile = getPhotoFileUri(photoFileName);

        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        Uri fileProvider = FileProvider.getUriForFile(getContext(), "com.codepath.fileprovider", photoFile);
        Log.d(TAG, "fileProvider " + fileProvider);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);
        Log.d(TAG, "packagemanager " + intent.resolveActivity(getContext().getPackageManager()));

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }

    }
    private File getPhotoFileUri(String fileName) {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(TAG, "failed to create directory");
        }

        // Return the file target for the photo based on filename
        return new File(mediaStorageDir.getPath() + File.separator + fileName);

    }
    //In here is where we check if the picture we took was successfully taken so we can then
    //display it in the ImageView
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Toast.makeText(getContext(), "Picture taken successfully", Toast.LENGTH_SHORT).show();
                // by this point we have the camera photo on disk
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                // RESIZE BITMAP, see section below
                // Load the taken image into a preview
                saveProfilePicture(photoFile);
            } else { // Result was a failure
                Toast.makeText(getContext(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
