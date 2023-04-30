package com.vn.tcshop.aitechc.weather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.vn.tcshop.aitechc.R;
import com.vn.tcshop.aitechc.db.WeatherDatabase;
import com.vn.tcshop.aitechc.db.entities.cityName;
import com.vn.tcshop.aitechc.db.entities.cityWeather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Screen2Activity extends AppCompatActivity {
    private WeatherDatabase db;
    private String name, url;
    private TextView cityTextView, temperatureTextView, descriptionTextView;
    private TextView windSpeedTextView, windDirectionTextView, pressureTextView, humdidityTextView;
    private JSONObject find;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen2);
        cityTextView = findViewById(R.id.city_name_textView);
        temperatureTextView = findViewById(R.id.temperature_textView);
        descriptionTextView = findViewById(R.id.description_textView);
        windSpeedTextView = findViewById(R.id.wind_speed_textView);
        windDirectionTextView = findViewById(R.id.wind_direction_textView);
        pressureTextView = findViewById(R.id.pressure_textView);
        humdidityTextView = findViewById(R.id.humidity_textView);

        db = Room.databaseBuilder(this, WeatherDatabase.class, "weather-db").allowMainThreadQueries().build();

        name = getIntent().getStringExtra("name");
        url = "https://api.weatherbit.io/v2.0/current?&city=" + name + "&country=VN&key=98a8a04f53c34112b27d63c13c122b39";

        Search();
    }

    private void Search() {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            try {

                cityName city = db.CityDAO().getCity(name.toLowerCase());
                if (city == null) {
                    cityName c = new cityName();
                    c.cityName = name;
                    db.CityDAO().insert(c);
                }

                cityName city1 = db.CityDAO().getCity(name);
                Toast.makeText(this, "Thành phố : " + city1.cityName, Toast.LENGTH_SHORT).show();

                JSONArray details = response.getJSONArray("data");

                find = details.getJSONObject(0);

                cityTextView.setText(find.getString("city_name"));
                temperatureTextView.setText(find.getString("temp") + "°C");
                JSONObject weather = find.getJSONObject("weather");
                String desc = weather.getString("description");
                descriptionTextView.setText(desc);
                windSpeedTextView.setText(find.getString("wind_spd"));
                windDirectionTextView.setText(find.getString("wind_dir"));
                pressureTextView.setText(find.getString("pres"));
                humdidityTextView.setText(find.getString("rh"));

                cityWeather cityWeathers = new cityWeather();
                cityWeathers.cityid = city1.cityid;
                cityWeathers.description = weather.getString("description");
                cityWeathers.windSpeed = find.getString("wind_spd");
                cityWeathers.humidity = find.getString("rh");
                cityWeathers.percip = find.getString("pres");
                cityWeathers.windDir = find.getString("wind_dir");
                cityWeathers.recordDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                cityWeathers.temperature = find.getString("temp") + "°C";

                db.CityDAO().insert(cityWeathers);

            } catch (JSONException e) {
                Toast.makeText(this, "Đã xảy ra lỗi vui lòng xem lại thành phố đang tìm kiếm", Toast.LENGTH_SHORT).show();
            }
        }, error -> {

        });

        Volley.newRequestQueue(this).add(request);
    }

    public void backtoscreen1(View view) {
        Intent intent = new Intent(this, Screen1Activity.class);
        startActivity(intent);
        finish();
    }

    public void movetohistory(View view) {
        Intent intent = new Intent(this, WeatherHistoryActivity.class);
        startActivity(intent);

    }
}