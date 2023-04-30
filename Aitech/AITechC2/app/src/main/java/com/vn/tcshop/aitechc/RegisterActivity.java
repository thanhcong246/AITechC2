package com.vn.tcshop.aitechc;

import static android.view.View.GONE;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.vn.tcshop.aitechc.Api.Api;
import com.vn.tcshop.aitechc.Models.RequestHandler;
import com.vn.tcshop.aitechc.Models.UsersBlogChat;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    private static final int CODE_GET_REQUEST = 1024;
    private static final int CODE_POST_REQUEST = 1025;
    private TextView tvLogin;
    private EditText edtName, edtEmail, edtPhone, edtAddress, edtPassword, edtConfirmPass;
    private Button btnRegister;
    private RadioGroup radGr;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        tvLogin = findViewById(R.id.regis_tvLogin);
        edtName = findViewById(R.id.regis_edtName);
        edtEmail = findViewById(R.id.regis_edtEmail);
        edtPhone = findViewById(R.id.regis_edtPhone);
        edtAddress = findViewById(R.id.regis_edtAddress);
        edtPassword = findViewById(R.id.regis_edtPassword);
        edtConfirmPass = findViewById(R.id.regis_edtConfirmPassword);
        radGr = findViewById(R.id.regis_radGr);
        btnRegister = findViewById(R.id.regis_btnRegister);
        progressBar = findViewById(R.id.progressBar_register);

        //firebase
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        // TODO: Chuyển qua activity login
        tvLogin();
        // TODO: Tạo tài khoản
        createRegister();
    }

    // START
    private void tvLogin() {
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });
    }
    // END

    // START
    private void createRegister() {
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = edtName.getText().toString().trim();
                String email = edtEmail.getText().toString().trim();
                String phone = edtPhone.getText().toString().trim();
                String address = edtAddress.getText().toString().trim();
                String password = edtPassword.getText().toString();
                String confirmPass = edtConfirmPass.getText().toString();
                int genderID = radGr.getCheckedRadioButtonId();
                RadioButton buttonRadGender = radGr.findViewById(genderID);
                String gender = buttonRadGender.getText().toString();

                if (TextUtils.isEmpty(name)) {
                    edtName.setError("Vui lòng nhập tên");
                    edtName.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(email)) {
                    edtEmail.setError("Vui lòng nhập email");
                    edtEmail.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(phone)) {
                    edtPhone.setError("Vui lòng nhập số điện thoại");
                    edtPhone.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(address)) {
                    edtAddress.setError("Vui lòng nhập địa chỉ");
                    edtAddress.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    edtPassword.setError("Vui lòng nhập mật khẩu");
                    edtPhone.requestFocus();
                    return;
                }
                if (!password.equals(confirmPass)) {
                    Toast.makeText(RegisterActivity.this, "Mật khẩu và xác nhận mật khẩu khác nhau", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!isValidPassword(password)) {
                    edtPassword.setError("Mật khẩu phải trên 6 kí tự");
                    edtPassword.requestFocus();
                    return;
                }
                HashMap<String, String> params = new HashMap<>();
                params.put("name", name);
                params.put("email", email);
                params.put("phone", phone);
                params.put("address", address);
                params.put("password", password);
                params.put("gender", gender);

                // TODO: hàm PerformNetworkRequest
                PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_CREATE_USER, params, CODE_POST_REQUEST);
                request.execute();

                // TODO: Hàm CreateUser (fribase)
                createUser(email, name, password);
            }
        });
    }
    // END


    private void createUser(String email, String name, String pass) {
        DatabaseReference userRef = database.getReference("user");
        userRef.orderByChild("userName").equalTo(name).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Log.e("name", "exit");
                } else {
                    mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                String userId = task.getResult().getUser().getUid();
                                String imageUri = "https://firebasestorage.googleapis.com/v0/b/aitechc2-110ea.appspot.com/o/anhdaidienuser.png?alt=media&token=6e6bc29a-0ffc-44e8-b7bc-6d5649aed91b";
                                UsersBlogChat user = new UsersBlogChat(userId, name, email, "", imageUri); // đặt trường mật khẩu thành ""
                                DatabaseReference newUserRef = database.getReference("user").child(userId);
                                newUserRef.setValue(user)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    FirebaseUser currentUser = mAuth.getCurrentUser();
                                                    if (currentUser != null) {
                                                        UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                                                                .setDisplayName(name)
                                                                .build();
                                                        currentUser.updateProfile(profileUpdate).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    Log.d("TAG", "User profile updated.");
                                                                    finish();
                                                                }
                                                            }
                                                        });
                                                    }
                                                } else {
                                                    Log.e("eoro", "Error in creating the user");
                                                }
                                            }
                                        });
                            } else {
                                Log.e("eror", task.getException().getMessage());
                            }
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("eorr", "Error in checking name");
            }
        });
    }


    /**
     * Hàm PerformNetworkRequest là một lớp con của lớp AsyncTask, được sử dụng để thực hiện các yêu cầu
     * mạng trong nền và trả về kết quả vào UI thread. Nó có ba tham số đầu vào, bao gồm một chuỗi URL,
     * một HashMap chứa tham số yêu cầu và một requestCode.
     * Trong phương thức onPreExecute(), ProgressBar được hiển thị để thông báo cho người dùng rằng một
     * tác vụ đang được thực hiện. Trong phương thức onPostExecute(), kết quả trả về từ doInBackground()
     * được xử lý. Nếu không có lỗi, một thông báo Toast sẽ được hiển thị và một Intent sẽ được sử dụng
     * để chuyển hướng người dùng đến LoginActivity. Nếu có lỗi, thông báo Toast sẽ được hiển thị để thông
     * báo cho người dùng biết về lỗi.
     * Phương thức doInBackground() chứa mã để thực hiện yêu cầu mạng. Nó tạo ra một đối tượng RequestHandler
     * để gửi yêu cầu và nhận phản hồi. Nếu requestCode là CODE_POST_REQUEST, yêu cầu POST được gửi.
     * Nếu requestCode là CODE_GET_REQUEST, yêu cầu GET được gửi. Kết quả được trả về dưới dạng chuỗi.
     * <p>
     * CODE_GET_REQUEST = 1024 & CODE_POST_REQUEST = 1025 :giá trị 1024 tượng trưng cho yêu cầu lấy dữ liệu
     * (GET request), còn giá trị 1025 tượng trưng cho yêu cầu đăng ký (POST request). Sử dụng các giá trị
     * hằng số này giúp tăng tính ổn định và dễ bảo trì cho đoạn mã, vì chúng có thể dễ dàng được sửa đổi
     * hoặc thay đổi nếu cần thiết.
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
                    Toast.makeText(RegisterActivity.this, object.getString("message"), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                } else {
                    Toast.makeText(RegisterActivity.this, object.getString("message"), Toast.LENGTH_SHORT).show();
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

    // Validation
    // START
    public boolean isValidPassword(String password) {
        if (password.length() < 6) {
            return false;
        }
        return true;
    }
    // END
}