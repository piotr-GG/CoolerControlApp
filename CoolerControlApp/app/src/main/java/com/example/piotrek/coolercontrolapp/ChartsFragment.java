package com.example.piotrek.coolercontrolapp;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by Piotrek on 2017-12-01.
 */

public class ChartsFragment extends Fragment {

    MainActivity mainActivity;

    RadioButton tempRadioButton;
    RadioButton voltageRadioButton;
    RadioButton rpmRadioButton;
    RadioButton pwmRadioButton;

    Button rangeButton;
    EditText rangeEditText;
    LineChart chart;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_charts, container, false);

        tempRadioButton = (RadioButton) rootView.findViewById(R.id.tempRadioButton);
        voltageRadioButton = (RadioButton) rootView.findViewById(R.id.voltageRadioButton);
        rpmRadioButton = (RadioButton) rootView.findViewById(R.id.rpmRadioButton);
        pwmRadioButton = (RadioButton) rootView.findViewById(R.id.pwmRadioButton);

        rangeButton = (Button) rootView.findViewById(R.id.rangeButton);
        rangeEditText = (EditText) rootView.findViewById(R.id.rangeEditText);

        chart = (LineChart) rootView.findViewById(R.id.chart);

        chart.setVisibleXRangeMaximum(20);

        Description desc = new Description();
        desc.setEnabled(false);
        chart.setDescription(desc);
        chart.setBorderWidth(10f);
        chart.setAutoScaleMinMaxEnabled(false);


        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(14f);
        xAxis.setTextColor(Color.BLACK);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);

        //chart.getViewPortHandler().translate(100f);

        YAxis yAxis = chart.getAxisLeft();
        yAxis.setTextSize(14f); // set the text size
        yAxis.setTextColor(Color.BLACK);
        yAxis.setGranularity(1f); // interval 1
        yAxis.setLabelCount(6, true); // force 6 labels

        YAxis yAxisR = chart.getAxisRight();
        yAxisR.setEnabled(false);


        rangeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chart.setVisibleXRangeMaximum(Integer.valueOf(rangeEditText.getText().toString()));
                chart.setVisibleXRangeMinimum(Integer.valueOf(rangeEditText.getText().toString()));
                chart.invalidate();
            }
        });

        tempRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 if(mainActivity == null){
                     mainActivity = (MainActivity) getActivity();
                 }
                ArrayList<HashMap<String, String>> thingspeakData = mainActivity.getThingspeakData();
                if(thingspeakData != null){
                    List<Entry> entries = new ArrayList<Entry>();



                    for(int i = 0; i < thingspeakData.size(); i++){
                        entries.add(new Entry(i, Float.valueOf(thingspeakData.get(i).get("Temperature"))));
                    }
                    LineDataSet dataSet = new LineDataSet(entries, "Temperature");
                    dataSet.setLineWidth(10f);
                    dataSet.setCircleRadius(10f);
                    dataSet.setCircleHoleRadius(8f);
                    LineData lineData = new LineData(dataSet);
                    lineData.setValueTextSize(12f);
                    chart.setData(lineData);
                    chart.setVisibleXRangeMaximum(8);
                    chart.invalidate();
                } else {
                    Toast.makeText(getContext(),"NO THINGSPEAK DATA",Toast.LENGTH_SHORT).show();
                }
            }
        });

        rpmRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mainActivity == null){
                    mainActivity = (MainActivity) getActivity();
                }
                ArrayList<HashMap<String, String>> thingspeakData = mainActivity.getThingspeakData();
                if(thingspeakData != null) {
                    List<Entry> entries = new ArrayList<Entry>();
                    for (int i = 0; i < thingspeakData.size(); i++) {
                        entries.add(new Entry(i, Float.valueOf(thingspeakData.get(i).get("RPM"))));
                    }
                    LineDataSet dataSet = new LineDataSet(entries, "RPM");
                    dataSet.setLineWidth(10f);
                    dataSet.setCircleRadius(10f);
                    dataSet.setCircleHoleRadius(8f);
                    LineData lineData = new LineData(dataSet);
                    lineData.setValueTextSize(12f);
                    chart.setData(lineData);
                    chart.setVisibleXRangeMaximum(8);
                    chart.invalidate();
                } else {
                    Toast.makeText(getContext(), "NO THINGSPEAK DATA", Toast.LENGTH_SHORT).show();
                }
            }

        });

        voltageRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mainActivity == null){
                    mainActivity = (MainActivity) getActivity();
                }
                ArrayList<HashMap<String, String>> thingspeakData = mainActivity.getThingspeakData();
                if(thingspeakData != null) {
                    List<Entry> entries = new ArrayList<Entry>();
                    for (int i = 0; i < thingspeakData.size(); i++) {
                        entries.add(new Entry(i, Math.round(Float.valueOf(thingspeakData.get(i).get("Voltage"))*100.0f)/100.0f));
                    }
                    LineDataSet dataSet = new LineDataSet(entries, "Voltage");
                    dataSet.setLineWidth(10f);
                    dataSet.setCircleRadius(10f);
                    dataSet.setCircleHoleRadius(8f);
                    LineData lineData = new LineData(dataSet);
                    lineData.setValueTextSize(12f);
                    chart.setData(lineData);
                    chart.setVisibleXRangeMaximum(8);
                    chart.invalidate();
                } else {
                    Toast.makeText(getContext(), "NO THINGSPEAK DATA", Toast.LENGTH_SHORT).show();
                }
            }
        });

        pwmRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mainActivity == null){
                    mainActivity = (MainActivity) getActivity();
                }
                ArrayList<HashMap<String, String>> thingspeakData = mainActivity.getThingspeakData();
                if(thingspeakData != null) {
                    List<Entry> entries = new ArrayList<Entry>();
                    for (int i = 0; i < thingspeakData.size(); i++) {
                        entries.add(new Entry(i, Float.valueOf(thingspeakData.get(i).get("PWM"))));
                    }
                    LineDataSet dataSet = new LineDataSet(entries, "PWM");
                    dataSet.setLineWidth(10f);
                    dataSet.setCircleRadius(10f);
                    dataSet.setCircleHoleRadius(8f);
                    LineData lineData = new LineData(dataSet);
                    lineData.setValueTextSize(12f);
                    chart.setData(lineData);
                    chart.setVisibleXRangeMaximum(8);
                    chart.invalidate();
                } else {
                    Toast.makeText(getContext(), "NO THINGSPEAK DATA", Toast.LENGTH_SHORT).show();
                }
            }
        });



        return rootView;
    }

    public void invalidateChart(){
        chart.invalidate();
    }
}
