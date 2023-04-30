package com.vn.tcshop.aitechc.weather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.vn.tcshop.aitechc.Adapters.WeatherAdapter;
import com.vn.tcshop.aitechc.R;
import com.vn.tcshop.aitechc.db.WeatherDatabase;
import com.vn.tcshop.aitechc.db.entities.cityName;
import com.vn.tcshop.aitechc.db.entities.cityWeather;

import java.util.ArrayList;
import java.util.List;

public class WeatherHistoryActivity extends AppCompatActivity {
    ArrayList<cityWeather> cityWeathers = new ArrayList<>();
    Spinner dynamicSpinner;
    private WeatherDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_history);

        db = Room.databaseBuilder(this, WeatherDatabase.class, "weather-db").allowMainThreadQueries().build();

        List<cityWeather> allcityw = db.CityDAO().getAllRecord();
        cityWeathers.clear();

        cityWeathers.addAll(allcityw);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.cities_recyclerView);
        // set a LinearLayoutManager with default vertical orientation
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        // call the constructor of CustomAdapter to send the reference and data to Adapter
        WeatherAdapter customAdapter = new WeatherAdapter(this,cityWeathers);
        recyclerView.setAdapter(customAdapter); // set the Adapter to RecyclerView

        dynamicSpinner = findViewById(R.id.dynamic_spinner);

        String[] city_Names ;

        city_Names = db.CityDAO().getCityNames().toArray(new String[0]);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item_layout, city_Names);

        dynamicSpinner.setAdapter(adapter);

        dynamicSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                Log.v("item", (String) parent.getItemAtPosition(position));
                String n = (String) parent.getItemAtPosition(position);
                cityName cityname = db.CityDAO().getCity(n);
                List<cityWeather> allcityw = db.CityDAO().getAllRecordC(cityname.cityid);
                cityWeathers.clear();
                cityWeathers.addAll(allcityw);
                customAdapter.notifyDataSetChanged();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

    }

    public void backtoscreen2(View view) {
        finish();
    }
}