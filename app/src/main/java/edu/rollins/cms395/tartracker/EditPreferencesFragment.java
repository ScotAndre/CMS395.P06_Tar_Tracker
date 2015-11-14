package edu.rollins.cms395.tartracker;

import android.preference.PreferenceFragment;
import android.os.Bundle;

public class EditPreferencesFragment extends PreferenceFragment {

    public EditPreferencesFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
