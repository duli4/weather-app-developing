package com.example.weatherapp;

import androidx.annotation.DrawableRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

public class Details extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager2 pager2;
    FragmentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        String WS, WT, Pre, Preci, Temp, Hum, Vis, CC;
        Intent HomeIntent = getIntent();
        WS = HomeIntent.getExtras().getString("WS");
        WT = HomeIntent.getExtras().getString("WT");
        Pre = HomeIntent.getExtras().getString("Pre");
        Preci = HomeIntent.getExtras().getString("Preci");
        Temp = HomeIntent.getExtras().getString("Temp");
        Hum = HomeIntent.getExtras().getString("Hum");
        Vis = HomeIntent.getExtras().getString("Vis");
        CC = HomeIntent.getExtras().getString("CC");

        System.out.println("xixixixi"+ WS);


        tabLayout = findViewById(R.id.tab_layout);
        pager2 = findViewById(R.id.view_pager2);

        FragmentManager fm = getSupportFragmentManager();
        adapter = new FragmentAdapter(fm, getLifecycle());
        pager2.setAdapter(adapter);

        tabLayout.addTab(tabLayout.newTab().setText("TODAY"));

//        tabLayout.setBackgroundColor(Color.parseColor("#000000"));
        tabLayout.addTab(tabLayout.newTab().setText("WEEKLY"));
        tabLayout.addTab(tabLayout.newTab().setText("WEATHER DATA"));
        tabLayout.getTabAt(0).setIcon(R.drawable.calendar_today);
        tabLayout.getTabAt(1).setIcon(R.drawable.trending_up);
        tabLayout.getTabAt(2).setIcon(R.drawable.thermometer_low);
        tabLayout.setTabTextColors(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
        tabLayout.setTabIconTint(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#FFFFFF"));


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        pager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.details_toolbar, menu);
        MenuItem menuItem = menu.findItem(R.id.twitter);

        menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                try{
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse( "http://twitter.com/intent/tweet?text=Check out Bellevue, Washington's weather! It is 56.86F!#CSCI571WeatherSearch")));
                } catch (ActivityNotFoundException e) {

                    e.printStackTrace();
                }
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }


}