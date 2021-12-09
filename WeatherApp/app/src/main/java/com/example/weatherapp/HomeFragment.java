package com.example.weatherapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
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

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class HomeFragment extends Fragment {
    String city, region, latitude, longitude;
    String loc, seachResultFav = "";
    String WS, WT, Pre, Preci, Temp, Hum, Vis, CC;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @org.jetbrains.annotations.NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_home, container, false);

        //        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());

        String geoURL = "https://ipinfo.io/?token=20ef1690f3db86";

        JsonObjectRequest geoJsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, geoURL, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            city = (String) response.get("city");
                            region = (String) response.get("region");
                            String cityState = city+ ", "+ region;
                            TextView location = (TextView) rootView.findViewById(R.id.location);
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
                                                ImageView weatherIcon = (ImageView) rootView.findViewById(R.id.weatherIcon);
                                                TextView temperature = (TextView) rootView.findViewById(R.id.temperature);
                                                TextView weatherType = (TextView) rootView.findViewById(R.id.weatherType);
                                                TextView humidtyData = (TextView) rootView.findViewById(R.id.humidtyData);
                                                TextView windSpeedData = (TextView) rootView.findViewById(R.id.windSpeedData);
                                                TextView visibilityData = (TextView) rootView.findViewById(R.id.visibilityData);
                                                TextView pressureData = (TextView) rootView.findViewById(R.id.pressureData);

                                                JSONObject data = response.getJSONObject("data");
                                                JSONArray timelines = data.getJSONArray("timelines");
                                                JSONObject element = (JSONObject) timelines.get(0);
                                                JSONArray intervals = element.getJSONArray("intervals");
                                                JSONObject today = (JSONObject) intervals.get(0);
                                                JSONObject values = today.getJSONObject("values");

                                                weatherType.setText(getWeatherInfo(values.getString("weatherCode")));

                                                weatherIcon.setImageResource(getXMLWeather(values.getString("weatherCode")));
                                                temperature.setText(Integer.toString((int) Math.round(values.getDouble("temperature"))) + " \u2109");
                                                humidtyData.setText(values.getString("humidity") + "%");
                                                windSpeedData.setText(values.getString("windSpeed") + "mph");
                                                visibilityData.setText(values.getString("visibility") + "mi");
                                                pressureData.setText(values.getString("pressureSurfaceLevel") + "inHg");


                                                JSONObject day1 = (JSONObject) intervals.get(1);
                                                JSONObject values1 = day1.getJSONObject("values");
                                                TextView date1 = (TextView) rootView.findViewById(R.id.date1);
                                                ImageView weatherRep1 = (ImageView) rootView.findViewById(R.id.weatherRep1);
                                                weatherRep1.setImageResource(getXMLWeather(values1.getString("weatherCode")));
                                                TextView minTemperature1 = (TextView) rootView.findViewById(R.id.minTemperature1);
                                                TextView maxTemperature1 = (TextView) rootView.findViewById(R.id.maxTemperature1);
                                                date1.setText(day1.getString("startTime").split("T",0)[0]);
                                                minTemperature1.setText(Integer.toString((int) Math.round(values1.getDouble("temperatureMin"))));
                                                maxTemperature1.setText(Integer.toString((int) Math.round(values1.getDouble("temperatureMax"))));

                                                JSONObject day2 = (JSONObject) intervals.get(2);
                                                JSONObject values2 = day2.getJSONObject("values");
                                                TextView date2 = (TextView) rootView.findViewById(R.id.date2);
                                                ImageView weatherRep2 = (ImageView) rootView.findViewById(R.id.weatherRep2);
                                                weatherRep2.setImageResource(getXMLWeather(values2.getString("weatherCode")));
                                                TextView minTemperature2 = (TextView) rootView.findViewById(R.id.minTemperature2);
                                                TextView maxTemperature2 = (TextView) rootView.findViewById(R.id.maxTemperature2);
                                                date2.setText(day2.getString("startTime").split("T",0)[0]);
                                                minTemperature2.setText(Integer.toString((int) Math.round(values2.getDouble("temperatureMin"))));
                                                maxTemperature2.setText(Integer.toString((int) Math.round(values2.getDouble("temperatureMax"))));

                                                JSONObject day3 = (JSONObject) intervals.get(3);
                                                JSONObject values3 = day3.getJSONObject("values");
                                                TextView date3 = (TextView) rootView.findViewById(R.id.date3);
                                                ImageView weatherRep3 = (ImageView) rootView.findViewById(R.id.weatherRep3);
                                                weatherRep3.setImageResource(getXMLWeather(values3.getString("weatherCode")));
                                                TextView minTemperature3 = (TextView) rootView.findViewById(R.id.minTemperature3);
                                                TextView maxTemperature3 = (TextView) rootView.findViewById(R.id.maxTemperature3);
                                                date3.setText(day3.getString("startTime").split("T",0)[0]);
                                                minTemperature3.setText(Integer.toString((int) Math.round(values3.getDouble("temperatureMin"))));
                                                maxTemperature3.setText(Integer.toString((int) Math.round(values3.getDouble("temperatureMax"))));

                                                JSONObject day4 = (JSONObject) intervals.get(4);
                                                JSONObject values4 = day4.getJSONObject("values");
                                                TextView date4 = (TextView) rootView.findViewById(R.id.date4);
                                                ImageView weatherRep4 = (ImageView) rootView.findViewById(R.id.weatherRep4);
                                                weatherRep4.setImageResource(getXMLWeather(values4.getString("weatherCode")));
                                                TextView minTemperature4 = (TextView) rootView.findViewById(R.id.minTemperature4);
                                                TextView maxTemperature4 = (TextView) rootView.findViewById(R.id.maxTemperature4);
                                                date4.setText(day4.getString("startTime").split("T",0)[0]);
                                                minTemperature4.setText(Integer.toString((int) Math.round(values4.getDouble("temperatureMin"))));
                                                maxTemperature4.setText(Integer.toString((int) Math.round(values4.getDouble("temperatureMax"))));

                                                JSONObject day5 = (JSONObject) intervals.get(5);
                                                JSONObject values5 = day5.getJSONObject("values");
                                                TextView date5 = (TextView) rootView.findViewById(R.id.date5);
                                                ImageView weatherRep5 = (ImageView) rootView.findViewById(R.id.weatherRep5);
                                                weatherRep5.setImageResource(getXMLWeather(values5.getString("weatherCode")));
                                                TextView minTemperature5 = (TextView) rootView.findViewById(R.id.minTemperature5);
                                                TextView maxTemperature5 = (TextView) rootView.findViewById(R.id.maxTemperature5);
                                                date5.setText(day5.getString("startTime").split("T",0)[0]);
                                                minTemperature5.setText(Integer.toString((int) Math.round(values5.getDouble("temperatureMin"))));
                                                maxTemperature5.setText(Integer.toString((int) Math.round(values5.getDouble("temperatureMax"))));

                                                JSONObject day6 = (JSONObject) intervals.get(6);
                                                JSONObject values6 = day6.getJSONObject("values");
                                                TextView date6 = (TextView) rootView.findViewById(R.id.date6);
                                                ImageView weatherRep6 = (ImageView) rootView.findViewById(R.id.weatherRep6);
                                                weatherRep6.setImageResource(getXMLWeather(values6.getString("weatherCode")));
                                                TextView minTemperature6 = (TextView) rootView.findViewById(R.id.minTemperature6);
                                                TextView maxTemperature6 = (TextView) rootView.findViewById(R.id.maxTemperature6);
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
                                                CardView card1 = rootView.findViewById(R.id.card1);
                                                card1.setOnClickListener(new View.OnClickListener() {
                                                    public void onClick(View v) {
                                                        System.out.println("original temo info: " + MinTemp + " " + MaxTemp1);
                                                        Intent intent = new Intent(getActivity(), Details.class);
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


        CardView card1 = rootView.findViewById(R.id.card1);
//        card1.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//
//                Intent intent = new Intent(getActivity(), Details.class);
//                intent.putExtra("WS", WS);
//                intent.putExtra("WT", WT);
////                intent.putExtra("Pre", Pre);
////                intent.putExtra("Preci", Preci);
////                intent.putExtra("Temp", Temp);
////                intent.putExtra("Hum", Hum);
////                intent.putExtra("Vis", Vis);
////                intent.putExtra("CC", CC);
//
//                startActivity(intent);
//            }
//        });

        return rootView;
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



}
