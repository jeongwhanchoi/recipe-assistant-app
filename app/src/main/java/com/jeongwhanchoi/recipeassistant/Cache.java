package com.jeongwhanchoi.recipeassistant;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Created by jeongwhanchoi on 11/04/2018.
 * This Class stores and loads data in cache using the url as a key.
 */

public class Cache {
    Context context;

    /**
     * Initialise
     *
     * @param context
     */
    public Cache(Context context) {
        this.context = context;
    }

    /**
     * Load a string data by url
     *
     * @param url
     * @return
     */
    public String load(String url) {
//        String key = getKey(url);
        String key = url;
        String ret = null;

        try {
            File cacheDir = context.getCacheDir();
            File file = new File(cacheDir, key + "data.bin");
            FileInputStream inputStream = new FileInputStream(file);

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        } catch (FileNotFoundException e) {
            Log.e("cache activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("cache activity", "Can not read file: " + e.toString());
        }

        return ret;
    }

    /**
     * Store a string data by url
     *
     * @param url
     * @param value
     */
    public void store(String url, String value) {
//        String key = getKey(url);
        String key = url;
        try {
            File cacheDir = context.getCacheDir();
            File file = new File(cacheDir, key + "data.bin");
            FileOutputStream stream = new FileOutputStream(file);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(stream);
            outputStreamWriter.write(value);
            outputStreamWriter.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }

    }
}
