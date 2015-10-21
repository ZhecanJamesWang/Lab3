package com.mobileproto.jovanduy.scavengerhunt;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;

public class Section_End extends Fragment {


    public Section_End() {
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
        View rootView = inflater.inflate(R.layout.fragment_section__end, container, false);
        create_webview(rootView);
        createButton(rootView);
        return rootView;
    }
    public  void create_webview(View view){
        WebView mWebView;
        mWebView = (WebView)view.findViewById(R.id.SectEnd_webView);

        // Enable Javascript
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        String link ="http://memecrunch.com/meme/QD5D/great-job/image.png";
        mWebView.loadUrl(link);
    }

    public void createButton(View view) {
        Button button = (Button) view.findViewById(R.id.next_video_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transitionToFragment();
            }
        });
    }

    public void transitionToFragment() {
        android.support.v4.app.FragmentManager fm = getActivity().getSupportFragmentManager();//TODO: change the import
        android.support.v4.app.FragmentTransaction transaction = fm.beginTransaction();
        android.support.v4.app.FragmentManager.BackStackEntry backEntry = fm.getBackStackEntryAt(getActivity().getFragmentManager().getBackStackEntryCount()-1);
        String str=backEntry.getName();
        Fragment fragment= getFragmentManager().findFragmentByTag(str);
        transaction.replace(R.id.container, fragment);
        transaction.commit();
    }


}
