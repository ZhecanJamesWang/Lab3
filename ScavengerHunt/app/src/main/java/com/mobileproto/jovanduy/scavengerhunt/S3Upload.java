package com.mobileproto.jovanduy.scavengerhunt;

import android.os.AsyncTask;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;

import java.io.File;
import java.io.InputStream;

/**
 * Created by Jordan on 10/8/15.
 */
public class S3Upload extends AsyncTask<Void, Void, Void> {

    static final String BUCKET_NAME = "olin-mobile-proto";


    @Override
    protected Void doInBackground(Void... params) {


        AmazonS3Client s3Client = new AmazonS3Client();
//        File fileToUpload = new File(File file"stuff");
//        PutObjectRequest putRequest = new PutObjectRequest(BUCKET_NAME, "MY-OBJECT-KEY", fileToUpload);
//        PutObjectResult putResponse = s3Client.putObject(putRequest);
        GetObjectRequest getRequest = new GetObjectRequest(BUCKET_NAME, "MY-OBJECT-KEY");
        S3Object getResponse = s3Client.getObject(getRequest);
        InputStream myObjectBytes = getResponse.getObjectContent();

        // myObjectBytes.close();
        return null;
    }
}
