package com.vn.tcshop.aitechc.Payments;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.icu.text.DecimalFormat;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.vn.tcshop.aitechc.Adapters.PaymentProductAdapter;
import com.vn.tcshop.aitechc.Api.Api;
import com.vn.tcshop.aitechc.Models.Payment;
import com.vn.tcshop.aitechc.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PaymentHistoryActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private Button btnConfirmPayment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_history);

        String code_cart = getIntent().getStringExtra("code_cart");
        Log.d("code_cart_payment", code_cart);
        getBillPayment(code_cart);

        recyclerView = findViewById(R.id.recycleViewPayment);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        getProductPayment(code_cart);
        btnConfirmPayment = findViewById(R.id.btnConfirmPayment);
        btnConfirmPayment.setOnClickListener(v -> {
            finish();
        });
    }

    private void getBillPayment(String code_cart) {
        String url = Api.URL_GET_BILL_PAYMENT;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {

                    try {
                        JSONObject jsonObject = new JSONObject(response); // jsonString là chuỗi JSON nhận được từ server
                        JSONArray cartShippingPaymentArray = jsonObject.getJSONArray("cart_shipping_payment");
                        for (int i = 0; i < cartShippingPaymentArray.length(); i++) {
                            JSONObject cartShippingPaymentObject = cartShippingPaymentArray.getJSONObject(i);
                            String name_user = cartShippingPaymentObject.getString("name_user_payment");
                            String address = cartShippingPaymentObject.getString("address_payment");
                            String phone = cartShippingPaymentObject.getString("phone_payment");
                            int cart_number = cartShippingPaymentObject.getInt("cart_number_payment");
                            double cart_cost = cartShippingPaymentObject.getDouble("cart_cost_payment");
                            int cart_status = cartShippingPaymentObject.getInt("cart_status_payment");
                            String code_cart1 = cartShippingPaymentObject.getString("code_cart_payment");
                            String cart_payment = cartShippingPaymentObject.getString("cart_payment");

                            DecimalFormat cart_cost_format = new DecimalFormat("#,##0.00");
                            // Đặt dữ liệu lấy về vào TextView
                            TextView tvNameUser = findViewById(R.id.nameUserPayment);
                            tvNameUser.setText(name_user);

                            TextView tvAddress = findViewById(R.id.addressUserPayment);
                            tvAddress.setText(address);

                            TextView tvPhone = findViewById(R.id.phoneUserPayment);
                            tvPhone.setText(phone);

                            TextView tvCartNumber = findViewById(R.id.numberProductPayment);
                            tvCartNumber.setText(String.valueOf(cart_number));

                            TextView tvCartCost = findViewById(R.id.totalCostPayment);
                            tvCartCost.setText(cart_cost_format.format(cart_cost) + " VND");

                            TextView tvCartStatus = findViewById(R.id.statusPayment);
                            if (cart_status==0){
                                tvCartStatus.setText("Đã Xác Nhận");
                            }
                            TextView tvCodeCart = findViewById(R.id.codeCartPayment);
                            tvCodeCart.setText(code_cart1);

                            TextView tvCartPayment = findViewById(R.id.cartPayments);
                            tvCartPayment.setText(cart_payment);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_LONG).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("code_cart_payment", code_cart);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void getProductPayment(String code_cart){
        String url = Api.URL_GET_PRODUCT_PAYMENT;
        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean error = jsonResponse.getBoolean("error");
                        String message = jsonResponse.getString("message");
                        JSONArray paymentsArray = jsonResponse.getJSONArray("cart_product_payment");

                        // Parse data
                        List<Payment> paymentsList = new ArrayList<>();
                        for (int i = 0; i < paymentsArray.length(); i++) {
                            JSONObject paymentJson = paymentsArray.getJSONObject(i);
                            Payment payment = new Payment(
                                    paymentJson.getString("name_product"),
                                    paymentJson.getString("cost_product"),
                                    paymentJson.getString("image_product")
                            );
                            paymentsList.add(payment);
                        }

                        // Update RecyclerView adapter
                        PaymentProductAdapter adapter = new PaymentProductAdapter(this, paymentsList);
                        recyclerView.setAdapter(adapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(this, "Error retrieving data", Toast.LENGTH_SHORT).show();
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("code_cart_payment", String.valueOf(code_cart));
                return params;
            }
        };

// Add the request to the request queue
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }
}