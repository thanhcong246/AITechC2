package com.vn.tcshop.aitechc.weather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.vn.tcshop.aitechc.R;

public class Screen1Activity extends AppCompatActivity {
    private EditText cityName;
    private String name ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen1);

        cityName = findViewById(R.id.city_name);
    }

    public void movetoScreen2(View view) {
        name = cityName.getText().toString();
        if (name.equals("")) {
            Toast.makeText(this, "Bạn chưa chọn thành phố.....", Toast.LENGTH_SHORT).show();
        }
        else {
            Intent intent = new Intent(this, Screen2Activity.class);
            intent.putExtra("name",name );
            startActivity(intent);
            finish();
        }
    }
    public void backtoSplash(View view) {
        onBackPressed();
    }
}