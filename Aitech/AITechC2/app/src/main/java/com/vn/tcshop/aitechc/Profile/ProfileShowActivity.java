package com.vn.tcshop.aitechc.Profile;

import static android.view.View.GONE;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.vn.tcshop.aitechc.Api.Api;
import com.vn.tcshop.aitechc.Blogs.BlogActivity;
import com.vn.tcshop.aitechc.Carts.CartActivity;
import com.vn.tcshop.aitechc.Carts.CartActivityHistory;
import com.vn.tcshop.aitechc.ChatBlog.BlogChatActivity;
import com.vn.tcshop.aitechc.LoginActivity;
import com.vn.tcshop.aitechc.MainActivity;
import com.vn.tcshop.aitechc.Models.RequestHandler;
import com.vn.tcshop.aitechc.Products.ProductActivity;
import com.vn.tcshop.aitechc.R;
import com.vn.tcshop.aitechc.contact.ContactActivity;
import com.vn.tcshop.aitechc.contact.MapsActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ProfileShowActivity extends AppCompatActivity {
    private static final int CODE_GET_REQUEST = 1024;
    private static final int CODE_POST_REQUEST = 1025;
    private Button btnLogout;
    private TextView tvId, tvName, tvEmail, tvContact, tvContact_map, tvChat;
    private ProgressBar progressBar;
    private ShapeableImageView imgView;
    OkHttpClient client;
    BottomNavigationView bottomNavigationView;
    private FirebaseDatabase database;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    ImageView btnEdit, btnDelete, tvCartHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_show);
        bottomNavigationView = findViewById(R.id.bottom_navigation_bar);
        bottomNavigationView.setSelectedItemId(R.id.menu_profile);
        progressBar = findViewById(R.id.progressBar_profile);
        tvId = findViewById(R.id.profile_tvId);
        tvName = findViewById(R.id.profile_tvName);
        tvEmail = findViewById(R.id.profile_tvEmail);
        imgView = findViewById(R.id.profile_imgView);
        btnLogout = findViewById(R.id.btn_profile_logout);
        btnEdit = findViewById(R.id.btn_profile_edit);
        btnDelete = findViewById(R.id.btn_profile_delete);
        tvContact = findViewById(R.id.contact);
        tvContact_map = findViewById(R.id.map_contact);
        tvChat = findViewById(R.id.chat_blog);
        tvCartHistory = findViewById(R.id.cart_history);

        client = new OkHttpClient();

        // Hiện thị thông tin lên profile
        Intent intent = getIntent();
        int id = intent.getIntExtra("id", -1);
        String name = intent.getStringExtra("name");
        String email = intent.getStringExtra("email");

        // Hiển thị thông tin lên giao diện
        tvId.setText(String.valueOf(id));
        tvName.setText(name);
        tvEmail.setText(email);

        // firebase
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        // TODO: hàm show_image_profile
        show_image_profile(imgView, String.valueOf(id));

        // -----------------------

        // TODO: hàm bottomBar
        bottomBar();
        // TODO : hàm logout đăng xuất và chuyển về activity login
        logout();
        // TODO: hàm updateProfile
        updateProfile();
        // TODO: hàm deleteProfile
        deleteProfile();
        // TODO: hàm contact
        contact();
        // TODO: hàm map_contact
        map_contact();
        // TODO: hàm chatBlog()
        chatBlog();

        //Lịch sử đơn hàng
        getCartHistoryActivity(id);
    }

    // START
    private void getCartHistoryActivity(int id) {
        tvCartHistory.setOnClickListener(view -> {
            Intent intent = new Intent(ProfileShowActivity.this, CartActivityHistory.class);
            intent.putExtra("id_user", id); // Thay id_user bằng giá trị cần truyền
            startActivity(intent);
        });
    }

    // END

    private void chatBlog() {
        tvChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileShowActivity.this, BlogChatActivity.class));
            }
        });
    }

    /**
     * Hàm bottomBar() được sử dụng để thiết lập sự kiện lắng nghe (listener) cho bottomNavigationView.
     * Khi người dùng chọn một mục trong thanh điều hướng dưới (bottom navigation bar), sự kiện
     * onNavigationItemSelected sẽ được gọi và phương thức switch sẽ được sử dụng để xác định mục được chọn bởi
     * người dùng. Nếu người dùng chọn mục "Home", hàm getUserInfo() sẽ được gọi để lấy thông tin người dùng
     * dựa trên địa chỉ email đã được cung cấp. Nếu người dùng chọn mục "Profile", hàm không thực hiện hành động
     * nào và nếu người dùng chọn mục "Shopping cart", một thông báo Toast sẽ xuất hiện để hiển thị lời nhắc về
     * giỏ hàng.
     */
    // START
    private void bottomBar() {
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_home:
                        getUserInfo(tvEmail.getText().toString());
                        return true;
                    case R.id.menu_profile:
                        return true;
                    case R.id.menu_shopping_cart:
                        getUserInfoToCart(tvName.getText().toString());
                        return true;
                    case R.id.menu_blog:
                        getUserInfoId_blog(tvId.getText().toString());
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
                    Intent intent = new Intent(ProfileShowActivity.this, CartActivity.class);
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
    //END

    // START
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
                    Intent intent = new Intent(ProfileShowActivity.this, ProductActivity.class);
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

    // START
    private void getUserInfoId_blog(String id) {
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
                    Intent intent = new Intent(ProfileShowActivity.this, BlogActivity.class);
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
    private void logout() {
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        });
    }
    // END

    // START
    private void map_contact() {
        tvContact_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileShowActivity.this, MapsActivity.class));
            }
        });
    }
    // END

    // START
    private void contact() {
        tvContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileShowActivity.this, ContactActivity.class));
            }
        });
    }
    // END


    /**
     * Phương thức "deleteProfile()" sẽ xóa tài khoản người dùng khi người dùng nhấn vào nút xóa tài khoản trong
     * giao diện người dùng. Khi người dùng nhấn nút, một hộp thoại cảnh báo sẽ hiển thị yêu cầu xác nhận xóa tài
     * khoản.
     * Nếu người dùng xác nhận xóa tài khoản bằng cách nhấn nút "yes", phương thức sẽ lấy thông tin người dùng
     * được truyền dưới dạng số nguyên và gửi yêu cầu mạng để xóa người dùng thông qua đường dẫn API. Nếu người
     * dùng không xác nhận xóa tài khoản bằng cách nhấn nút "no", không có hành động nào sẽ được thực hiện.
     * Phương thức sử dụng lớp AlertDialog để hiển thị hộp thoại cảnh báo, và sử dụng hình ảnh biểu tượng xóa
     * để minh họa cho hành động xóa tài khoản.
     */
    // START
    private void deleteProfile() {
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ProfileShowActivity.this);
                builder.setTitle("Xóa tài khoản")
                        .setMessage("Mọi thông tin của bạn sẽ bị xóa vĩnh viễn bạn có chắc chắn xóa?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = getIntent();
                                int id = intent.getIntExtra("id", -1);
                                PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_DELETE_USER + id, null, CODE_GET_REQUEST);
                                request.execute();

                                currentUser = mAuth.getCurrentUser();
                                deleteUser(currentUser);
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .setIcon(R.drawable.ic_dialog_delete)
                        .setCancelable(false)
                        .show();
            }
        });
    }
    // END

    private void deleteUser(FirebaseUser user) {
        // Xóa dữ liệu trong Firebase Realtime Database
        String userId = user.getUid();
        DatabaseReference userRef = database.getReference("user").child(userId);
        userRef.removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        // Xóa người dùng trong Firebase Authentication
        user.delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            finish();
                        } else {
                            Log.e("err",task.getException().getMessage());
                        }
                    }
                });
    }

    // hàm updateProfile
    // START
    private void updateProfile() {
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getUserInfoEdit(tvEmail.getText().toString());
            }
        });
    }
    // END

    /**
     * Lớp PerformNetworkRequest nhận đầu vào là một đường dẫn url, một HashMap<String, String> params và một
     * mã yêu cầu requestCode. Tham số url là đường dẫn đến API được gọi để thực hiện yêu cầu mạng. Tham số params
     * là một HashMap chứa các tham số yêu cầu gửi đến API, ví dụ như tên đăng nhập và mật khẩu. Tham số
     * requestCode là một mã số để xác định loại yêu cầu được gửi đến API.
     * <p>
     * Phương thức onPreExecute() được gọi trước khi tác vụ nền bắt đầu, và phương thức này sẽ hiển thị một
     * thanh tiến trình ProgressBar để người dùng biết rằng ứng dụng đang thực hiện một tác vụ.
     * <p>
     * Phương thức onPostExecute() được gọi sau khi tác vụ nền đã hoàn thành. Phương thức này sẽ ẩn thanh tiến
     * trình ProgressBar và xử lý kết quả trả về từ API. Kết quả trả về là một chuỗi JSON, được chuyển đổi sang
     * một đối tượng JSONObject và xử lý để hiển thị thông báo Toast cho người dùng.
     * <p>
     * Phương thức doInBackground() là nơi thực hiện các tác vụ nền. Nó sử dụng lớp RequestHandler để thực hiện
     * yêu cầu mạng, gửi yêu cầu POST hoặc GET tới địa chỉ API tùy thuộc vào mã requestCode được truyền vào. Sau
     * đó, kết quả được trả về dưới dạng một chuỗi.
     */
    // START
    private class PerformNetworkRequest extends AsyncTask<Void, Void, String> {
        String url;
        HashMap<String, String> params;
        int requestCode;

        PerformNetworkRequest(String url, HashMap<String, String> params, int requestCode) {
            this.url = url;
            this.params = params;
            this.requestCode = requestCode;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressBar.setVisibility(GONE);
            try {
                JSONObject object = new JSONObject(s);
                if (!object.getBoolean("error")) {
                    Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();

            if (requestCode == CODE_POST_REQUEST)
                return requestHandler.sendPostRequest(url, params);


            if (requestCode == CODE_GET_REQUEST)
                return requestHandler.sendGetRequest(url);

            return null;
        }
    }
    // END

    /**
     * Hàm "getUserInfo" trong đó được sử dụng một lớp con AsyncTask để lấy thông tin người dùng từ một API
     * thông qua phương thức POST.
     * Phương thức này có một tham số đầu vào là email, đại diện cho email của người dùng được tìm kiếm.
     * Trong phương thức getUserInfo, lớp con AsyncTask được sử dụng để thực hiện việc gọi API bất đồng bộ,
     * đồng thời truy xuất và trả về dữ liệu từ máy chủ API thông qua đối tượng RequestHandler.
     * Sau khi nhận được dữ liệu, phương thức onPostExecute được gọi và dữ liệu được chuyển đổi từ định dạng
     * chuỗi sang đối tượng JSONObject để lấy thông tin của người dùng.
     * Thông tin người dùng bao gồm id, name, email, phone, address và gender được lấy từ đối tượng JSONObject
     * và được đóng gói vào đối tượng Intent.
     * Cuối cùng, phương thức startActivity được gọi để khởi chạy một hoạt động mới, trong trường hợp này là
     * MainActivity, và truyền đối tượng Intent chứa thông tin người dùng vừa lấy được đến hoạt động mới này.
     * Sau khi hoàn tất, hoạt động hiện tại sẽ được kết thúc bằng phương thức overridePendingTransition.
     */
    // START
    private void getUserInfo(String email) {
        class GetUserTask extends AsyncTask<Void, Void, String> {
            @Override
            protected String doInBackground(Void... voids) {
                RequestHandler requestHandler = new RequestHandler();
                HashMap<String, String> params = new HashMap<>();
                params.put("email", email);
                return requestHandler.sendPostRequest(Api.URL_USER_BY_EMAIL, params);
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
                    Intent intent = new Intent(ProfileShowActivity.this, MainActivity.class);
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


    /**
     * Hàm "getUserInfoEdit" trong đó được sử dụng một lớp con AsyncTask để lấy thông tin người dùng từ một API
     * thông qua phương thức POST.
     * Phương thức này có một tham số đầu vào là email, đại diện cho email của người dùng được tìm kiếm.
     * Trong phương thức getUserInfoEdit, lớp con AsyncTask được sử dụng để thực hiện việc gọi API bất đồng bộ,
     * đồng thời truy xuất và trả về dữ liệu từ máy chủ API thông qua đối tượng RequestHandler.
     * Sau khi nhận được dữ liệu, phương thức onPostExecute được gọi và dữ liệu được chuyển đổi từ định dạng
     * chuỗi sang đối tượng JSONObject để lấy thông tin của người dùng.
     * Thông tin người dùng bao gồm id, name, email, phone, address và gender được lấy từ đối tượng JSONObject
     * và được đóng gói vào đối tượng Intent.
     * Cuối cùng, phương thức startActivity được gọi để khởi chạy một hoạt động mới, trong trường hợp này là
     * ProfileEditActivity, và truyền đối tượng Intent chứa thông tin người dùng vừa lấy được đến hoạt động mới này.
     */
    // START
    private void getUserInfoEdit(String email) {
        class GetUserTask extends AsyncTask<Void, Void, String> {
            @Override
            protected String doInBackground(Void... voids) {
                RequestHandler requestHandler = new RequestHandler();
                HashMap<String, String> params = new HashMap<>();
                params.put("email", email);
                return requestHandler.sendPostRequest(Api.URL_USER_BY_EMAIL, params);
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
                    Intent intent = new Intent(ProfileShowActivity.this, ProfileEditActivity.class);
                    intent.putExtra("id", id);
                    intent.putExtra("name", name);
                    intent.putExtra("email", email);
                    intent.putExtra("phone", phone);
                    intent.putExtra("address", address);
                    intent.putExtra("gender", gender);

                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        GetUserTask getUserTask = new GetUserTask();
        getUserTask.execute();
    }
    // END

    /**
     * Hàm show_image_profile được sử dụng để hiển thị hình ảnh của người dùng dựa trên ID của người dùng đó.
     * <p>
     * Trong hàm này, trước tiên ta sử dụng RequestBody để đóng gói dữ liệu cần gửi đến API, bao gồm tham số
     * "apicall" với giá trị "getUsersById" để yêu cầu lấy thông tin người dùng dựa trên ID và tham số "id"
     * với giá trị là ID của người dùng. Sau đó, ta sử dụng đối tượng Request để tạo một yêu cầu HTTP POST đến
     * API với các thông tin đã đóng gói.
     * Khi nhận được phản hồi từ API, ta kiểm tra xem phản hồi có bị lỗi hay không bằng cách kiểm tra giá trị
     * boolean của trường "error" trong đối tượng JSON trả về. Nếu không có lỗi, ta lấy thông tin người dùng từ
     * đối tượng JSON và lấy URL hình ảnh của người dùng từ trường "image" của đối tượng JSON. Ta sử dụng thư viện
     * Picasso để tải hình ảnh từ URL đã lấy được và hiển thị lên ImageView truyền vào.
     * Nếu phản hồi từ API bị lỗi, ta xử lý lỗi tại đây và ghi log ra để có thể theo dõi và sửa lỗi sau này. Nếu
     * xảy ra lỗi trong quá trình gửi yêu cầu hoặc nhận phản hồi từ API, ta sẽ hiển thị một Toast thông báo lỗi
     * và ghi log ra để theo dõi.
     */
    // STAT
    public void show_image_profile(final ImageView imageView, final String id) {
        RequestBody formBody = new FormBody.Builder()
                .add("apicall", "getUsersById")
                .add("id", id)
                .build();

        Request request = new Request.Builder()
                .url(Api.URL_USER_BY_ID)
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull okhttp3.Call call, @NonNull Response response) throws IOException {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    if (!jsonObject.getBoolean("error")) {
                        JSONArray usersArray = jsonObject.getJSONArray("users");
                        JSONObject userObject = usersArray.getJSONObject(0);
                        final String imgUrl = Api.URL_IMG + userObject.getString("image");
                        Log.d("IMAGE_URL", imgUrl); // log the image URL to check if it's correct
                        // kiểm tra xem có hình ảnh không
                        if (userObject.isNull("image")) {
                            ProfileShowActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    imgView.setImageResource(R.drawable.ic_person);
                                }
                            });
                            return;
                        }
                        ProfileShowActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Picasso.get()
                                        .load(imgUrl)
                                        .resize(400, 400)
                                        .centerCrop()
                                        .into(imgView);
                            }
                        });
                    } else {
                        // handle the error case here
                        Log.e("API_ERROR", jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull okhttp3.Call call, @NonNull IOException e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("error", e.getMessage());
            }

        });
    }
    // END

}