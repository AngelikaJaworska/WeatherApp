package com.example.sams.weatherapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sams.weatherapp.model.Weather;
import com.example.sams.weatherapp.model.WeatherModel;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


public class MainActivity extends AppCompatActivity implements IWeatherCallback{

    private final static String TAG = "MainActivity";

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private CoordinatorLayout coordinator;
    private ViewPager viewPager;
    private LinearLayout llsearch;
    private EditText etLocalization;

    private ViewPagerAdapter adapter;

    private OneFragment oneFragment;
    private TwoFragment twoFragment;
    private ThreeFragment threeFragment;

    public static final String MY_PREFS_NAME = "MyPrefsFile";
    public String result;
    public Button btnSavedLocalization;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnSavedLocalization = (Button)findViewById(R.id.btnSavedLocalization);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        llsearch = (LinearLayout) findViewById(R.id.llsearch);
        coordinator = (CoordinatorLayout) findViewById(R.id.coordinator);
        etLocalization = (EditText) findViewById(R.id.etLocalization);

        boolean isNetwork = isNetworkAvailable();
        if(isNetwork) {
            new NetworkTask(MainActivity.this, this, null).execute();
        }
    }

    public void searchIt(View view)
    {
        llsearch.setVisibility(View.VISIBLE);
        coordinator.setVisibility(View.GONE);
    }

    public void goBack(View view)
    {
        llsearch.setVisibility(View.GONE);
        coordinator.setVisibility(View.VISIBLE);

        boolean isNetwork = isNetworkAvailable();
        if(isNetwork) {
            new NetworkTask(MainActivity.this, this, null).execute();
        }
    }

    public void searchLoc(View view)
    {
        String localization = etLocalization.getText().toString();
        result = localization.replaceAll("\\s+$", "");
        boolean isNetwork = isNetworkAvailable();
        if(isNetwork) {
            new NetworkTask(MainActivity.this, this, result).execute();
        }
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        if(result != null)
        {
            btnSavedLocalization.setText(result);
            settingValuesInPreferences();
        }
    }

    public void settingValuesInPreferences()
    {
        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putString("localization", result);
        editor.commit();
    }

    public void retrieveDataFromPreferences(View view)
    {
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String restoredText = prefs.getString("localization", null);
        if (restoredText != null) {
            String name = prefs.getString("localization", "no loc");//"No name defined" is the default value.
            Log.i(TAG, "odczytano: " + name);
            boolean isNetwork = isNetworkAvailable();
            if(isNetwork) {
                new NetworkTask(MainActivity.this, this, name).execute();
            }
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager)
                MainActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        // if no network is available networkInfo will be null
        // otherwise check if we are connected
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        oneFragment = new OneFragment();
        twoFragment = new TwoFragment();
        threeFragment = new ThreeFragment();
        adapter.addFragment(oneFragment, "Dzisiaj");
        adapter.addFragment(twoFragment, "Jutro");
        adapter.addFragment(threeFragment, "5 Dni");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void refreshView(WeatherModel weatherModel) {
        llsearch.setVisibility(View.GONE);
        coordinator.setVisibility(View.VISIBLE);

        adapter.setWeatherModel(weatherModel);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<WeatherFragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();
        private WeatherModel weatherModel;
        private int currentPosition;

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
            currentPosition = 0;
        }

        public void setWeatherModel(WeatherModel weatherModel) {
            this.weatherModel = weatherModel;
            mFragmentList.get(currentPosition).refreshView(weatherModel);
        }

        @Override
        public WeatherFragment getItem(int position) {
            Log.i(TAG, "getItem: " + position);
            WeatherFragment fragment = mFragmentList.get(position);
            return fragment;
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(WeatherFragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            Log.i(TAG, "setPrimaryItem pos:" + position);
            currentPosition = position;
            mFragmentList.get(position).refreshView(weatherModel);
            super.setPrimaryItem(container, position, object);
        }
    }
}