package com.mobileproto.jovanduy.scavengerhunt;

import android.content.Context;
import android.util.Log;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;

import java.io.File;
import java.net.URL;
import java.util.Date;

/**
 * Created by Jordan on 10/12/15.
 */
public class S3Service {

    static final String BUCKET_NAME = "olin-mobile-proto";
    static final String ACCESS_KEY = "AKIAISEFKD6O3QSZGHUQ";
    static final String SECRET_KEY = "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";
    public Context context;
    public AmazonS3 s3;
    public TransferUtility transferUtility;
    AWSCredentials credentials;

    public S3Service(Context context) {
        this.context = context;
        this.credentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
        this.s3 = new AmazonS3Client(credentials);
//        this.s3 = new AmazonS3Client();
        this.transferUtility = new TransferUtility(s3, context);
    }

    public void uploadFile(String key, File file) {
        TransferObserver observer = transferUtility.upload(BUCKET_NAME, key, file);
    }

    public URL downloadFile(String key) {
//        TransferObserver observer = transferUtility.download(BUCKET_NAME, key, file);
        GeneratePresignedUrlRequest urlRequest = new GeneratePresignedUrlRequest(BUCKET_NAME, key);
        urlRequest.setExpiration(new Date(System.currentTimeMillis() + 3600000));  // Added an hour's worth of milliseconds to the current time.
        //urlRequest.setResponseHeaders(override);
        URL url = s3.generatePresignedUrl(urlRequest);
        Log.d("URL:", url.toString());

        return url;
    }

//    @Override
//    protected Void doInBackground(Void... params) {
//        AWSCredentials credentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
//        AmazonS3Client s3Client = new AmazonS3Client(credentials);
//        GetObjectRequest getRequest = new GetObjectRequest(BUCKET_NAME, "MY-OBJECT-KEY");
//
//    }
}
