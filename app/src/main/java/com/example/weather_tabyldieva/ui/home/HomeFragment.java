package com.example.weather_tabyldieva.ui.home;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.example.weather_tabyldieva.R;
import com.example.weather_tabyldieva.databinding.FragmentHomeBinding;
import com.example.weather_tabyldieva.models.Clouds;
import com.example.weather_tabyldieva.models.Main;
import com.example.weather_tabyldieva.models.Model;
import com.example.weather_tabyldieva.models.Sys;
import com.example.weather_tabyldieva.models.Wind;
import com.example.weather_tabyldieva.remote_data.RetrofitBuilder;
import com.example.weather_tabyldieva.remote_data.WeatherApi;

import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private Integer temperature;
    private Integer tempMaximal;
    private Integer tempMinimal;
    private int humidity;
    private int windSpeed;
    private int cloudiness;
    private String currentTime = java.text.DateFormat.getDateTimeInstance().format(new Date());

    final String apiKey = WeatherApi.URL_KEY;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        binding.rainLotty.setAnimation(R.raw.rain);
        binding.snowLotty.setAnimation(R.raw.snow);
        binding.localtime.setText(currentTime);

        Call<Model> call = RetrofitBuilder.getInstance().getCurrentWeather("Bishkek", apiKey);

        call.enqueue(new Callback<Model>() {

            @Override
            public void onResponse(Call<Model> call, Response<Model> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Main main_model = response.body().getMain_model();
                    Wind wind_model = response.body().getWind_model();
                    Clouds clouds_model = response.body().getClouds_model();
                    Sys sys_model = response.body().getSys_model();

                    temperature = makeFromKelvin(main_model.getTemp());
                    tempMaximal = makeFromKelvin(main_model.getTempMax());
                    tempMinimal = makeFromKelvin(main_model.getTempMin());

                    humidity = main_model.getHumidity();
                    windSpeed = (int) wind_model.getSpeed();
                    cloudiness = clouds_model.getAll();

                    binding.tempC.setText(temperature + "°C");
                    binding.maxMinTemp.setText(tempMaximal + " °C↑  \n" + tempMinimal + " °C↓");
                    binding.cityName.setText("Bishkek");
                    binding.humidity.setText(humidity + " %");
                    binding.pressure.setText(main_model.getPressure() + "\nmBar");
                    binding.wind.setText(windSpeed + " m/s");
                    binding.cloudy.setText(cloudiness + " %");
                    binding.sunrise.setText(String.valueOf(getCurrDateTime(sys_model.getSunrise())));
                    binding.sunset.setText(String.valueOf(getCurrDateTime(sys_model.getSunset())));
                    binding.timeZone.setText(String.valueOf(response.body().getTimeZone()));

                    boolean isDayTime = isDayTime(sys_model.getSunrise(), sys_model.getSunset());
                    updateWeatherConditions(isDayTime);
                } else {
                    Toast.makeText(requireActivity(), "No WeatherForecast data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Model> call, @NonNull Throwable throwable) {
                Toast.makeText(requireActivity(), "Failed to fetch data: " + throwable.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                Log.e("TAG", throwable.getLocalizedMessage());
            }
        });

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.slideUpBottomSheet.setOnClickListener(v -> {
            if (binding.bottomSheet.getVisibility() == View.GONE) {
                binding.bottomSheet.setVisibility(View.VISIBLE);
            } else {
                binding.bottomSheet.setVisibility(View.GONE);
            }

            binding.rainLotty.setVisibility(View.INVISIBLE);
            binding.snowLotty.setVisibility(View.INVISIBLE);
            binding.blueSky.setVisibility(View.VISIBLE);
            binding.badWeatherSky.setVisibility(View.INVISIBLE);
            binding.condition.setText("...");
            binding.inputCity.setText("...");
            binding.isRainOrNot.setText("...");
            binding.sun.setVisibility(View.INVISIBLE);
        });

        binding.search.setOnClickListener(v1 -> {
            if (!binding.inputCity.getText().toString().isEmpty()) {
                Call<Model> call = RetrofitBuilder.getInstance().getCurrentWeather(binding.inputCity.getText().toString(), apiKey);
                call.enqueue(new Callback<Model>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(Call<Model> call, @NonNull Response<Model> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            Main main_model = response.body().getMain_model();
                            Wind wind_model = response.body().getWind_model();
                            Clouds clouds_model = response.body().getClouds_model();
                            Sys sys_model = response.body().getSys_model();

                            temperature = makeFromKelvin(main_model.getTemp());
                            tempMaximal = makeFromKelvin(main_model.getTempMax());
                            tempMinimal = makeFromKelvin(main_model.getTempMin());

                            humidity = main_model.getHumidity();
                            windSpeed = (int) wind_model.getSpeed();
                            cloudiness = clouds_model.getAll();

                            binding.tempC.setText(temperature + "°C");
                            binding.maxMinTemp.setText(tempMaximal + " °C↑  \n" + tempMinimal + " °C↓");
                            binding.cityName.setText(binding.inputCity.getText().toString());
                            binding.humidity.setText(humidity + " %");
                            binding.pressure.setText(main_model.getPressure() + "\nmBar");
                            binding.wind.setText(windSpeed + " m/s");
                            binding.cloudy.setText(cloudiness + " %");
                            binding.sunrise.setText(String.valueOf(getCurrDateTime(sys_model.getSunrise())));
                            binding.sunset.setText(String.valueOf(getCurrDateTime(sys_model.getSunset())));
                            binding.timeZone.setText(String.valueOf(response.body().getTimeZone()));

                            boolean isDayTime = isDayTime(sys_model.getSunrise(), sys_model.getSunset());
                            updateWeatherConditions(isDayTime);
                        } else {
                            Toast.makeText(requireActivity(), "No WeatherForecast data", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Model> call, @NonNull Throwable throwable) {
                        Toast.makeText(requireActivity(), "No WeatherForecast data" + throwable.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                binding.bottomSheet.setVisibility(View.GONE);
            } else {
                Toast.makeText(requireActivity(), "Input name of city!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private int makeFromKelvin(double kelvin) {
        return (int) (kelvin - 273.15);
    }

    public String getCurrDateTime(long milliseconds) {
        return java.text.DateFormat.getDateTimeInstance().format(new Date(milliseconds * 1000));
    }

    private boolean isDayTime(long sunrise, long sunset) {
        long currentTimeMillis = System.currentTimeMillis() / 1000;
        return currentTimeMillis > sunrise && currentTimeMillis < sunset;
    }

    private void updateWeatherConditions(boolean isDayTime) {
        if (isDayTime) {
            if (temperature > 25) {
                setClearWeather(isDayTime);
            } else if (temperature > 20) {
                setPartlyCloudyWeather(isDayTime);
            } else if (temperature > 15 && humidity < 90 && !isRainPossible()) {
                setCloudyWeather(isDayTime);
            } else if (temperature > 10 && humidity > 80) {
                setRainyWeather(isDayTime);
            } else if (temperature < 5) {
                setSnowyWeather(isDayTime);
            } else {
                setDefaultWeather(isDayTime);
            }
        } else {
            setNightWeather(isDayTime);
        }
    }

    private void setDefaultWeather(boolean isDayTime) {
        String weatherDescription = "Weather conditions are not defined";
        if (isDayTime) {
            weatherDescription += ", Day";
        } else {
            weatherDescription += ", Night";
        }
        binding.sun.setVisibility(View.INVISIBLE);
        binding.blueSky.setVisibility(View.VISIBLE);
        binding.nightSky.setVisibility(View.INVISIBLE);
        binding.badWeatherSky.setVisibility(View.INVISIBLE);
        binding.rainLotty.setVisibility(View.INVISIBLE);
        binding.snowLotty.setVisibility(View.INVISIBLE);
        binding.isRainOrNot.setVisibility(View.INVISIBLE);
        setWeatherDescription(weatherDescription);
    }

    private void setClearWeather(boolean isDayTime) {
        String weatherDescription = "Clear sky";
        if (isDayTime) {
            weatherDescription += ", Day";
        } else {
            weatherDescription += ", Night";
        }
        binding.sun.setVisibility(View.VISIBLE);
        binding.blueSky.setVisibility(View.VISIBLE);
        binding.nightSky.setVisibility(View.INVISIBLE);
        binding.badWeatherSky.setVisibility(View.INVISIBLE);
        binding.rainLotty.setVisibility(View.INVISIBLE);
        binding.snowLotty.setVisibility(View.INVISIBLE);
        binding.isRainOrNot.setVisibility(View.INVISIBLE);
        setWeatherDescription(weatherDescription);
    }

    private void setPartlyCloudyWeather(boolean isDayTime) {
        binding.sun.setVisibility(View.VISIBLE);
        binding.blueSky.setVisibility(View.VISIBLE);
        binding.nightSky.setVisibility(View.INVISIBLE);
        binding.badWeatherSky.setVisibility(View.INVISIBLE);
        binding.rainLotty.setVisibility(View.INVISIBLE);
        binding.snowLotty.setVisibility(View.INVISIBLE);
        binding.isRainOrNot.setVisibility(View.INVISIBLE);
        setWeatherDescription("Partly cloudy");
    }

    private void setCloudyWeather(boolean isDayTime) {
        binding.sun.setVisibility(View.INVISIBLE);
        binding.blueSky.setVisibility(View.INVISIBLE);
        binding.nightSky.setVisibility(View.INVISIBLE);
        binding.badWeatherSky.setVisibility(View.VISIBLE);
        binding.rainLotty.setVisibility(View.INVISIBLE);
        binding.snowLotty.setVisibility(View.INVISIBLE);
        binding.isRainOrNot.setVisibility(View.INVISIBLE);
        setWeatherDescription("Cloudy");
    }

    private void setRainyWeather(boolean isDayTime) {
        if (isRainPossible()) {
            binding.sun.setVisibility(View.INVISIBLE);
            binding.blueSky.setVisibility(View.INVISIBLE);
            binding.nightSky.setVisibility(View.INVISIBLE);
            binding.badWeatherSky.setVisibility(View.VISIBLE);
            binding.rainLotty.setVisibility(View.VISIBLE);
            binding.snowLotty.setVisibility(View.INVISIBLE);
            binding.isRainOrNot.setVisibility(View.VISIBLE);
            binding.isRainOrNot.setText("Rain is possible ");
            setWeatherDescription("Rainy");
        } else {
            setCloudyWeather(isDayTime); // Если дождь не возможен, отображаем облачную погоду
            setWeatherDescription("Cloudy with no rain");
        }
    }

    private void setSnowyWeather(boolean isDayTime) {
        if (isSnowPossible(temperature, cloudiness)) {
            binding.sun.setVisibility(View.INVISIBLE);
            binding.blueSky.setVisibility(View.INVISIBLE);
            binding.nightSky.setVisibility(View.INVISIBLE);
            binding.badWeatherSky.setVisibility(View.VISIBLE);
            binding.rainLotty.setVisibility(View.INVISIBLE);
            binding.snowLotty.setVisibility(View.VISIBLE);
            binding.isRainOrNot.setVisibility(View.VISIBLE);
            binding.isRainOrNot.setText("Snow is possible ");
            setWeatherDescription("Snowy");
        } else {
            setCloudyWeather(isDayTime); // Если снег не возможен, отображаем облачную погоду
            setWeatherDescription("Cloudy with no snow");
        }
    }

    private void setNightWeather(boolean isDayTime) {
        binding.sun.setVisibility(View.INVISIBLE);
        binding.blueSky.setVisibility(View.INVISIBLE);
        binding.nightSky.setVisibility(View.VISIBLE);
        binding.badWeatherSky.setVisibility(View.INVISIBLE);
        binding.rainLotty.setVisibility(View.INVISIBLE);
        binding.snowLotty.setVisibility(View.INVISIBLE);
        binding.isRainOrNot.setVisibility(View.INVISIBLE);
        setWeatherDescription("Night");
    }

    private void setWeatherDescription(String description) {
        binding.condition.setText(description);
    }

    private boolean isRainPossible() {
        return binding.isRainOrNot.getVisibility() == View.VISIBLE && binding.isRainOrNot.getText().toString().equals("Rain is possible ");
    }

    private boolean isSnowPossible(int temperature, int cloudiness) {
        return temperature < 0 && cloudiness > 50;
    }

}
