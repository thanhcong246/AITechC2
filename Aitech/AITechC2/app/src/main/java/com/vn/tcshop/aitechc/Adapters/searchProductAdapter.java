package com.vn.tcshop.aitechc.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.vn.tcshop.aitechc.Models.Product;
import com.vn.tcshop.aitechc.Products.ProductDetailActivity;
import com.vn.tcshop.aitechc.R;

import java.util.List;

public class searchProductAdapter extends RecyclerView.Adapter<searchProductAdapter.ViewHolder> {
    private final Context context;
    private final List<Product> productList;

    public searchProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_product_recycleview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("username", Context.MODE_PRIVATE);
        String data = sharedPreferences.getString("data", "");
        Log.d("TAG", "data = " + data);
        Product product = productList.get(position);
        holder.bind(product);
        holder.itemView.setOnClickListener(v -> {
            Product selectedProduct = productList.get(position);
            Intent intent = new Intent(context, ProductDetailActivity.class);
            intent.putExtra("product", selectedProduct.getName());
            intent.putExtra("username", data);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView idProduct;
        TextView nameProduct;
        TextView costProduct;
        ImageView imageProduct;
        TextView detailProduct;
        TextView categoryProduct;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            idProduct = itemView.findViewById(R.id.idProduct);
            nameProduct = itemView.findViewById(R.id.nameProduct);
            costProduct = itemView.findViewById(R.id.costProduct);
            imageProduct = itemView.findViewById(R.id.imageProduct);
            detailProduct = itemView.findViewById(R.id.detailProduct);
            categoryProduct = itemView.findViewById(R.id.categoryProduct);
        }

        public void bind(Product product) {
            idProduct.setText(String.valueOf(product.getId()));
            nameProduct.setText(product.getName());
            costProduct.setText(product.getCost());
            Picasso.get().load(product.getImage()).into(imageProduct);
            detailProduct.setText(product.getDetail());
            categoryProduct.setText(product.getCategory());
        }
    }
}