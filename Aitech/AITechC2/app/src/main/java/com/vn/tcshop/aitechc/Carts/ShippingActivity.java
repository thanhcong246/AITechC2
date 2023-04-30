package com.vn.tcshop.aitechc.Carts;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.vn.tcshop.aitechc.Adapters.CartShippingAdapter;
import com.vn.tcshop.aitechc.Api.Api;
import com.vn.tcshop.aitechc.Models.CartDetail;
import com.vn.tcshop.aitechc.Payments.PaymentActivity;
import com.vn.tcshop.aitechc.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShippingActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TextView name_user;
    private TextView address_user;
    private TextView phone_user;
    private TextView note_user;
    private Button btnAddAddress;
    private TextView back_shipping;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipping);

        back_shipping = findViewById(R.id.back_shipping);

        Intent intent = getIntent();
        String nameUser = intent.getStringExtra("name_user");
        SharedPreferences sharedPreferences = getSharedPreferences("code_cart", Context.MODE_PRIVATE);
        String code_cart = sharedPreferences.getString("code_cart", "");
        Double cart_cost = (double) sharedPreferences.getFloat("cart_cost", 0);

        SharedPreferences sharedPreferences1 = getSharedPreferences("shipping", Context.MODE_PRIVATE);
        int id_shipping = sharedPreferences1.getInt("id_shipping", 1);
        Log.e("idshiping", String.valueOf(id_shipping));

        SharedPreferences sharedPreferences2 = getSharedPreferences("numberProduct", Context.MODE_PRIVATE);
        int cart_number = sharedPreferences2.getInt("number_product", 0);

        recyclerView = findViewById(R.id.recycleviewProductCartShipping);
        getCartDetail(nameUser);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        if (nameUser != null) {
            getShippingAddress(nameUser);
        }

        //Lấy thông tin người dùng
        name_user = findViewById(R.id.name_user);
        address_user = findViewById(R.id.address_user);
        phone_user = findViewById(R.id.phone_user);
        note_user = findViewById(R.id.note_user);

        btnAddAddress = findViewById(R.id.btnAddAddress);
        btnAddAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String namee_user = name_user.getText().toString();
                String phonee_user = phone_user.getText().toString();
                String addresss_user = address_user.getText().toString();
                String notee_user = note_user.getText().toString();
                if (TextUtils.isEmpty(namee_user) || TextUtils.isEmpty(addresss_user) || TextUtils.isEmpty(phonee_user) || TextUtils.isEmpty(notee_user)) {
                    Toast.makeText(getApplicationContext(), "Thông tin còn để trống", Toast.LENGTH_SHORT).show();
                    return;
                }
                setAddressShipping(nameUser, namee_user, phonee_user, addresss_user, notee_user);

            }
        });

        Button btnPayment = findViewById(R.id.btnDirectPayment);
        btnPayment.setOnClickListener(v -> {
            String namee_user = name_user.getText().toString();
            String phonee_user = phone_user.getText().toString();
            String addresss_user = address_user.getText().toString();
            String notee_user = note_user.getText().toString();
            if (TextUtils.isEmpty(namee_user) || TextUtils.isEmpty(addresss_user) || TextUtils.isEmpty(phonee_user) || TextUtils.isEmpty(notee_user)) {
                Toast.makeText(getApplicationContext(), "Bạn chưa lưu thông tin trước khi thanh toán", Toast.LENGTH_SHORT).show();
                return;
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Xác nhận thanh toán");
            builder.setMessage("Bạn có chắc muốn thanh toán?");
            builder.setPositiveButton("Đồng ý", (dialog, which) -> setDirectPayment(name_user.getText().toString(), code_cart, cart_number, cart_cost, String.valueOf(id_shipping)));
            builder.setNegativeButton("Hủy bỏ", null);
            builder.show();

        });


        back_shipping();
    }

    private void back_shipping() {
        back_shipping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void getShippingAddress(String nameUser) {
        // Khởi tạo RequestQueue để thực hiện yêu cầu mạng
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        // Tạo một StringRequest để lấy dữ liệu từ API
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api.URL_GET_ADDRESS, response -> {
            try {
                // Chuyển đổi dữ liệu JSON thành một đối tượng JSON
                JSONObject jsonObject = new JSONObject(response);
                if (!jsonObject.getBoolean("error")) {
                    Object cartShipping = jsonObject.get("cart_shipping");
                    if (cartShipping instanceof JSONObject) {
                        JSONObject shippingObj = (JSONObject) cartShipping;
                        // Lấy các giá trị của đối tượng shipping
                        int id_shipping = shippingObj.getInt("id_shipping");

                        String name = shippingObj.getString("name");
                        String phone = shippingObj.getString("phone");
                        String address = shippingObj.getString("address");
                        String note = shippingObj.getString("note");


                        // Đưa các giá trị vào các EditText
                        name_user.setText(name);
                        address_user.setText(address);
                        phone_user.setText(phone);
                        note_user.setText(note);

                    } else if (cartShipping instanceof JSONArray) {
                        JSONArray shippingArray = (JSONArray) cartShipping;
                        // Lấy đối tượng shipping đầu tiên trong mảng
                        JSONObject shippingObj = shippingArray.getJSONObject(0);
                        // Lấy các giá trị của đối tượng shipping
                        int id_shipping = shippingObj.getInt("id_shipping");
                        String name = shippingObj.getString("name");
                        String phone = shippingObj.getString("phone");
                        String address = shippingObj.getString("address");
                        String note = shippingObj.getString("note");

                        // Đưa các giá trị vào các EditText
                        name_user.setText(name);
                        address_user.setText(address);
                        phone_user.setText(phone);
                        note_user.setText(note);
                    }
                } else {
                    // Hiển thị thông báo lỗi
                    String message = jsonObject.getString("message");
                    Toast.makeText(getApplicationContext(), "Error: " + message, Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            // Xử lý lỗi
            Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
        }) {
            // Thêm tham số vào yêu cầu POST
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("cart_shipping", nameUser);
                return params;
            }
        };

        // Thêm StringRequest vào RequestQueue để thực hiện yêu cầu mạng
        requestQueue.add(stringRequest);
    }

    private void setAddressShipping(String nameUser, String name_user, String phone_user, String address_user, String note_user) {
        String url = Api.URL_SET_ADDRESS;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    // Xử lý kết quả trả về khi thành công
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String idShipping = jsonObject.getString("id_shipping");
                        Log.d("id_shipping", idShipping);
                        //   Toast.makeText(getApplicationContext(), "Thêm Thông tin thanh toán thành công. Id_shipping = " + idShipping, Toast.LENGTH_SHORT).show();
                        // Sau khi đặt địa chỉ mới thành công, cập nhật thông tin địa chỉ giao hàng
                        SharedPreferences sharedPreferences = getSharedPreferences("shipping", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt("id_shipping", Integer.parseInt(idShipping));
                        editor.apply();
                        getShippingAddress(nameUser);
                        finish();
                        startActivity(getIntent());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    // Xử lý kết quả trả về khi có lỗi xảy ra
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("name", nameUser);
                params.put("name_user", name_user);
                params.put("phone_user", phone_user);
                params.put("address_user", address_user);
                params.put("note_user", note_user);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    //getCartDetail
    private void getCartDetail(String nameUser) {
        // Khởi tạo một RequestQueue để thực hiện các yêu cầu mạng
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        // Định nghĩa URL của API tìm kiếm sản phẩm
        String url = Api.URL_CART_DETAIL;
        // Tạo một StringRequest để gửi yêu cầu POST đến API
        @SuppressLint({"NotifyDataSetChanged", "DefaultLocale", "SetTextI18n"}) StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    // Xử lý kết quả trả về từ API ở đây
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        Log.d("hello", response);
                        boolean error = jsonObject.getBoolean("error");

                        if (!error) {
                            JSONArray cartDetails = jsonObject.getJSONArray("cart_detail");
                            List<CartDetail> cartDetailList = new ArrayList<>();
                            double totalCost = 0.0; // khởi tạo biến tính tổng cost_product

                            for (int i = 0; i < cartDetails.length(); i++) {
                                JSONObject productObj = cartDetails.getJSONObject(i);
                                int id_cart_detail = productObj.getInt("id_cart_details");
                                int code_cart = productObj.getInt("code_cart");
                                String name_product = productObj.getString("name_product");
                                String cost_product = productObj.getString("cost_product");
                                String name_user = productObj.getString("name_user");
                                String image_product = productObj.getString("image_product");

                                // thêm giá trị cost_product vào biến tổng totalCost
                                totalCost += Double.parseDouble(cost_product);

                                CartDetail cartDetail = new CartDetail(id_cart_detail, code_cart, image_product, name_product, cost_product, name_user);
                                cartDetailList.add(cartDetail);
                                SharedPreferences sharedPreferences = getSharedPreferences("code_cart", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("code_cart", String.valueOf(code_cart));
                                editor.apply();
                            }

                            // Create an instance of the ProductAdapter class and pass the list of products to it
                            CartShippingAdapter cartShippingAdapter = new CartShippingAdapter(this, cartDetailList);

                            // Set the adapter to the RecyclerView
                            recyclerView.setAdapter(cartShippingAdapter);
                            cartShippingAdapter.notifyDataSetChanged();

                            // hiển thị tổng cost_product lên TextView
                            TextView allCostProducts = findViewById(R.id.allCostProduct);
                            allCostProducts.setText(String.format("%.2f", totalCost) + " VND");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    // Xử lý lỗi nếu có ở đây
                }) {
            @Override
            protected Map<String, String> getParams() {
                // Định nghĩa các tham số của yêu cầu POST ở đây
                Map<String, String> params = new HashMap<>();
                params.put("cart_detail", nameUser);
                return params;
            }
        };

        // Thêm yêu cầu vào RequestQueue để thực hiện
        requestQueue.add(stringRequest);
    }

    private void setDirectPayment(String name_user, String code_cart, int cart_number, double cart_cost, String id_shipping) {
        String url = Api.URL_SET_DIRECT_PAYMENT;

        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String cart_date = formatter.format(new Date());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    // Xử lý kết quả trả về khi thành công
                    Toast.makeText(getApplicationContext(), "Thanh Toán Thành Công", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, PaymentActivity.class);
                    intent.putExtra("code_cart", code_cart);
                    finish();
                    startActivity(intent);
                },
                error -> {
                    // Xử lý kết quả trả về khi có lỗi xảy ra
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("name_user", name_user);
                params.put("code_cart", code_cart);
                params.put("cart_date", cart_date);
                params.put("cart_number", String.valueOf(cart_number));
                params.put("cart_cost", String.valueOf(cart_cost));
                params.put("id_shipping", id_shipping);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}