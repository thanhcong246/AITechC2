package com.vn.tcshop.aitechc.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vn.tcshop.aitechc.Models.CartHistory;
import com.vn.tcshop.aitechc.Payments.PaymentHistoryActivity;
import com.vn.tcshop.aitechc.R;

import java.util.List;

public class CartHistoryListAdapter extends RecyclerView.Adapter<CartHistoryListAdapter.ViewHolder> {
    private final Context context;
    private final List<CartHistory> cartHistoryList;

    public CartHistoryListAdapter(Context context, List<CartHistory> cartHistoryList) {
        this.context = context;
        this.cartHistoryList = cartHistoryList;
    }

    @NonNull
    @Override
    public CartHistoryListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_product_cart_history_recycleview, parent, false);
        return new ViewHolder(view, cartHistoryList);
    }


    @Override
    public void onBindViewHolder(@NonNull CartHistoryListAdapter.ViewHolder holder, int position) {
        CartHistory cartHistory = cartHistoryList.get(position);
        holder.bind(cartHistory);
    }

    @Override
    public int getItemCount() {
        return cartHistoryList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView code_cart;
        TextView cart_date;
        TextView cart_number;
        TextView cart_cost;
        TextView cart_status;
        TextView cart_payment;
        Button btnPaymentDetail;
        List<CartHistory> cartHistoryList;

        public ViewHolder(@NonNull View itemView, List<CartHistory> cartHistoryList) {
            super(itemView);
            code_cart = itemView.findViewById(R.id.code_cart);
            cart_date = itemView.findViewById(R.id.cart_date);
            cart_number = itemView.findViewById(R.id.cart_number);
            cart_cost = itemView.findViewById(R.id.cart_cost);
            cart_status = itemView.findViewById(R.id.cart_status);
            cart_payment = itemView.findViewById(R.id.cart_payment);
            btnPaymentDetail = itemView.findViewById(R.id.btnCartHistoryPaymentDetail);
            this.cartHistoryList = cartHistoryList;
            btnPaymentDetail.setOnClickListener(v -> {
                Intent intent = new Intent(itemView.getContext(), PaymentHistoryActivity.class);
                String codeCart = cartHistoryList.get(getAdapterPosition()).getCode_cart();
                intent.putExtra("code_cart", codeCart);
                itemView.getContext().startActivity(intent);
            });

        }

        public void bind(CartHistory cartHistory){
            code_cart.setText(cartHistory.getCode_cart());
            cart_date.setText(cartHistory.getCart_date());
            cart_number.setText(String.valueOf(cartHistory.getCart_number()));
            cart_cost.setText(String.valueOf(cartHistory.getCart_cost()));
            cart_payment.setText(cartHistory.getCart_payment());

            int status = cartHistory.getCart_status();
            String cartStatus = "";
            if (status == 0) {
                cartStatus = "Đã Xác Nhận";
            } else {
                // add more cases for other status values here if needed
            }
            cart_status.setText(cartStatus);
        }
    }
}