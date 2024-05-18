package com.example.weather_tabyldieva.remote_data;

import com.example.weather_tabyldieva.models.Model;
import com.example.weather_tabyldieva.models.WeatherModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherApi {

    public static final String URL_KEY ="b5d5a5bf1bc1ce3ef2c2786cc2844805";

    public static final String BASE_URL ="https://api.openweathermap.org";

    @GET("/data/2.5/weather")
    Call<Model> getCurrentWeather(
            @Query("q") String name,
            @Query("appid") String key);

    @GET("/data/2.5/weather?q=London&appid=b5d5a5bf1bc1ce3ef2c2786cc2844805")
    Call<WeatherModel> getWeather(
            @Query("q") String name,
            @Query("appid") String key);
}
