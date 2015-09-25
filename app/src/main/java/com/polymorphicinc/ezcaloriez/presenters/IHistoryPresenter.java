package com.polymorphicinc.ezcaloriez.presenters;

public interface IHistoryPresenter {
    void AddCalorieToHistoryList(int calorieAmount, int currentCalorieAmount);
    void InitializeCalorieLimit();
    void CheckForCalorieAmountFromPrefs();
    void CheckForCalorieHistoryItems();
    boolean HasCurrentCalorieAmountChanged();
    void ResetCalorieHasChangedPref();
    void SetupDailyReset();
    void SetDailyCalorieLimit(int dailyLimit);
    void SubtractFromCurrentCalorieAmount(int amountToSubtract, int currentCalorieAmount);
}
