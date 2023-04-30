package com.vn.tcshop.aitechc.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.vn.tcshop.aitechc.ChatBlog.BlogChatActivity;
import com.vn.tcshop.aitechc.ChatBlog.chatwindoActivity;
import com.vn.tcshop.aitechc.Models.UsersBlogChat;
import com.vn.tcshop.aitechc.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserBlogAdpter extends RecyclerView.Adapter<UserBlogAdpter.viewholder> {
    Context blogChatActivity;
    ArrayList<UsersBlogChat> usersArrayList;

    public UserBlogAdpter(BlogChatActivity blogChatActivity, ArrayList<UsersBlogChat> usersArrayList) {
        this.blogChatActivity = blogChatActivity;
        this.usersArrayList = usersArrayList;
    }

    @NonNull
    @Override
    public UserBlogAdpter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(blogChatActivity).inflate(R.layout.user_item, parent, false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserBlogAdpter.viewholder holder, int position) {

        UsersBlogChat users = usersArrayList.get(position);
        holder.username.setText(users.getUserName());
        Picasso.get().load(users.getProfilepic()).into(holder.userimg);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(blogChatActivity, chatwindoActivity.class);
                intent.putExtra("nameeee", users.getUserName());
                intent.putExtra("reciverImg", users.getProfilepic());
                intent.putExtra("uid", users.getUserId());
                blogChatActivity.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return usersArrayList.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        CircleImageView userimg;
        TextView username;

        public viewholder(@NonNull View itemView) {
            super(itemView);
            userimg = itemView.findViewById(R.id.userimg);
            username = itemView.findViewById(R.id.username);
        }
    }
}
