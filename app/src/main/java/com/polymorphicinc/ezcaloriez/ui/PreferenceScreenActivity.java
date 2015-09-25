package com.polymorphicinc.ezcaloriez.ui;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.polymorphicinc.ezcaloriez.R;

public class PreferenceScreenActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference);

        // Replace the default view with the preference fragment
        getFragmentManager().beginTransaction().replace(R.id.preference_container, new PreferenceScreenFragment()).commit();
    }

    public static class PreferenceScreenFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
        }
    }

}
