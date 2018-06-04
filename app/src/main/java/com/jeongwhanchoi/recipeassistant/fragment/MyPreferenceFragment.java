package com.jeongwhanchoi.recipeassistant.fragment;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.Preference;

import com.jeongwhanchoi.recipeassistant.Configurations;
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

        android.preference.Preference myPref = findPreference("pref_enable_column");

        myPref.setOnPreferenceChangeListener(new android.preference.Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(android.preference.Preference preference, Object o) {
                if ((boolean) o) {
                    Configurations.LIST_MENU_TYPE = Configurations.LIST_2COLUMNS;
                } else {
                    Configurations.LIST_MENU_TYPE = Configurations.LIST_FULLWIDTH;
                }
                return true;
            }
        });
    }
}
