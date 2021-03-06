package com.example.piotrek.coolercontrolapp;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

/**
 * Created by Piotrek on 2017-12-01.
 */


public class SettingsFragment extends Fragment {
    Spinner languageSpinner;
    Spinner postDataIntervalSpinner;
    Button applyButton;

    String[] languages;
    String[] intervals;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_settings,container, false);
        languageSpinner = (Spinner) rootView.findViewById(R.id.languageSpinner);
        postDataIntervalSpinner = (Spinner) rootView.findViewById(R.id.postDataIntervalSpinner);
        applyButton = (Button) rootView.findViewById(R.id.applyButton);

        Resources resources = getResources();
        languages = resources.getStringArray(R.array.languageOptions_pl);
        intervals = resources.getStringArray(R.array.intervals);

        languageSpinner.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, languages));
        postDataIntervalSpinner.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, intervals));

        languageSpinner.setSelection(1);
        postDataIntervalSpinner.setSelection(1);

        languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(getContext(), languages[i],Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        postDataIntervalSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(getContext(), intervals[i],Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        return rootView;
    }
}