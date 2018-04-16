package com.jeongwhanchoi.recipeassistant;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by jeongwhanchoi on 11/04/2018.
 */
public class LoadImage {
    FragmentActivity activity;

    final int REQUEST_CODE = 51;

    Uri selectedImage;
    int reqWidth;
    int reqHeight;

    //translation
    String explanationTitle = "Load Picture Permission";
    String explanationDesc = "The app needs to read the picture from your storage.";
    String explanationOK = "OK";
    String explanationCancel = "CANCEL";

    //GETTERS and SETTERS for translation
//    public void setExplanationTitle(String explanationTitle) {
//        this.explanationTitle = explanationTitle;
//    }
//
//    public void setExplanationDesc(String explanationDesc) {
//        this.explanationDesc = explanationDesc;
//    }
//
//    public void setExplanationOK(String explanationOK) {
//        this.explanationOK = explanationOK;
//    }
//
//    public void setExplanationCancel(String explanationCancel) {
//        this.explanationCancel = explanationCancel;
//    }

    public LoadImage(FragmentActivity activity) {
        this.activity = activity;
    }

    public Bitmap decodeSampledBitmapFromResource(Uri selectedImage, int reqWidth, int reqHeight) {
        this.selectedImage = selectedImage;
        this.reqHeight = reqHeight;
        this.reqWidth = reqWidth;

        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_EXTERNAL_STORAGE)) {

                // Show an expanation to the user *asynchronously* -- don't block this thread waiting for the user's response! After the user sees the explanation, try again to request the permission.
                Alert alert = new Alert();
                alert.DisplayText(explanationTitle, explanationDesc, explanationOK, explanationCancel, activity);
                alert.show(activity.getSupportFragmentManager(), explanationTitle);
                alert.setPositiveButtonListener(new Alert.PositiveButtonListener() {
                    @Override
                    public void onPositiveButton(String input) {
                        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE);
                    }
                });
            } else {

                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE);
            }
            return null;
        } else {
            return decodeSampledBitmapFromResource_direct(selectedImage, reqWidth, reqHeight);
        }
    }

    public Bitmap decodeSampledBitmapFromResource_direct(Uri selectedImage, int reqWidth, int reqHeight) {
        try {
            return decodeSampledBitmapFromStream(selectedImage, reqWidth, reqHeight);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

    public Bitmap decodeSampledBitmapFromStream(Uri selectedImage, int reqWidth, int reqHeight)throws IOException {

        InputStream inputStream = activity.getContentResolver().openInputStream(selectedImage);
        // First decode with inJustDecodeBounds=true to check dimensions
        if (!(inputStream instanceof BufferedInputStream)) {
            inputStream = new BufferedInputStream(inputStream);
        }
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Rect rect = new Rect(-1, -1, -1, -1);
        BitmapFactory.decodeStream(inputStream, rect, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
         inputStream = activity.getContentResolver().openInputStream(selectedImage);
        return BitmapFactory.decodeStream(inputStream, rect, options);
    }


    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }


    /**
     * Call this to share image when permission is granted.
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    public Bitmap onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Share.
                    return decodeSampledBitmapFromResource_direct(selectedImage, reqWidth, reqHeight);

                } else {
                    // permission denied, boo! Disable the functionality that depends on this permission.
                    return null;
                }
            }

        }
        return null;
    }
}
