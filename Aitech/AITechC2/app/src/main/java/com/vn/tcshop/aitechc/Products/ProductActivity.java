package com.vn.tcshop.aitechc.Products;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.vn.tcshop.aitechc.Adapters.CategoryListAdapter;
import com.vn.tcshop.aitechc.Adapters.ProductAdapter;
import com.vn.tcshop.aitechc.Api.Api;
import com.vn.tcshop.aitechc.Blogs.BlogActivity;
import com.vn.tcshop.aitechc.Carts.CartActivity;
import com.vn.tcshop.aitechc.MainActivity;
import com.vn.tcshop.aitechc.Models.Category;
import com.vn.tcshop.aitechc.Models.Product;
import com.vn.tcshop.aitechc.Models.RequestHandler;
import com.vn.tcshop.aitechc.Profile.ProfileShowActivity;
import com.vn.tcshop.aitechc.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProductActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    BottomNavigationView bottomNavigationView;
    private TextView tvName;
    private final List<Category> categoryList = new ArrayList<>();
    private ListView listView;
    private final boolean isSearching = false;
    private SearchView searchView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        bottomNavigationView = findViewById(R.id.bottom_navigation_bar);
        bottomNavigationView.setSelectedItemId(R.id.menu_product);
        recyclerView = findViewById(R.id.recycleviewProduct);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        bottomBar();
        Intent intent = getIntent();
        tvName = findViewById(R.id.nameUser);
        String name = intent.getStringExtra("name");
        tvName.setText(name);
        SharedPreferences sharedPreferences = getSharedPreferences("username", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("data", name);
        editor.apply();
        //category listview
        listView = findViewById(R.id.listviewCategory);
        getCategory();
        getProduct();

        // Khởi tạo searchView và thiết lập Listener
        searchView = findViewById(R.id.searchView);
        search();
    }

    private void search() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                class GetUserTask extends AsyncTask<Void, Void, String> {
                    @Override
                    protected String doInBackground(Void... voids) {
                        RequestHandler requestHandler = new RequestHandler();
                        HashMap<String, String> params = new HashMap<>();
                        params.put("name", tvName.getText().toString());
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
                            Intent intent = new Intent(ProductActivity.this, ProductSearchActivity.class);
                            intent.putExtra("id", id);
                            intent.putExtra("name", name);
                            intent.putExtra("email", email);
                            intent.putExtra("phone", phone);
                            intent.putExtra("gender", gender);
                            intent.putExtra("query", query);

                            startActivity(intent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

                GetUserTask getUserTask = new GetUserTask();
                getUserTask.execute();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private String getUserInfoToProfile(String name) {
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
                    Intent intent = new Intent(ProductActivity.this, ProfileShowActivity.class);
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

    private String getUserInfoToMain(String name) {
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
                    Intent intent = new Intent(ProductActivity.this, MainActivity.class);
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

    private String getUserInfoToCart(String name) {
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
                    Intent intent = new Intent(ProductActivity.this, CartActivity.class);
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

    private void bottomBar() {
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.menu_home:
                    getUserInfoToMain(tvName.getText().toString());
                    return true;
                case R.id.menu_product:
                    return true;
                case R.id.menu_profile:
                    getUserInfoToProfile(tvName.getText().toString());
                    return true;
                case R.id.menu_shopping_cart:
                    getUserInfoToCart(tvName.getText().toString());
                    return true;
                case R.id.menu_blog:
                    getUserInfoIDBlog(tvName.getText().toString());
                    return true;
            }
            return false;
        });
    }

    private void getUserInfoIDBlog(String name) {
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
                    String address = userObject.getString("address");
                    String gender = userObject.getString("gender");

                    // Đóng gói thông tin người dùng vào Intent
                    Intent intent = new Intent(ProductActivity.this, BlogActivity.class);
                    intent.putExtra("id", id);
                    intent.putExtra("name", name);
                    intent.putExtra("email", email);
                    intent.putExtra("phone", phone);
                    intent.putExtra("address", address);
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
    }

    //getCategory
    private void getCategory() {
        // Khởi tạo RequestQueue để thực hiện yêu cầu mạng
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        // Tạo một StringRequest để lấy dữ liệu từ API
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Api.URL_CATEGORY, response -> {
            try {
                // Chuyển đổi dữ liệu JSON thành một đối tượng JSON
                JSONObject jsonObject = new JSONObject(response);
                boolean error = jsonObject.getBoolean("error");

                // Kiểm tra xem có lỗi xảy ra hay không
                if (!error) {
                    // Lấy danh sách các category
                    JSONArray categories = jsonObject.getJSONArray("categories");

                    // Duyệt qua danh sách các category và chuyển đổi thành đối tượng Category
                    for (int i = 0; i < categories.length(); i++) {
                        JSONObject categoryObj = categories.getJSONObject(i);
                        int categoryId = categoryObj.getInt("id");
                        String categoryName = categoryObj.getString("category");
                        Category category = new Category(categoryId, categoryName);
                        categoryList.add(category);
                        listView.setOnItemClickListener((parent, view, position, id) -> {
                            // Lấy đối tượng Category từ ListView
                            Category item = (Category) parent.getItemAtPosition(position);

                            // Lấy tên danh mục từ đối tượng Category
                            String categoryName1 = item.getName();

                            // Tạo một Intent mới để chuyển đến màn hình khác
                            Intent intent = new Intent(getApplicationContext(), ProductByCategoryActivity.class);

                            // Truyền tên danh mục qua Intent
                            intent.putExtra("CATEGORY_NAME", categoryName1);
                            intent.putExtra("username", tvName.getText());

                            // Mở màn hình mới
                            startActivity(intent);
                        });
                    }
                    // Hiển thị danh sách danh mục lên ListView
                    CategoryListAdapter adapter = new CategoryListAdapter(ProductActivity.this, R.layout.layout_category_list, categoryList);
                    listView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
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
        });

        // Thêm StringRequest vào RequestQueue để thực hiện yêu cầu mạng
        requestQueue.add(stringRequest);
    }

    //getProduct
    private void getProduct() {
        // Check if getProductByName is currently executing
        if (isSearching) {
            // getProductByName is executing, do not execute getProduct
            return;
        }
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Api.URL_PRODUCT, response -> {
            Log.d("ProductActivity", "Response: " + response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                boolean error = jsonObject.getBoolean("error");

                if (!error) {
                    JSONArray products = jsonObject.getJSONArray("product");
                    List<Product> productList = new ArrayList<>();
                    for (int i = 0; i < products.length(); i++) {
                        JSONObject productObj = products.getJSONObject(i);
                        int id = productObj.getInt("id");
                        String name = productObj.getString("name");
                        String cost = productObj.getString("cost");
                        String image = productObj.getString("image");
                        String detail = productObj.getString("detail");
                        String category = productObj.getString("category");

                        Product product = new Product(id, name, cost, image, detail, category, 0);
                        productList.add(product);
                    }

                    // Create an instance of the ProductAdapter class and pass the list of products to it
                    ProductAdapter productAdapter = new ProductAdapter(ProductActivity.this, productList);

                    // Set the adapter to the RecyclerView
                    recyclerView.setAdapter(productAdapter);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, Throwable::printStackTrace);

        requestQueue.add(stringRequest);
    }
}