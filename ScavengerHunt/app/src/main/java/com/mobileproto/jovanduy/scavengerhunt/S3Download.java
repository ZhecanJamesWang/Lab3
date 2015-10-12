package com.mobileproto.jovanduy.scavengerhunt;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;

import java.io.File;
import java.io.InputStream;

/**
 * Created by Jordan on 10/11/15.
 */
public class S3Download extends AsyncTask<Uri, Integer, Uri> {

    static final String BUCKET_NAME = "olin-mobile-proto";
    static final String ACCESS_KEY = "AKIAISEFKD6O3QSZGHUQ";
    static final String SECRET_KEY = "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";
    public Context context;
    public AmazonS3 s3;
    public TransferUtility transferUtility;

    public S3Download(Context context) {
        this.context = context;
        this.s3 = new AmazonS3Client();
        this.transferUtility = new TransferUtility(s3, context);
    }

    public void uploadFile(String key, File file) {
        TransferObserver observer = transferUtility.upload(BUCKET_NAME, key, file);
    }

    public void downloadFile(String key, File file) {
        TransferObserver observer = transferUtility.download(BUCKET_NAME, key, file);
    }

    @Override
    protected Uri doInBackground(Uri... params) {
        //File file = new File(context.getFilesDir(), "vid1");
        AWSCredentials credentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
        AmazonS3Client s3Client = new AmazonS3Client(credentials);
        try {
            GetObjectRequest getRequest = new GetObjectRequest(BUCKET_NAME, "MVI_3146.MOV");
            S3Object getResponse = s3Client.getObject(getRequest);
            InputStream myObjectBytes = getResponse.getObjectContent();
            myObjectBytes.close();
        } catch (Exception exception) {
            Log.e("ERRORRRRRRR", exception.getMessage());
        }

        return params[0];
    }

    @Override
    protected void onPostExecute(Uri uri) {
        Log.d("OMGGGGG", uri.toString());
    }

}
