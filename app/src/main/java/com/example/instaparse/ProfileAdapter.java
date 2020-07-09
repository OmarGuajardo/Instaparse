package com.example.instaparse;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.instaparse.models.Post;
import com.parse.ParseFile;

import java.util.List;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ViewHolder> {

    List<Post> posts;
    Context context;
    public ProfileAdapter(Context context, List<Post> posts) {
        this.posts = posts;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post_profile,parent,false);
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

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivItemPostProfile;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivItemPostProfile = itemView.findViewById(R.id.ivItemPostProfile);
        }

        public void bind(Post post) {
            ParseFile image = post.getImage();
            if(image != null) {
                Glide.with(context)
                        .load(post.getImage().getUrl())
                        .centerCrop()
                        .into(ivItemPostProfile);
                return;
            }
            Glide.with(context)
                    .load("https://eska.com/wp-content/uploads/2019/09/eska-mono-black-voor.jpg")
                    .centerCrop()
                    .into(ivItemPostProfile);
        }
    }
}
