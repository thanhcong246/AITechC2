package com.vn.tcshop.aitechc.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.vn.tcshop.aitechc.Models.Payment;
import com.vn.tcshop.aitechc.R;

import java.util.List;

public class PaymentProductAdapter extends RecyclerView.Adapter<PaymentProductAdapter.ViewHolder> {
    private final Context context;
    private final List<Payment> paymentList;

    public PaymentProductAdapter(Context context, List<Payment> paymentList) {
        this.context = context;
        this.paymentList = paymentList;
    }

    @NonNull
    @Override
    public PaymentProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.layout_payment_recycleview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentProductAdapter.ViewHolder holder, int position) {
        Payment payment = paymentList.get(position);
        holder.bind(payment);

    }

    @Override
    public int getItemCount() {
        return paymentList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView nameProductPayment;
        TextView costProductPayment;
        ImageView imageProductPayment;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameProductPayment = itemView.findViewById(R.id.nameProductPayment);
            costProductPayment = itemView.findViewById(R.id.costProductPayment);
            imageProductPayment = itemView.findViewById(R.id.imageProductPayment);
        }

        public void bind(Payment payment){
            Picasso.get().load(payment.getImage_product_payment()).into(imageProductPayment);
            nameProductPayment.setText(payment.getName_product_payment());
            costProductPayment.setText(payment.getCost_product_payment());
        }
    }
}
