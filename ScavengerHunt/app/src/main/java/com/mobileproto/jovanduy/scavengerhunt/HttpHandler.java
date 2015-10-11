package com.mobileproto.jovanduy.scavengerhunt;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Jordan on 10/8/15.
 */
public class HttpHandler {

    public RequestQueue queue;

    public HttpHandler(Context context) { queue = Volley.newRequestQueue(context); }

    public void accessServer(String searchQuery) {

    }

}
