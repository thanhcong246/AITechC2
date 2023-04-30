package com.vn.tcshop.aitechc.ChatBot;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.vn.tcshop.aitechc.Adapters.ImageAdapter;
import com.vn.tcshop.aitechc.Api.Api;
import com.vn.tcshop.aitechc.Models.ChatImgBot;
import com.vn.tcshop.aitechc.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ChatImgBotActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    TextView welcomeTextView;
    EditText messageEditText;
    ImageButton sendButton;
    ImageView back_button;
    LottieAnimationView lottieAnimationView;
    List<ChatImgBot> messageList;
    ImageAdapter messageAdapter;
    MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_img_bot);

        messageList = new ArrayList<>();
        recyclerView = findViewById(R.id.recycle_view_c);
        welcomeTextView = findViewById(R.id.welcome_text_c);
        lottieAnimationView = findViewById(R.id.chat_loading_bot_img);
        messageEditText = findViewById(R.id.message_edt_c);
        sendButton = findViewById(R.id.send_btn_c);
        back_button = findViewById(R.id.img_back_chatBot_c);

        //setup recycler view
        messageAdapter = new ImageAdapter(messageList);
        recyclerView.setAdapter(messageAdapter);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llm);
        llm.setStackFromEnd(true);

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String messageChat = messageEditText.getText().toString();
                if (TextUtils.isEmpty(messageChat)){
                    Toast.makeText(getApplicationContext(), "Bạn chưa nhập câu hỏi", Toast.LENGTH_SHORT).show();
                    return;
                }
                String question = messageEditText.getText().toString().trim();
                addToChat(question, ChatImgBot.SENT_BY_ME);
                messageEditText.setText("");
                callAPI(question);
                welcomeTextView.setVisibility(View.GONE);
                lottieAnimationView.setVisibility(View.GONE);
            }
        });
    }

    void addToChat(String message, String sentBy) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                messageList.add(new ChatImgBot(message, sentBy));
                messageAdapter.notifyDataSetChanged();
                recyclerView.smoothScrollToPosition(messageAdapter.getItemCount());
            }
        });
    }

    void addResponse(String response) {
        messageList.remove(messageList.size() - 1);
        addToChat(response, ChatImgBot.SENT_BY_BOT);
    }

    void callAPI(String question) {
        //okhttp
        messageList.add(new ChatImgBot("Bạn đợi Bot một chút... ", ChatImgBot.SENT_BY_BOTT));
        setmessageEditText(true);
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("prompt", question);
            jsonBody.put("size", "512x512");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody requestBody = RequestBody.create(jsonBody.toString(), JSON);
        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/images/generations")
                .header("Authorization", Api.KEY)
                .post(requestBody)
                .build();
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                addResponse("Failed to load response due to " + e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        String imgUrl = jsonObject.getJSONArray("data").getJSONObject(0).getString("url");
                        addResponse(imgUrl);
                        setmessageEditText(false);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    addResponse("Failed to load response due to " + response.body().string());
                }
            }
        });
    }
    void setmessageEditText(boolean edtSend){
        runOnUiThread(()->{
            if (edtSend){
                messageEditText.setEnabled(false);
            }else {
                messageEditText.setEnabled(true);
            }
        });
    }

}