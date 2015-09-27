package com.polymorphicinc.ezcaloriez.presenters;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.polymorphicinc.ezcaloriez.CalorieHistory;
import com.polymorphicinc.ezcaloriez.models.HistoryModel;
import com.polymorphicinc.ezcaloriez.ui.IHistoryView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class HistoryPresenter implements IHistoryPresenter {

    public IHistoryView historyView;
    public HistoryModel historyModel;

    public Context context;

    public HistoryPresenter(IHistoryView historyView, Context context) {
        this.historyView = historyView;
        this.context = context;

        historyModel = new HistoryModel(this, context);
    }

    @Override
    public boolean HasCurrentCalorieAmountChanged() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getBoolean("prefs_key_has_value_changed", false);
    }

    @Override
    public void ResetCalorieHasChangedPref() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("prefs_key_has_value_changed", false).apply();
    }

    @Override
    public void SetupDailyReset() {
        historyModel.CreateDailyResetAlarmManager();
    }

    @Override
    public void AddCalorieToHistoryList(int calorieAmount, int currentCalorieAmount) {
        // Get the date (and covert it the month/day/year format) and the calorie amount from what the user entered.
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/d/yyyy", Locale.US);
        String currentDate = simpleDateFormat.format(calendar.getTime());

        // Get the time
        SimpleDateFormat timeDateFormatter = new SimpleDateFormat("h:mm a", Locale.getDefault());
        String time = timeDateFormatter.format(Calendar.getInstance().getTime());

        // Create the calorie history item and then save it (via ORM Sugar Library)
        CalorieHistory calorieHistory = new CalorieHistory(currentDate, calorieAmount, time);
        calorieHistory.save();

        // Notify the view of the new item that should be inserted.
        historyView.InsertNewListItem(calorieHistory);

        SubtractFromCurrentCalorieAmount(calorieAmount, currentCalorieAmount);
    }

    @Override
    public void CheckForCalorieAmountFromPrefs() {
        // Get the current calorie amount from the model and send it to the view
        historyView.UpdateDailyCalorieViewAmount(historyModel.GetCurrentCalorieAmount());
    }

    @Override
    public void CheckForCalorieHistoryItems() {
        // Get all calorie history items and add them back to the list
        List<CalorieHistory> calorieHistoryList = CalorieHistory.listAll(CalorieHistory.class);
        for (CalorieHistory item : calorieHistoryList) {
            historyView.InsertNewListItem(item);
        }
    }

    @Override
    public void InitializeCalorieLimit() {
        // Is it the first launch? If so display the popup otherwise do nothing
        if (historyModel.CheckForFirstLaunch(context)) {
            historyView.PickCalorieLimit();
        }
    }

    @Override
    public void SetDailyCalorieLimit(int dailyLimit) {
        // Send the daily limit to the model to save it
        historyModel.SetDailyCalorieLimit(dailyLimit);

        // Display the daily value
        historyView.UpdateDailyCalorieViewAmount(dailyLimit);
    }

    @Override
    public void SubtractFromCurrentCalorieAmount(int amountToSubtract, int currentCalorieAmount) {

        int finalAmount = currentCalorieAmount-amountToSubtract;

        historyModel.SetCurrentCalorieAmount(finalAmount);

        historyView.UpdateDailyCalorieViewAmount(finalAmount);
    }
}
