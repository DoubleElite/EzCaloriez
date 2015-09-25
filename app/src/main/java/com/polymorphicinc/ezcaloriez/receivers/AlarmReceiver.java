package com.polymorphicinc.ezcaloriez.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;


public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        // Reset the current calorie amount to the daily calorie amount limit.

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        String dailyLimit = sp.getString("prefs_key_daily_calorie_amount", "");

        // Now that we have the current value and the daily limit the user has set,
        // let's reset the current value back to the daily limit.
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("prefs_key_current_calorie_amount", dailyLimit);
        // We also are setting a bool that says a value has changed so we can check for a change in our activity.
        editor.putBoolean("prefs_key_has_value_changed", true);
        editor.apply();

    }
}
