package com.mobileproto.jovanduy.scavengerhunt;

import android.content.Context;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;

import java.io.File;

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

    public S3Service(Context context) {
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

//    @Override
//    protected Void doInBackground(Void... params) {
//        AWSCredentials credentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
//        AmazonS3Client s3Client = new AmazonS3Client(credentials);
//        GetObjectRequest getRequest = new GetObjectRequest(BUCKET_NAME, "MY-OBJECT-KEY");
//
//    }
}
