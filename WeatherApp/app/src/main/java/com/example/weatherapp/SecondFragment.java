package com.example.weatherapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.highsoft.highcharts.common.HIColor;
import com.highsoft.highcharts.common.HIGradient;
import com.highsoft.highcharts.common.HIStop;
import com.highsoft.highcharts.common.hichartsclasses.HIArearange;
import com.highsoft.highcharts.common.hichartsclasses.HIBackground;
import com.highsoft.highcharts.common.hichartsclasses.HICSSObject;
import com.highsoft.highcharts.common.hichartsclasses.HIChart;
import com.highsoft.highcharts.common.hichartsclasses.HIData;
import com.highsoft.highcharts.common.hichartsclasses.HIDataLabels;
import com.highsoft.highcharts.common.hichartsclasses.HIEvents;
import com.highsoft.highcharts.common.hichartsclasses.HILabels;
import com.highsoft.highcharts.common.hichartsclasses.HILegend;
import com.highsoft.highcharts.common.hichartsclasses.HIOptions;
import com.highsoft.highcharts.common.hichartsclasses.HIPane;
import com.highsoft.highcharts.common.hichartsclasses.HIPlotOptions;
import com.highsoft.highcharts.common.hichartsclasses.HISolidgauge;
import com.highsoft.highcharts.common.hichartsclasses.HITitle;
import com.highsoft.highcharts.common.hichartsclasses.HITooltip;
import com.highsoft.highcharts.common.hichartsclasses.HIXAxis;
import com.highsoft.highcharts.common.hichartsclasses.HIYAxis;
import com.highsoft.highcharts.core.HIChartView;
import com.highsoft.highcharts.core.HIFunction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SecondFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SecondFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    String minTemp, minTemp1, minTemp2, minTemp3, minTemp4, minTemp5, minTemp6;
    String maxTemp, maxTemp1, maxTemp2, maxTemp3, maxTemp4, maxTemp5, maxTemp6;
    int x1, x2, x3, x4, x5, x6, x7;
    int y1, y2, y3, y4, y5, y6, y7;
    public SecondFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SecondFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SecondFragment newInstance(String param1, String param2) {
        SecondFragment fragment = new SecondFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_third, container, false);

        try {
            minTemp = getArguments().getString("minTemp");
            minTemp1 = getArguments().getString("minTemp1");
            minTemp2 = getArguments().getString("minTemp2");
            minTemp3 = getArguments().getString("minTemp3");
            minTemp4 = getArguments().getString("minTemp4");
            minTemp5 = getArguments().getString("minTemp5");
            minTemp6 = getArguments().getString("minTemp6");

            maxTemp = getArguments().getString("maxTemp");
            maxTemp1 = getArguments().getString("maxTemp1");
            maxTemp2 = getArguments().getString("maxTemp2");
            maxTemp3 = getArguments().getString("maxTemp3");
            maxTemp4 = getArguments().getString("maxTemp4");
            maxTemp5 = getArguments().getString("maxTemp5");
            maxTemp6 = getArguments().getString("maxTemp6");

            System.out.println("min value and max value is: " + minTemp + " " + maxTemp1);

            x1 = Integer.parseInt(minTemp);
            x2 = Integer.parseInt(minTemp1);
            x3 = Integer.parseInt(minTemp2);
            x4 = Integer.parseInt(minTemp3);
            x5 = Integer.parseInt(minTemp4);
            x6 = Integer.parseInt(minTemp5);
            x7 = Integer.parseInt(minTemp6);

            y1 = Integer.parseInt(maxTemp);
            y2 = Integer.parseInt(maxTemp1);
            y3 = Integer.parseInt(maxTemp2);
            y4 = Integer.parseInt(maxTemp3);
            y5 = Integer.parseInt(maxTemp4);
            y6 = Integer.parseInt(maxTemp5);
            y7 = Integer.parseInt(maxTemp6);

        } catch (Exception e) {
            e.printStackTrace();
        }

        HIChartView chartView = rootView.findViewById(R.id.hc);

        HIOptions options = new HIOptions();

        HIChart chart = new HIChart();
        chart.setType("arearange");
        chart.setZoomType("x");
        options.setChart(chart);

        HITitle title = new HITitle();
        title.setText("Temperature variation by day");
        options.setTitle(title);

        HIXAxis xaxis = new HIXAxis();
        ArrayList<String> categories = new ArrayList<>();
        for (int i = 0; i < 11; i++) {
            categories.add(String.valueOf(i));
        }
        xaxis.setCategories(categories);

        HILabels labels = new HILabels();
        labels.setStep(2);
        xaxis.setLabels(labels);

        options.setXAxis(new ArrayList(){{add(xaxis);}});

        HIYAxis yaxis = new HIYAxis();
        yaxis.setTitle(new HITitle());
        options.setYAxis(new ArrayList<>(Collections.singletonList(yaxis)));

        HITooltip tooltip = new HITooltip();
        tooltip.setShadow(true);
        tooltip.setValueSuffix("F");
        options.setTooltip(tooltip);

        HILegend legend = new HILegend();
        legend.setEnabled(false);
        options.setLegend(legend);

        HIArearange series = new HIArearange();
        series.setName("Temperatures");

        LinkedList<HIStop> stops = new LinkedList<>();
        HIGradient gradient = new HIGradient(0, 0, 1, 0);
        stops.add(new HIStop(0, HIColor.initWithHexValue("EFAD48")));
        stops.add(new HIStop(1, HIColor.initWithHexValue("2E8CF6")));
        series.setFillColor(HIColor.initWithLinearGradient(gradient,stops));

        Object[][] seriesData = new Object[][]{

                { x1, y1},
                { x2, y2},
                { x3, y3},
                { x4, y4},
                { x5, y5},
                { x6, y6},
                { x7, y7},

        };
        series.setData(new ArrayList<>(Arrays.asList(seriesData)));
        options.setSeries(new ArrayList<>(Arrays.asList(series)));

        chartView.setOptions(options);

        // Inflate the layout for this fragment
        return rootView;

    }

}