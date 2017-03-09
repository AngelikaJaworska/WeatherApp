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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by sams on 2017-02-28.
 */
public class ThreeFragment extends Fragment implements IWeatherCallback {

    private static final String TAG = MainActivity.class.getSimpleName();

    private TextView tvLocalization;
    private TextView tvTemp1;
    private TextView tvDay1;

    private TextView tvTemp2;
    private TextView tvDay2;

    private TextView tvTemp3;
    private TextView tvDay3;

    private TextView tvTemp4;
    private TextView tvDay4;

    private TextView tvTemp5;
    private TextView tvDay5;

    private ImageView imageView1;
    private ImageView imageView2;
    private ImageView imageView3;
    private ImageView imageView4;
    private ImageView imageView5;

    public ThreeFragment() {
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_three, container, false);
        tvLocalization = (TextView)view.findViewById(R.id.tvLocalization);

        imageView1 = (ImageView)view.findViewById(R.id.imageView1);
        imageView2 = (ImageView)view.findViewById(R.id.imageView2);
        imageView3 = (ImageView)view.findViewById(R.id.imageView3);
        imageView4 = (ImageView)view.findViewById(R.id.imageView4);
        imageView5 = (ImageView)view.findViewById(R.id.imageView5);

        tvDay1 = (TextView)view.findViewById(R.id.tvDay1);
        tvTemp1 = (TextView)view.findViewById(R.id.tvTemp1);

        tvDay2 = (TextView)view.findViewById(R.id.tvDay2);
        tvTemp2 = (TextView)view.findViewById(R.id.tvTemp2);

        tvDay3 = (TextView)view.findViewById(R.id.tvDay3);
        tvTemp3 = (TextView)view.findViewById(R.id.tvTemp3);

        tvDay4 = (TextView)view.findViewById(R.id.tvDay4);
        tvTemp4 = (TextView)view.findViewById(R.id.tvTemp4);

        tvDay5 = (TextView)view.findViewById(R.id.tvDay5);
        tvTemp5 = (TextView)view.findViewById(R.id.tvTemp5);

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

        String icon1 = weatherModel.getList().get(1).getWeather().get(0).getIcon();
        new IconTask(imageView1,icon1,100).execute();

        String icon2 = weatherModel.getList().get(2).getWeather().get(0).getIcon();
        new IconTask(imageView2,icon2, 100).execute();

        String icon3 = weatherModel.getList().get(3).getWeather().get(0).getIcon();
        new IconTask(imageView3,icon3,100).execute();

        String icon4 = weatherModel.getList().get(4).getWeather().get(0).getIcon();
        new IconTask(imageView4,icon4,100).execute();

        String icon5 = weatherModel.getList().get(5).getWeather().get(0).getIcon();
        new IconTask(imageView5,icon5,100).execute();

        Long l1 = Long.parseLong(weatherModel.getList().get(1).getDt().toString()) * 1000L;
        tvDay1.setText(getDate(l1));

        Long l2 = Long.parseLong(weatherModel.getList().get(2).getDt().toString()) * 1000L;
        tvDay2.setText(getDate(l2));

        Long l3 = Long.parseLong(weatherModel.getList().get(3).getDt().toString()) * 1000L;
        tvDay3.setText(getDate(l3));

        Long l4 = Long.parseLong(weatherModel.getList().get(4).getDt().toString()) * 1000L;
        tvDay4.setText(getDate(l4));

        Long l5 = Long.parseLong(weatherModel.getList().get(5).getDt().toString()) * 1000L;
        tvDay5.setText(getDate(l5));

        Double d1 = convertKelvinToCelcius(weatherModel.getList().get(1).getTemp().getDay());
        tvTemp1.setText(String.format("%1$.1f°", d1));

        Double d2 = convertKelvinToCelcius(weatherModel.getList().get(2).getTemp().getDay());
        tvTemp2.setText(String.format( "%1$.1f°", d2));

        Double d3 = convertKelvinToCelcius(weatherModel.getList().get(3).getTemp().getDay());
        tvTemp3.setText(String.format( "%1$.1f°", d3));

        Double d4 = convertKelvinToCelcius(weatherModel.getList().get(4).getTemp().getDay());
        tvTemp4.setText(String.format( "%1$.1f°", d4));

        Double d5 = convertKelvinToCelcius(weatherModel.getList().get(5).getTemp().getDay());
        tvTemp5.setText(String.format( "%1$.1f°", d5));
    }

    public Double convertKelvinToCelcius(Double kelvin){
        return kelvin - 273.15;
    }

    private String getDate(long timeStamp){

        try{
            DateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            Date netDate = new Date(timeStamp);
            return sdf.format(netDate);
        }
        catch(Exception ex){
            return "xx";
        }
    }


}


