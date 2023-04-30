package com.vn.tcshop.aitechc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.vn.tcshop.aitechc.Api.Api;
import com.vn.tcshop.aitechc.Blogs.BlogActivity;
import com.vn.tcshop.aitechc.Carts.CartActivity;
import com.vn.tcshop.aitechc.ChatBot.ChatBotActivity;
import com.vn.tcshop.aitechc.ChatBot.ChatImgBotActivity;
import com.vn.tcshop.aitechc.Models.RequestHandler;
import com.vn.tcshop.aitechc.Products.ProductActivity;
import com.vn.tcshop.aitechc.Products.ProductSearchActivity;
import com.vn.tcshop.aitechc.Profile.ProfileShowActivity;
import com.vn.tcshop.aitechc.weather.SplashScreenActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    private TextView tvName;
    private ImageView imgChatBot, imgChatIMGBot, weatherTv, developingTv;
    private SearchView recycleviewProductSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottom_navigation_bar);
        bottomNavigationView.setSelectedItemId(R.id.menu_home);
        tvName = findViewById(R.id.main_tvName);
        imgChatBot = findViewById(R.id.main_chatBotAl);
        imgChatIMGBot = findViewById(R.id.main_chatIMGBotAl);
        weatherTv = findViewById(R.id.main_weather);
        developingTv = findViewById(R.id.main_developing);
        recycleviewProductSearch = findViewById(R.id.recycleviewProductSearch);

        // slider images
        ImageSlider imageSlider = findViewById(R.id.imgslide);
        ArrayList<SlideModel> slideModels = new ArrayList<>();

        slideModels.add(new SlideModel(R.drawable.m1, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.m2, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.m3, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.m4, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.m5, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.m6, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.m7, ScaleTypes.FIT));

        imageSlider.setImageList(slideModels, ScaleTypes.FIT);

        // lấy dữ liệu từ bên login
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        tvName.setText(name);
        // -------------------------


        // TODO: hàm bottomBar
        bottomBar();

        // TODO: hàm ChatBot
        chatBot();
        // TODO: hàm ChatIMGBot
        chatIMGBot();
        // TODO: hàm Weather
        weather();
        // TODO: ham Developing
        developing();
        // TODO: hàm search
        search();
    }

    private void search() {
        // Khởi tạo searchView và thiết lập Listener
        recycleviewProductSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
                            Intent intent = new Intent(MainActivity.this, ProductSearchActivity.class);
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

    private void developing() {
        developingTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, DevelopingActivity.class));
            }
        });
    }

    private void weather() {
        weatherTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SplashScreenActivity.class));
            }
        });
    }

    private void chatIMGBot() {
        imgChatIMGBot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ChatImgBotActivity.class));
            }
        });
    }

    private void chatBot() {
        imgChatBot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ChatBotActivity.class));
            }
        });
    }

    /**
     * Hàm bottomBar xử lý sự kiện khi người dùng chọn một item trong Bottom Navigation Bar. Khi người dùng chọn một item,
     * hàm sẽ kiểm tra ID của item được chọn và thực hiện một số tác vụ tương ứng. Cụ thể:
     * <p>
     * Nếu người dùng chọn "menu_home" thì không có tác vụ gì được thực hiện.
     * Nếu người dùng chọn "menu_profile" thì hàm sẽ gọi hàm "getUserInfo" để lấy thông tin người dùng và hiển thị
     * lên giao diện.
     * Nếu người dùng chọn "menu_shopping_cart"
     */
    // START
    private void bottomBar() {
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_home:
                        return true;
                    case R.id.menu_profile:
                        getUserInfo(tvName.getText().toString());
                        return true;
                    case R.id.menu_shopping_cart:
                        getUserInfoToCart(tvName.getText().toString());
                        return true;
                    case R.id.menu_blog:
                        getUserInfoIDBlog(tvName.getText().toString());
                        return true;
                    case R.id.menu_product:
                        getUserInfoProduct(tvName.getText().toString());
                        return true;
                }
                return false;
            }
        });
    }
    // END

    //START
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
                    Intent intent = new Intent(MainActivity.this, CartActivity.class);
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
    // END

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
                    Intent intent = new Intent(MainActivity.this, BlogActivity.class);
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

    /**
     * Hàm getUserInfo là một phương thức để lấy thông tin người dùng bằng tên. Nó sử dụng một đối tượng AsyncTask
     * để thực hiện một yêu cầu HTTP POST đến một API với các thông tin tên người dùng được gửi trong tham số
     * yêu cầu. Nếu yêu cầu thành công, kết quả trả về được chuyển đến phương thức onPostExecute để xử lý dữ liệu
     * trả về và khởi tạo một Intent mới để chuyển đến màn hình hiển thị thông tin cá nhân của người dùng.
     */
    // START
    private void getUserInfo(String name) {
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
                    Intent intent = new Intent(MainActivity.this, ProfileShowActivity.class);
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
    // END

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
                    Intent intent = new Intent(MainActivity.this, ProductActivity.class);
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
}