package com.jeongwhanchoi.recipeassistant.fragment;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.jeongwhanchoi.recipeassistant.R;

/**
 * Created by jeongwhanchoi on 11/04/2018.
 *
 * Preferences fragment. This is shown inside SettingsActivity.
 * It is not added in the main fragment because the PreferenceFragment doesnt extend the support Fragment :/
 */

public class MyPreferenceFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.fragment_settings);
    }
}
