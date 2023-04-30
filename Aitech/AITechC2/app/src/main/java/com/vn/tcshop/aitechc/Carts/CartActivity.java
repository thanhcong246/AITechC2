package com.vn.tcshop.aitechc.Carts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.vn.tcshop.aitechc.Adapters.CartDetailAdapter;
import com.vn.tcshop.aitechc.Api.Api;
import com.vn.tcshop.aitechc.Blogs.BlogActivity;
import com.vn.tcshop.aitechc.MainActivity;
import com.vn.tcshop.aitechc.Models.CartDetail;
import com.vn.tcshop.aitechc.Models.RequestHandler;
import com.vn.tcshop.aitechc.Products.ProductActivity;
import com.vn.tcshop.aitechc.Profile.ProfileShowActivity;
import com.vn.tcshop.aitechc.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CartActivity extends AppCompatActivity {
    private TextView id_User;
    private RecyclerView recyclerView;
    private Button removeAllButton;
    private Button shippingButton;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        id_User = findViewById(R.id.idUser);
        bottomNavigationView = findViewById(R.id.bottom_navigation_bar);
        bottomNavigationView.setSelectedItemId(R.id.menu_shopping_cart);

        recyclerView = findViewById(R.id.recycleviewProductCart);
        Intent intent = getIntent();
        int id = intent.getIntExtra("id", -1);

        // Hiển thị thông tin lên giao diện
        id_User.setText(String.valueOf(id));
        String nameUser = intent.getStringExtra("name");

        getCartDetail(nameUser);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        removeAllButton = findViewById(R.id.btnDeleteCart);
        removeAllButton.setOnClickListener(v -> {
            removeAllCartDetail(nameUser);
        });
        shippingButton = findViewById(R.id.btnAddToShipping);
        shippingButton.setOnClickListener(v -> {
            // kiểm tra số lượng phần tử trong Adapter của RecyclerView
            int itemCount = Objects.requireNonNull(recyclerView.getAdapter()).getItemCount();
            if (itemCount == 0) {
                Toast.makeText(getApplicationContext(), "Không có sản phẩm trong giỏ hàng!", Toast.LENGTH_SHORT).show();
                return;
            } else {
                // chuyển sang ShippingActivity
                Intent i = new Intent(this, ShippingActivity.class);
                i.putExtra("name_user", nameUser);
                startActivity(i);
            }
        });

        // TODO: hàm bottomBar
        bottomBar();
    }

    private void bottomBar() {
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_home:
                        getUserInfoId_home(id_User.getText().toString());
                        return true;
                    case R.id.menu_profile:
                        getUserInfoId_profile(id_User.getText().toString());
                        return true;
                    case R.id.menu_shopping_cart:
                        return true;
                    case R.id.menu_blog:
                        getUserInfoToBlog(id_User.getText().toString());
                        return true;
                    case R.id.menu_product:
                        getUserInfoProduct(id_User.getText().toString());
                        return true;
                }
                return false;
            }
        });
    }

    // START
    private void getUserInfoToBlog(String id) {
        class GetUserTask extends AsyncTask<Void, Void, String> {
            @Override
            protected String doInBackground(Void... voids) {
                RequestHandler requestHandler = new RequestHandler();
                HashMap<String, String> params = new HashMap<>();
                params.put("id", id);
                return requestHandler.sendPostRequest(Api.URL_USER_BY_ID, params);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                try {
                    JSONObject object = new JSONObject(s);
                    JSONArray users = object.getJSONArray("users");
                    JSONObject userObject = users.getJSONObject(0);

                    // Lấy thông tin người dùng
                    int id = userObject.getInt("id");
                    String name = userObject.getString("name");
                    String email = userObject.getString("email");
                    String phone = userObject.getString("phone");
                    String address = userObject.getString("address");
                    String gender = userObject.getString("gender");
                    String image = userObject.getString("image");

                    // Đóng gói thông tin người dùng vào Intent
                    Intent intent = new Intent(CartActivity.this, BlogActivity.class);
                    intent.putExtra("id", id);
                    intent.putExtra("name", name);
                    intent.putExtra("email", email);
                    intent.putExtra("phone", phone);
                    intent.putExtra("address", address);
                    intent.putExtra("gender", gender);
                    intent.putExtra("image", image);

                    startActivity(intent);
                    overridePendingTransition(0, 0);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        GetUserTask getUserTask = new GetUserTask();
        getUserTask.execute();
    }
    // END

    // START
    private void getUserInfoProduct(String id) {
        class GetUserTask extends AsyncTask<Void, Void, String> {
            @Override
            protected String doInBackground(Void... voids) {
                RequestHandler requestHandler = new RequestHandler();
                HashMap<String, String> params = new HashMap<>();
                params.put("id", id);
                return requestHandler.sendPostRequest(Api.URL_USER_BY_ID, params);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                try {
                    JSONObject object = new JSONObject(s);
                    JSONArray users = object.getJSONArray("users");
                    JSONObject userObject = users.getJSONObject(0);

                    // Lấy thông tin người dùng
                    int id = userObject.getInt("id");
                    String name = userObject.getString("name");
                    String email = userObject.getString("email");
                    String phone = userObject.getString("phone");
                    String address = userObject.getString("address");
                    String gender = userObject.getString("gender");
                    String image = userObject.getString("image");

                    // Đóng gói thông tin người dùng vào Intent
                    Intent intent = new Intent(CartActivity.this, ProductActivity.class);
                    intent.putExtra("id", id);
                    intent.putExtra("name", name);
                    intent.putExtra("email", email);
                    intent.putExtra("phone", phone);
                    intent.putExtra("address", address);
                    intent.putExtra("gender", gender);
                    intent.putExtra("image", image);

                    startActivity(intent);
                    overridePendingTransition(0, 0);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        GetUserTask getUserTask = new GetUserTask();
        getUserTask.execute();
    }
    // END

    // START
    private void getUserInfoId_profile(String id) {
        class GetUserTask extends AsyncTask<Void, Void, String> {
            @Override
            protected String doInBackground(Void... voids) {
                RequestHandler requestHandler = new RequestHandler();
                HashMap<String, String> params = new HashMap<>();
                params.put("id", id);
                return requestHandler.sendPostRequest(Api.URL_USER_BY_ID, params);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                try {
                    JSONObject object = new JSONObject(s);
                    JSONArray users = object.getJSONArray("users");
                    JSONObject userObject = users.getJSONObject(0);

                    // Lấy thông tin người dùng
                    int id = userObject.getInt("id");
                    String name = userObject.getString("name");
                    String email = userObject.getString("email");
                    String phone = userObject.getString("phone");
                    String address = userObject.getString("address");
                    String gender = userObject.getString("gender");
                    String image = userObject.getString("image");

                    // Đóng gói thông tin người dùng vào Intent
                    Intent intent = new Intent(CartActivity.this, ProfileShowActivity.class);
                    intent.putExtra("id", id);
                    intent.putExtra("name", name);
                    intent.putExtra("email", email);
                    intent.putExtra("phone", phone);
                    intent.putExtra("address", address);
                    intent.putExtra("gender", gender);
                    intent.putExtra("image", image);

                    startActivity(intent);
                    overridePendingTransition(0, 0);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        GetUserTask getUserTask = new GetUserTask();
        getUserTask.execute();
    }
    // END

    // START
    private void getUserInfoId_home(String id) {
        class GetUserTask extends AsyncTask<Void, Void, String> {
            @Override
            protected String doInBackground(Void... voids) {
                RequestHandler requestHandler = new RequestHandler();
                HashMap<String, String> params = new HashMap<>();
                params.put("id", id);
                return requestHandler.sendPostRequest(Api.URL_USER_BY_ID, params);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                try {
                    JSONObject object = new JSONObject(s);
                    JSONArray users = object.getJSONArray("users");
                    JSONObject userObject = users.getJSONObject(0);

                    // Lấy thông tin người dùng
                    int id = userObject.getInt("id");
                    String name = userObject.getString("name");
                    String email = userObject.getString("email");
                    String phone = userObject.getString("phone");
                    String address = userObject.getString("address");
                    String gender = userObject.getString("gender");
                    String image = userObject.getString("image");

                    // Đóng gói thông tin người dùng vào Intent
                    Intent intent = new Intent(CartActivity.this, MainActivity.class);
                    intent.putExtra("id", id);
                    intent.putExtra("name", name);
                    intent.putExtra("email", email);
                    intent.putExtra("phone", phone);
                    intent.putExtra("address", address);
                    intent.putExtra("gender", gender);
                    intent.putExtra("image", image);

                    startActivity(intent);
                    overridePendingTransition(0, 0);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        GetUserTask getUserTask = new GetUserTask();
        getUserTask.execute();
    }
    // END

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
                            int numberProduct = 0;

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
                                numberProduct ++;

                                CartDetail cartDetail = new CartDetail(id_cart_detail, code_cart, image_product, name_product, cost_product, name_user);
                                cartDetailList.add(cartDetail);
                                SharedPreferences sharedPreferences = getSharedPreferences("code_cart", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("code_cart", String.valueOf(code_cart));
                                editor.putFloat("cart_cost", (float) totalCost);
                                editor.apply();
                            }

                            SharedPreferences sharedPreferences = getSharedPreferences("numberProduct", Context.MODE_PRIVATE);
                            @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putInt("number_product", numberProduct);
                            editor.apply();

                            // Create an instance of the ProductAdapter class and pass the list of products to it
                            CartDetailAdapter cartDetailAdapter = new CartDetailAdapter(this, cartDetailList);

                            // Set the adapter to the RecyclerView
                            recyclerView.setAdapter(cartDetailAdapter);
                            cartDetailAdapter.notifyDataSetChanged();

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

    private void removeAllCartDetail(String username) {
        String url = Api.URL_REMOVE_CART;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    // Xử lý kết quả trả về khi thành công
                    Toast.makeText(getApplicationContext(), "Xóa Tất Cả Sản Phẩm Thành Công", Toast.LENGTH_SHORT).show();
                    finish();
                    startActivity(getIntent());
                },
                error -> {
                    // Xử lý kết quả trả về khi có lỗi xảy ra
                    Toast.makeText(getApplicationContext(), "Có lỗi xảy khi xóa giỏ hàng", Toast.LENGTH_SHORT).show();
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("remove_cart_detail", username);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}