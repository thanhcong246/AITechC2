package com.vn.tcshop.aitechc.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vn.tcshop.aitechc.Models.ProductComment;
import com.vn.tcshop.aitechc.R;

import java.util.List;

public class CommentProductAdapter extends RecyclerView.Adapter<CommentProductAdapter.ViewHolder>{
    private final Context context;
    private final List<ProductComment> commentList;

    public CommentProductAdapter(Context context, List<ProductComment> commentList) {
        this.context = context;
        this.commentList = commentList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_comment_recycleview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProductComment comment = commentList.get(position);
        holder.bind(comment);
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameUser;
        TextView nameProduct;
        TextView commentProduct;
        RatingBar rate;
        TextView date;

        public ViewHolder(View itemView){
            super(itemView);
            nameUser = itemView.findViewById(R.id.nameComment);
            nameProduct = itemView.findViewById(R.id.productComment);
            commentProduct = itemView.findViewById(R.id.textComment);
            rate = itemView.findViewById(R.id.ratingComment);
            date = itemView.findViewById(R.id.dateComment);
        }

        public void bind(ProductComment comment){
            nameUser.setText(comment.getUserName());
            nameProduct.setText(comment.getProductName());
            commentProduct.setText(comment.getComment());
            rate.setRating(comment.getRate());
            date.setText(comment.getDate());
        }
    }
}
