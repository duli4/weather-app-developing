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
import android.view.View;
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
import androidx.fragment.app.FragmentManager;
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
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class FirstFragment extends Fragment {
    String city, region, latitude, longitude;
    String loc, seachResultFav = "";
    String WS, WT, Pre, Preci, Temp, Hum, Vis, CC;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @org.jetbrains.annotations.NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_first, container, false);
        try {
            WS = getArguments().getString("WS");
            TextView windSpeed = rootView.findViewById(R.id.ws);
            windSpeed.setText(WS);

            WT = getArguments().getString("WT");
            TextView weatherType = rootView.findViewById(R.id.wt);
            weatherType.setText(WT);

            Pre = getArguments().getString("Pre");
            TextView pressure = rootView.findViewById(R.id.pre);
            pressure.setText(Pre);


            Preci = getArguments().getString("Preci");
            TextView precipitation = rootView.findViewById(R.id.preci);
            precipitation.setText(Preci);

            Temp = getArguments().getString("Temp");
            TextView temperature = rootView.findViewById(R.id.temp);
            temperature.setText(Temp);


            Hum = getArguments().getString("Hum");
            TextView humidity = rootView.findViewById(R.id.hum);
            humidity.setText(Hum);

            Vis = getArguments().getString("Vis");
            TextView visibile = rootView.findViewById(R.id.vis);
            visibile.setText(Vis);

            CC = getArguments().getString("CC");
            TextView cloudCover = rootView.findViewById(R.id.cc);
            cloudCover.setText(CC);

            System.out.println("this is another fav fragment, and value is: " + WS + " " + Preci + " " + Temp + " " + Hum + " " + Vis + " " + CC);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return rootView;
    }

}
