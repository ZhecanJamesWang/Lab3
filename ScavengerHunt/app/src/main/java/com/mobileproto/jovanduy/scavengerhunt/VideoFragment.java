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
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import android.support.v4.app.Fragment;

import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import java.util.ArrayList;




public class VideoFragment extends Fragment {

    GPSTracker gps;
    private Boolean gpsTestingNearby = true;
    private String TAG = "VIDEO FRAGMENT";
    private String mCurrentPhotoPath;
    static final int REQUEST_TAKE_PHOTO = 1;

    private double  target_latitude;
    private double  target_longitude;

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

    private HuntProgress huntProgress;

    public VideoFragment() {
        this.stageFinal = 0;
        this.currStage = 0;
//        MainActivity mainActivity = (MainActivity) getActivity();
//        huntProgress = mainActivity.huntProgress;
        this.huntProgress = new HuntProgress();
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

//        if (gpsTestingNearby){
//            target_latitude = 42.280929;
//            target_longitude = -71.237755;
//        }
//        else{
//            target_latitude = 39.904211;
//            target_longitude = 116.407395;
//        }

        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                huntProgress.updateCurrStage(-1);
                updateView(huntProgress.getCurrStage());
//                currStage -= 1;
//                updateView(currStage);
            }
        });

        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                huntProgress.updateCurrStage(1);
                updateView(huntProgress.getCurrStage());
//                currStage += 1;
//                updateView(currStage);
            }
        });
        loadNext(huntProgress.getCurrStage());
    return view;
    }

    public HuntProgress getHuntProgress() {
        return huntProgress;
    }
    /**
     * Load the info for the next stage and then update the view based on that new stage
     * @param stage stage in scavenger hunt from which to get info
     */
    public void loadNext (final int stage) {
        server.getNextInfo(stage, new Callback() {
            @Override
            public void callback(boolean success, double lat, double longi, String vid, boolean isLast) {
                huntProgress.addLatitude(lat);
                huntProgress.addLongitude(longi);
                huntProgress.addUrl(vid);
                huntProgress.setOnLastStage(isLast);

//                latitudes.add(stage, lat);
//                longitudes.add(stage, longi);
//                videos.add(stage, vid);
//                onLastStage = isLast;
                updateView(stage);

            }
        });
    }
    /**
     * update the video view to show the correct video and (un)enable left/right buttons if needed
     * @param stage stage from which to use in the video view
     */
    public void updateView(int stage) {
//        video = Uri.parse(urlBase + videos.get(stage));
        target_latitude = huntProgress.getLatitude(huntProgress.getCurrStage());
        target_longitude = huntProgress.getLongitude(huntProgress.getCurrStage());
        video = Uri.parse(huntProgress.getUrl(stage));
        if (stage == huntProgress.getStageFinal()) {
            rightButton.setEnabled(false);
            textView.setText(getString(R.string.stage) + stage + ", current stage"); // Shows up as int??? TODO: set up in strings.xml
            checkGps.setText(getString(R.string.GPS_check));
        } else {
            rightButton.setEnabled(true);
            textView.setText(getString(R.string.stage) + stage + ", previous stage");
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
        pDialog.setTitle(getString(R.string.stage) + stage + getString(R.string.video)); //HELP! Error with strings???
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

    public Dialog createDialog(final Double latitude, final Double longitude) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        Double latitude_offset = latitude - target_latitude;
//        Double longitude_offset = longitude - target_longitude;
        Double latitude_offset = 9.0;
        Double longitude_offset = 9.0;
        if (Math.abs(latitude_offset) < 10 && Math.abs(longitude_offset) < 10) {
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

            return builder.create();
        }
        else {
            builder.setTitle(R.string.GPS_Checking_Fail_MSG)
                    .setMessage("latitude_offset:" + "\n" + latitude_offset.toString() + "\n" + "longitude_offset:" + "\n" + longitude_offset.toString())
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

                    if (huntProgress.getCurrStage() == huntProgress.getStageFinal()) {
                        // check if GPS enabled
                        if (gps.canGetLocation()) {

                            double latitude = gps.getLatitude();
                            double longitude = gps.getLongitude();


                            // \n is for new line
                            Toast.makeText(getActivity(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
                            Dialog dialog = createDialog(latitude, longitude);
                            dialog.show();
                        } else {
                            // can't get location
                            // GPS or Network is not enabled
                            // Ask user to enable GPS/network in settings
                            gps.showSettingsAlert();
                        }
                    } else {
                        double lat = huntProgress.getLatitude(huntProgress.getCurrStage());
                        double longi = huntProgress.getLongitude(huntProgress.getCurrStage());
                        Toast.makeText(getActivity(), Double.toString(lat) + Double.toString(longi), Toast.LENGTH_LONG).show();
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
            PhotoView photo_view_fragment = new PhotoView();
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
