package com.vn.tcshop.aitechc.ResetPass;

import static android.view.View.GONE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.vn.tcshop.aitechc.Api.Api;
import com.vn.tcshop.aitechc.LoginActivity;
import com.vn.tcshop.aitechc.Models.RequestHandler;
import com.vn.tcshop.aitechc.Profile.ProfileEditActivity;
import com.vn.tcshop.aitechc.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ConfirmPassLogActivity extends AppCompatActivity {
    private static final int CODE_GET_REQUEST = 1024;
    private static final int CODE_POST_REQUEST = 1025;
    private FirebaseAuth mAuth;
    private TextView resetPass_tvLogin;
    private EditText edtEmail, edtPassword, edtConfirmPass;
    private Button btnReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_pass_log);
        resetPass_tvLogin = findViewById(R.id.resetPass_tvLogin);
        edtEmail = findViewById(R.id.resetPass_Email);
        edtPassword = findViewById(R.id.resetPass_Password);
        edtConfirmPass = findViewById(R.id.resetPass_ComfirmPassword);
        btnReset = findViewById(R.id.resetPass_btnSubmit);

        //firebase
        mAuth = FirebaseAuth.getInstance();

        loginTv();

        resetPassUser();
    }

    private void resetPassUser() {
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = edtEmail.getText().toString();
                String passwrod = edtPassword.getText().toString();
                String confirmPass = edtConfirmPass.getText().toString();
                if (TextUtils.isEmpty(email)) {
                    edtEmail.setError("Vui lòng nhập email");
                    return;
                }
                if (TextUtils.isEmpty(passwrod)) {
                    edtPassword.setError("Vui lòng nhập mật khẩu");
                    return;
                }
                if (!TextUtils.isEmpty(passwrod) && !isValidPassword(passwrod)) {
                    edtPassword.setError("Mật khẩu phải trên 6 kí tự");
                    edtPassword.requestFocus();
                    return;
                }
                if (!passwrod.equals(confirmPass)) {
                    Toast.makeText(ConfirmPassLogActivity.this, "Mật khẩu và xác nhận mật khẩu khác nhau", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Kiểm tra định dạng email
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(ConfirmPassLogActivity.this, "Địa chỉ email không đúng định dạng", Toast.LENGTH_SHORT).show();
                    return;
                }
                HashMap<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("password", passwrod);
                PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_RESET_PASS_USER, params, CODE_POST_REQUEST);
                request.execute();
                setResetPassUserInfo(email, passwrod, mAuth.getCurrentUser());

            }
        });
    }

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
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject object = new JSONObject(s);
                if (!object.getBoolean("error")) {
                    Toast.makeText(ConfirmPassLogActivity.this, object.getString("message"), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ConfirmPassLogActivity.this, LoginActivity.class));
                } else {
                    Toast.makeText(ConfirmPassLogActivity.this, object.getString("message"), Toast.LENGTH_SHORT).show();
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

    private void setResetPassUserInfo(String email, String newPassword, FirebaseUser currentUser) {
        FirebaseAuth.getInstance().fetchSignInMethodsForEmail(email)
                .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                    @Override
                    public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                        boolean emailExists = !task.getResult().getSignInMethods().isEmpty();
                        if (emailExists) {
                            // Thực hiện đổi mật khẩu
                            currentUser.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        finish();
                                    } else {
                                        Log.e("error", task.getException().getMessage());
                                    }
                                }
                            });
                        } else {
                            // Thông báo email không tồn tại
                            Log.e("email","email không tồn tại");
                        }
                    }
                });
    }

    private void loginTv() {
        resetPass_tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ConfirmPassLogActivity.this, LoginActivity.class));
            }
        });
    }

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