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

    public View view;
    public VideoView videoView;
    public ProgressDialog pDialog;
    public HuntProgress huntProgress;

    public VideoFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_video, container, false);
        videoView = (VideoView) view.findViewById(R.id.video_view);
        huntProgress = new HuntProgress();
        S3Service s3Service = new S3Service(getContext());
//        URL url = s3Service.downloadFile("MVI_3146.3gp");

        // Execute StreamVideo AsyncTask

        // Create a progressbar
        pDialog = new ProgressDialog(getContext());
        // Set progressbar title
        pDialog.setTitle("Stage " + huntProgress.getStage() + " video");
        // Set progressbar message
        pDialog.setMessage("Buffering...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        // Show progressbar
        pDialog.show();

        try {
            // Start the MediaController
            MediaController mediaController = new MediaController(getContext());
//            mediaController.setAnchorView(videoView);
            // Get the URL from String VideoURL
//            Uri video = Uri.parse(url.toURI().toString());
            Uri video = Uri.parse("https://s3.amazonaws.com/olin-mobile-proto/MVI_3146.3gp");
            videoView.setMediaController(mediaController);
            videoView.setVideoURI(video);

        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }

        videoView.requestFocus();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            // Close the progress bar and play the video
            public void onPrepared(MediaPlayer mp) {
                pDialog.dismiss();
                videoView.start();

            }
        });


        return view;
    }
}
