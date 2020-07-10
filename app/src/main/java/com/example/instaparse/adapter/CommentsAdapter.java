package com.example.instaparse.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.instaparse.R;
import com.example.instaparse.models.Comment;
import com.parse.ParseException;

import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder> {

    public String TAG = "CommentsAdapter";
    List<Comment> comments;
    Context context;

    public CommentsAdapter(List<Comment> comments, Context context) {
        this.comments = comments;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Comment newComment = comments.get(position);
        try {
            holder.bind(newComment);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.d(TAG, "onBindViewHolder: ",e);
        }


    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCommentUser;
        TextView tvCommentBody;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCommentBody = itemView.findViewById(R.id.tvCommentBody);
            tvCommentUser = itemView.findViewById(R.id.tvCommentUser);
        }

        public void bind(Comment comment) throws ParseException {
            Log.d(TAG, "binding " + comment.getDescription());
            tvCommentBody.setText(comment.getDescription());

            tvCommentUser.setText(comment.getUser().getUsername());
            tvCommentUser.setPaintFlags(tvCommentUser.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        }
    }
}
