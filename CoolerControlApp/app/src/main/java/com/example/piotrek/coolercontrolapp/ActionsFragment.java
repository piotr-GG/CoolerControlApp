package com.example.piotrek.coolercontrolapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import static android.R.attr.value;

/**
 * Created by Piotrek on 2017-12-01.
 */


public class ActionsFragment extends Fragment {

    ToggleButton modeToggleButton;
    EditText tempEditText;
    EditText rpmEditText;
    Button applyActionsButton;
    MainActivity mainActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_actions, container, false);

        modeToggleButton = (ToggleButton) rootView.findViewById(R.id.modeToggleButton);
        tempEditText = (EditText) rootView.findViewById(R.id.tempEditText);
        rpmEditText = (EditText) rootView.findViewById(R.id.rpmEditText);
        applyActionsButton = (Button) rootView.findViewById(R.id.applyActionsButton);


        rpmEditText.setVisibility(View.VISIBLE);
        tempEditText.setVisibility(View.GONE);


        modeToggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(modeToggleButton.isChecked()) {
                    MainActivity.MODE = MainActivity.AUTO_MODE;
                    rpmEditText.setVisibility(View.GONE);
                    tempEditText.setVisibility(View.VISIBLE);
                    Toast.makeText(getContext(),"Enter required temperature value",Toast.LENGTH_SHORT).show();
                } else {
                    MainActivity.MODE = MainActivity.MANUAL_MODE;
                    rpmEditText.setVisibility(View.VISIBLE);
                    tempEditText.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "Enter required RPM value", Toast.LENGTH_SHORT).show();

                }
            }
        });

        applyActionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String regulatedValue;
                boolean successfulRegulation;
                if(modeToggleButton.isChecked()) {
                    regulatedValue = tempEditText.getText().toString();
                    Integer value = Integer.valueOf(regulatedValue);
                    successfulRegulation = mainActivity.setAutoValue(value);
                } else {
                    regulatedValue = rpmEditText.getText().toString();
                    Integer value = Integer.valueOf(regulatedValue);
                    successfulRegulation = mainActivity.setManualValue(value);
                }

                if(successfulRegulation) {
                    if(modeToggleButton.isChecked()) {
                        MainActivity.MODE = MainActivity.AUTO_MODE;
                        Toast.makeText(getContext(), "Auto regulation in progress! TEMP VALUE = " + regulatedValue, Toast.LENGTH_SHORT).show();
                    } else {
                        MainActivity.MODE = MainActivity.MANUAL_MODE;
                        Toast.makeText(getContext(), "Manual regulation in progress! RPM VALUE = " + regulatedValue, Toast.LENGTH_SHORT).show();
                    }
                }
                else Toast.makeText(getContext(), "Something went wrong!", Toast.LENGTH_SHORT).show();
            }
        });
        return rootView;
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        mainActivity = (MainActivity) getActivity();
    }
}
