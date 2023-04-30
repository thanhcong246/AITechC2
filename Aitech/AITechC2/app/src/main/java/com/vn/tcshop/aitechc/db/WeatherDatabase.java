package com.vn.tcshop.aitechc.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.vn.tcshop.aitechc.db.dao.cityDAO;
import com.vn.tcshop.aitechc.db.entities.cityName;
import com.vn.tcshop.aitechc.db.entities.cityWeather;

@Database(entities = {cityWeather.class, cityName.class}, version = 1)
public abstract class WeatherDatabase extends RoomDatabase {
    public abstract cityDAO CityDAO();
}
