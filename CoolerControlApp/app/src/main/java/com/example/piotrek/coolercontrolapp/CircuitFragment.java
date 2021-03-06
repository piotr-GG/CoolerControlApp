package com.example.piotrek.coolercontrolapp;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

import static com.example.piotrek.coolercontrolapp.R.id.dutyCycleValueTextView;
import static com.example.piotrek.coolercontrolapp.R.id.rpmValueTextView;
import static com.example.piotrek.coolercontrolapp.R.id.tempValueTextView;
import static com.example.piotrek.coolercontrolapp.R.id.voltageValueTextView;

/**
 * Created by Piotrek on 2017-12-01.
 */

public class CircuitFragment extends Fragment {

    ImageView wirnik;
    private ImageView circuitImage;
    private ObjectAnimator anim;
    private Switch relaySwitch;

    private TextView voltageValueTextView;
    private TextView dutyCycleValueTextView;
    private TextView rpmValueTextView;
    private TextView tempValueTextView;
    private TextView circuitUpdatedAtTextView;

    private TextView modeTextView;

    private MainActivity mainActivity;
    private boolean initialized = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_circuit, container, false);
        wirnik = (ImageView) rootView.findViewById(R.id.wirnik);
        circuitImage = (ImageView) rootView.findViewById(R.id.circuitImage);
        relaySwitch = (Switch) rootView.findViewById(R.id.relaySwitch);
        voltageValueTextView = (TextView) rootView.findViewById(R.id.voltageValueTextView);
        dutyCycleValueTextView = (TextView) rootView.findViewById(R.id.dutyCycleValueTextView);
        rpmValueTextView = (TextView) rootView.findViewById(R.id.rpmValueTextView);
        tempValueTextView = (TextView) rootView.findViewById(R.id.tempValueTextView);

        circuitUpdatedAtTextView = (TextView) rootView.findViewById(R.id.circuitUpdatedAt);
        modeTextView = (TextView) rootView.findViewById(R.id.modeTextView);

        relaySwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(relaySwitch.isChecked()){
                    if(mainActivity.setRelay("ON")){
                        relayAnim(true);
                    } else {
                        relaySwitch.setChecked(false);
                    }
                } else {
                    if(mainActivity.setRelay("OFF")){
                        relayAnim(false);
                    } else {
                        relaySwitch.setChecked(true);
                    }
                }
            }
        });

        anim = ObjectAnimator.ofFloat(wirnik, "rotation", 0, 720 );
        anim.setDuration(10);
        anim.setRepeatCount(ObjectAnimator.INFINITE);
        anim.setRepeatMode(ObjectAnimator.RESTART);
        return rootView;
    }

    public void setParameters(ArrayList<HashMap<String, String>> parameters){
        if(!parameters.isEmpty()) {
            int rpm = Integer.valueOf(parameters.get(parameters.size()-1).get("RPM"));
            rpm = rpm * 60;

            anim.setFloatValues(0, rpm*120);
            anim.start();

            rpmValueTextView.setText(String.valueOf(rpm) +" RPM");

            //double voltage = Float.valueOf(parameters.get(parameters.size()-1).get("Voltage"));
            //voltage = Math.round(voltage*100.0)/100.0;
            voltageValueTextView.setText(parameters.get(parameters.size()-1).get("Voltage") + "V");

            tempValueTextView.setText(parameters.get(parameters.size()-1).get("Temperature") + " deg C");

            double dutyCycle = (Float.valueOf(parameters.get(parameters.size()-1).get("PWM")) / 1024.0) * 100.0;
            dutyCycleValueTextView.setText(String.valueOf((int) dutyCycle) + "%");
            if(!initialized){
                if(parameters.get(parameters.size()-1).get("Relay").equals("1")) {
                    relayAnim(true);
                }
                else {
                    relayAnim(false);
                }
                initialized = true;
            }

            if(MainActivity.MODE == MainActivity.AUTO_MODE)  modeTextView.setText("AUTO MODE");
            else modeTextView.setText("MANUAL MODE");

            circuitUpdatedAtTextView.setText(parameters.get(parameters.size()-1).get("Date") + " " + parameters.get(parameters.size()-1).get("Time"));
        }
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        mainActivity = (MainActivity) getActivity();
    }

    private void relayAnim(boolean closed){
        if(closed){
            relaySwitch.setChecked(true);
            relaySwitch.setText("RELAY ON");
            circuitImage.setImageResource(R.mipmap.pwm_closed);
        } else {
            relaySwitch.setChecked(false);
            relaySwitch.setText("RELAY OFF");
            circuitImage.setImageResource(R.mipmap.pwm_open);
        }
    }
}
