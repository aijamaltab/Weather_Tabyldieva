package com.example.weather_tabyldieva.remote_data;


import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitBuilder {
    private static WeatherApi instance;
    private RetrofitBuilder(){

    }
    public static WeatherApi getInstance(){
        if(instance==null){
            instance=initInstance();
        }
        return instance;
    }

    private static WeatherApi initInstance(){
        return new Retrofit
                .Builder()
                .baseUrl("https://api.openweathermap.org")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(WeatherApi.class);
    }
}
