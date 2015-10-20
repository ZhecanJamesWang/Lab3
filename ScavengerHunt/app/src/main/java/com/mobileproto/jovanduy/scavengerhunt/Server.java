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
 * Created by Jordan on 10/16/15.
 */
public class Server {

    private RequestQueue queue;
    private String url;

    public Server(Context context) {
        queue = Volley.newRequestQueue(context);
        url = "http://45.55.65.113/";
    }

    public void getInfo(final Callback callback) {
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                new JSONObject(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ArrayList<Double> latitudes = new ArrayList<>();
                        ArrayList<Double> longitudes = new ArrayList<>();
                        ArrayList<String> videos = new ArrayList<>();
                        try {
                            JSONArray path = response.getJSONArray("path");
                            for (int i=0; i<path.length(); i++) {
                                JSONObject stage = (JSONObject) path.get(i);
                                latitudes.add(stage.getDouble("latitude"));
                                longitudes.add(stage.getDouble("longitude"));
                                videos.add(stage.getString("s3id"));

                            }
                        } catch (Exception e) {
                            callback.callback(false, null, null, null);
                            Log.e("Error!", e.getMessage());
                        }
                        callback.callback(true, latitudes, longitudes, videos);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error!", error.getMessage());
                        callback.callback(false, null, null, null);
                    }
                }
        );
        queue.add(request);
    }
}
