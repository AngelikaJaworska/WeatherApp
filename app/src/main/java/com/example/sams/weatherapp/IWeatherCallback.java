package com.example.sams.weatherapp;

import com.example.sams.weatherapp.model.WeatherModel;

/**
 * Created by sams on 2017-02-26.
 */
public interface IWeatherCallback {

    void refreshView(WeatherModel weatherModel);
}
