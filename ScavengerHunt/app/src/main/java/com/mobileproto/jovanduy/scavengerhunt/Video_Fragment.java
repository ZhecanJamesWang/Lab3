package com.mobileproto.jovanduy.scavengerhunt;

/**
 * Created by root on 10/8/15.
 */

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_video_, container, false);
        create_button(rootview, "GPS");
        create_button(rootview, "back_menu_button");
    return rootview;
    }

    public Dialog Create_Dialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.GPS_Checking_MSG)
                .setPositiveButton(R.string.GPS_Checking_Camera, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSILES!
                        Log.d(TAG, "camera");
                        dispatchTakePictureIntent();

                    }
                })
                .setNegativeButton(R.string.GPS_Checking_Cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        Log.d(TAG, "cancel");
                    }
                });
        return builder.create();
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
                        Dialog dialog = Create_Dialog();
                        dialog.show();
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

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        Log.d(TAG, "dispatchTakePictureIntent ");
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                Log.d(TAG, "createimagefile ");
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Log.d(TAG, "getphoto");
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
            Log.d(TAG, "getphotofailing ");
        }
    }
}
