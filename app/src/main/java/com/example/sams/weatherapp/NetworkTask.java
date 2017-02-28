package com.example.sams.weatherapp;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.example.sams.weatherapp.model.WeatherModel;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by sams on 2017-02-26.
 */
public class NetworkTask extends AsyncTask<Void, Void, WeatherModel> {

    private static final String TAG = NetworkTask.class.getSimpleName();
    private static final String APIKey = "e9851779417a301c57d555fb9ff9329f";
    private Context context;
    private IWeatherCallback weatherCallback;

    private ProgressDialog spinner;

    public NetworkTask(Context context, IWeatherCallback weatherCallback) {
        this.context = context;
        spinner = new ProgressDialog(context);
        this.weatherCallback = weatherCallback;
    }

    @Override
    protected void onPreExecute() {
        spinner.setMessage("Updating data");
        spinner.show();
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(WeatherModel weatherModel) {
        super.onPostExecute(weatherModel);
        weatherCallback.refreshView(weatherModel);
        spinner.dismiss();
    }

    @Override
    protected WeatherModel doInBackground(Void... params) {
        Location location = getLocation();
        if(location == null)
        {
            return null;
        }

        String link = "http://api.openweathermap.org/data/2.5/forecast/daily?lat="+location.getLatitude()+"&lon="+location.getLongitude()+"&lang=pl&appid="+APIKey;
        try {
            URL url = new URL(link);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.connect();

            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String webPage = "";
            String data = "";

            while ((data = reader.readLine()) != null) {
                webPage += data + "\n";
            }
            Log.d(TAG, webPage);

            Gson gson = new Gson();
            WeatherModel weather = gson.fromJson(webPage, WeatherModel.class);

            Log.d(TAG, weather.getCity().getName());
            return weather;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }



        return null;
    }

    private Location getLocation() {
        LocationManager locManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        Criteria searchProviderCriteria = new Criteria();
        searchProviderCriteria.setPowerRequirement(Criteria.POWER_LOW);
        searchProviderCriteria.setAccuracy(Criteria.ACCURACY_COARSE);
        searchProviderCriteria.setCostAllowed(false);

        String provider = locManager.getBestProvider(searchProviderCriteria, true);

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return null;
        }

        Location loc = locManager.getLastKnownLocation(provider);
        return loc;

    }


}
