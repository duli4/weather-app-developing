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
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

public class HomeActivity extends AppCompatActivity {

    String city, region, latitude, longitude;
    String loc, seachResultFav = "";
    ArrayList<String> list = null;
    FragmentStateAdapter pagerAdapter;
    private ViewPager2 viewpager2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        list = new ArrayList<String>();
        try {
            ArrayList<String> mylist = (ArrayList<String>) getIntent().getSerializableExtra("cityAndState");
            if (mylist != null)
            {
                list = mylist;
            }
            System.out.println("back city is: " + list);

//            seachResultFav = searchStrCity;
//            list.add(searchStrCity);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            System.out.println("list length is: " + list.size());
        } catch (Exception e) {
            e.printStackTrace();
        }

        viewpager2 = findViewById(R.id.pager);
        pagerAdapter = new ScreenSlidePageAdapter(this);
        viewpager2.setAdapter(pagerAdapter);


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
                list.add(query);
                Intent intent = new Intent(HomeActivity.this,Searchable.class);
                System.out.println("home list" + list);
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

        return super.onCreateOptionsMenu(menu);
    }

    private class ScreenSlidePageAdapter extends FragmentStateAdapter {
        public ScreenSlidePageAdapter(HomeActivity mainActivity) {
            super(mainActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            if (position == 0) {
                return new HomeFragment();
            }
            for (int i = 1; i < list.size() + 1; i++) {
                if (i == position) {
                    Bundle bundle = new Bundle();
                    bundle.putString("edttext", list.get(i - 1));
                    FavActivity fragobj = new FavActivity();
                    fragobj.setArguments(bundle);
                    return fragobj;
                }
            }
            return null;
        }

        @Override
        public int getItemCount() {
            return list.size()+1;
        }
    }

}