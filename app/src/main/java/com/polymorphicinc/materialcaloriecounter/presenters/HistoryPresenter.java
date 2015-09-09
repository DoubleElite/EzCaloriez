package com.polymorphicinc.materialcaloriecounter.presenters;

import com.polymorphicinc.materialcaloriecounter.CalorieHistory;
import com.polymorphicinc.materialcaloriecounter.ui.IHistoryView;

import java.util.ArrayList;
import java.util.List;


public class HistoryPresenter implements IHistoryPresenter {

    public IHistoryView historyView;

    public HistoryPresenter(IHistoryView historyView) {
        this.historyView = historyView;
    }

    @Override
    public void PullHistory() {
        // Fake data stuff here
        List<CalorieHistory> calorieHistories = new ArrayList<>();
        for(int i = 0; i <= 15; i++) {
            calorieHistories.add(new CalorieHistory("Date", 200 + i));
        }

        historyView.UpdateListView(calorieHistories);
    }
}
