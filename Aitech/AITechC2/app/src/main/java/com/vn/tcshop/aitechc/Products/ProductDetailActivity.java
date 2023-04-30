package com.vn.tcshop.aitechc.Products;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.vn.tcshop.aitechc.Adapters.CommentProductAdapter;
import com.vn.tcshop.aitechc.Adapters.ProductAdapter;
import com.vn.tcshop.aitechc.Api.Api;
import com.vn.tcshop.aitechc.Models.Product;
import com.vn.tcshop.aitechc.Models.ProductComment;
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
import java.util.Objects;

public class ProductDetailActivity extends AppCompatActivity {
    private RecyclerView recyclerViewDetail;
    private RecyclerView recyclerViewComment;
    private RatingBar ratingProduct;
    private TextView userName;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        // Lấy dữ liệu tên danh mục từ Intent
        Intent intent = getIntent();
        String nameProduct = intent.getStringExtra("product");

        recyclerViewDetail = findViewById(R.id.recycleviewProductDetail);
        recyclerViewDetail.setLayoutManager(new LinearLayoutManager(this));

        recyclerViewComment = findViewById(R.id.commentRecycleView);
        recyclerViewComment.setLayoutManager(new LinearLayoutManager(this));
        // Lấy username
        userName = findViewById(R.id.nameUser);
        String nameUser = intent.getStringExtra("username");
        userName.setText(nameUser);

        // Thiết lập thanh toolbar
        Toolbar toolbar = findViewById(R.id.toolbarDetailBack);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        Toolbar toolbarDetailName = findViewById(R.id.toolbarDetailName);
        toolbarDetailName.setTitle(nameProduct);

        LinearLayout commentLayout = findViewById(R.id.commentLayout);
        EditText comment = commentLayout.findViewById(R.id.comment);
        RatingBar commentRatingBar = commentLayout.findViewById(R.id.commentRatingBar);
        Button commentButton = commentLayout.findViewById(R.id.commentButton);
        // Gắn listener cho button comment
        commentButton.setOnClickListener(view -> {
            String commentt = comment.getText().toString();
            if (TextUtils.isEmpty(commentt)) {
                Toast.makeText(getApplicationContext(), "Bình luận không được trống", Toast.LENGTH_SHORT).show();
                return;
            }
            float rating = commentRatingBar.getRating();
            if (rating == 0) {
                Toast.makeText(getApplicationContext(), "Vui lòng đánh giá", Toast.LENGTH_SHORT).show();
                return;
            }
            // Gọi hàm postCommentToAPI khi người dùng click vào button comment
            postCommentToAPI(nameUser, nameProduct, String.valueOf(comment.getText()), commentRatingBar.getRating());
        });

        // Sử dụng tên danh mục để lấy sản phẩm theo danh mục
        getDetailProduct(nameProduct);

        getCommentProduct(nameProduct);

