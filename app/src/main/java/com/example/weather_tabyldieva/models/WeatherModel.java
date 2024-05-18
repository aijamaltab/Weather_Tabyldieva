package com.example.weather_tabyldieva.models;

import com.google.gson.annotations.SerializedName;

public class WeatherModel {

    @SerializedName("icon")
    String icon;

    @SerializedName("cod")
    Integer cod;
}
