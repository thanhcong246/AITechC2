package com.vn.tcshop.aitechc.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.vn.tcshop.aitechc.Models.ChatImgBot;
import com.vn.tcshop.aitechc.R;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.MyViewHolder> {
    List<ChatImgBot> messageList;

    public ImageAdapter(List<ChatImgBot> messageList) {
        this.messageList = messageList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View chatView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatimg_item,null);
        MyViewHolder myViewHolder = new MyViewHolder(chatView);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ChatImgBot message = messageList.get(position);
        if (message.getSentBy().equals(ChatImgBot.SENT_BY_ME)){
            holder.leftChatView.setVisibility(View.GONE);
            holder.rightChatView.setVisibility(View.VISIBLE);
            holder.rightTextView.setText(message.getMessage());
        }else if (message.getSentBy().equals(ChatImgBot.SENT_BY_BOTT)){
            holder.leftTextView.setVisibility(View.VISIBLE);
            holder.rightChatView.setVisibility(View.GONE);
            holder.leftTextView.setText(message.getMessage());
        }else{
            holder.rightChatView.setVisibility(View.GONE);
            holder.leftChatView.setVisibility(View.VISIBLE);
            holder.leftTextView.setVisibility(View.GONE);
            Picasso.get().load(message.getMessage()).into(holder.leftTextViewImg);
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout leftChatView, rightChatView;
        TextView rightTextView, leftTextView;
        ImageView leftTextViewImg;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            leftChatView = itemView.findViewById(R.id.left_chat_view_c);
            rightChatView = itemView.findViewById(R.id.right_chat_view_c);
            leftTextView =  itemView.findViewById(R.id.left_chat_text_view_c);
            rightTextView = itemView.findViewById(R.id.right_chat_text_view_c);
            leftTextViewImg = itemView.findViewById(R.id.left_chat_text_view_img);
        }
    }
}