package com.example.piotrek.coolercontrolapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Piotrek on 2017-12-02.
 */

public class LogFragment extends Fragment {
    private TextView logTextView;
    private TextView commandHistoryTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_log, container, false);
        logTextView = (TextView) rootView.findViewById(R.id.statusTextView);
        commandHistoryTextView = (TextView) rootView.findViewById(R.id.commandTextView);
        return rootView;
    }

    public void setLog(String time)
    {
        logTextView.setText(time);
    }

    public void addCommandHistoryRecord(String record){
        commandHistoryTextView.setText(commandHistoryTextView.getText().toString() + "\n" + record);
    }
}
