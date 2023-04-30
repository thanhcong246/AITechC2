package com.vn.tcshop.aitechc.Blogs;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.vn.tcshop.aitechc.Adapters.PostAdapter;
import com.vn.tcshop.aitechc.Api.Api;
import com.vn.tcshop.aitechc.Carts.CartActivity;
import com.vn.tcshop.aitechc.MainActivity;
import com.vn.tcshop.aitechc.Models.Post;
import com.vn.tcshop.aitechc.Models.RequestHandler;
import com.vn.tcshop.aitechc.Products.ProductActivity;
import com.vn.tcshop.aitechc.Profile.ProfileEditActivity;
import com.vn.tcshop.aitechc.Profile.ProfileShowActivity;
import com.vn.tcshop.aitechc.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BlogActivity extends AppCompatActivity {
    private static final int PReqCode = 2;
    private static final int REQUESCODE = 2;
    private TextView blog_idUser;
    private Button btnAdd_blog, blog_add_submit;
    private ImageView imageAvt, imageBlog;
    private EditText edtTitle, edtDescription;
    Dialog popAddPost;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private Uri pickedImgUri = null;
    private RecyclerView recyclerView;
    PostAdapter postAdapter;
    List<Post> postList;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog);
        blog_idUser = findViewById(R.id.blog_id);
        bottomNavigationView = findViewById(R.id.bottom_navigation_bar);
        bottomNavigationView.setSelectedItemId(R.id.menu_blog);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        // --------
        recyclerView = findViewById(R.id.blog_recycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Posts");
        // --------

        Intent intent = getIntent();
        int id = intent.getIntExtra("id", -1);

        // Hiển thị thông tin lên giao diện
        blog_idUser.setText(String.valueOf(id));

        btnAdd_blog = findViewById(R.id.blog_add_btn);
        btnAdd_blog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iniPopup();
                popAddPost.show();
            }
        });

        // TODO: hàm bottomBar
        bottomBar();
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
                        getUserInfoId_home(blog_idUser.getText().toString());
                        return true;
                    case R.id.menu_profile:
                        getUserInfoId_profile(blog_idUser.getText().toString());
                        return true;
                    case R.id.menu_shopping_cart:
                        getUserInfoToCart(blog_idUser.getText().toString());
                        return true;
                    case R.id.menu_blog:
                        return true;
                    case R.id.menu_product:
                        getUserInfoProduct(blog_idUser.getText().toString());
                        return true;
                }
                return false;
            }
        });
    }
    // END

    // START
    private void getUserInfoToCart(String id) {
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
                    Intent intent = new Intent(BlogActivity.this, CartActivity.class);
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
    private void getUserInfoProduct(String id) {
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
                    Intent intent = new Intent(BlogActivity.this, ProductActivity.class);
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
    private void getUserInfoId_profile(String id) {
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
                    Intent intent = new Intent(BlogActivity.this, ProfileShowActivity.class);
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
    private void getUserInfoId_home(String id) {
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
                    Intent intent = new Intent(BlogActivity.this, MainActivity.class);
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

    private void iniPopup() {
        popAddPost = new Dialog(this);
        popAddPost.setContentView(R.layout.blog_add_post);
        popAddPost.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popAddPost.getWindow().setLayout(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.WRAP_CONTENT);
        popAddPost.getWindow().getAttributes().gravity = Gravity.TOP;

        imageAvt = popAddPost.findViewById(R.id.blog_add_imageView_avt);
        imageBlog = popAddPost.findViewById(R.id.blog_add_imageView);
        edtTitle = popAddPost.findViewById(R.id.blog_add_title);
        edtDescription = popAddPost.findViewById(R.id.blog_add_description);
        blog_add_submit = popAddPost.findViewById(R.id.btn_blog_add);

        // load image
        if (currentUser.getPhotoUrl() == null) {
            // Set default image
            Glide.with(this)
                    .load("https://firebasestorage.googleapis.com/v0/b/aitechc2-110ea.appspot.com/o/user_photo%2Fanhdaidienuser.png?alt=media&token=cf229d09-44f7-4577-a6d7-e9e8157f2841")
                    .into(imageAvt);
        } else {
            // Set user's photo
            Glide.with(this)
                    .load(currentUser.getPhotoUrl())
                    .into(imageAvt);
        }

        imageBlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAndRequestForPermission();
            }
        });

        blog_add_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!edtTitle.getText().toString().isEmpty() && !edtDescription.toString().isEmpty() && pickedImgUri != null) {
                    StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("blog_images");
                    final StorageReference imageFilePath = storageReference.child(pickedImgUri.getLastPathSegment());
                    imageFilePath.putFile(pickedImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String defaultPhotoUrl = "";
                                    if (currentUser.getPhotoUrl() == null) {
                                        // Set default user with photo
                                        defaultPhotoUrl = "https://firebasestorage.googleapis.com/v0/b/aitechc2-110ea.appspot.com/o/user_photo%2Fanhdaidienuser.png?alt=media&token=cf229d09-44f7-4577-a6d7-e9e8157f2841";
                                    } else {
                                        defaultPhotoUrl = currentUser.getPhotoUrl().toString();
                                    }
                                    String imageDowloadLink = uri.toString();
                                    Post post = new Post(edtTitle.getText().toString(), edtDescription.getText().toString(),
                                            imageDowloadLink, currentUser.getUid(), defaultPhotoUrl);
                                    addPost(post);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    showMessage(e.getMessage());
                                }
                            });
                        }
                    });
                } else {
                    showMessage("Vui lòng chọn ảnh và không được để trống");
                }
            }
        });
    }

    private void addPost(Post post) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Posts").push();
        String key = myRef.getKey();
        post.setPostKey(key);
        myRef.setValue(post).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                showMessage("Đăng bài thành công");
                popAddPost.dismiss();
            }
        });
    }

    private void checkAndRequestForPermission() {
        if (ContextCompat.checkSelfPermission(BlogActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(BlogActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Toast.makeText(this, "Cho phép truy cập", Toast.LENGTH_SHORT).show();
            } else {
                ActivityCompat.requestPermissions(BlogActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PReqCode);
            }
        } else {
            openGallery();
        }
    }

    private void openGallery() {
        // open gallery
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, REQUESCODE);
    }

    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUESCODE && data != null) {
            pickedImgUri = data.getData();
            Log.e("path", pickedImgUri.toString());
            imageBlog.setImageURI(pickedImgUri);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postList = new ArrayList<>();
                for (DataSnapshot postsnap : snapshot.getChildren()) {
                    Post post = postsnap.getValue(Post.class);
                    postList.add(post);
                }
                postAdapter = new PostAdapter(BlogActivity.this, postList);
                recyclerView.setAdapter(postAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}