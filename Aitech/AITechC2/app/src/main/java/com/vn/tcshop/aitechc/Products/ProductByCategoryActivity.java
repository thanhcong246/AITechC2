package com.vn.tcshop.aitechc.Products;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.vn.tcshop.aitechc.Adapters.CategoryProductAdapter;
import com.vn.tcshop.aitechc.Api.Api;
import com.vn.tcshop.aitechc.Models.Product;
import com.vn.tcshop.aitechc.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ProductByCategoryActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TextView tvName;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_by_category);

        // Lấy dữ liệu tên danh mục từ Intent
        Intent intent = getIntent();
        String categoryName = intent.getStringExtra("CATEGORY_NAME");

        // Sử dụng tên danh mục để lấy sản phẩm theo danh mục
        getProductByCategory(categoryName);

        // Tìm kiếm RecyclerView và thiết lập adapter cho nó
        recyclerView = findViewById(R.id.recycleviewProductByCategory);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        // Thiết lập thanh toolbar
        Toolbar toolbar = findViewById(R.id.toolbarCategory);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        Toolbar toolbarCategoryName = findViewById(R.id.toolbarCategoryName);
        toolbarCategoryName.setTitle(categoryName);

        tvName = findViewById(R.id.nameUser);


    }


    // Bắt sự kiện khi người dùng bấm nút back
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getProductByCategory(String productName) {
        // Khởi tạo một RequestQueue để thực hiện các yêu cầu mạng
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        // Định nghĩa URL của API tìm kiếm sản phẩm
        String url = Api.URL_CATEGORY_PRODUCT;
        // Tạo một StringRequest để gửi yêu cầu POST đến API
        @SuppressLint("NotifyDataSetChanged") StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    // Xử lý kết quả trả về từ API ở đây
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        Log.d("DDD", response);
                        boolean error = jsonObject.getBoolean("error");

                        if (!error) {
                            JSONArray products = jsonObject.getJSONArray("category");
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
                            CategoryProductAdapter categoryProductAdapter = new CategoryProductAdapter(this, productList);

                            // Set the adapter to the RecyclerView
                            recyclerView.setAdapter(categoryProductAdapter);
                            categoryProductAdapter.notifyDataSetChanged();
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
                params.put("category", productName);
                return params;
            }
        };

        // Thêm yêu cầu vào RequestQueue để thực hiện
        requestQueue.add(stringRequest);
    }

}