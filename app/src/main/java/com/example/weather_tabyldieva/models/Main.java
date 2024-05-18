package com.example.weather_tabyldieva.models;

import com.google.gson.annotations.SerializedName;

public class Main {

    @SerializedName("temp")
    private Double temp;

    @SerializedName("name")
    private String name;

    @SerializedName("temp_max")
    private Double tempMax;

    @SerializedName("temp_min")
    private Double tempMin;

    @SerializedName("humidity")
    private int humidity;
    @SerializedName("pressure")
    private int pressure;

    public Double getTemp() {
        return temp;
    }

    public String getName() {
        return name;
    }

    public Double getTempMax() {
        return tempMax;
    }

    public Double getTempMin() {
        return tempMin;
    }

    public int getHumidity() {
        return humidity;
    }

    public int getPressure() {
        return pressure;
    }
}
