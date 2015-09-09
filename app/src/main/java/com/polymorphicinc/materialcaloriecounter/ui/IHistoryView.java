package com.polymorphicinc.materialcaloriecounter.ui;

import com.polymorphicinc.materialcaloriecounter.CalorieHistory;

import java.util.ArrayList;
import java.util.List;

public interface IHistoryView {
    void UpdateListView(List<CalorieHistory> list);
}
