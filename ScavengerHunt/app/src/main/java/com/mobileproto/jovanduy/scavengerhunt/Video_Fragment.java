package com.mobileproto.jovanduy.scavengerhunt;

/**
 * Created by root on 10/8/15.
 */

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.support.v4.app.Fragment;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Video_Fragment  extends Fragment {

    // GPSTracker class
    GPSTracker gps;
    private String TAG = "VIDEO FRAGMENT";
    private String mCurrentPhotoPath;
    static final int REQUEST_TAKE_PHOTO = 1;
    private double  target_latitude = 123;
    private double  target_longitude = 123;
    private Boolean GPS_testing = true;
    static final int REQUEST_IMAGE_CAPTURE = 1;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_video_, container, false);
        create_button(rootview, "GPS");
        create_button(rootview, "back_menu_button");
    return rootview;
    }

    public Dialog Create_Dialog(Double latitude, Double longitude) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        if (latitude == target_latitude && longitude == target_longitude) {
            builder.setMessage(R.string.GPS_Checking_Success_MSG)
                    .setPositiveButton(R.string.GPS_Checking_Camera, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // FIRE ZE MISSILES!
                            Log.d(TAG, "camera");
//                        dispatchTakePictureIntent();

                        }
                    })
                    .setNegativeButton(R.string.GPS_Checking_Cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                            Log.d(TAG, "cancel");
                            dialog.dismiss();
                        }
                    });
            return builder.create();
        } else {
            Double latitude_offset = latitude - target_latitude;
            Double longitude_offset = longitude - target_longitude;
            builder.setTitle(R.string.GPS_Checking_Fail_MSG)
                    .setMessage("latitude_offset:" +"\n"+ latitude_offset.toString()+"\n"+"longitude_offset:" +"\n"+ longitude_offset.toString())
                    .setPositiveButton(R.string.GPS_Checking_Retry, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Log.d(TAG, "Retry GPS");
                            double latitude = gps.getLatitude();
                            double longitude = gps.getLongitude();
                            Create_Dialog(latitude, longitude);

                        }
                    })
                    .setNegativeButton(R.string.GPS_Checking_Return, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                            Log.d(TAG, "Return");
                            dialog.dismiss();
                        }
                    });

            return builder.create();
        }
    }


    public void create_button(View v, String button){
        if (button.equals("GPS") ){
            final Button GPS_button;
            GPS_button = (Button) v.findViewById(R.id.GPS_button);
            // create class object
            gps = new GPSTracker(getActivity());

            GPS_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {

                    // check if GPS enabled
                    if (gps.canGetLocation()) {

                        double latitude = gps.getLatitude();
                        double longitude = gps.getLongitude();
                        if (GPS_testing) {
                            target_latitude = latitude;
                            target_longitude = longitude;
                        }


                        // \n is for new line
                        Toast.makeText(getActivity(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
                        Dialog dialog = Create_Dialog(latitude, longitude);
                        dialog.show();
                    } else {
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

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
            Log.d(TAG, "getphotofailing ");
        }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            Photo_View photo_view_fragment = new Photo_View();
            Bundle frag = new Bundle();
            frag.putString("uri", data.getData().toString());
            photo_view_fragment.setArguments(frag);
            transitionToFragment(photo_view_fragment);
        }
    }

    public void transitionToFragment(Fragment fragment) {
        android.support.v4.app.FragmentManager fm = getActivity().getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.commit();
    }
}
