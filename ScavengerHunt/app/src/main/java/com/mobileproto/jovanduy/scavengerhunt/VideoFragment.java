package com.mobileproto.jovanduy.scavengerhunt;

import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.VideoView;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;

import java.io.File;
import java.net.URL;

/**
 * Created by Jordan on 10/6/15.
 */
public class VideoFragment extends Fragment {

    private View view;
    private VideoView videoView;
    private ProgressDialog pDialog;
    private Button leftButton;
    private Button rightButton;
    private Button checkGps;

    private int currStage;
    private Uri video;
    private double latitude;
    private double longitude;
    private HuntProgress huntProgress;

    public VideoFragment() {
        this.currStage = 0;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_video, container, false);
        videoView = (VideoView) view.findViewById(R.id.video_view);
        leftButton = (Button) view.findViewById(R.id.left_btn);
        rightButton = (Button) view.findViewById(R.id.right_btn);
        checkGps = (Button) view.findViewById(R.id.gps_check_btn);
        huntProgress = new HuntProgress(getContext());
        setUpButton(leftButton);
        setUpButton(rightButton);

        updateView(currStage);

        return view;
    }

    public void setUpButton(final Button button) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (button == leftButton) {
                    currStage -= 1;
                    updateView(currStage);
                }
                else if (button == rightButton) {
                    currStage += 1;
                    updateView(currStage);
                }
            }
        });
    }

    public void getNext() {
        //TODO: update
    }

    public void updateView (int stage) {
        video = Uri.parse(huntProgress.getUrl(stage));
        latitude = huntProgress.getLatitude(stage);
        longitude = huntProgress.getLongitude(stage);
        if (currStage == huntProgress.getStage()) {
            rightButton.setEnabled(false);
        } else {
            rightButton.setEnabled(true);
        }
        if (currStage == 0) {
            leftButton.setEnabled(false);
        } else {
            leftButton.setEnabled(true);
        }

        pDialog = new ProgressDialog(getContext());
        pDialog.setTitle("Stage " + huntProgress.getStage() + " video");
        pDialog.setMessage("Buffering...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
        MediaController mediaController = new MediaController(getContext());
        videoView.setMediaController(mediaController);
        videoView.setVideoURI(video);
        videoView.requestFocus();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            // Close the progress bar and play the video
            public void onPrepared(MediaPlayer mp) {
                pDialog.dismiss();
                videoView.start();

            }
        });
    }
}
