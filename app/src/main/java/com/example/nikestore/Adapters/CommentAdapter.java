package com.example.nikestore.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nikestore.Model.Comment;
import com.example.nikestore.R;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.item> {

    Context c;
    List<Comment> commentList;
    boolean showAll = false;

    public CommentAdapter(Context c, List<Comment> commentList) {
        this.c = c;
        this.commentList = commentList;
    }

    public CommentAdapter(Context c, List<Comment> commentList , boolean showAll) {
        this.c = c;
        this.commentList = commentList;
        this.showAll = showAll;
    }

    @NonNull
    @Override
    public item onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        if (showAll == false)
            view = LayoutInflater.from(c).inflate(R.layout.item_gride_comment , viewGroup , false);
        else
            view = LayoutInflater.from(c).inflate(R.layout.item_comment , viewGroup , false);
        return new item(view);
    }

    @Override
    public void onBindViewHolder(@NonNull item item, int position) {
        item.bindComments(commentList.get(position));
    }

    @Override
    public int getItemCount() {
        if (commentList.size() > 3 && !showAll)
            return 3;
        else
            return commentList.size();
    }

    public class item extends RecyclerView.ViewHolder{
        TextView txt_comment_title , txt_comment_date , txt_comment_author , txt_comment_content;
        public item(@NonNull View itemView) {
            super(itemView);
            txt_comment_title = itemView.findViewById(R.id.txt_comment_title);
            txt_comment_date = itemView.findViewById(R.id.txt_comment_date);
            txt_comment_author = itemView.findViewById(R.id.txt_comment_author);
            txt_comment_content = itemView.findViewById(R.id.txt_comment_content);
        }

        public void bindComments(Comment comment){
            txt_comment_title.setText(comment.getTitle());
            txt_comment_date.setText(comment.getDate());
            txt_comment_author.setText(comment.getAuthor().getEmail());
            txt_comment_content.setText(comment.getContent());
        }

    }
}
