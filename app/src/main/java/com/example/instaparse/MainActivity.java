package com.example.instaparse;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

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
import com.google.android.material.bottomnavigation.BottomNavigationView;
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
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 42;
    public String photoFileName = "photo.jpg";
    File photoFile;

    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


//        queryPosts();

        //Will open the camera
        binding.btnTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //launch camera
                launchCamera();
            }
        });
        //Will handle the submission of the post
        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String description = binding.etDescription.getText().toString();
                if(!description.isEmpty()){
                    //Allow user to make post
                    ParseUser currentUser = ParseUser.getCurrentUser();
                    savePost(description,currentUser,photoFile);
                    return;
                }
                if(photoFile == null || binding.ivPostPicture.getDrawable() == null){
                Toast.makeText(MainActivity.this, "You must submit a picture with your post", Toast.LENGTH_SHORT).show();
                return;

                }
                Toast.makeText(MainActivity.this, "Description must not be empty", Toast.LENGTH_SHORT).show();
            }
        });

        binding.bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment;
                switch (item.getItemId()) {
                    case R.id.action_home:
                        Toast.makeText(MainActivity.this, "Clicked Home", Toast.LENGTH_SHORT).show();
                        // do something here
                        return true;
                    case R.id.action_compose:
                        // do something here
                        Toast.makeText(MainActivity.this, "Clicked Compose", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.action_profile:
                        // do something here
                        Toast.makeText(MainActivity.this, "Clicked Profile", Toast.LENGTH_SHORT).show();
                        return true;
                    default: return true;
                }
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
        Uri fileProvider = FileProvider.getUriForFile(MainActivity.this, "com.codepath.fileprovider", photoFile);
        Log.d(TAG, "fileProvider " + fileProvider);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);
        Log.d(TAG, "packagemanager " + intent.resolveActivity(getPackageManager()));

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }

    }

    //In here is where we check if the picture we took was successfully taken so we can then
    //display it in the ImageView
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // by this point we have the camera photo on disk
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                // RESIZE BITMAP, see section below
                // Load the taken image into a preview
                binding.ivPostPicture.setImageBitmap(takenImage);
            } else { // Result was a failure
                Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private File getPhotoFileUri(String fileName) {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(TAG, "failed to create directory");
        }

        // Return the file target for the photo based on filename
        return new File(mediaStorageDir.getPath() + File.separator + fileName);

    }

    //Will make a new object of Post and set the data accordingly
    //Then will save it to the DB
    private void savePost(String description, ParseUser currentUser,File photoFile) {
        Post post = new Post();
        post.setDescription(description);
//        post.setImage();
        post.setUser(currentUser);
        post.setImage(new ParseFile(photoFile));
        post.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e != null){
                    Log.e(TAG, "Something when wrong when postin ",e );
                    return;
                }
                Log.d(TAG, "Post Saved succesful");
                binding.etDescription.setText("");
                binding.ivPostPicture.setImageResource(0);
            }
        });
    }

    private void queryPosts() {
        // Specify which class to query
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if(e != null){
                    Log.e(TAG, "error with querying posts", e );
                    return;
                }
                for(Post post: posts){
                    Log.d(TAG, "this is the post desc " + post.getDescription() + " username " +post.getUser().getUsername());
                }
            }
        });
    }
}