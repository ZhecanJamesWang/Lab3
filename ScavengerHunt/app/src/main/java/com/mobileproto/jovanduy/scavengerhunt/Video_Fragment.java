package com.mobileproto.jovanduy.scavengerhunt;

/**
 * Created by root on 10/8/15.
 */

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import android.support.v4.app.Fragment;

public class Video_Fragment  extends Fragment {

    // GPSTracker class
    GPSTracker gps;
    private String TAG = "VIDEO FRAGMENT";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_video_, container, false);
        create_button(rootview, "GPS");
        create_button(rootview, "back_menu_button");
    return rootview;
    }




    public void create_button(View v, String button){
        if (button.equals("GPS") ){
            Button GPS_button;
            GPS_button = (Button) v.findViewById(R.id.GPS_button);
            // create class object
            gps = new GPSTracker(getActivity());

            GPS_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {

                    // check if GPS enabled
                    if(gps.canGetLocation()){

                        double latitude = gps.getLatitude();
                        double longitude = gps.getLongitude();

                        // \n is for new line
                        Toast.makeText(getActivity(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
                    }else{
                        // can't get location
                        // GPS or Network is not enabled
                        // Ask user to enable GPS/network in settings
                        gps.showSettingsAlert();
                    }

                }
            });
        }
        else if (button.equals("back_menu_button")) {
            Log.d(TAG, "back_menu_button");
            Button back_menu_button;
            Log.d(TAG,"0");
            back_menu_button = (Button) v.findViewById(R.id.back_menu_button);
            Log.d(TAG,"1");
            back_menu_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG,"2");
                    android.support.v4.app.FragmentManager fm = getActivity().getSupportFragmentManager();
                    android.support.v4.app.FragmentTransaction transaction = fm.beginTransaction();
                    MainActivityFragment mainactivityfragment = new MainActivityFragment();
                    transaction.replace(R.id.container, mainactivityfragment);
                    transaction.commit();

                }
            });
        }
    }
}
