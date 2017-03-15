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
import android.widget.TextView;

import com.example.sams.weatherapp.model.WeatherModel;

/**
 * Created by sams on 2017-02-27.
 */
public class TwoFragment extends Fragment implements IWeatherCallback {

    private static final String TAG = MainActivity.class.getSimpleName();

    private TextView tvLocalization;
    private TextView tvTemp;
    private ImageView imageView;
    private TextView tvDescription;
    private TextView tvPressure;
    private TextView tvHumidity;
    private TextView tvWindSpeed;
    private TextView tvCloudPerCent;
    private TextView tvPrecipitation;

    public TwoFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_two, container, false);
        tvLocalization = (TextView)view.findViewById(R.id.tvLocalization);
        tvTemp = (TextView)view.findViewById(R.id.tvTemp);
        imageView = (ImageView)view.findViewById(R.id.imageView);
        tvDescription = (TextView)view.findViewById(R.id.tvDescription);
        tvPressure = (TextView) view.findViewById(R.id.tvPressure);
        tvHumidity =  (TextView) view.findViewById(R.id.tvHumidity);
        tvWindSpeed = (TextView) view.findViewById(R.id.tvWindSpeed);
        tvCloudPerCent = (TextView) view.findViewById(R.id.tvCloudPerCent);
        tvPrecipitation = (TextView) view.findViewById(R.id.tvPrecipitation);

        boolean isNetwork = isNetworkAvailable();
        Log.d("MainActivity", "isNetwork: " + isNetwork);

        if(isNetwork) {
            new NetworkTask(getContext(), this).execute();
        }
        return view;
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager)
                getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        // if no network is available networkInfo will be null
        // otherwise check if we are connected
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }


    @Override
    public void refreshView(WeatherModel weatherModel) {
        tvLocalization.setText(weatherModel.getCity().getName());
        Double d = convertKelvinToCelcius(weatherModel.getList().get(1).getTemp().getDay());
        tvTemp.setText(String.format("%1$.1f°", d));

        String icon = weatherModel.getList().get(1).getWeather().get(0).getIcon();
        new IconTask(imageView,icon,225).execute();

        tvDescription.setText(weatherModel.getList().get(1).getWeather().get(0).getDescription());

        Double pressure = weatherModel.getList().get(1).getPressure();
        tvPressure.setText(String.format("Ciśnienie atmosferyczne: %1$.1f hPa", pressure));

        int humidity = weatherModel.getList().get(1).getHumidity();
        tvHumidity.setText("Wilgotność powietrza: "+String.valueOf(humidity) + "%");

        Double windSpeed = weatherModel.getList().get(1).getSpeed();
        tvWindSpeed.setText(String.format("Szybkość wiatru: %1$.1f m/sek", windSpeed));

        int cloud = weatherModel.getList().get(1).getClouds();
        tvCloudPerCent.setText("Procentowe zachmurzenie: " + String.valueOf(cloud) + "%");

    }

    public Double convertKelvinToCelcius(Double kelvin){
        return kelvin - 273.15;
    }


}
