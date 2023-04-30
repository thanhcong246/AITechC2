package com.vn.tcshop.aitechc.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.vn.tcshop.aitechc.R;
import com.vn.tcshop.aitechc.db.WeatherDatabase;
import com.vn.tcshop.aitechc.db.entities.cityName;
import com.vn.tcshop.aitechc.db.entities.cityWeather;

import java.util.ArrayList;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.MyViewHolder> {
    private final ArrayList<cityWeather> cities;
    Context context;
    private WeatherDatabase db;

    public WeatherAdapter(Context context, ArrayList<cityWeather> cities) {
        this.cities = cities;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // infalte the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.weather_recycler_view, parent, false);
        // set the view's size, margins, paddings and layout parameters
        MyViewHolder vh = new MyViewHolder(v); // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        db = Room.databaseBuilder(context.getApplicationContext(), WeatherDatabase.class, "weather-db").allowMainThreadQueries().build();
        cityWeather city = cities.get(position);

        cityName cityname = db.CityDAO().getCitybyID(city.cityid);

        holder.name.setText("TP: " + cityname.cityName);
        holder.temperature.setText(city.temperature);
        holder.windSpeed.setText("Gió: " + city.windSpeed);
        holder.windDir.setText("Hướng: " + city.windDir);
        holder.humidity.setText("Độ ẩm: " + city.humidity);
        holder.date.setText("Ngày: " + city.recordDate);

        // implement setOnClickListener event on item view.
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return cities.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        // init the item view's
        TextView name, temperature, windSpeed, windDir, humidity, date;

        public MyViewHolder(View itemView) {
            super(itemView);
            // get the reference of item view's
            name = (TextView) itemView.findViewById(R.id.city_name_text_id);
            temperature = (TextView) itemView.findViewById(R.id.temperature_id);
            windSpeed = (TextView) itemView.findViewById(R.id.wind_speed_id);
            windDir = (TextView) itemView.findViewById(R.id.wind_dir_id);
            humidity = (TextView) itemView.findViewById(R.id.wind_humidity_id);
            date = (TextView) itemView.findViewById(R.id.date_id);

        }
    }
}