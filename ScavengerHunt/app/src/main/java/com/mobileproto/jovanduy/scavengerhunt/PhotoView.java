package com.mobileproto.jovanduy.scavengerhunt;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

public class PhotoView extends Fragment {

    private String TAG = "PhotoView";
    private  Uri URI;
    private ImageView imgView;
    static final int REQUEST_TAKE_PHOTO = 1;
    static final int REQUEST_IMAGE_CAPTURE = 1;

    public PhotoView() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_photo__view, container, false);
        createImageview(rootView);
        createRetakeButton(rootView);
        createSubmitButton(rootView);
        return rootView;
    }

    public void createImageview(View v){
        Bundle bundle = this.getArguments();
        String uri = bundle.getString("uri", null);
        Log.d("create_imageview", uri);
        URI = Uri.parse(uri);
        imgView = (ImageView)v.findViewById(R.id.imageView);
        imgView.setImageURI(URI);
        imgView.setRotation(90);
        
    }
    public void createRetakeButton(View v) {
        Log.d(TAG, "retakeButton");
        Button retakeButton;
        retakeButton = (Button) v.findViewById(R.id.Retake);
        retakeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });
        }
    public void createSubmitButton(View v) {
        Log.d(TAG, "submitButton");
        Button submitButton;
        submitButton = (Button) v.findViewById(R.id.Submit);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "submit");
                SectionEnd sectionEndFragment = new SectionEnd();
                transitionToFragment(sectionEndFragment);
            }
        });
    }
    public void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            URI = Uri.parse(data.getData().toString());
            imgView.setImageURI(URI);
        }
    }
    public void transitionToFragment(Fragment fragment) {
        android.support.v4.app.FragmentManager fm = getActivity().getSupportFragmentManager();//TODO: change the import
        android.support.v4.app.FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.commit();
    }
}

