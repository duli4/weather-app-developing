package com.example.weatherapp;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
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
    innerAdapter adapter;
    String minTemp, minTemp1, minTemp2, minTemp3, minTemp4, minTemp5, minTemp6;
    String maxTemp, maxTemp1, maxTemp2, maxTemp3, maxTemp4, maxTemp5, maxTemp6;
    String WS, WT, Pre, Preci, Temp, Hum, Vis, CC;
    String preci_1, hum_1, CC_1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        try {
            Intent HomeIntent = getIntent();
            WS = HomeIntent.getExtras().getString("WS");
            WT = HomeIntent.getExtras().getString("WT");
            Pre = HomeIntent.getExtras().getString("Pre");
            Preci = HomeIntent.getExtras().getString("Preci");
            Temp = HomeIntent.getExtras().getString("Temp");
            Hum = HomeIntent.getExtras().getString("Hum");
            Vis = HomeIntent.getExtras().getString("Vis");
            CC = HomeIntent.getExtras().getString("CC");
            minTemp = HomeIntent.getExtras().getString("minTemp");
            minTemp1 = HomeIntent.getExtras().getString("minTemp1");
            minTemp2 = HomeIntent.getExtras().getString("minTemp2");
            minTemp3 = HomeIntent.getExtras().getString("minTemp3");
            minTemp4 = HomeIntent.getExtras().getString("minTemp4");
            minTemp5 = HomeIntent.getExtras().getString("minTemp5");
            minTemp6 = HomeIntent.getExtras().getString("minTemp6");

            maxTemp = HomeIntent.getExtras().getString("maxTemp");
            maxTemp1 = HomeIntent.getExtras().getString("maxTemp1");
            maxTemp2 = HomeIntent.getExtras().getString("maxTemp2");
            maxTemp3 = HomeIntent.getExtras().getString("maxTemp3");
            maxTemp4 = HomeIntent.getExtras().getString("maxTemp4");
            maxTemp5 = HomeIntent.getExtras().getString("maxTemp5");
            maxTemp6 = HomeIntent.getExtras().getString("maxTemp6");

            preci_1 = HomeIntent.getExtras().getString("preci_1");
            hum_1 = HomeIntent.getExtras().getString("hum_1");
            CC_1 =  HomeIntent.getExtras().getString("CC_1");
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("xixixixi"+ maxTemp + " " + minTemp4);



        tabLayout = findViewById(R.id.tab_layout);
        pager2 = findViewById(R.id.view_pager2);

        FragmentManager fm = getSupportFragmentManager();
        adapter = new innerAdapter(fm, getLifecycle());
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

    private class innerAdapter extends FragmentStateAdapter {

        public innerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
            super(fragmentManager, lifecycle);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {

            switch (position) {

                case 0:
                    Bundle bundle = new Bundle();
                    bundle.putString("WS", WS);
                    bundle.putString("WT", WT);
                    bundle.putString("Pre", Pre);
                    bundle.putString("Preci", Preci);
                    bundle.putString("Temp", Temp);
                    bundle.putString("Hum", Hum);
                    bundle.putString("Vis", Vis);
                    bundle.putString("CC", CC);
                    FirstFragment fragobj = new FirstFragment();
                    fragobj.setArguments(bundle);
                    return fragobj;
                case 1:
                    Bundle bundle2 = new Bundle();
                    System.out.println("cases 1 in details: " + minTemp);
                    bundle2.putString("minTemp", minTemp);
                    bundle2.putString("minTemp1", minTemp1);
                    bundle2.putString("minTemp2", minTemp2);
                    bundle2.putString("minTemp3", minTemp3);
                    bundle2.putString("minTemp4", minTemp4);
                    bundle2.putString("minTemp5", minTemp5);
                    bundle2.putString("minTemp6", minTemp6);

                    bundle2.putString("maxTemp", maxTemp);
                    bundle2.putString("maxTemp1", maxTemp1);
                    bundle2.putString("maxTemp2", maxTemp2);
                    bundle2.putString("maxTemp3", maxTemp3);
                    bundle2.putString("maxTemp4", maxTemp4);
                    bundle2.putString("maxTemp5", maxTemp5);
                    bundle2.putString("maxTemp6", maxTemp6);

                    SecondFragment fragobj2 = new SecondFragment();
                    fragobj2.setArguments(bundle2);
                    return fragobj2;
                case 2:
                    Bundle bundle3 = new Bundle();
                    bundle3.putString("preci_1", preci_1);
                    bundle3.putString("hum_1", hum_1);
                    bundle3.putString("CC_1", CC_1);
                    System.out.println("send value to frg3: "+ preci_1 + " " + hum_1 + " " + CC_1);
                    ThirdFragment fragobj3 = new ThirdFragment();
                    fragobj3.setArguments(bundle3);
                    return fragobj3;
                default:
                    return null;
            }
        }


        @Override
        public int getItemCount() {
            return 3;
        }
    }

}