package com.jeongwhanchoi.recipeassistant.aws;

import android.support.multidex.MultiDexApplication;

public class Application extends MultiDexApplication {
    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize the AWS Provider
        AWSProvider.initialize(getApplicationContext());


        registerActivityLifecycleCallbacks(new ActivityLifeCycle());

    }
}