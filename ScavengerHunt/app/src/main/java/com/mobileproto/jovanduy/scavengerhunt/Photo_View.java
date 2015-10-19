package com.mobileproto.jovanduy.scavengerhunt;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;

public class Photo_View extends Fragment {

    public Photo_View() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_photo__view, container, false);
        create_imgeview(rootView);
        return rootView;
    }

    public void create_imgeview(View v){
        Bundle bundle = this.getArguments();
        String uri = bundle.getString("uri", null);
        Uri URI = Uri.parse(uri);
        ImageView img_view;
        img_view = (ImageView)v.findViewById(R.id.imageView);
        img_view.setImageURI(URI);
    }

}
