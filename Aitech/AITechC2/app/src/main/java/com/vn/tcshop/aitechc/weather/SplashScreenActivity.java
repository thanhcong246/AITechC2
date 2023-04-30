package com.vn.tcshop.aitechc.weather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.vn.tcshop.aitechc.R;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
    }
    public void movetoScreen1(View view) {
        Intent intent = new Intent(this,Screen1Activity.class);
        startActivity(intent);
        finish();
    }
}