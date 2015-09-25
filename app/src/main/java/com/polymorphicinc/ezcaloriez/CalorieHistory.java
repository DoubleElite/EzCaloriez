package com.polymorphicinc.ezcaloriez;

import com.orm.SugarRecord;

public class CalorieHistory extends SugarRecord<CalorieHistory> {
    public String date;
    public String time;
    public int numberOfCalories;

    public CalorieHistory() {

    }

    public CalorieHistory(String date, int numberOfCalories) {
        this.date = date;
        this.numberOfCalories = numberOfCalories;
    }

    public CalorieHistory(String date, int numberOfCalories, String time) {
        this.date = date;
        this.numberOfCalories = numberOfCalories;
        this.time = time;
    }

}
