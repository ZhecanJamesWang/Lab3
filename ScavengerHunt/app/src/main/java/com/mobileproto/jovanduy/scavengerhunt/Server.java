package com.mobileproto.jovanduy.scavengerhunt;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Class to connect to server
 */
public class Server {

    private RequestQueue queue;
    private String url;

    public Server(Context context) {
        queue = Volley.newRequestQueue(context);
        url = "http://45.55.65.113/";
    }

    /**
     * gets info (lat, long, url) for next stage in scavenger hunt
     * @param stage the stage of which to get the info
     * @param callback callback which is set later in order to do stuff with the info
     */
    public void getNextInfo(final int stage, final Callback callback) {
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url+"scavengerhunt",
                new JSONObject(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        double latitude = 0;
                        double longitude = 0;
                        String video = "";
                        boolean isLast = false;
                        try {
                            JSONArray path = response.getJSONArray("path");
                            if (path.length() == stage + 1) {
                                isLast = true;
                            }
                            JSONObject stageInfo = (JSONObject) path.get(stage);
                            latitude = stageInfo.getDouble("latitude");
                            longitude = stageInfo.getDouble("longitude");
                            video = stageInfo.getString("s3id");
                        } catch (Exception e) {
                            callback.callback(false, 0, 0, null, false);
                            Log.e("Error!", e.getMessage());
                        }
                        callback.callback(true, latitude, longitude, video, isLast);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error!", error.getMessage()+ " no internet...");
                        callback.callback(false, 0, 0, null, false);
                    }
                }
        );
        queue.add(request);
    }
}
