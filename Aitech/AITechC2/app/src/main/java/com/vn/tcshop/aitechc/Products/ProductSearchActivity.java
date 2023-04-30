package com.vn.tcshop.aitechc.Products;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.vn.tcshop.aitechc.Adapters.searchProductAdapter;
import com.vn.tcshop.aitechc.Api.Api;
import com.vn.tcshop.aitechc.MainActivity;
import com.vn.tcshop.aitechc.Models.Product;
import com.vn.tcshop.aitechc.Models.RequestHandler;
import com.vn.tcshop.aitechc.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ProductSearchActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TextView tvName;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_search);

        Intent intent = getIntent();
        if (intent.hasExtra("query")) {
            String query = intent.getStringExtra("query");
            getProductByName(query);
        }
        tvName = findViewById(R.id.nameUser);
        String name = intent.getStringExtra("name");
        tvName.setText(name);

        // Tìm kiếm RecyclerView và thiết lập adapter cho nó
        recyclerView = findViewById(R.id.recycleviewProductSearch);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        TextView back = findViewById(R.id.toolbar);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getUserInfoProduct(tvName.getText().toString());
            }
        });
    }

    private String getUserInfoProduct(String name) {
        class GetUserTask extends AsyncTask<Void, Void, String> {
            @Override
            protected String doInBackground(Void... voids) {
                RequestHandler requestHandler = new RequestHandler();
                HashMap<String, String> params = new HashMap<>();
                params.put("name", name);
                return requestHandler.sendPostRequest(Api.URL_USER_BY_NAME, params);
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
                    String gender = userObject.getString("gender");

                    // Đóng gói thông tin người dùng vào Intent
                    Intent intent = new Intent(ProductSearchActivity.this, ProductActivity.class);
                    intent.putExtra("id", id);
                    intent.putExtra("name", name);
                    intent.putExtra("email", email);
                    intent.putExtra("phone", phone);
                    intent.putExtra("gender", gender);

                    startActivity(intent);
                    overridePendingTransition(0, 0);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        GetUserTask getUserTask = new GetUserTask();
        getUserTask.execute();
        return name;
    }


    private void getProductByName(String productName) {
        // Khởi tạo một RequestQueue để thực hiện các yêu cầu mạng
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        // Định nghĩa URL của API tìm kiếm sản phẩm
        String url = Api.URL_SEARCH_PRODUCT;
        // Tạo một StringRequest để gửi yêu cầu POST đến API
        @SuppressLint("NotifyDataSetChanged") StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    // Xử lý kết quả trả về từ API ở đây
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        boolean error = jsonObject.getBoolean("error");

                        if (!error) {
                            JSONArray products = jsonObject.getJSONArray("search");
                            List<Product> productList = new ArrayList<>();
                            for (int i = 0; i < products.length(); i++) {
                                JSONObject productObj = products.getJSONObject(i);
                                int id = productObj.getInt("id");
                                String name = productObj.getString("name");
                                String cost = productObj.getString("cost");
                                String image = productObj.getString("image");
                                String detail = productObj.getString("detail");
                                String category = productObj.getString("category");
                                int rate = productObj.getInt("rate");

                                Product product = new Product(id, name, cost, image, detail, category, rate);
                                productList.add(product);
                            }

                            // Create an instance of the ProductAdapter class and pass the list of products to it
                            searchProductAdapter searchProductAdapter = new searchProductAdapter(this, productList);

                            // Set the adapter to the RecyclerView
                            recyclerView.setAdapter(searchProductAdapter);
                            searchProductAdapter.notifyDataSetChanged();
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
                params.put("search", productName);
                return params;
            }
        };

        // Thêm yêu cầu vào RequestQueue để thực hiện
        requestQueue.add(stringRequest);
    }
}
