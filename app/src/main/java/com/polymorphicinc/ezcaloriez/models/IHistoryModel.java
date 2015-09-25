package com.polymorphicinc.ezcaloriez.models;


import android.content.Context;

public interface IHistoryModel {
    boolean CheckForFirstLaunch(Context context);
    int GetCurrentCalorieAmount();
    int GetDailyCalorieLimit();
    void CreateDailyResetAlarmManager();
    void SetCurrentCalorieAmount(int amount);
    void SetDailyCalorieLimit(int amount);
}
