package com.mobileproto.jovanduy.scavengerhunt;

import android.os.Bundle;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



public class CompassFragment extends Fragment implements SensorEventListener{

    // define the display assembly compass picture
    private ImageView image;

    // record the compass picture angle turned
    private float currentDegree = 0f;

    // device sensor manager
    private SensorManager mSensorManager;
    private String longitude;
    private String latitude;
    private String targetLongitude;
    private String targetLatitude;
    private double distance;
    private double[] locationInfo;
    TextView tvHeading;

    public CompassFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_compass, container, false);
        createImageview(rootView);
        Bundle bundle = this.getArguments();
        longitude = bundle.getString("longitude", null);
        latitude = bundle.getString("longitude", null);
        targetLongitude = bundle.getString("target_longitude", null);
        targetLatitude = bundle.getString("target_latitude", null);

        locationInfo = calculateDistance(latitude, longitude, targetLatitude, targetLongitude);

        return rootView;
    }
    public void createImageview(View v){
        image = (ImageView) v.findViewById(R.id.Compass_View);
        // TextView that will tell the user what degree is he heading
        tvHeading = (TextView)v.findViewById(R.id.Heading);

        // initialize your android device sensor capabilities
        mSensorManager = (SensorManager) getActivity().getSystemService(getActivity().SENSOR_SERVICE);
    }
    @Override
    public void onResume() {
        super.onResume();

        // for the system's orientation sensor registered listeners
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    public void onPause() {
        super.onPause();

        // to stop the listener and save battery
//        mSensorManager.unregisterListener(this);
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // not in use
    }
    @Override
    public void onSensorChanged(SensorEvent event) {

        // get the angle around the z-axis rotated
        float degree = Math.round(event.values[0]);

        tvHeading.setText("Heading: " + Float.toString(degree) + " degrees");

        // create a rotation animation (reverse turn degree degrees)
        RotateAnimation ra = new RotateAnimation(
                currentDegree,
                -degree,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f);

        // how long the animation will take place
        ra.setDuration(210);

        // set the animation after the end of the reservation status
        ra.setFillAfter(true);

        // Start the animation
        image.startAnimation(ra);
        currentDegree = -degree;

    }
    public double[] calculateDistance(String slat1, String slon1, String slat2, String slon2){
        double lat1 = Double.parseDouble(slat1);
        double lon1 = Double.parseDouble(slon1);
        double lat2 = Double.parseDouble(slat2);
        double lon2 = Double.parseDouble(slon2);
        String direction;
        Double latitude_offset = lat1 - lat2;
        Double longitude_offset = lon1 - lon2;
        if (longitude_offset < 0){
            if (longitude_offset < 0){
                direction = "SE";
            }
            else if(longitude_offset>0){
                direction = "SN";
            }
            else if(longitude_offset == 0){
                
            }
        }

        if (latitude_offset

        Double R = 6378.137; // Radius of earth in KM
        double dLat = (lat2 - lat1) * Math.PI / 180;
        double dLon = (lon2 - lon1) * Math.PI / 180;
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(lat1 * Math.PI / 180) * Math.cos(lat2 * Math.PI / 180) *
                        Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double d = R * c;


        return new double[] {d * 1000, latitude_offset, longitude_offset}; // meters
    }

}

