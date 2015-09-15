package com.polymorphicinc.materialcaloriecounter.presenters;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.polymorphicinc.materialcaloriecounter.CalorieHistory;
import com.polymorphicinc.materialcaloriecounter.R;
import com.polymorphicinc.materialcaloriecounter.ui.IHistoryView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class HistoryPresenter implements IHistoryPresenter {

    public IHistoryView historyView;

    public HistoryPresenter(IHistoryView historyView) {
        this.historyView = historyView;
    }

    @Override
    public void AddCalorieToHistoryList(int calorieAmount) {
        // Get the date and the calorie amount from what the user entered.
        Date date = new Date();
        CalorieHistory calorieHistory = new CalorieHistory(date.toString(), Integer.valueOf(calorieAmount));
        // Notify the view of the new item that should be inserted.
        historyView.InsertNewListItem(calorieHistory);

        UpdateDailyCalorieAmount(-calorieAmount);
    }

    @Override
    public int GetDailyCalroieAmountFromPrefs(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        String PREFSKEYAMOUNT = context.getResources().getString(R.string.prefs_key_daily_calorie_amount);
        String amountFromPrefs = sp.getString(PREFSKEYAMOUNT, "1600");

        return Integer.valueOf(amountFromPrefs);
    }

    @Override
    public void UpdateDailyCalorieAmount(int amount) {
        // Update the view with the new calorie amount
        historyView.UpdateDailyCalorieViewAmount(amount);
    }
}
