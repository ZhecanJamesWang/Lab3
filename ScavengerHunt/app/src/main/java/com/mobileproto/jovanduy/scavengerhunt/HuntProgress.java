package com.mobileproto.jovanduy.scavengerhunt;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by Jordan on 10/13/15.
 */
public class HuntProgress {

    private int stage = 0;
    private Server server;
    private ArrayList<Double> latitudes;
    private ArrayList<Double> longitudes;
    private ArrayList<String> videos;
    private String urlBase = "https://s3.amazonaws.com/olin-mobile-proto/";
    private ArrayList<String> images;
    private boolean pathGotten;

    public HuntProgress(Context context) {
        server = new Server(context);
        pathGotten = false;
    }

//    public void getPath() {
//        server.getInfo(new Callback() {
//            @Override
//            public void callback(boolean success, ArrayList lats, ArrayList longs, ArrayList vids) {
//                latitudes = lats;
//                longitudes = longs;
//                videos = vids;
//                pathGotten = true;
//            }
//        });
//    }

    public boolean foundPath() {
        return pathGotten;
    }

    public int getStage() {
        return stage;
    }

    public void updateStage() {
        stage += 1;
    }

    public String getUrl(int stage) {
        return urlBase + videos.get(stage);
    }

    public double getLatitude(int stage) {
        return latitudes.get(stage);
    }

    public double getLongitude(int stage) {
        return longitudes.get(stage);
    }

    public void setImage(int stage, String image) {
//        images.add(image)
        images.set(stage, image);
    }
}
