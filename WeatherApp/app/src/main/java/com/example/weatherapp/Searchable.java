package com.example.weatherapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.view.MenuItemCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class Searchable extends AppCompatActivity {

    String cityAndState, latitude, longitude, reCity = "";
    ArrayList<String> list = new ArrayList<String>();
    int Flag = 0;
    String WS, WT, Pre, Preci, Temp, Hum, Vis, CC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);
//        Intent HomeIntent = getIntent();
//        cityAndState = HomeIntent.getExtras().getString("cityAndState");
        list = (ArrayList<String>) getIntent().getSerializableExtra("cityAndState");
        cityAndState = list.get(list.size() - 1);
        list.remove(list.size() - 1);
        System.out.println("searchable list is: " + cityAndState + list);
        TextView location = (TextView) findViewById(R.id.location);
        location.setText(cityAndState);

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);

        String geoURL = "https://maps.googleapis.com/maps/api/geocode/json?address="
                        + cityAndState + "&key=AIzaSyCiknUSvpLnLnwp8JoWDaY8GWWIfUWhx60";

        JsonObjectRequest geoJsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, geoURL, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray results = response.getJSONArray("results");
                            JSONObject element = results.getJSONObject(0);
                            JSONObject geometry = element.getJSONObject("geometry");
                            JSONObject location = geometry.getJSONObject("location");
                            latitude = location.getString("lat");
                            longitude = location.getString("lng");

