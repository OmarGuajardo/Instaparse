package com.example.instaparse;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.parse.ParseFile;

import org.w3c.dom.Text;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    private List<Post> posts;
    private Context context;

    public PostAdapter(List<Post> posts, Context context) {
        this.posts = posts;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post,parent,false);
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

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvUserName;
        ImageView ivPostPicture;
        TextView tvPostDescription;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            ivPostPicture = itemView.findViewById(R.id.ivPostPicture);
            tvPostDescription = itemView.findViewById(R.id.tvPostDescription);
        }


        public void bind(Post post) {

            //Bind the data to the view elements
            tvUserName.setText(post.getUser().getUsername());
            tvPostDescription.setText(post.getDescription());
            ParseFile image = post.getImage();
            if(image != null){
            Glide.with(context)
                    .load(post.getImage().getUrl())
                    .into(ivPostPicture);
            }
        }
    }
}
