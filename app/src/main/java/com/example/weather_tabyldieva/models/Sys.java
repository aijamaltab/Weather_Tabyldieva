package com.example.weather_tabyldieva.models;

import com.google.gson.annotations.SerializedName;

public class Sys {

    @SerializedName("sunrise")
    private long sunrise;

    @SerializedName("sunset")
    private long sunset;

    public long getSunrise() {
        return sunrise;
    }

    public long getSunset() {
        return sunset;
    }
}
