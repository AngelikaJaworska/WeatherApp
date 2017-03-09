package com.example.sams.weatherapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Icon;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.example.sams.weatherapp.R;

import java.io.IOException;
import java.net.URL;

/**
 * Created by sams on 2017-02-28.
 */
public class IconTask extends AsyncTask <Void, Void,Bitmap> {
    private  ImageView imageView;
    private String iconName;
    private int size;

    public IconTask(ImageView imageView, String iconName, int size) {
        this.imageView = imageView;
        this.iconName = iconName;
        this.size = size;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        imageView.setImageBitmap(bitmap);
    }

    @Override
    protected Bitmap doInBackground(Void... params) {

        return getIcon();
    }

    public Bitmap getIcon()
    {
        String link = "http://openweathermap.org/img/w/" + iconName + ".png";

        try{
            URL url = new URL(link);
            Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            int newHeight= size;
            int newWidth= size;
            Bitmap bmp2=Bitmap.createScaledBitmap(bmp, newWidth, newHeight, true);
            return bmp2;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
