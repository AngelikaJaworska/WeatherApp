package com.example.sams.weatherapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;

import com.example.sams.weatherapp.model.WeatherModel;

/**
 * Created by sams on 2017-03-19.
 */
public abstract class WeatherFragment extends Fragment {
    private final static String TAG = "WeatherFragment";

    public abstract void refreshView(WeatherModel weatherModel);

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Log.i(TAG, "onViewCreated");
        super.onViewCreated(view, savedInstanceState);
    }
}
