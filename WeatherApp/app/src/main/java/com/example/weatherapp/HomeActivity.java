package com.example.weatherapp;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HomeActivity extends AppCompatActivity {

    String city, region, latitude, longitude;
    String loc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        Fragment newFragment =new  ViewPagerWithCircleIndicatorView();
        FragmentTransaction transaction =getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment,newFragment);
        transaction.commit();

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);

        String geoURL = "https://ipinfo.io/?token=20ef1690f3db86";

        JsonObjectRequest geoJsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, geoURL, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            city = (String) response.get("city");
                            region = (String) response.get("region");
                            String cityState = city+ ", "+ region;
                            TextView location = (TextView) findViewById(R.id.location);
                            location.setText(cityState);

                            loc = (String) response.get("loc");
                            latitude = loc.split(",",0)[0];
                            longitude = loc.split(",",0)[1];

                            String tmrURL = "https://api.tomorrow.io/v4/timelines?location="
                                    + latitude + ','
                                    + longitude +
                                    "&fields=precipitationIntensity,precipitationType,windSpeed,windGust,windDirection,temperatureMax,temperatureMin,temperatureApparent,cloudCover,cloudBase,cloudCeiling,weatherCode,temperature,humidity,pressureSurfaceLevel,visibility,cloudCover,uvIndex,precipitationProbability,sunriseTime,sunsetTime"
                                    + "&timesteps=1d&units=imperial&timezone=America/Los_Angeles&apikey=C5JMDFsiGmycb43l7QsGc2nV15uiENPi";

                            JsonObjectRequest tmrJsonObjectRequest = new JsonObjectRequest
                                    (Request.Method.GET, tmrURL, null, new Response.Listener<JSONObject>() {

                                        @Override
                                        public void onResponse(JSONObject response) {
                                            try {
                                                System.out.println(tmrURL);
                                                ImageView weatherIcon = (ImageView) findViewById(R.id.weatherIcon);
                                                TextView temperature = (TextView) findViewById(R.id.temperature);
                                                TextView weatherType = (TextView) findViewById(R.id.weatherType);
                                                TextView humidtyData = (TextView) findViewById(R.id.humidtyData);
                                                TextView windSpeedData = (TextView) findViewById(R.id.windSpeedData);
                                                TextView visibilityData = (TextView) findViewById(R.id.visibilityData);
                                                TextView pressureData = (TextView) findViewById(R.id.pressureData);

                                                JSONObject data = response.getJSONObject("data");
                                                JSONArray timelines = data.getJSONArray("timelines");
                                                JSONObject element = (JSONObject) timelines.get(0);
                                                JSONArray intervals = element.getJSONArray("intervals");
                                                JSONObject today = (JSONObject) intervals.get(0);
                                                JSONObject values = today.getJSONObject("values");

                                                weatherType.setText(getWeatherInfo(values.getString("weatherCode")));
                                                temperature.setText(Integer.toString((int) Math.round(values.getDouble("temperature"))) + " \u2109");
                                                humidtyData.setText(values.getString("humidity") + "%");
                                                windSpeedData.setText(values.getString("windSpeed") + "mph");
                                                visibilityData.setText(values.getString("visibility") + "mi");
                                                pressureData.setText(values.getString("pressureSurfaceLevel") + "inHg");

                                                JSONObject day1 = (JSONObject) intervals.get(1);
                                                JSONObject values1 = day1.getJSONObject("values");
                                                TextView date1 = (TextView) findViewById(R.id.date1);
                                                ImageView weatherRep1 = (ImageView) findViewById(R.id.weatherRep1);
                                                TextView minTemperature1 = (TextView) findViewById(R.id.minTemperature1);
                                                TextView maxTemperature1 = (TextView) findViewById(R.id.maxTemperature1);
                                                date1.setText(day1.getString("startTime").split("T",0)[0]);
                                                minTemperature1.setText(Integer.toString((int) Math.round(values1.getDouble("temperatureMin"))));
                                                maxTemperature1.setText(Integer.toString((int) Math.round(values1.getDouble("temperatureMax"))));

                                                JSONObject day2 = (JSONObject) intervals.get(2);
                                                JSONObject values2 = day2.getJSONObject("values");
                                                TextView date2 = (TextView) findViewById(R.id.date2);
                                                ImageView weatherRep2 = (ImageView) findViewById(R.id.weatherRep2);
                                                TextView minTemperature2 = (TextView) findViewById(R.id.minTemperature2);
                                                TextView maxTemperature2 = (TextView) findViewById(R.id.maxTemperature2);
                                                date2.setText(day2.getString("startTime").split("T",0)[0]);
                                                minTemperature2.setText(Integer.toString((int) Math.round(values2.getDouble("temperatureMin"))));
                                                maxTemperature2.setText(Integer.toString((int) Math.round(values2.getDouble("temperatureMax"))));

                                                JSONObject day3 = (JSONObject) intervals.get(3);
                                                JSONObject values3 = day3.getJSONObject("values");
                                                TextView date3 = (TextView) findViewById(R.id.date3);
                                                ImageView weatherRep3 = (ImageView) findViewById(R.id.weatherRep3);
                                                TextView minTemperature3 = (TextView) findViewById(R.id.minTemperature3);
                                                TextView maxTemperature3 = (TextView) findViewById(R.id.maxTemperature3);
                                                date3.setText(day3.getString("startTime").split("T",0)[0]);
                                                minTemperature3.setText(Integer.toString((int) Math.round(values3.getDouble("temperatureMin"))));
                                                maxTemperature3.setText(Integer.toString((int) Math.round(values3.getDouble("temperatureMax"))));

                                                JSONObject day4 = (JSONObject) intervals.get(4);
                                                JSONObject values4 = day4.getJSONObject("values");
                                                TextView date4 = (TextView) findViewById(R.id.date4);
                                                ImageView weatherRep4 = (ImageView) findViewById(R.id.weatherRep4);
                                                TextView minTemperature4 = (TextView) findViewById(R.id.minTemperature4);
                                                TextView maxTemperature4 = (TextView) findViewById(R.id.maxTemperature4);
                                                date4.setText(day4.getString("startTime").split("T",0)[0]);
                                                minTemperature4.setText(Integer.toString((int) Math.round(values4.getDouble("temperatureMin"))));
                                                maxTemperature4.setText(Integer.toString((int) Math.round(values4.getDouble("temperatureMax"))));

                                                JSONObject day5 = (JSONObject) intervals.get(5);
                                                JSONObject values5 = day5.getJSONObject("values");
                                                TextView date5 = (TextView) findViewById(R.id.date5);
                                                ImageView weatherRep5 = (ImageView) findViewById(R.id.weatherRep5);
                                                TextView minTemperature5 = (TextView) findViewById(R.id.minTemperature5);
                                                TextView maxTemperature5 = (TextView) findViewById(R.id.maxTemperature5);
                                                date5.setText(day5.getString("startTime").split("T",0)[0]);
                                                minTemperature5.setText(Integer.toString((int) Math.round(values5.getDouble("temperatureMin"))));
                                                maxTemperature5.setText(Integer.toString((int) Math.round(values5.getDouble("temperatureMax"))));

                                                JSONObject day6 = (JSONObject) intervals.get(6);
                                                JSONObject values6 = day6.getJSONObject("values");
                                                TextView date6 = (TextView) findViewById(R.id.date6);
                                                ImageView weatherRep6 = (ImageView) findViewById(R.id.weatherRep6);
                                                TextView minTemperature6 = (TextView) findViewById(R.id.minTemperature6);
                                                TextView maxTemperature6 = (TextView) findViewById(R.id.maxTemperature6);
                                                date6.setText(day6.getString("startTime").split("T",0)[0]);
                                                minTemperature6.setText(Integer.toString((int) Math.round(values6.getDouble("temperatureMin"))));
                                                maxTemperature6.setText(Integer.toString((int) Math.round(values6.getDouble("temperatureMax"))));

                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                                        }
                                    }, new Response.ErrorListener() {

                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            error.printStackTrace();
                                        }
                                    });

                            queue.add(tmrJsonObjectRequest);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        // Add the request to the RequestQueue.
        queue.add(geoJsonObjectRequest);

    }

    private String getWeatherInfo(String code_number) {
        if(code_number.equals("1000"))
        {
            return "Clear";
        }
        if(code_number.equals("1100"))
        {
            return "Mostly Clear";
        }
        if(code_number.equals("1101"))
        {
            return "Partly Cloudy";
        }
        if(code_number.equals("1102"))
        {
            return "Mostly Cloudy";
        }
        if(code_number.equals("1001"))
        {
            return "Cloudy";
        }
        if(code_number.equals("2000"))
        {
            return "Fog";
        }
        if(code_number.equals("2100"))
        {
            return "Light Fog";
        }
        if(code_number.equals("8000"))
        {
            return "   Thunderstorm";
        }
        if(code_number.equals("5001"))
        {
            return "Flurries";
        }
        if(code_number.equals("5100"))
        {
            return "Light Snow";
        }
        if(code_number.equals("5000"))
        {
            return "Snow";
        }
        if(code_number.equals("5101"))
        {
            return "Heavy Snow";
        }
        if(code_number.equals("7102"))
        {
            return "Light Ice Pellets";
        }
        if(code_number.equals("7000"))
        {
            return "Ice Pellets";
        }
        if(code_number.equals("7101"))
        {
            return "Heavy Ice Pellets";
        }
        if(code_number.equals("4000"))
        {
            return "Drizzle";
        }
        if(code_number.equals("6000"))
        {
            return "Freezing Drizzle";
        }
        if(code_number.equals("6200"))
        {
            return "Light Freezing Rain";
        }
        if(code_number.equals("6001"))
        {
            return "Freezing Rain";
        }
        if(code_number.equals("6201"))
        {
            return "Heavy Freezing Rain";
        }
        if(code_number.equals("4200"))
        {
            return "Light Rain";
        }
        if(code_number.equals("4001"))
        {
            return "Rain";
        }
        if(code_number.equals("4201"))
        {
            return "Heavy Rain";
        }
        return "null";
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.search_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Enter the name of city");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Searchable Activity
                Intent intent = new Intent(HomeActivity.this,Searchable.class);
                intent.putExtra("cityAndState", query);
                System.out.println("At Home: " + query);
                startActivity(intent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // AUTO COMPLETE LATER
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

}