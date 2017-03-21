package com.example.sams.weatherapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.sams.weatherapp.model.WeatherModel;

/**
 * Created by sams on 2017-02-27.
 */
public class OneFragment extends WeatherFragment {

    private static final String TAG = OneFragment.class.getSimpleName();

    private TextView tvLocalization;
    private TextView tvTemp;
    private ImageView imageView;
    private TextView tvDescription;
    private TextView tvPressure;
    private TextView tvHumidity;
    private TextView tvWindSpeed;
    private TextView tvCloudPerCent;
    private TextView tvPrecipitation;

    public OneFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_one, container, false);
        tvLocalization = (TextView)view.findViewById(R.id.tvLocalization);
        tvTemp = (TextView)view.findViewById(R.id.tvTemp);
        imageView = (ImageView)view.findViewById(R.id.imageView);
        tvDescription = (TextView)view.findViewById(R.id.tvDescription);
        tvPressure = (TextView) view.findViewById(R.id.tvPressure);
        tvHumidity =  (TextView) view.findViewById(R.id.tvHumidity);
        tvWindSpeed = (TextView) view.findViewById(R.id.tvWindSpeed);
        tvCloudPerCent = (TextView) view.findViewById(R.id.tvCloudPerCent);
        tvPrecipitation = (TextView) view.findViewById(R.id.tvPrecipitation);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Log.i(TAG, "onViewCreated");
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void refreshView(WeatherModel weatherModel) {
        if (weatherModel == null) {
            Log.i(TAG, "null");
            return;
        }
        Log.i(TAG, "refreshView");

        tvLocalization.setText(weatherModel.getCity().getName());
        Double d = convertKelvinToCelcius(weatherModel.getList().get(0).getTemp().getDay());
        tvTemp.setText(String.format("%1$.1f°", d));

        String icon = weatherModel.getList().get(0).getWeather().get(0).getIcon();
        new IconTask(imageView,icon,225).execute();

        tvDescription.setText(weatherModel.getList().get(0).getWeather().get(0).getDescription());

        Double pressure = weatherModel.getList().get(0).getPressure();
        tvPressure.setText(String.format("Ciśnienie atmosferyczne: %1$.1f hPa", pressure));

        int humidity = weatherModel.getList().get(0).getHumidity();
        tvHumidity.setText("Wilgotność powietrza: "+String.valueOf(humidity) + "%");

        Double windSpeed = weatherModel.getList().get(0).getSpeed();
        tvWindSpeed.setText(String.format("Szybkość wiatru: %1$.1f m/sek", windSpeed));

        int cloud = weatherModel.getList().get(0).getClouds();
        tvCloudPerCent.setText("Procentowe zachmurzenie: " + String.valueOf(cloud) + "%");

        Double precipitation = weatherModel.getList().get(0).getRain();
        if(precipitation == null)
        {
            precipitation = 0.0;
        }
        tvPrecipitation.setText(String.format("Opady w ciągu ostatnich 3h: %1$.1f mm",  precipitation));


    }

    public Double convertKelvinToCelcius(Double kelvin){

        return kelvin - 273.15;
    }

}
