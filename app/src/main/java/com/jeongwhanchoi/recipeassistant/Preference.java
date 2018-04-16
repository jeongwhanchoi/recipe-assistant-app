package com.jeongwhanchoi.recipeassistant;

import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

/**
 * Created by jeongwhanchoi on 11/04/2018.
 * <p>
 * This handles loading of preferences from server
 */
public class Preference {

    public interface onPreferenceDownloadedListener {
        void onPreferenceDownloaded(String string);
    }

    /**
     * Load a single preference from server by name
     *
     * @param context
     * @param name                 - name of preference
     * @param preferenceDownloaded - Callback to return preference
     */
    public static void load(Context context, String name, final onPreferenceDownloadedListener preferenceDownloaded) {

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(context);

        //combine url
        String url = Configurations.SERVER_URL + "api/preference/" + name;

        //generate request
        StringRequest req = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);
                preferenceDownloaded.onPreferenceDownloaded(response);
            }
        }, new Response.ErrorListener() {
            @Override
            // Handles errors that occur due to Volley
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", "Error");
            }
        });

        // Add the request to the RequestQueue.
        queue.add(req);
    }


}