        SharedPreferences sharedPreferences = getSharedPreferences("cost_product", Context.MODE_PRIVATE);
        String costProduct = sharedPreferences.getString("cost_product", "");
        Button addProductToCart = findViewById(R.id.btnAddToCart);
        addProductToCart.setOnClickListener(v -> postProductToCart(nameProduct, nameUser, costProduct));
    }

    private void postProductToCart(String productName, String username, String costProduct) {
        String url = Api.URL_SET_PRODUCT_TO_CART;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    // Xử lý kết quả trả về khi thành công
                    Toast.makeText(getApplicationContext(), "Thêm Sản Phẩm thành công", Toast.LENGTH_SHORT).show();
                },
                error -> {
                    // Xử lý kết quả trả về khi có lỗi xảy ra
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("name_user", username);
                params.put("name_product", productName);
                params.put("cost_product", costProduct);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void postCommentToAPI(String username, String productName, String comment, float rate) {
        String url = Api.URL_SET_COMMENT; // thay đổi đường dẫn tới API theo đúng yêu cầu của bạn
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String date = formatter.format(new Date());
        Log.d("date", date);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    // Xử lý kết quả trả về khi thành công
                    Toast.makeText(getApplicationContext(), "Bình luận thành công!", Toast.LENGTH_SHORT).show();
                    finish();
                    startActivity(getIntent());
                },
                error -> {
                    // Xử lý kết quả trả về khi có lỗi xảy ra
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("name_user", username);
                params.put("name_product", productName);
                params.put("comment_product", comment);
                params.put("rate", String.valueOf(rate));
                params.put("date_comment", date);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
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


    private void getCommentProduct(String commentProduct) {
        // Khởi tạo một RequestQueue để thực hiện các yêu cầu mạng
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        // Định nghĩa URL của API tìm kiếm sản phẩm
        String url = Api.URL_COMMENT_PRODUCT;
        // Tạo một StringRequest để gửi yêu cầu POST đến API
        @SuppressLint("NotifyDataSetChanged") StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    // Xử lý kết quả trả về từ API ở đây
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        Log.d("Comment", response);
                        boolean error = jsonObject.getBoolean("error");

                        if (!error) {
                            JSONArray comments = jsonObject.getJSONArray("comment");
                            List<ProductComment> commentList = new ArrayList<>();
                            for (int i = 0; i < comments.length(); i++) {
                                JSONObject productObj = comments.getJSONObject(i);
                                String name_user = productObj.getString("name_user");
                                String name_product = productObj.getString("name_product");
                                String comment_product = productObj.getString("comment_product");
                                float rate = (float) productObj.getDouble("rate");
                                String date_comment = productObj.getString("date_comment");

                                ProductComment comment = new ProductComment(name_user, name_product, comment_product, rate, date_comment);
                                commentList.add(comment);
                            }

                            // Create an instance of the ProductAdapter class and pass the list of products to it
                            CommentProductAdapter commentProductAdapter = new CommentProductAdapter(this, commentList);

                            // Set the adapter to the RecyclerView
                            recyclerViewComment.setAdapter(commentProductAdapter);
                            commentProductAdapter.notifyDataSetChanged();
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
                params.put("comment", commentProduct);
                return params;
            }
        };

        // Thêm yêu cầu vào RequestQueue để thực hiện
        requestQueue.add(stringRequest);
    }

    private void getDetailProduct(String productName) {
        // Khởi tạo một RequestQueue để thực hiện các yêu cầu mạng
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        // Định nghĩa URL của API tìm kiếm sản phẩm
        String url = Api.URL_DETAIL_PRODUCT;
        // Tạo một StringRequest để gửi yêu cầu POST đến API
        @SuppressLint("NotifyDataSetChanged") StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    // Xử lý kết quả trả về từ API ở đây
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        Log.d("hello", response);
                        boolean error = jsonObject.getBoolean("error");

                        if (!error) {
                            JSONArray products = jsonObject.getJSONArray("detail");
                            List<Product> productList = new ArrayList<>();
                            for (int i = 0; i < products.length(); i++) {
                                JSONObject productObj = products.getJSONObject(i);
                                int id = productObj.getInt("id");
                                String name = productObj.getString("name");
                                String cost = productObj.getString("cost");
                                String image = productObj.getString("image");
                                String detail = productObj.getString("detail");
                                String category = productObj.getString("category");
                                float rate = (float) productObj.getDouble("rate");

                                Product product = new Product(id, name, cost, image, detail, category, rate);
                                productList.add(product);
                                //rating
                                ratingProduct = findViewById(R.id.ratingProduct);
                                ratingProduct.setRating(rate);

                                SharedPreferences sharedPreferences = getSharedPreferences("cost_product", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("cost_product", cost);
                                editor.apply();
                            }

                            // Create an instance of the ProductAdapter class and pass the list of products to it
                            ProductAdapter ProductAdapter = new ProductAdapter(this, productList);

                            // Set the adapter to the RecyclerView
                            recyclerViewDetail.setAdapter(ProductAdapter);
                            ProductAdapter.notifyDataSetChanged();
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
                params.put("detail", productName);
                return params;
            }
        };

        // Thêm yêu cầu vào RequestQueue để thực hiện
        requestQueue.add(stringRequest);
    }
}