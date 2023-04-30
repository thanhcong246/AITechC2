package com.vn.tcshop.aitechc.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;
import com.vn.tcshop.aitechc.Api.Api;
import com.vn.tcshop.aitechc.Models.CartDetail;
import com.vn.tcshop.aitechc.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartDetailAdapter extends RecyclerView.Adapter<CartDetailAdapter.ViewHolder> {
    private final Context context;
    private final List<CartDetail> cartDetailList;

    public CartDetailAdapter(Context context, List<CartDetail> cartDetailList) {
        this.context = context;
        this.cartDetailList = cartDetailList;
    }

    @NonNull
    @Override
    public CartDetailAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.layout_product_cart_recycleview, parent, false);
        return new ViewHolder(view, context);
    }


    @Override
    public void onBindViewHolder(@NonNull CartDetailAdapter.ViewHolder holder, int position) {
        CartDetail cartDetail = cartDetailList.get(position);
        holder.bind(cartDetail);
    }

    @Override
    public int getItemCount() {
        return cartDetailList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private final Context context;
        ImageView imageProductCart;
        TextView nameProductCart;
        TextView costProductCart;
        TextView nameUserCart;
        TextView codeCart;
        TextView idCartDetail;
        Button btnRemoveCartById;

        public ViewHolder(@NonNull View itemView, Context context) {
            super(itemView);

            this.context = context;
            imageProductCart = itemView.findViewById(R.id.imageProductCart);
            nameProductCart = itemView.findViewById(R.id.nameProductCart);
            costProductCart = itemView.findViewById(R.id.costProductCart);
            nameUserCart = itemView.findViewById(R.id.nameUserCart);
            codeCart = itemView.findViewById(R.id.code_cart_detail);
            idCartDetail = itemView.findViewById(R.id.id_cart_detail);
            btnRemoveCartById = itemView.findViewById(R.id.btnDeleteCartById);
        }

        @SuppressLint("SetTextI18n")
        public void bind(CartDetail cartDetail) {
            Picasso.get().load(cartDetail.getImage_product()).into(imageProductCart);
            nameProductCart.setText(cartDetail.getName_product());
            costProductCart.setText(cartDetail.getCost_product());
            nameUserCart.setText(cartDetail.getName_user());
            codeCart.setText(Integer.toString(cartDetail.getCode_cart()));
            idCartDetail.setText(Integer.toString(cartDetail.getId_cart_detail()));
            int idCart = cartDetail.getId_cart_detail();
            btnRemoveCartById.setOnClickListener(v -> {
                removeCartById(idCart);
            });
        }

        private void removeCartById(int idCartDetail){
            String url = Api.URL_REMOVE_CART_BY_ID;
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    response -> {
                        // Xử lý kết quả trả về khi thành công
                        Toast.makeText(context, "Xóa Sản Phẩm thành công", Toast.LENGTH_SHORT).show();
                        ((Activity) context).finish();
                        context.startActivity(((Activity) context).getIntent());
                    },
                    error -> {
                        // Xử lý kết quả trả về khi có lỗi xảy ra
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("remove_cart_detail_by_id", String.valueOf(idCartDetail));
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(stringRequest);

        }
    }
}
