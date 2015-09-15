package com.polymorphicinc.materialcaloriecounter.presenters;

import android.content.Context;

import java.util.List;

public interface IHistoryPresenter {
    void AddCalorieToHistoryList(int calorieAmount);
    int GetDailyCalroieAmountFromPrefs(Context context);
    void UpdateDailyCalorieAmount(int amount);
}
