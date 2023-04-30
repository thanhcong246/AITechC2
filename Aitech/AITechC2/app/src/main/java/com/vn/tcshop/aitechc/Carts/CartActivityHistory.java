package com.vn.tcshop.aitechc.Carts;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.vn.tcshop.aitechc.Adapters.CartHistoryListAdapter;
import com.vn.tcshop.aitechc.Api.Api;
import com.vn.tcshop.aitechc.Models.CartHistory;
import com.vn.tcshop.aitechc.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartActivityHistory extends AppCompatActivity {
    private RecyclerView recyclerView;
    private Button btnPaymentDetail;
    private TextView back_history;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_history);

        int id_user = getIntent().getIntExtra("id_user", 0);
        Log.d("id_user", String.valueOf(id_user));

        recyclerView = findViewById(R.id.recycleviewProductCartHistory);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        getCartHistoryData(id_user);

        back_history = findViewById(R.id.back_history);
        back_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void getCartHistoryData(int id_user) {
        @SuppressLint("NotifyDataSetChanged") StringRequest stringRequest = new StringRequest(Request.Method.POST, Api.URL_GET_CART_HISTORY,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        boolean error = jsonObject.getBoolean("error");
                        String message = jsonObject.getString("message");
                        List<CartHistory> cartHistoryList = new ArrayList<>();
                        JSONArray cartHistoryArray = jsonObject.getJSONArray("cart_history");
                        if (!error) {
                            for (int i = 0; i < cartHistoryArray.length(); i++) {
                                JSONObject cartHistoryObject = cartHistoryArray.getJSONObject(i);
                                String codeCart = cartHistoryObject.getString("code_cart");
                                int cartStatus = cartHistoryObject.getInt("cart_status");
                                String cartDate = cartHistoryObject.getString("cart_date");
                                int cartNumber = cartHistoryObject.getInt("cart_number");
                                double cartCost = cartHistoryObject.getDouble("cart_cost");
                                String cartPayment = cartHistoryObject.getString("cart_payment");
                                CartHistory cartHistory = new CartHistory(codeCart, cartStatus, cartDate, cartNumber, cartCost, cartPayment);
                                cartHistoryList.add(cartHistory);
                            }
                            CartHistoryListAdapter cartHistoryListAdapter = new CartHistoryListAdapter(this, cartHistoryList);
                            recyclerView.setAdapter(cartHistoryListAdapter);
                            cartHistoryListAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(CartActivityHistory.this, message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(CartActivityHistory.this, "JSON error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(CartActivityHistory.this, "Volley error: " + error.getMessage(), Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id_user", String.valueOf(id_user));
                return params;
            }
        };

        Volley.newRequestQueue(this).add(stringRequest);
    }

}