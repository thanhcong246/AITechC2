package com.vn.tcshop.aitechc;

import static android.view.View.GONE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.vn.tcshop.aitechc.Api.Api;
import com.vn.tcshop.aitechc.Models.RequestHandler;
import com.vn.tcshop.aitechc.Profile.ProfileEditActivity;
import com.vn.tcshop.aitechc.ResetPass.ConfirmPassLogActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {
    private static final int CODE_GET_REQUEST = 1024;
    private static final int CODE_POST_REQUEST = 1025;
    private TextView tvRegister, tvReset;
    private Button btnLogin;
    private EditText edtEmail, edtPassword;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        tvRegister = findViewById(R.id.login_tvRegister);
        tvReset = findViewById(R.id.login_tvResetPass);
        edtEmail = findViewById(R.id.login_edtEmail);
        edtPassword = findViewById(R.id.login_edtPassword);
        btnLogin = findViewById(R.id.login_btnLogin);
        progressBar = findViewById(R.id.progressBar_login);

        // firebase
        mAuth = FirebaseAuth.getInstance();

        // TODO: chuyển qua activity register
        tvRegister();
        // TODO: chuyển qua activity resetPass
        resetPass();
        // TODO: Đăng nhập
        login();
    }

    private void resetPass() {
        tvReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, ConfirmPassLogActivity.class));
            }
        });
    }

    // start
    private void tvRegister() {
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }
    // end


    // start
    private void login() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Lấy dữ liệu từ các trường EditText
                String email = edtEmail.getText().toString();
                String password = edtPassword.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    edtEmail.setError("Vui lòng nhập email");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    edtPassword.setError("Vui lòng nhập mật khẩu");
                    return;
                }

                // Tạo đối tượng HashMap để chứa các tham số của yêu cầu
                HashMap<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("password", password);

                // TODO: Gọi hàm PerformNetworkRequest để thực hiện yêu cầu
                PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_LOGIN, params, CODE_POST_REQUEST);
                request.execute();

                // TODO: hàm signIn (firebase)
                signIn(email,password);
            }
        });
    }
    // end

    private void signIn(String email, String pass) {
        mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    finish();
                }
            }
        });
    }

    /**
     * Đây là một class được tạo ra để thực hiện các request tới server trong ứng dụng Android. Class này được
     * kế thừa từ lớp AsyncTask, nó sử dụng một đối số Void để thực hiện các thao tác trên nền tảng khác.
     * <p>
     * Các thuộc tính trong class bao gồm:
     * url: Địa chỉ URL của server mà request được gửi đến.
     * params: Danh sách các tham số được truyền trong request.
     * requestCode: Mã request (CODE_POST_REQUEST hoặc CODE_GET_REQUEST).
     * <p>
     * Các phương thức trong class bao gồm:
     * <p>
     * onPreExecute(): Phương thức được gọi trước khi request được thực hiện, trong phương thức này sẽ
     * hiển thị progress bar để thông báo cho người dùng biết rằng request đang được thực hiện.
     * <p>
     * onPostExecute(): Phương thức được gọi sau khi request đã được thực hiện, trong phương thức này sẽ
     * ẩn progress bar và xử lý dữ liệu trả về từ server.
     * <p>
     * doInBackground(): Phương thức này sẽ thực hiện request đến server và trả về dữ liệu được lấy từ
     * server dưới dạng String.
     * <p>
     * Trong class này còn có một phương thức getUserInfo() để lấy thông tin người dùng sau khi đã đăng nhập
     * thành công và chuyển sang MainActivity. Phương thức này sử dụng một lớp AsyncTask khác để thực hiện
     * request đến server và lấy thông tin người dùng. Kết quả lấy được sẽ được đóng gói vào Intent và
     * chuyển sang MainActivity để hiển thị thông tin người dùng.
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
                    getUserInfo(edtEmail.getText().toString()); // Lấy thông tin người dùng và chuyển sang MainActivity
                } else {
                    Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        private void getUserInfo(String email) {
            class GetUserTask extends AsyncTask<Void, Void, String> {
                @Override
                protected String doInBackground(Void... voids) {
                    RequestHandler requestHandler = new RequestHandler();
                    HashMap<String, String> params = new HashMap<>();
                    params.put("email", email);
                    return requestHandler.sendPostRequest(Api.URL_USER_BY_EMAIL, params); // URL_GET_USER_BY_EMAIL là URL truy vấn người dùng bằng email
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
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("id", id);
                        intent.putExtra("name", name);
                        intent.putExtra("email", email);
                        intent.putExtra("phone", phone);
                        intent.putExtra("address", address);
                        intent.putExtra("gender", gender);

                        startActivity(intent); // Chuyển sang trang chủ MainActivity
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            GetUserTask getUserTask = new GetUserTask();
            getUserTask.execute();
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
}