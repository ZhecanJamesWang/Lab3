package com.mobileproto.jovanduy.scavengerhunt;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;
/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    private VideoFragment videoFragment;
    private String TAG = "main_activity_fragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        create_button(rootView, "start_button");
        create_button(rootView, "guide_button");
        create_webview(rootView);
        return rootView;

    }

    public void transitionToFragment(Fragment fragment) {
        android.support.v4.app.FragmentManager fm = getActivity().getSupportFragmentManager();//TODO: change the import
        android.support.v4.app.FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.container, fragment);
        if (fragment == videoFragment) {
            transaction.addToBackStack(videoFragment.toString());
        }
        transaction.commit();
    }

    public void create_button(View v, String button) {
        if (button.equals("start_button")) {
            Log.d(TAG, "start_button");
            Button start_button;
            start_button = (
                    Button) v.findViewById(R.id.start_button);

            start_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    VideoFragment videoFragment = new VideoFragment();
                    transitionToFragment(videoFragment);
                }
            });
        }
        else if (button.equals("guide_button")) {
            Log.d(TAG, "guide_button");
            Button guide_button;
            guide_button = (Button) v.findViewById(R.id.guide_button);
            guide_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity(), "TBD", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    public  void create_webview(View view){
        WebView mWebView;
        mWebView = (WebView)view.findViewById(R.id.start_webview);

        // Enable Javascript
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        String link ="http://scavengerhuntfilm.com/wp-content/themes/scavenger-hunt/img/logo.jpg";
        mWebView.loadUrl(link);
    }
}
