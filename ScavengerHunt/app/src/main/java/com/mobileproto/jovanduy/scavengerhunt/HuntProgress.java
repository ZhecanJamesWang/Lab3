package com.mobileproto.jovanduy.scavengerhunt;

import android.content.Context;

import java.util.ArrayList;

/**
 * Class for keeping track of the stage of the scavenger hunt
 */
public class HuntProgress { //TODO: actually use this class! Good for MVC

    private int currStage;
    private int stageFinal;
    private ArrayList<Double> latitudes;
    private ArrayList<Double> longitudes;
    private ArrayList<String> videos;
    private ArrayList<String> images;
    private String urlBase = "https://s3.amazonaws.com/olin-mobile-proto/";
    private boolean onLastStage;




//    private int stage = 0;
//    private ArrayList<Double> latitudes;
//    private ArrayList<Double> longitudes;
//    private ArrayList<String> videos;
//    private String urlBase = "https://s3.amazonaws.com/olin-mobile-proto/";
//    private ArrayList<String> images;
    private boolean pathGotten;

    public HuntProgress() {
        pathGotten = false;
        onLastStage = false;
    }

    public boolean foundPath() {
        return pathGotten;
    }

    public boolean isOnLastStage() {
        return onLastStage;
    }

    public void setOnLastStage(boolean bool) {
        onLastStage = bool;
    }

    public int getCurrStage() {
        return currStage;
    }

    public void updateCurrStage(int num) {
        currStage += num;
    }

    public int getStageFinal() {
        return stageFinal;
    }

    public void updateStageFinal(int num) {
        stageFinal += num;
    }

    public String getUrl(int stage) {
        return urlBase + videos.get(stage);
    }

    public void addUrl(String url) {
        videos.add(url);
    }

    public double getLatitude(int stage) {
        return latitudes.get(stage);
    }

    public double getLongitude(int stage) {
        return longitudes.get(stage);
    }

    public void addLatitude(double latitude) {
        latitudes.add(latitude);
    }

    public void addLongitude(double longitude) {
        longitudes.add(longitude);
    }

    public void setImage(String image) {
//        images.add(image)
        images.set(currStage, image);
    }

    public String getImage(int stage) {
        return images.get(stage);
    }
}
