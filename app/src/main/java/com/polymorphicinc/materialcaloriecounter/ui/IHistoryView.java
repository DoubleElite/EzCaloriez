package com.polymorphicinc.materialcaloriecounter.ui;

import com.polymorphicinc.materialcaloriecounter.CalorieHistory;

import java.util.ArrayList;
import java.util.List;

public interface IHistoryView {
    void InsertNewListItem(CalorieHistory calorieHistoryItem);
    void UpdateDailyCalorieViewAmount(int amount);
}
