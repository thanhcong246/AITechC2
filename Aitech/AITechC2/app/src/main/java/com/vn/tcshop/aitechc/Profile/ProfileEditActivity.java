package com.vn.tcshop.aitechc.Profile;

import static android.view.View.GONE;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.vn.tcshop.aitechc.Api.Api;
import com.vn.tcshop.aitechc.Models.RequestHandler;
import com.vn.tcshop.aitechc.Models.UsersBlogChat;
import com.vn.tcshop.aitechc.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ProfileEditActivity extends AppCompatActivity {
    private static final int CODE_GET_REQUEST = 1024;
    private static final int CODE_POST_REQUEST = 1025;
    private static final int STORAGE_PERMISSION_CODE = 123;
    private static final int PICK_IMAGE_REQUEST = 1;
    static int PReqCode = 1;
    static int REQUESCODE = 1;
    private FirebaseAuth mAuth;
    FirebaseDatabase database;
    FirebaseStorage storage;
    private Button btnBack, btnEdit;
    private EditText edtName, edtPhone, edtAddress, edtPassword, edtConfirmPass;
    private TextView edtID, edtEmail;
    private RadioGroup radGr;
    private ProgressBar progressBar;
    private ImageView imgView, btn_imgView;
    private Uri filepath, pickedImgUri;
    private Bitmap bitmap;
    OkHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);
        edtID = findViewById(R.id.profile_edit_edtID);
        edtName = findViewById(R.id.profile_edit_edtName);
        edtEmail = findViewById(R.id.profile_edit_edtEmail);
        edtPhone = findViewById(R.id.profile_edit_edtPhone);
        edtAddress = findViewById(R.id.profile_edit_edtAddress);
        edtPassword = findViewById(R.id.profile_edit_edtPassword);
        edtConfirmPass = findViewById(R.id.profile_edit_edtCofirmPass);
        radGr = findViewById(R.id.profile_edit_radGr);
        btnEdit = findViewById(R.id.profile_edit_btnEdit);
        btnBack = findViewById(R.id.profile_edit_btnBack);
        progressBar = findViewById(R.id.progressBar_profile_edit);
        imgView = findViewById(R.id.profile_edit_imgView);
        btn_imgView = findViewById(R.id.profile_edit_imgView_btn);

        //firebase
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();


        // Hiển thị thông tin
        Intent intent = getIntent();
        int id = intent.getIntExtra("id", -1);
        String name = intent.getStringExtra("name");
        String email = intent.getStringExtra("email");
        String phone = intent.getStringExtra("phone");
        String address = intent.getStringExtra("address");
        String gender = intent.getStringExtra("gender");
        if (gender.equals("Nam")) {
            radGr.check(R.id.profile_edit_radMale);
        } else {
            radGr.check(R.id.profile_edit_radFemale);
        }
        edtID.setText(String.valueOf(id));
        edtName.setText(name);
        edtEmail.setText(email);
        edtPhone.setText(phone);
        edtAddress.setText(address);
        client = new OkHttpClient();

        // TODO: Hàm show_image_profile
        show_image_profile(imgView, String.valueOf(id));

        // ---------------

        // TODO: Hàm back
        back();
        // TODO: Hàm updateProfile
        updateProfile();
        // TODO: Hàm imgProfileShow
        imgProfileShow();
        // TODO: Hàm requestStoragePermission
        requestStoragePermission();
    }

    /**
     * Phương thức này được sử dụng để yêu cầu quyền truy cập vào bộ nhớ ngoài của thiết bị
     * (WRITE_EXTERNAL_STORAGE). Nó kiểm tra xem quyền đã được cấp hay chưa, nếu đã cấp thì không yêu cầu lại.
     * Nếu quyền chưa được cấp, nó kiểm tra xem có cần hiển thị giải thích cho người dùng về lý do yêu cầu quyền
     * không. Sau đó, nó yêu cầu quyền truy cập vào bộ nhớ ngoài bằng cách sử dụng phương thức
     * ActivityCompat.requestPermissions(). Nếu người dùng chưa cấp quyền, hộp thoại yêu cầu quyền sẽ hiển thị
     * để người dùng có thể chấp nhận hoặc từ chối quyền truy cập.
     */
    // START
    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
        }
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }
    // END

    /**
     * Phương thức imgProfileShow() được sử dụng để hiển thị hộp thoại để chọn hình ảnh từ bộ nhớ của thiết bị
     * khi người dùng nhấn vào nút btn_imgView.
     * <p>
     * Trong phương thức này, ta tạo một đối tượng Intent với hành động là Intent.ACTION_PICK để chọn một tệp ảnh.
     * Chúng ta cũng đặt kiểu dữ liệu cho intent bằng phương thức setType() và truyền vào "image/*" để chỉ định
     * rằng chỉ các tệp ảnh có thể được chọn.
     * <p>
     * Sau đó, ta gọi phương thức startActivityForResult() để bắt đầu activity mới và hiển thị danh sách tệp ảnh
     * từ bộ nhớ của thiết bị cho người dùng chọn. Phương thức này yêu cầu một đối tượng Intent và một mã yêu
     * cầu (PICK_IMAGE_REQUEST trong trường hợp này), giúp phân biệt giữa các kết quả trả về khác nhau từ các
     * activity khác nhau mà ta có thể khởi động.
     */
    // START
    private void imgProfileShow() {
        btn_imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });
    }
    // END

    /**
     * Phương thức onRequestPermissionsResult được gọi khi người dùng cấp hoặc từ chối quyền mà ứng dụng yêu cầu.
     * Nó có ba tham số đầu vào: requestCode, permissions và grantResults.
     * <p>
     * requestCode: mã yêu cầu được sử dụng để xác định yêu cầu nào đang được xử lý.
     * permissions: mảng chuỗi chứa danh sách các quyền được yêu cầu.
     * grantResults: mảng số nguyên chứa kết quả cấp quyền tương ứng với mỗi quyền được yêu cầu. Nếu quyền được
     * cấp, giá trị tương ứng sẽ là PackageManager.PERMISSION_GRANTED, ngược lại nếu bị từ chối, giá trị
     * sẽ là PackageManager.PERMISSION_DENIED.
     * <p>
     * Trong phương thức này, nếu requestCode trùng với mã yêu cầu cấp quyền lưu trữ và grantResults cho biết rằng
     * quyền đã được cấp (grantResults[0] == PackageManager.PERMISSION_GRANTED) thì sẽ hiển thị thông báo
     * "Permission granted now you can read the storage", nếu không sẽ hiển thị thông báo "Oops you just denied
     * the permission".
     */
    // START
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Quyền đã được cấp", Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(this, "Bạn đã từ chối quyền", Toast.LENGTH_LONG).show();
            }
        }
    }
    // END

    /**
     * Phương thức onActivityResult() được gọi sau khi hoạt động con được khởi động bằng cách gọi
     * startActivityForResult() và kết thúc. Nó nhận các tham số là requestCode, resultCode, và data.
     * <p>
     * Trong phương thức này, requestCode là mã yêu cầu trả về khi hoạt động con được khởi động, resultCode là
     * kết quả trả về khi hoạt động con kết thúc, và data là một đối tượng Intent chứa dữ liệu trả về từ hoạt
     * động con.
     * <p>
     * Nếu requestCode là PICK_IMAGE_REQUEST và resultCode là RESULT_OK và data khác null và data.getData() không
     * phải là null, thì nó lấy đường dẫn của tập tin hình ảnh được chọn, cố gắng tải bitmap từ đường dẫn đó và
     * đặt nó vào ImageView imgView.
     */
    // START
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            pickedImgUri = data.getData();
            Log.e("path",pickedImgUri.toString());
            filepath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filepath);
                imgView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    // END

    /**
     * Phương thức uploadImage() có chức năng thực hiện tải lên một ảnh từ thiết bị của người dùng lên server.
     * <p>
     * Đầu tiên, phương thức này sử dụng intent để nhận được id của người dùng hiện tại từ Activity trước đó.
     * Sau đó, phương thức này sử dụng phương thức getPath() để lấy đường dẫn của tập tin ảnh trên thiết bị.
     * <p>
     * Tiếp theo, phương thức này tạo một request body dưới dạng multipart/form-data để chứa thông tin về id của
     * người dùng và tập tin ảnh. Trong đó, phương thức addFormDataPart() được sử dụng để thêm các phần tử vào
     * request body, trong đó "id" là tên của phần tử và String.valueOf(id) là giá trị của phần tử. "upload" là
     * tên tập tin ảnh được chọn và RequestBody.create() được sử dụng để tạo đối tượng RequestBody cho tập tin này.
     * <p>
     * Sau đó, phương thức này tạo một request đến server với url của server và request body được tạo ở trên.
     * Request này sử dụng phương thức POST để tải lên tập tin ảnh.
     * <p>
     * Cuối cùng, phương thức này sử dụng Executor để thực hiện request trên một luồng khác, vì việc thực hiện
     * request trên luồng chính có thể gây khó khăn cho hiệu suất của ứng dụng. Nếu request không thành công,
     * phương thức này sẽ in ra một thông báo lỗi.
     */
    // START
    private void uploadImage() {
        Intent intent = getIntent();
        int id = intent.getIntExtra("id", -1);
        String path = getPath(filepath);

        // Tạo request body
        File file = new File(path);
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("id", String.valueOf(id))
                .addFormDataPart("upload", file.getName(),
                        RequestBody.create(MediaType.parse("image/*"), file))
                .build();

        // Tạo request
        Request request = new Request.Builder()
                .url(Api.UPLOAD_URL_IMG_PROFILE)
                .post(requestBody)
                .build();

        // Thực hiện request trên một luồng khác
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            OkHttpClient client = new OkHttpClient();
            try {
                Response response = client.newCall(request).execute();
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
    // END

    /**
     * Phương thức getPath được sử dụng trong ứng dụng Android để lấy đường dẫn tuyệt đối của tệp được chọn từ
     * thư viện hình ảnh của thiết bị. Đầu vào của phương thức là một đối tượng Uri đại diện cho tệp được chọn.
     * Phương thức sử dụng đối tượng Cursor để truy vấn các thông tin về tệp được chọn từ MediaStore.Images.Media,
     * bao gồm cả đường dẫn tệp. Sau khi truy vấn, phương thức trích xuất đường dẫn tệp từ Cursor và trả về đường
     * dẫn đó dưới dạng một chuỗi String. Nếu tệp không được tìm thấy, phương thức sẽ trả về giá trị null.
     */
    // START
    private String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String path = cursor.getString(column_index);
        cursor.close();
        return path;
    }
    // END

    /**
     * Phương thức updateProfile() được gọi khi người dùng nhấn nút "Cập nhật" trong giao diện chỉnh sửa thông
     * tin cá nhân. Trong phương thức này, các giá trị nhập vào từ các trường nhập liệu (tên, email, số điện thoại,
     * địa chỉ, mật khẩu, xác nhận mật khẩu và giới tính) được lấy ra và kiểm tra tính hợp lệ của chúng.
     * <p>
     * Nếu các giá trị nhập vào hợp lệ, một PerformNetworkRequest được tạo ra và thực thi để gửi yêu cầu cập nhật
     * thông tin cá nhân của người dùng đến server thông qua API Api.URL_UPDATE_USER. Nếu người dùng đã chọn hình
     * ảnh để tải lên, hình ảnh sẽ được tải lên trước khi thực hiện yêu cầu cập nhật thông tin cá nhân.
     * <p>
     * Nếu giá trị nhập vào không hợp lệ, lỗi sẽ được hiển thị và phương thức sẽ dừng lại.
     */
    // START
    private void updateProfile() {
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String profile_edit_id = edtID.getText().toString();
                String profile_edit_name = edtName.getText().toString().trim();
                String profile_edit_email = edtEmail.getText().toString().trim();
                String profile_edit_phone = edtPhone.getText().toString().trim();
                String profile_edit_address = edtAddress.getText().toString().trim();
                String profile_edit_password = edtPassword.getText().toString();
                String profile_edit_confirmPass = edtConfirmPass.getText().toString();
                int genderID = radGr.getCheckedRadioButtonId();
                RadioButton buttonRadGender = radGr.findViewById(genderID);
                String profile_edit_gender = buttonRadGender.getText().toString();
                if (TextUtils.isEmpty(profile_edit_name)) {
                    edtName.setError("Vui lòng nhập tên");
                    edtName.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(profile_edit_email)) {
                    edtEmail.setError("Vui lòng nhập email");
                    edtEmail.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(profile_edit_phone)) {
                    edtPhone.setError("Vui lòng nhập số điện thoại");
                    edtPhone.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(profile_edit_address)) {
                    edtAddress.setError("Vui lòng nhập địa chỉ");
                    edtEmail.requestFocus();
                    return;
                }
                if (!TextUtils.isEmpty(profile_edit_password) && !isValidPassword(profile_edit_password)) {
                    edtPassword.setError("Mật khẩu phải trên 6 kí tự");
                    edtPassword.requestFocus();
                    return;
                }
                if (!profile_edit_password.equals(profile_edit_confirmPass)) {
                    Toast.makeText(ProfileEditActivity.this, "Mật khẩu và xác nhận mật khẩu khác nhau", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (filepath != null) {
                    HashMap<String, String> params = new HashMap<>();
                    params.put("id", profile_edit_id);
                    params.put("name", profile_edit_name);
                    params.put("email", profile_edit_email);
                    params.put("phone", profile_edit_phone);
                    params.put("address", profile_edit_address);
                    params.put("password", profile_edit_password);
                    params.put("gender", profile_edit_gender);
                    PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_UPDATE_USER, params, CODE_POST_REQUEST);
                    request.execute();
                    uploadImage();
                    updateUserInfo(profile_edit_name,profile_edit_email,profile_edit_password,pickedImgUri,mAuth.getCurrentUser());
                    saveUserDataChat(profile_edit_name,profile_edit_email,profile_edit_password,pickedImgUri);
                } else {
                    HashMap<String, String> params = new HashMap<>();
                    params.put("id", profile_edit_id);
                    params.put("name", profile_edit_name);
                    params.put("email", profile_edit_email);
                    params.put("phone", profile_edit_phone);
                    params.put("address", profile_edit_address);
                    params.put("password", profile_edit_password);
                    params.put("gender", profile_edit_gender);
                    PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_UPDATE_USER, params, CODE_POST_REQUEST);
                    request.execute();
                    updateUserInfo(profile_edit_name,profile_edit_email,profile_edit_password,null,mAuth.getCurrentUser());
                    saveUserDataChat(profile_edit_name,profile_edit_email,profile_edit_password,null);
                }
            }
        });
    }
    // END

    private void updateUserInfo(String name, String email, String pass, Uri pickedImgUri, FirebaseUser currentUser) {
        UserProfileChangeRequest.Builder profileUpdateBuilder = new UserProfileChangeRequest.Builder()
                .setDisplayName(name);
        if (pickedImgUri != null) {
            StorageReference mStorage = FirebaseStorage.getInstance().getReference().child("user_photo");
            final StorageReference imageFilePath = mStorage.child(pickedImgUri.getLastPathSegment());
            imageFilePath.putFile(pickedImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            profileUpdateBuilder.setPhotoUri(uri);
                            UserProfileChangeRequest profileUpdate = profileUpdateBuilder.build();
                            updateUserInfoAfterProfileUpdate(profileUpdate, currentUser, email, pass);
                        }
                    });
                }
            });
        } else {
            updateUserInfoAfterProfileUpdate(profileUpdateBuilder.build(), currentUser, email, pass);
        }
    }

    private void updateUserInfoAfterProfileUpdate(UserProfileChangeRequest profileUpdate, FirebaseUser currentUser, String email, String pass) {
        currentUser.updateProfile(profileUpdate).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    if (!TextUtils.isEmpty(pass)) {
                        currentUser.updatePassword(pass).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d("pass","update pass ss");
                                } else {
                                    Log.e("error", task.getException().getMessage());
                                }
                            }
                        });
                    }
                } else {
                    Log.e("eroro",task.getException().getMessage());
                }
            }
        });
    }


    private void saveUserDataChat(String name, String email, String password, Uri setImageUri) {
        DatabaseReference reference = database.getReference().child("user").child(mAuth.getUid());
        StorageReference storageReference = storage.getReference().child("upload").child(mAuth.getUid());

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String oldName = snapshot.child("userName").getValue().toString();
                String oldProfile = snapshot.child("profilepic").getValue().toString();

                if (name.equals(oldName) && setImageUri == null) {
                    // Nothing has changed, so do not update the database
                    finish();
                } else {
                    // Something has changed, so update the database
                    if (setImageUri!=null) {
                        storageReference.putFile(setImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String finalImageUri = uri.toString();
                                        UsersBlogChat users = new UsersBlogChat(mAuth.getUid(), name, email, password, finalImageUri);
                                        reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    finish();
                                                } else {
                                                   Log.e("er",";pop");
                                                }
                                            }
                                        });
                                    }
                                });
                            }
                        });
                    } else {
                        UsersBlogChat users = new UsersBlogChat(mAuth.getUid(), name, email, password, oldProfile);
                        reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    finish();
                                } else {
                                    Log.e("eror","Something went wrong");
                                }
                            }
                        });
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("error",database.toString());
            }
        });
    }




    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    // START
    private void back() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getUserInfoId(edtID.getText().toString());
            }
        });
    }
    // END


    /**
     * Đây là một lớp PerformNetworkRequest được xây dựng để thực hiện các yêu cầu mạng (network request)
     * bất đồng bộ trong một AsyncTask. Lớp này có các thuộc tính là url, params và requestCode được sử dụng để
     * thực hiện yêu cầu mạng.
     * <p>
     * Trong phương thức onPreExecute(), ta sẽ hiển thị progressBar để thể hiện tiến độ thực hiện yêu cầu mạng.
     * Trong phương thức onPostExecute(), ta sẽ kiểm tra kết quả trả về từ yêu cầu mạng, nếu không có lỗi, ta sẽ
     * hiển thị thông báo và gọi phương thức getUserInfoId() để lấy thông tin người dùng theo id. Ngược lại, nếu
     * có lỗi, ta sẽ hiển thị thông báo lỗi.
     * <p>
     * Trong phương thức doInBackground(), ta sử dụng đối tượng RequestHandler để gửi yêu cầu mạng, sử dụng phương
     * thức sendPostRequest() nếu requestCode là CODE_POST_REQUEST, hoặc sử dụng phương thức sendGetRequest() nếu
     * requestCode là CODE_GET_REQUEST. Sau khi thực hiện yêu cầu mạng, phương thức này sẽ trả về kết quả dưới dạng
     * chuỗi
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
                    getUserInfoId(edtID.getText().toString());
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
     * Phương thức getUserInfoId lấy thông tin người dùng dựa trên id được truyền vào. Nó sử dụng lớp AsyncTask
     * để thực hiện yêu cầu mạng trên một luồng khác để không làm đóng băng giao diện người dùng.
     * <p>
     * Trong phương thức doInBackground, nó sử dụng RequestHandler để gửi yêu cầu POST đến API với tham số là
     * id của người dùng cần lấy thông tin.
     * <p>
     * Trong phương thức onPostExecute, nó xử lý phản hồi từ API bằng cách tạo một đối tượng JSONObject, lấy mảng
     * người dùng và đối tượng người dùng đầu tiên trong mảng để lấy thông tin chi tiết của người dùng.
     * <p>
     * Sau đó, nó đóng gói thông tin người dùng vào một Intent và chuyển sang ProfileShowActivity để hiển thị
     * thông tin. Nếu có lỗi xảy ra, nó in ra stack trace để debug.
     */
    // START
    private void getUserInfoId(String id) {
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
                    Intent intent = new Intent(ProfileEditActivity.this, ProfileShowActivity.class);
                    intent.putExtra("id", id);
                    intent.putExtra("name", name);
                    intent.putExtra("email", email);
                    intent.putExtra("phone", phone);
                    intent.putExtra("address", address);
                    intent.putExtra("gender", gender);
                    intent.putExtra("image", image);

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

    // Validation
    // START
    public boolean isValidPassword(String password) {
        if (password.length() < 6) {
            return false;
        }
        return true;
    }
    // END

    /**
     * Phương thức show_image_profile được sử dụng để hiển thị hình ảnh của người dùng trong ứng dụng. Nó lấy
     * mã người dùng dưới dạng chuỗi (id), sau đó tạo một yêu cầu POST đến API của ứng dụng để lấy thông tin của
     * người dùng có mã đó. Kết quả trả về là một đối tượng JSON chứa thông tin người dùng, trong đó có một trường
     * chứa đường dẫn đến hình ảnh của người dùng. Phương thức sử dụng thư viện Picasso để tải hình ảnh từ URL và
     * hiển thị nó trong ImageView được truyền vào phương thức. Nếu không tìm thấy hình ảnh của người dùng, phương
     * thức sẽ hiển thị mặc định hình ảnh của ứng dụng thay vì hình ảnh người dùng.
     */
    // START
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
                        if (userObject.isNull("image")) {
                            imgView.setImageResource(R.drawable.ic_person);
                            return;
                        }
                        ProfileEditActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Picasso.get()
                                        .load(imgUrl)
                                        .resize(400, 400)
                                        .centerCrop()
                                        .into(imageView);
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