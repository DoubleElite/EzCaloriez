package com.polymorphicinc.ezcaloriez.models;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.polymorphicinc.ezcaloriez.presenters.IHistoryPresenter;
import com.polymorphicinc.ezcaloriez.receivers.AlarmReceiver;

import java.util.Calendar;

public class HistoryModel implements IHistoryModel {

    IHistoryPresenter historyPresenter;
    AlarmManager am;
    Context context;

    public HistoryModel(IHistoryPresenter historyPresenter, Context context) {
        this.historyPresenter = historyPresenter;
        this.context = context;
    }

    @Override
    public void CreateDailyResetAlarmManager() {

        final int DAYINMILISECONDS = 86400000;

        // Set the calendar to point to 6am
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());;
        calendar.set(Calendar.HOUR_OF_DAY, 6);
        calendar.set(Calendar.MINUTE, 0);

        // Setup the intent and pendingintent for the alarmreceiver
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        // Initialize the alarmmanager
        am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        // Set it to repeat at 6am every 24 hours
        am.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), DAYINMILISECONDS, pendingIntent);
    }

    @Override
    public boolean CheckForFirstLaunch(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        if(sp.getString("prefs_key_daily_calorie_amount", null) == null) {
            return true;
        }
        return false;
    }

    @Override
    public int GetCurrentCalorieAmount() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        // Check if the calorie amount has been saved yet, if so we want to grab it from prefs
        if (sp.getString("prefs_key_current_calorie_amount", null) != null)
            return Integer.valueOf(sp.getString("prefs_key_current_calorie_amount", "1600"));
        else
            return 0;
    }

    @Override
    public int GetDailyCalorieLimit() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        // Check if the calorie amount has been saved yet, if so we want to grab it from prefs
        if (sp.getString("prefs_key_daily_calorie_amount", null) != null)
            return Integer.valueOf(sp.getString("prefs_key_daily_calorie_amount", "1600"));
        else
            return 0;
    }

    @Override
    public void SetCurrentCalorieAmount(int amount) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("prefs_key_current_calorie_amount", String.valueOf(amount));
        editor.apply();
    }

    @Override
    public void SetDailyCalorieLimit(int amount) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("prefs_key_daily_calorie_amount", String.valueOf(amount));
        editor.apply();
    }
}
