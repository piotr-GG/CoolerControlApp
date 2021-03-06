package com.example.piotrek.coolercontrolapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Piotrek on 2017-12-01.
 */


public class ConnectionFragment extends Fragment {

    EditText ipAddressEditText;
    Button connectButton;
    Button disconnectButton;
    ImageView connectionImageView;
    TextView statusTextView;
    TextView logTextView;

    Button sendButton;
    EditText commandEditText;
    MainActivity mainActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_connect, container, false);
        ipAddressEditText = (EditText) rootView.findViewById(R.id.ipAddressTextView);
        connectionImageView = (ImageView) rootView.findViewById(R.id.connectionImageView);
        statusTextView = (TextView) rootView.findViewById(R.id.statusTextView);
        logTextView = (TextView) rootView.findViewById(R.id.logTextView);

        sendButton = (Button) rootView.findViewById(R.id.sendButton);
        commandEditText = (EditText) rootView.findViewById(R.id.commandEditText);

        connectButton = (Button) rootView.findViewById(R.id.connectButton);
        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ip = ipAddressEditText.getText().toString().trim();
                if(mainActivity == null) mainActivity = (MainActivity) getActivity();
                if(!ip.isEmpty()){
                    boolean connectionEstablished = mainActivity.establishConnection(ip, 80);
                    if(connectionEstablished) {
                        MainActivity.isConnected = true;
                        Toast.makeText(getContext(),"Connection established",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        MainActivity.isConnected = false;
                        Toast.makeText(getContext(),"Failed to establish connection",Toast.LENGTH_SHORT).show();
                    }
                    setAccordingToConnection();
                } else {
                    Toast.makeText(getContext(),"ERROR - No input in IP Address field!",Toast.LENGTH_SHORT).show();
                }
            }
        });

        disconnectButton = (Button) rootView.findViewById(R.id.disconnectButton);
        disconnectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mainActivity == null) mainActivity = (MainActivity) getActivity();
                if(mainActivity.disconnect()) MainActivity.isConnected = false;
                setAccordingToConnection();
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity tygrys = (MainActivity) getActivity();
                tygrys.sendCommand(commandEditText.getText().toString().trim());
            }
        });

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        setAccordingToConnection();
    }

    private void setAccordingToConnection(){
        if(MainActivity.isConnected){
            connectionImageView.setImageResource(R.mipmap.wifi_ok);
            statusTextView.setText("Connected");
        } else {
            connectionImageView.setImageResource(R.mipmap.wifi_no);
            statusTextView.setText("Disconnected");
        }
    }



    public void setLog(String time){
        logTextView.setText(time);
    }

}