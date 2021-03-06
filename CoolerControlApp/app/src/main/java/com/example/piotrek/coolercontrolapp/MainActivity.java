package com.example.piotrek.coolercontrolapp;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ConnectionFragment connectionFragment;
    private LogFragment logFragment;
    private ParametersFragment parametersFragment;
    private CircuitFragment circuitFragment;
    private ActionsFragment actionsFragment;
    private ChartsFragment chartsFragment;
    private SettingsFragment settingsFragment;

    private ViewPager mViewPager;
    static boolean isConnected = false;

    private Socket socket;
    private Thread socketThread;
    private String url = "https://api.thingspeak.com/channels/351083/feeds.json?api_key=OV6QP498FJIWVL2C&results=1";
    private String TAG = MainActivity.class.getSimpleName();

    private String ipAddress;
    private int port;

    private ArrayList<HashMap<String, String>> thingspeakData;

    public static int MODE;
    public static final int AUTO_MODE = 0;
    public static final int MANUAL_MODE = 1;

    public int howManyTimes = 0;
    private CountDownTimer timer;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        thingspeakData = new ArrayList<>();
        MODE = MainActivity.AUTO_MODE;

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());


        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        new ClockTask().execute();

        timer = new CountDownTimer(5000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                new Thread(new Runnable() {
                    public void run() {

                        String jsonStr = null;
                        HttpHandler sh = new HttpHandler();
                        jsonStr = sh.makeServiceCall(url);
                        Log.e(TAG, "Response from url:" + jsonStr);
                        if(jsonStr != null){
                            try{
                                JSONObject jsonObj = new JSONObject(jsonStr);
                                JSONArray feeds = jsonObj.getJSONArray("feeds");

                                for(int i=0;i<feeds.length();i++){
                                    JSONObject o = feeds.getJSONObject(i);


                                    String temperature = o.getString("field1");
                                    String pwm = o.getString("field2");
                                    String rpm = o.getString("field3");
                                    String relay = o.getString("field4");
                                    String voltage = o.getString("field5");
                                    String created_at = o.getString("created_at");


                                    HashMap<String, String> newFeed = new HashMap<String, String>();
                                    newFeed.put("Temperature",temperature);
                                    newFeed.put("PWM",pwm);
                                    newFeed.put("RPM", rpm);
                                    newFeed.put("Relay",relay);
                                    newFeed.put("Voltage", voltage);

                                    String date = created_at.substring(0,10);
                                    String time = created_at.substring(11,19);
                                    newFeed.put("Date", date);
                                    newFeed.put("Time", time);

                                    howManyTimes++;
                                    thingspeakData.add(newFeed);
                                }
                            } catch (JSONException e){
                                e.printStackTrace();
                            }
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Nic nie zwrocilo", Toast.LENGTH_LONG).show();
                        }
                    }
                }).start();

                timer.start();
            }
        }.start();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public boolean establishConnection(String ipAddress, int port){
        this.ipAddress = ipAddress;
        this.port = port;

        socketThread = new Thread(new ClientThread());
        socketThread.start();
        return true;
    }

    public boolean disconnect(){
        if(socket != null) {
            try {
                socket.close();
                socket = null;
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            Toast.makeText(getApplicationContext(), "NO CONNECTION TO BE CLOSED", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
    public boolean checkConnectionStatus(){
        if(socket != null){
            if(!socket.isClosed()) return true;
        }
        Toast.makeText(getApplicationContext(),"NO CONNECTION", Toast.LENGTH_SHORT).show();
        return false;
    }

    public boolean setManualValue(int rpmValue){
        if(checkConnectionStatus()) return sendCommand("MANUAL#" + String.valueOf(rpmValue));
        else return false;
    }

    public boolean setAutoValue(int tempValue){
        if(checkConnectionStatus()) return sendCommand("AUTO#" + String.valueOf(tempValue));
        else return false;
    }

    public boolean setRelay(String relayState){
        if(checkConnectionStatus()) return sendCommand("RELAY#" + relayState);
        else return false;
    }

    public boolean sendCommand(String command){
        if(checkConnectionStatus()) {
            try {
                PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                out.println(command);
                if(logFragment!= null) logFragment.addCommandHistoryRecord(command);
                //out.close();
                return true;

            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        } else return false;
    }

    public ArrayList<HashMap<String, String>> getThingspeakData(){
        return thingspeakData;
    }
    class ClientThread implements Runnable {

        public void run(){
            try {
                socket = new Socket(ipAddress, 80);
            } catch (UnknownHostException e1) {
                e1.getMessage();
            } catch (IOException e1) {
                e1.getMessage();
            }
        }

    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch(position){
                /*
                case 0:
                    if(connectionFragment == null) connectionFragment = new ConnectionFragment();
                    return connectionFragment;
                case 1:
                    if(logFragment == null) logFragment = new LogFragment();
                    return logFragment;
                case 2:
                    if(parametersFragment == null) parametersFragment = new ParametersFragment();
                    return parametersFragment;
                case 3:
                    if(circuitFragment == null) circuitFragment = new CircuitFragment();
                    return circuitFragment;
                case 4:
                    if(actionsFragment == null) actionsFragment = new ActionsFragment();
                    return actionsFragment;
                case 5:
                    if(chartsFragment == null) chartsFragment = new ChartsFragment();
                    return chartsFragment;
                case 6:
                    if(settingsFragment == null) settingsFragment = new SettingsFragment();
                    return settingsFragment;
                    */
                case 0:
                    if(connectionFragment == null) connectionFragment = new ConnectionFragment();
                    return connectionFragment;
                case 1:
                    if(logFragment == null) logFragment = new LogFragment();
                    return logFragment;
                case 2:
                    if(parametersFragment == null) parametersFragment = new ParametersFragment();
                    return parametersFragment;
                case 3:
                    if(actionsFragment == null) actionsFragment = new ActionsFragment();
                    return actionsFragment;
                case 4:
                    if(chartsFragment == null) chartsFragment = new ChartsFragment();
                    return chartsFragment;
                case 5:
                    if(circuitFragment == null) circuitFragment = new CircuitFragment();
                    return circuitFragment;
            }
            return null;
        }

        @Override
        public int getCount() {
            return 6;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Connection";
                case 1:
                    return "Log";
                case 2:
                    return "Parameters";
                case 3:
                    return "Actions";
                case 4:
                    return "Charts";
                case 5:
                    return "Circuit";
            }
            return null;
        }
    }

    class ClockTask extends AsyncTask<Integer, Integer, Void> {


        @Override
        protected Void doInBackground(Integer... params) {
            int i =0;
            while (true) {
                SystemClock.sleep(1000);
                i++;

                publishProgress(i);
                if(i == 9999999) {
                    i = 0;
                }
            }
        }
        @Override
        protected void onProgressUpdate(Integer... progress){
            if(connectionFragment != null) {

                if(progress[0]%5 == 0) {
                    //thingspeakTask.execute();
                    //Toast.makeText(getApplicationContext(), "TEEST", Toast.LENGTH_SHORT).show();
                }

                String connFragment = "";
                connFragment = "Czas: " + String.valueOf(progress[0]) + "\n";
                if(socket != null) {
                    connFragment += "SOCKET ON\nIP: "+socket.getInetAddress()+"\nPort: "+String.valueOf(socket.getPort());
                } else {
                    connFragment += "SOCKET OFF";
                }


                if(!thingspeakData.isEmpty()){
                    connFragment += "\nTemperature: " + thingspeakData.get(thingspeakData.size()-1).get("Temperature") + "    RPM: " + thingspeakData.get(thingspeakData.size()-1).get("RPM") +
                            "\nDate: " + thingspeakData.get(thingspeakData.size()-1).get("Date") + "     Time: " + thingspeakData.get(thingspeakData.size()-1).get("Time")
                            +"     Ile razy: " + String.valueOf(howManyTimes);
                } else {
                    connFragment += "\nNO THINGSPEAK DATA AVAILABLE";
                }

                if (logFragment != null) logFragment.setLog(connFragment);
                if (chartsFragment != null) chartsFragment.invalidateChart();


            }
            if(parametersFragment != null && !thingspeakData.isEmpty()) {
                parametersFragment.setParameters(thingspeakData);
            }

            if(circuitFragment != null && !thingspeakData.isEmpty()) {
                circuitFragment.setParameters(thingspeakData);
            }
        }
    }

}