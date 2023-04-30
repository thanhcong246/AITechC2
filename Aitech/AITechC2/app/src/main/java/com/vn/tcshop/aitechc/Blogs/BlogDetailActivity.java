package com.vn.tcshop.aitechc.Blogs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vn.tcshop.aitechc.Adapters.CommentAdapter;
import com.vn.tcshop.aitechc.Models.Comment;
import com.vn.tcshop.aitechc.R;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class BlogDetailActivity extends AppCompatActivity {
    private ImageView imgPost, imgUserPost, imgCurrentUser, back_button;
    private TextView tvPostDesc, tvPostDateName, tvPostTitle;
    private EditText edtTextComment;
    private Button btnAddComment;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;
    RecyclerView RvComment;
    CommentAdapter commentAdapter;
    List<Comment> commentList;
    static String COMMENT_KEY = "Comment";
    String PostKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_detail);

        imgPost = findViewById(R.id.post_detail_img);
        imgUserPost = findViewById(R.id.post_detail_user_img);
        imgCurrentUser = findViewById(R.id.post_detail_currentUser_img);
        tvPostTitle = findViewById(R.id.post_detail_title);
        tvPostDesc = findViewById(R.id.post_detail_description);
        tvPostDateName = findViewById(R.id.post_detail_date_name);
        edtTextComment = findViewById(R.id.post_detail_comment);
        btnAddComment = findViewById(R.id.post_detail_add_comment);
        RvComment = findViewById(R.id.rv_comment);
        back_button = findViewById(R.id.back_button);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnAddComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference commentReference = firebaseDatabase.getReference("Comment").child(PostKey).push();
                String comment_content = edtTextComment.getText().toString();
                String uid = firebaseUser.getUid();
                String uname = firebaseUser.getDisplayName();
                String uimg = "";
                if (TextUtils.isEmpty(comment_content)) {
                    showMessage("Bạn chưa bình luận");
                    return;
                }
                if (firebaseUser.getPhotoUrl() == null) {
                    // Set default user with photo
                    uimg = "https://firebasestorage.googleapis.com/v0/b/aitechc2-110ea.appspot.com/o/user_photo%2Fanhdaidienuser.png?alt=media&token=cf229d09-44f7-4577-a6d7-e9e8157f2841";
                } else {
                    uimg = firebaseUser.getPhotoUrl().toString();
                }
                Comment comment = new Comment(comment_content, uid, uimg, uname);

                commentReference.setValue(comment).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        showMessage("Bạn đã bình luận");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("fail to add comment: " , e.getMessage());
                    }
                });
            }
        });

        String postImage = getIntent().getExtras().getString("postImage");
        Glide.with(this).load(postImage).into(imgPost);

        String postTitle = getIntent().getExtras().getString("title");
        tvPostTitle.setText(postTitle);

        String userpostImage = getIntent().getExtras().getString("userPhoto");
        Glide.with(this).load(userpostImage).into(imgUserPost);

        String postDecscription = getIntent().getExtras().getString("description");
        tvPostDesc.setText(postDecscription);

        if (firebaseUser.getPhotoUrl() == null) {
            // Set default image
            Glide.with(this)
                    .load("https://firebasestorage.googleapis.com/v0/b/aitechc2-110ea.appspot.com/o/user_photo%2Fanhdaidienuser.png?alt=media&token=cf229d09-44f7-4577-a6d7-e9e8157f2841")
                    .into(imgCurrentUser);
        } else {
            // Set user's photo
            Glide.with(this)
                    .load(firebaseUser.getPhotoUrl())
                    .into(imgCurrentUser);
        }

        PostKey = getIntent().getExtras().getString("postKey");

        String date = timestampToString(getIntent().getExtras().getLong("postDate"));
        tvPostDateName.setText(date);

        initRvComment();
    }

    private void initRvComment() {
        RvComment.setLayoutManager(new LinearLayoutManager(this));
        DatabaseReference commentRef = firebaseDatabase.getReference(COMMENT_KEY).child(PostKey);
        commentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                commentList = new ArrayList<>();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    Comment comment = snap.getValue(Comment.class);
                    commentList.add(comment);
                }
                edtTextComment.setText("");
                commentAdapter = new CommentAdapter(getApplicationContext(), commentList);
                RvComment.setAdapter(commentAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private String timestampToString(long time) {
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(time);
        Date date = calendar.getTime();
        String formattedDate = DateFormat.getDateInstance().format(date);
        return formattedDate;
    }

    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}