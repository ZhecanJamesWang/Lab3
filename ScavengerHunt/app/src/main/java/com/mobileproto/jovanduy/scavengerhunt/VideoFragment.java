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
import android.widget.TextView;
import android.widget.VideoView;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;

/**
 * Fragment for displying the video view
 */
public class VideoFragment extends Fragment {

    private View view;
    private VideoView videoView;
    private ProgressDialog pDialog;
    private Button leftButton;
    private Button rightButton;
    private Button checkGps;
    private TextView textView;

    private int currStage;
    private int stageFinal;
    private Server server;
    private ArrayList<Double> latitudes;
    private ArrayList<Double> longitudes;
    private ArrayList<String> videos;
    private ArrayList<String> images;
    private String urlBase = "https://s3.amazonaws.com/olin-mobile-proto/";
    private boolean onLastStage;

    private Uri video;


    public VideoFragment() {
        this.stageFinal = 0;
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
        textView = (TextView) view.findViewById(R.id.text);
        server = new Server(getContext());
        latitudes = new ArrayList<>();
        longitudes = new ArrayList<>();
        videos = new ArrayList<>();
        setUpButton(leftButton);
        setUpButton(rightButton);
        setUpButton(checkGps);

        loadNext(currStage);

        return view;
    }

    /**
     * Set up buttons for clicking functionality
     * @param button
     */
    public void setUpButton(final Button button) { //TODO: separate into three different methods
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
                } else {
                    if (currStage == stageFinal) {
                        if(!onLastStage) {
                            stageFinal += 1;
                            currStage += 1;
                            loadNext(stageFinal);
                        } else {
                            Log.d("YOU'RE DONE!!", "YOU'RE DONE!!");
                        }
                    }
                }
            }
        });
    }

    /**
     * Load the info for the next stage and then update the view based on that new stage
     * @param stage stage in scavenger hunt from which to get info
     */
    public void loadNext (final int stage) {
        server.getNextInfo(stage, new Callback() {
            @Override
            public void callback(boolean success, double lat, double longi, String vid, boolean isLast) {
                latitudes.add(stage, lat);
                longitudes.add(stage, longi);
                videos.add(stage, vid);
                onLastStage = isLast;
                updateView(stage);

            }
        });
    }

    /**
     * update the video view to show the correct video and (un)enable left/right buttons if needed
     * @param stage stage from which to use in the video view
     */
    public void updateView(int stage) {
        video = Uri.parse(urlBase + videos.get(stage));
        if (stage == stageFinal) {
            rightButton.setEnabled(false);
            textView.setText(R.string.stage + stage + ", current stage"); // Shows up as int???
        } else {
            rightButton.setEnabled(true);
            textView.setText("Stage " + stage + ", previous stage");
        }
        if (stage == 0) {
            leftButton.setEnabled(false);
        } else {
            leftButton.setEnabled(true);
        }

        pDialog = new ProgressDialog(getContext());
        pDialog.setTitle("Stage " + currStage + " video");
        pDialog.setMessage("Buffering..."); //TODO getActivity.getString....
//        pDialog.setTitle(R.string.stage + currStage + R.string.video); //HELP! Error with strings???
//        pDialog.setMessage(R.string.buffering);
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
