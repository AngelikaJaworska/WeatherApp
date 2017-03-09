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
import android.widget.TextView;

import com.example.sams.weatherapp.model.WeatherModel;

/**
 * Created by sams on 2017-02-27.
 */
public class TwoFragment extends Fragment implements IWeatherCallback {

    private static final String TAG = MainActivity.class.getSimpleName();

    private TextView tvLocalization;
    private TextView tvTemp;

    public TwoFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_two, container, false);
        tvLocalization = (TextView)view.findViewById(R.id.tvLocalization);
        tvTemp = (TextView)view.findViewById(R.id.tvTemp);

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
        tvTemp.setText(String.format( "%1$.1f°", d));


    }

    public Double convertKelvinToCelcius(Double kelvin){
        return kelvin - 273.15;
    }


}