//                            System.out.println(latitude + ", " + longitude );

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
                                                weatherIcon.setImageResource(getXMLWeather(values.getString("weatherCode")));

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
                                                weatherRep1.setImageResource(getXMLWeather(values1.getString("weatherCode")));
                                                TextView minTemperature1 = (TextView) findViewById(R.id.minTemperature1);
                                                TextView maxTemperature1 = (TextView) findViewById(R.id.maxTemperature1);
                                                date1.setText(day1.getString("startTime").split("T",0)[0]);
                                                minTemperature1.setText(Integer.toString((int) Math.round(values1.getDouble("temperatureMin"))));
                                                maxTemperature1.setText(Integer.toString((int) Math.round(values1.getDouble("temperatureMax"))));

                                                JSONObject day2 = (JSONObject) intervals.get(2);
                                                JSONObject values2 = day2.getJSONObject("values");
                                                TextView date2 = (TextView) findViewById(R.id.date2);
                                                ImageView weatherRep2 = (ImageView) findViewById(R.id.weatherRep2);
                                                weatherRep2.setImageResource(getXMLWeather(values2.getString("weatherCode")));
                                                TextView minTemperature2 = (TextView) findViewById(R.id.minTemperature2);
                                                TextView maxTemperature2 = (TextView) findViewById(R.id.maxTemperature2);
                                                date2.setText(day2.getString("startTime").split("T",0)[0]);
                                                minTemperature2.setText(Integer.toString((int) Math.round(values2.getDouble("temperatureMin"))));
                                                maxTemperature2.setText(Integer.toString((int) Math.round(values2.getDouble("temperatureMax"))));

                                                JSONObject day3 = (JSONObject) intervals.get(3);
                                                JSONObject values3 = day3.getJSONObject("values");
                                                TextView date3 = (TextView) findViewById(R.id.date3);
                                                ImageView weatherRep3 = (ImageView) findViewById(R.id.weatherRep3);
                                                weatherRep3.setImageResource(getXMLWeather(values3.getString("weatherCode")));
                                                TextView minTemperature3 = (TextView) findViewById(R.id.minTemperature3);
                                                TextView maxTemperature3 = (TextView) findViewById(R.id.maxTemperature3);
                                                date3.setText(day3.getString("startTime").split("T",0)[0]);
                                                minTemperature3.setText(Integer.toString((int) Math.round(values3.getDouble("temperatureMin"))));
                                                maxTemperature3.setText(Integer.toString((int) Math.round(values3.getDouble("temperatureMax"))));

                                                JSONObject day4 = (JSONObject) intervals.get(4);
                                                JSONObject values4 = day4.getJSONObject("values");
                                                TextView date4 = (TextView) findViewById(R.id.date4);
                                                ImageView weatherRep4 = (ImageView) findViewById(R.id.weatherRep4);
                                                weatherRep4.setImageResource(getXMLWeather(values4.getString("weatherCode")));
                                                TextView minTemperature4 = (TextView) findViewById(R.id.minTemperature4);
                                                TextView maxTemperature4 = (TextView) findViewById(R.id.maxTemperature4);
                                                date4.setText(day4.getString("startTime").split("T",0)[0]);
                                                minTemperature4.setText(Integer.toString((int) Math.round(values4.getDouble("temperatureMin"))));
                                                maxTemperature4.setText(Integer.toString((int) Math.round(values4.getDouble("temperatureMax"))));

                                                JSONObject day5 = (JSONObject) intervals.get(5);
                                                JSONObject values5 = day5.getJSONObject("values");
                                                TextView date5 = (TextView) findViewById(R.id.date5);
                                                ImageView weatherRep5 = (ImageView) findViewById(R.id.weatherRep5);
                                                weatherRep5.setImageResource(getXMLWeather(values5.getString("weatherCode")));
                                                TextView minTemperature5 = (TextView) findViewById(R.id.minTemperature5);
                                                TextView maxTemperature5 = (TextView) findViewById(R.id.maxTemperature5);
                                                date5.setText(day5.getString("startTime").split("T",0)[0]);
                                                minTemperature5.setText(Integer.toString((int) Math.round(values5.getDouble("temperatureMin"))));
                                                maxTemperature5.setText(Integer.toString((int) Math.round(values5.getDouble("temperatureMax"))));

                                                JSONObject day6 = (JSONObject) intervals.get(6);
                                                JSONObject values6 = day6.getJSONObject("values");
                                                TextView date6 = (TextView) findViewById(R.id.date6);
                                                ImageView weatherRep6 = (ImageView) findViewById(R.id.weatherRep6);
                                                weatherRep6.setImageResource(getXMLWeather(values5.getString("weatherCode")));
                                                TextView minTemperature6 = (TextView) findViewById(R.id.minTemperature6);
                                                TextView maxTemperature6 = (TextView) findViewById(R.id.maxTemperature6);
                                                date6.setText(day6.getString("startTime").split("T",0)[0]);
                                                minTemperature6.setText(Integer.toString((int) Math.round(values6.getDouble("temperatureMin"))));
                                                maxTemperature6.setText(Integer.toString((int) Math.round(values6.getDouble("temperatureMax"))));

                                                WT = getWeatherInfo(values.getString("weatherCode"));
                                                WS = values.getString("windSpeed") + "mph";
                                                Pre = values.getString("pressureSurfaceLevel") + "inHg";
                                                Preci = values.getString("precipitationProbability") + "%";
                                                Temp = Integer.toString((int) Math.round(values.getDouble("temperature"))) + " \u2109";
                                                Hum = values.getString("humidity") + "%";
                                                Vis = values.getString("visibility") + "mi";
                                                CC = values.getString("cloudCover") + "%";
                                                String preci_1 = Integer.toString((int) Math.round(values.getDouble("precipitationProbability")));
                                                String hum_1 = Integer.toString((int) Math.round(values.getDouble("humidity")));
                                                String CC_1 = Integer.toString((int) Math.round(values.getDouble("cloudCover")));

                                                String MinTemp = Integer.toString((int) Math.round(values.getDouble("temperatureMin")));
                                                String MinTemp1 = Integer.toString((int) Math.round(values1.getDouble("temperatureMin")));
                                                String MinTemp2= Integer.toString((int) Math.round(values2.getDouble("temperatureMin")));
                                                String MinTemp3= Integer.toString((int) Math.round(values3.getDouble("temperatureMin")));
                                                String MinTemp4= Integer.toString((int) Math.round(values4.getDouble("temperatureMin")));
                                                String MinTemp5= Integer.toString((int) Math.round(values5.getDouble("temperatureMin")));
                                                String MinTemp6= Integer.toString((int) Math.round(values6.getDouble("temperatureMin")));

                                                String MaxTemp = Integer.toString((int) Math.round(values.getDouble("temperatureMax")));
                                                String MaxTemp1 = Integer.toString((int) Math.round(values1.getDouble("temperatureMax")));
                                                String MaxTemp2 = Integer.toString((int) Math.round(values2.getDouble("temperatureMax")));
                                                String MaxTemp3 = Integer.toString((int) Math.round(values3.getDouble("temperatureMax")));
                                                String MaxTemp4 = Integer.toString((int) Math.round(values4.getDouble("temperatureMax")));
                                                String MaxTemp5 = Integer.toString((int) Math.round(values5.getDouble("temperatureMax")));
                                                String MaxTemp6 = Integer.toString((int) Math.round(values6.getDouble("temperatureMax")));
                                                CardView card1 = findViewById(R.id.card1);
                                                card1.setOnClickListener(new View.OnClickListener() {
                                                    public void onClick(View v) {
                                                        System.out.println("original temo info: " + MinTemp + " " + MaxTemp1);
                                                        Intent intent = new Intent(Searchable.this, Details.class);
                                                        intent.putExtra("WS", WS);
                                                        intent.putExtra("WT", WT);
                                                        intent.putExtra("Pre", Pre);
                                                        intent.putExtra("Preci", Preci);
                                                        intent.putExtra("Temp", Temp);
                                                        intent.putExtra("Hum", Hum);
                                                        intent.putExtra("Vis", Vis);
                                                        intent.putExtra("CC", CC);
                                                        intent.putExtra("minTemp", MinTemp);
                                                        intent.putExtra("minTemp1", MinTemp1);
                                                        intent.putExtra("minTemp2", MinTemp2);
                                                        intent.putExtra("minTemp3", MinTemp3);
                                                        intent.putExtra("minTemp4", MinTemp4);
                                                        intent.putExtra("minTemp5", MinTemp5);
                                                        intent.putExtra("minTemp6", MinTemp6);

                                                        intent.putExtra("maxTemp", MaxTemp);
                                                        intent.putExtra("maxTemp1", MaxTemp1);
                                                        intent.putExtra("maxTemp2", MaxTemp2);
                                                        intent.putExtra("maxTemp3", MaxTemp3);
                                                        intent.putExtra("maxTemp4", MaxTemp4);
                                                        intent.putExtra("maxTemp5", MaxTemp5);
                                                        intent.putExtra("maxTemp6", MaxTemp6);

                                                        intent.putExtra("preci_1", preci_1);
                                                        intent.putExtra("hum_1", hum_1);
                                                        intent.putExtra("CC_1", CC_1);
                                                        startActivity(intent);
                                                    }
                                                });
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

        // Add the request to the RequestQueue.
        queue.add(geoJsonObjectRequest);
        FloatingActionButton add_btn = findViewById(R.id.fav);
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Flag == 0)
                {
                    Context context = getApplicationContext();
                    CharSequence text = cityAndState + " add to favorites";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                    add_btn.setImageResource(R.drawable.map_marker_minus);
                    reCity = cityAndState;
                    list.add(cityAndState);
                    Flag = 1;
                }
                else
                {
                    Context context = getApplicationContext();
                    CharSequence text = cityAndState + " was removed from favorites";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                    add_btn.setImageResource(R.drawable.map_marker_plus);
                    reCity = cityAndState;
                    list.remove(cityAndState);
                    Flag = 0;
                }

            }
        });

    }

    private int getXMLWeather(String code_number)
    {
        if(code_number.equals("1000"))
        {
            return R.drawable.ic_clear_day;
        }
        if(code_number.equals("1100"))
        {
            return R.drawable.ic_mostly_clear_day;
        }
        if(code_number.equals("1101"))
        {
            return R.drawable.ic_partly_cloudy_day;
        }
        if(code_number.equals("1102"))
        {
            return R.drawable.ic_mostly_cloudy;
        }
        if(code_number.equals("1001"))
        {
            return R.drawable.ic_cloudy;
        }
        if(code_number.equals("2000"))
        {
            return R.drawable.ic_fog;
        }
        if(code_number.equals("2100"))
        {
            return R.drawable.ic_fog_light;
        }
        if(code_number.equals("8000"))
        {
            return R.drawable.ic_tstorm;
        }
        if(code_number.equals("5001"))
        {
            return R.drawable.ic_flurries;
        }
        if(code_number.equals("5100"))
        {
            return R.drawable.ic_snow_light;
        }
        if(code_number.equals("5000"))
        {
            return R.drawable.ic_snow;
        }
        if(code_number.equals("5101"))
        {
            return R.drawable.ic_snow_heavy;
        }
        if(code_number.equals("7102"))
        {
            return R.drawable.ic_ice_pellets_light;
        }
        if(code_number.equals("7000"))
        {
            return R.drawable.ic_ice_pellets;
        }
        if(code_number.equals("7101"))
        {
            return R.drawable.ic_ice_pellets_heavy;
        }
        if(code_number.equals("4000"))
        {
            return R.drawable.ic_drizzle;
        }
        if(code_number.equals("6000"))
        {
            return R.drawable.ic_freezing_drizzle;
        }
        if(code_number.equals("6200"))
        {
            return R.drawable.ic_freezing_rain;
        }
        if(code_number.equals("6001"))
        {
            return R.drawable.ic_freezing_rain;
        }
        if(code_number.equals("6201"))
        {
            return R.drawable.ic_freezing_rain_heavy;
        }
        if(code_number.equals("4200"))
        {
            return R.drawable.ic_rain_light;
        }
        if(code_number.equals("4001"))
        {
            return R.drawable.ic_rain;
        }
        if(code_number.equals("4201"))
        {
            return R.drawable.ic_rain_heavy;
        }
        return 0;
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

    public void turnOnDetails(View view) {

        Intent detailsIntent = new Intent(Searchable.this, Details.class);
        startActivity(detailsIntent);

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
                Intent intent = new Intent(Searchable.this,Searchable.class);
                intent.putExtra("cityAndState", list);
                startActivity(intent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // AUTO COMPLETE LATER
                return false;
            }
        });

        MenuItemCompat.setOnActionExpandListener(menuItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                Intent intent = new Intent(Searchable.this, HomeActivity.class);
                intent.putExtra("cityAndState", list);
                startActivity(intent);
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }
}