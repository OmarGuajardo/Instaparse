package com.example.instaparse;

import android.content.Context;
import android.content.Intent;
import android.os.Parcel;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.parse.ParseFile;

import org.parceler.Parcels;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    private List<Post> posts;
    private Context context;
    public String TAG = "PostAdapter";

    public PostAdapter(List<Post> posts, Context context) {
        this.posts = posts;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public PostAdapter() {
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvUserName;
        ImageView ivPostPicture;
        ImageView ivProfilePicture;
        TextView tvPostDescription;
        TextView tvTimeStamp;
        TextView tvLikesCounter;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            ivPostPicture = itemView.findViewById(R.id.ivPostPictureFeed);
            ivProfilePicture = itemView.findViewById(R.id.ivProfilePicture);
            tvPostDescription = itemView.findViewById(R.id.tvPostDescription);
            cardView = itemView.findViewById(R.id.cardView);
            tvTimeStamp = itemView.findViewById(R.id.tvTimeStamp);
            tvLikesCounter = itemView.findViewById(R.id.tvLikesCounter);
        }


        public void bind(final Post post) {
            Date date = post.getCreatedAt();
            SimpleDateFormat DateFor = new SimpleDateFormat("dd MMMM yyyy");
            String stringDate = DateFor.format(date);
            //Bind the data to the view elements
            tvUserName.setText(post.getUser().getUsername());
            tvPostDescription.setText(post.getDescription());
            tvTimeStamp.setText(stringDate);
            tvLikesCounter.setText(post.getLikes().toString() + " Likes");

            if (post.getUser().getParseFile("profilePicture") != null) {
                Glide.with(context)
                        .load(post.getUser().getParseFile("profilePicture").getUrl())
                        .circleCrop()
                        .into(ivProfilePicture);
            } else {
                ivProfilePicture.setImageResource(R.drawable.ic_baseline_person_24);

            }
            ParseFile image = post.getImage();
            Log.d(TAG, "image normal + " + image);
            if (image != null) {
                Glide.with(context)
                        .load(post.getImage().getUrl())
                        .centerCrop()
                        .into(ivPostPicture);
            } else {
                Glide.with(context)
                        .load("https://wallpaperaccess.com/full/676563.jpg")
                        .centerCrop()
                        .into(ivPostPicture);
            }

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context, PostDetailActivity.class);
                    i.putExtra("postSelected", Parcels.wrap(post));
                    context.startActivity(i);
                }
            });

            tvUserName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context, ProfileActivity.class);
                    i.putExtra("userSelected", Parcels.wrap(post.getUser()));
                    context.startActivity(i);
                }
            });

        }
    }
}
