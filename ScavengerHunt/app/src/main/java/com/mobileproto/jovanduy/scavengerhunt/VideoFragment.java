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
import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;




public class VideoFragment extends Fragment {

    private Boolean GPS_testing = true;
    // GPSTracker class
    GPSTracker gps;
    private String TAG = "VIDEO FRAGMENT";
    private String mCurrentPhotoPath;
    static final int REQUEST_TAKE_PHOTO = 1;
    private double  target_latitude = 123;
    private double  target_longitude = 123;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private View view;
    private VideoView videoView;
    private ProgressDialog pDialog;
    private Button leftButton;
    private Button rightButton;
    private Button checkGps;
    private TextView textView;

    private int currStage;
    private int stageFinal;
    private Server server;
    private ArrayList<Double> latitudes;
    private ArrayList<Double> longitudes;
    private ArrayList<String> videos;
    private ArrayList<String> images;
    private String urlBase = "https://s3.amazonaws.com/olin-mobile-proto/";
    private boolean onLastStage;
    private Uri video;



    public VideoFragment() {
        this.stageFinal = 0;
        this.currStage = 0;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_video, container, false);
        videoView = (VideoView) view.findViewById(R.id.video_view);
        leftButton = (Button) view.findViewById(R.id.left_btn);
        rightButton = (Button) view.findViewById(R.id.right_btn);
        checkGps = (Button) view.findViewById(R.id.gps_check_btn);
        textView = (TextView) view.findViewById(R.id.text);
        server = new Server(getContext());
        latitudes = new ArrayList<>();
        longitudes = new ArrayList<>();
        videos = new ArrayList<>();
        createGPSButton(view);
        createMenuButton(view);

        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currStage -= 1;
                updateView(currStage);
            }
        });

        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currStage += 1;
                updateView(currStage);
            }
        });

        loadNext(currStage);
    return view;
    }

    /**
     * Load the info for the next stage and then update the view based on that new stage
     * @param stage stage in scavenger hunt from which to get info
     */
    public void loadNext (final int stage) {
        server.getNextInfo(stage, new Callback() {
            @Override
            public void callback(boolean success, double lat, double longi, String vid, boolean isLast) {
                latitudes.add(stage, lat);
                longitudes.add(stage, longi);
                videos.add(stage, vid);
                onLastStage = isLast;
                updateView(stage);

            }
        });
    }
    /**
     * update the video view to show the correct video and (un)enable left/right buttons if needed
     * @param stage stage from which to use in the video view
     */
    public void updateView(int stage) {
        video = Uri.parse(urlBase + videos.get(stage));
        if (stage == stageFinal) {
            rightButton.setEnabled(false);
            textView.setText(R.string.stage + stage + ", current stage"); // Shows up as int??? TODO: set up in strings.xml
        } else {
            rightButton.setEnabled(true);
            textView.setText("Stage " + stage + ", previous stage");
            checkGps.setText("View image\n& GPS");
        }
        if (stage == 0) {
            leftButton.setEnabled(false);
        } else {
            leftButton.setEnabled(true);
        }

        pDialog = new ProgressDialog(getContext());
//        pDialog.setTitle("Stage " + currStage + " video");
//        pDialog.setMessage("Buffering..."); //TODO getActivity.getString....
        pDialog.setTitle(getString(R.string.stage) + currStage + getString(R.string.video)); //HELP! Error with strings???
        pDialog.setMessage(getString(R.string.buffering));
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
        MediaController mediaController = new MediaController(getContext());
        videoView.setMediaController(mediaController);
        videoView.setVideoURI(video);
        videoView.requestFocus();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            // Close the progress bar and play the video
            public void onPrepared(MediaPlayer mp) {
                pDialog.dismiss();
                videoView.start();

            }
        });
    }

    public Dialog Create_Dialog(final Double latitude, final Double longitude) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        if (latitude - target_latitude < 10 && longitude - target_longitude < 10) {
            builder.setMessage(R.string.GPS_Checking_Success_MSG)
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
                            dialog.dismiss();
                        }
                    });
//            if (currStage == stageFinal) {
//                if(!onLastStage) {
//                    stageFinal += 1;
//                    currStage += 1;
//                    loadNext(stageFinal);
//                } else {
//                    Log.d("YOU'RE DONE!!", "YOU'RE DONE!!");
//                }
//            } else {
////                    server.getUploadedImage(currStage,);
//            }

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
                    })
                    .setNeutralButton(R.string.GPS_Checking_Hint, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Bundle offset = new Bundle();
                            offset.putString("latitude", latitude.toString());
                            offset.putString("longitude", longitude.toString());
                            offset.putString("target_latitude", String.valueOf(target_latitude));
                            offset.putString("target_longitude", String.valueOf(target_longitude));

                            CompassFragment compassFragment = new CompassFragment();
                            compassFragment.setArguments(offset);
                            transitionToFragment(compassFragment);
                        }
                    });

            return builder.create();
        }
    }


    public void createGPSButton(View v){
            final Button GPS_button;//TODO: move these to the top
            GPS_button = (Button) v.findViewById(R.id.gps_check_btn);
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
                        else{
                            target_latitude = 12344444;
                            target_longitude = 12344444;
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

    public void createMenuButton(View v){
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

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
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
