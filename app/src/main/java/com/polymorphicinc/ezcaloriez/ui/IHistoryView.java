package com.polymorphicinc.ezcaloriez.ui;

import com.polymorphicinc.ezcaloriez.CalorieHistory;

public interface IHistoryView {
    void InsertNewListItem(CalorieHistory calorieHistoryItem);
    void UpdateDailyCalorieViewAmount(int amount);
    void PickCalorieLimit();
}
