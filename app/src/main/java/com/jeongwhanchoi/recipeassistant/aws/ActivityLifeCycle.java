package com.jeongwhanchoi.recipeassistant.aws;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.jeongwhanchoi.recipeassistant.aws.AWSProvider;

import static com.jeongwhanchoi.recipeassistant.aws.AWSProvider.*;

class ActivityLifeCycle implements android.app.Application.ActivityLifecycleCallbacks {
    private int depth = 0;

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
    }

    @Override
    public void onActivityStarted(Activity activity) {
        if (depth == 0) {
            Log.d("ActivityLifeCycle", "Application entered foreground");
            getInstance().getPinpointManager().getSessionClient().startSession();
            getInstance().getPinpointManager().getAnalyticsClient().submitEvents();
        }
        depth++;
    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {
        depth--;
        if (depth == 0) {
            Log.d("ActivityLifeCycle", "Application entered background");
            getInstance().getPinpointManager().getSessionClient().stopSession();
            getInstance().getPinpointManager().getAnalyticsClient().submitEvents();
        }

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }
}
