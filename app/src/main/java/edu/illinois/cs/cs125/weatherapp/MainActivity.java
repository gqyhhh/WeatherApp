package edu.illinois.cs.cs125.weatherapp;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.PicassoProvider;

import java.lang.reflect.Type;

import edu.illinois.cs.cs125.weatherapp.Helper.Helper;
import edu.illinois.cs.cs125.weatherapp.Model.Main;
import edu.illinois.cs.cs125.weatherapp.Model.OpenWeatherMap;
import edu.illinois.cs.cs125.weatherapp.common.Common;

import static android.webkit.ConsoleMessage.MessageLevel.LOG;

public class MainActivity extends AppCompatActivity implements LocationListener {


    TextView txtCity, txtLastUpdate, txtDescription, txtHumidity, txtCelsius, txtTime, txtLat, txtTitle;
    ImageView imageView;

    LocationManager locationManager;
    String provider;
    static double lat, lng;
    OpenWeatherMap openWeatherMap = new OpenWeatherMap();

    int MY_PERMISSION = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Control
        txtCity = (TextView) findViewById(R.id.txtCity);
        txtLastUpdate = (TextView) findViewById(R.id.txtLastUpdate);
        txtDescription = (TextView) findViewById(R.id.txtDescription);
        txtHumidity = (TextView) findViewById(R.id.txtHumidity);
        txtCelsius = (TextView) findViewById(R.id.txtCelsius);
        txtTime = (TextView) findViewById(R.id.txtTime);
        txtLat = (TextView) findViewById(R.id.txtLat);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        imageView = (ImageView) findViewById(R.id.imageView);

        txtTitle.setTextColor(Color.BLUE);
        txtLat.setTextColor(Color.BLACK);
        txtTime.setTextColor(Color.BLACK);
        txtCelsius.setTextColor(Color.BLACK);
        txtHumidity.setTextColor(Color.BLACK);
        txtDescription.setTextColor(Color.BLACK);
        txtLastUpdate.setTextColor(Color.BLACK);
        txtCity.setTextColor(Color.BLACK);



        //get coordinates
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        provider = locationManager.getBestProvider(new Criteria(), false);

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {


            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                    Manifest.permission.INTERNET,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_NETWORK_STATE,
                    Manifest.permission.SYSTEM_ALERT_WINDOW,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE


            }, MY_PERMISSION);
            return;
        }
        Location location = locationManager.getLastKnownLocation(provider);
        final Button button = findViewById(R.id.button1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                if (imageView.getVisibility() == View.VISIBLE) {
                    imageView.setVisibility(View.INVISIBLE);
                    txtCelsius.setTextColor(Color.WHITE);
                    txtCelsius.setBackgroundColor(Color.BLACK);
                    txtCity.setTextColor(Color.WHITE);
                    txtCity.setBackgroundColor(Color.BLACK);
                    txtTime.setTextColor(Color.WHITE);
                    txtTime.setBackgroundColor(Color.BLACK);
                    txtLastUpdate.setTextColor(Color.WHITE);
                    txtLastUpdate.setBackgroundColor(Color.BLACK);
                    txtHumidity.setTextColor(Color.WHITE);
                    txtHumidity.setBackgroundColor(Color.BLACK);
                    txtDescription.setTextColor(Color.WHITE);
                    txtDescription.setBackgroundColor(Color.BLACK);
                    txtLat.setTextColor(Color.WHITE);
                    txtLat.setBackgroundColor(Color.BLACK);

                } else {
                    imageView.setVisibility(View.VISIBLE);
                    txtCelsius.setTextColor(Color.BLACK);
                    txtCelsius.setBackgroundColor(Color.WHITE);
                    txtCity.setTextColor(Color.BLACK);
                    txtCity.setBackgroundColor(Color.WHITE);
                    txtTime.setTextColor(Color.BLACK);
                    txtTime.setBackgroundColor(Color.WHITE);
                    txtLastUpdate.setTextColor(Color.BLACK);
                    txtLastUpdate.setBackgroundColor(Color.WHITE);
                    txtHumidity.setTextColor(Color.BLACK);
                    txtHumidity.setBackgroundColor(Color.WHITE);
                    txtDescription.setTextColor(Color.BLACK);
                    txtDescription.setBackgroundColor(Color.WHITE);
                    txtLat.setTextColor(Color.BLACK);
                    txtLat.setBackgroundColor(Color.WHITE);
                }
            }
        });
        if (location == null) {
            Log.e("TAG", "No Location");
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                    Manifest.permission.INTERNET,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_NETWORK_STATE,
                    Manifest.permission.SYSTEM_ALERT_WINDOW,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE


            }, MY_PERMISSION);
        }
        locationManager.requestLocationUpdates(provider, 400, 1, this);

    }

    @Override
    public void onLocationChanged(Location location) {
        lat = location.getLatitude();
        lng = location.getLongitude();

        new GetWeather().execute(Common.apiRequest(String.valueOf(lat),String.valueOf(lng)));


    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private class GetWeather extends AsyncTask<String,Void,String> {

        ProgressDialog pd = new ProgressDialog(MainActivity.this);


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setTitle("Please wait...");
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String stream = null;
            String urlString = params[0];

            Helper http = new Helper();
            stream = http.getHTTPData(urlString);
            return stream;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s.contains("Error: Not found city")) {
                pd.dismiss();
                return;
            }
            Gson gson = new Gson();
            Type mType = new TypeToken<OpenWeatherMap>(){}.getType();
            openWeatherMap = gson.fromJson(s,mType);
            pd.dismiss();

            txtCity.setText(String.format("%s,%s",openWeatherMap.getName(),openWeatherMap.getSys().getCountry()));
            txtLastUpdate.setText(String.format("Last Updated: %s", Common.getDateNow()));
            txtDescription.setText(String.format("%s",openWeatherMap.getWeather().get(0).getDescription()));
            txtHumidity.setText(String.format("%d%%",openWeatherMap.getMain().getHumidity()));
            txtTime.setText(String.format("%s/%s",Common.unixTimeStampToDateTime(openWeatherMap.getSys().getSunrise()),
                    Common.unixTimeStampToDateTime(openWeatherMap.getSys().getSunset())));
            txtCelsius.setText(String.format("%.2f °C",openWeatherMap.getMain().getTemp()));
            Picasso.get().load(Common.getImage(openWeatherMap.getWeather().get(0).getIcon())).into(imageView);
            txtLat.setText(String.format("Lat: %s/ Lng: %s",openWeatherMap.getCoord().getLat(),openWeatherMap.getCoord().getLon()));
        }



    }

}
