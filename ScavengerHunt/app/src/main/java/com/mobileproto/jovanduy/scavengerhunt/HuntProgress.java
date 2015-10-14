package com.mobileproto.jovanduy.scavengerhunt;

import java.util.ArrayList;

/**
 * Created by Jordan on 10/13/15.
 */
public class HuntProgress {

    public int stage = 0;
    public String urlBase = "https://s3.amazonaws.com/olin-mobile-proto/";
    public String[] videos = {"MVI_3146.3gp", "MVI_3145.3gp", "MVI_3144.3gp", "MVI_3147.3gp", "MVI_3141.3gp", "MVI_3140.3gp"};
    public double[] latitudes = {42.29386, 42.292987, 42.292733, 42.293445, 42.293108, 42.292701};
    public double[] longitudes = {-71.26483, -71.264039, -71.263977, -71.263481, -71.262802, -71.262054};
    public ArrayList<String> images;

    public HuntProgress() { }

    public int getStage() {
        return stage;
    }

    public String getUrl(int stage) {
        return urlBase + videos[stage];
    }

    public void setImage(int stage, String image) {
//        images.add(image)
        images.set(stage, image);
    }
}
