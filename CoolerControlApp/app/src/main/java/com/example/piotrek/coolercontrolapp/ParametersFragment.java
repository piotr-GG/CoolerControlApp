package com.example.piotrek.coolercontrolapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Piotrek on 2017-12-01.
 */

public class ParametersFragment extends Fragment {

    TextView rpmValueTextView;
    TextView voltageValueTextView;
    TextView tempValueTextView;
    TextView dutyCycleValueTextView;

    TextView updatedAtTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_parameters, container, false);

        voltageValueTextView = (TextView) rootView.findViewById(R.id.voltageValueTextView);
        tempValueTextView = (TextView) rootView.findViewById(R.id.tempValueTextView);
        dutyCycleValueTextView = (TextView) rootView.findViewById(R.id.dutyCycleValueTextView);
        rpmValueTextView = (TextView) rootView.findViewById(R.id.hallRPMValueTextView);

        updatedAtTextView = (TextView) rootView.findViewById(R.id.updatedAtTextView);
        return rootView;
    }

    public void setParameters(ArrayList<HashMap<String, String>> parameters){
        if(!parameters.isEmpty()) {
            int rpm = Integer.valueOf(parameters.get(parameters.size()-1).get("RPM"));
            rpm = rpm * 60;
            rpmValueTextView.setText(String.valueOf(rpm) +" RPM");

            double voltage = Float.valueOf(parameters.get(parameters.size()-1).get("Voltage"));
            voltage = Math.round(voltage*100.0)/100.0;
            voltageValueTextView.setText(String.valueOf(voltage) + "V");

            tempValueTextView.setText(parameters.get(parameters.size()-1).get("Temperature") + " deg C");

            double dutyCycle = (Float.valueOf(parameters.get(parameters.size()-1).get("PWM")) / 1024.0) * 100.0;
            dutyCycleValueTextView.setText(String.valueOf((int) dutyCycle) + "%");

            updatedAtTextView.setText(parameters.get(parameters.size()-1).get("Date") + " " + parameters.get(parameters.size()-1).get("Time"));

        }
    }

}
