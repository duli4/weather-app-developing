package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // set theme back to normal after splash screen
        setTheme(R.style.Theme_WeatherApp);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView fetchWeatherView = (TextView) findViewById(R.id.fetchWeatherText);
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);


        // Display loading page for 3 sec
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                Log.i("info", "jin lai le");
                fetchWeatherView.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(intent);

                Fragment newFragment =new  ViewPagerWithCircleIndicatorView();
                FragmentTransaction transaction =getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.card1,newFragment);
                transaction.commit();
            }
        }, 3000);
    }
}