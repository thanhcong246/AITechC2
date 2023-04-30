package com.vn.tcshop.aitechc.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.vn.tcshop.aitechc.Models.CartDetail;
import com.vn.tcshop.aitechc.R;

import java.util.List;

public class CartShippingAdapter extends RecyclerView.Adapter<CartShippingAdapter.ViewHolder> {
    private final Context context;
    private final List<CartDetail> cartDetailList;

    public CartShippingAdapter(Context context, List<CartDetail> cartDetailList) {
        this.context = context;
        this.cartDetailList = cartDetailList;
    }

    @NonNull
    @Override
    public CartShippingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.layout_product_cart_shipping_recycleview, parent, false);
        return new ViewHolder(view, context);
    }


    @Override
    public void onBindViewHolder(@NonNull CartShippingAdapter.ViewHolder holder, int position) {
        CartDetail cartDetail = cartDetailList.get(position);
        holder.bind(cartDetail);
    }

    @Override
    public int getItemCount() {
        return cartDetailList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final Context context;
        ImageView imageProductCart;
        TextView nameProductCart;
        TextView costProductCart;
        TextView nameUserCart;
        TextView codeCart;
        TextView idCartDetail;

        public ViewHolder(@NonNull View itemView, Context context) {
            super(itemView);

            this.context = context;
            imageProductCart = itemView.findViewById(R.id.imageProductCart);
            nameProductCart = itemView.findViewById(R.id.nameProductCart);
            costProductCart = itemView.findViewById(R.id.costProductCart);
            nameUserCart = itemView.findViewById(R.id.nameUserCart);
            codeCart = itemView.findViewById(R.id.code_cart_detail);
            idCartDetail = itemView.findViewById(R.id.id_cart_detail);
        }

        @SuppressLint("SetTextI18n")
        public void bind(CartDetail cartDetail) {
            Picasso.get().load(cartDetail.getImage_product()).into(imageProductCart);
            nameProductCart.setText(cartDetail.getName_product());
            costProductCart.setText(cartDetail.getCost_product());
            nameUserCart.setText(cartDetail.getName_user());
            codeCart.setText(Integer.toString(cartDetail.getCode_cart()));
            idCartDetail.setText(Integer.toString(cartDetail.getId_cart_detail()));
        }
    }
}
