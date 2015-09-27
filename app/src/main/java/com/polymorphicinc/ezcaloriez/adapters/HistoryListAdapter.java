package com.polymorphicinc.ezcaloriez.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.polymorphicinc.ezcaloriez.CalorieHistory;
import com.polymorphicinc.ezcaloriez.R;

import java.util.List;

public class HistoryListAdapter extends RecyclerView.Adapter<HistoryListAdapter.HistoryListViewHolder> {

    List<CalorieHistory> calorieHistoryList;


    public HistoryListAdapter(List<CalorieHistory> historyList) {
        this.calorieHistoryList = historyList;
    }

    // I think this is run once, it generates the view holder from the layout that we are using for each list item.
    // This way it won't have to grab it each time we make a new list item. It's all stored on our view holder.
    @Override
    public HistoryListViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.calorie_history_item, parent, false);

        return new HistoryListViewHolder(itemView);
    }

    // This executes everytime we make a new list item, it binds the data to the view.
    @Override
    public void onBindViewHolder(HistoryListViewHolder holder, int position) {
        // Get the current calorHistory object and set it's data to the views.
        CalorieHistory calorieHistory = calorieHistoryList.get(position);
        // SET DATE
        holder.tvDate.setText(calorieHistory.date);
        // SET AMOUNT
        String calorieAmount = String.valueOf(calorieHistory.numberOfCalories) + " calories";
        holder.tvAmount.setText(calorieAmount);
        // SET TIME
        holder.tvTime.setText(calorieHistory.time);
    }

    @Override
    public int getItemCount() {
        // Return the size of our array.
        return calorieHistoryList.size();
    }

    public static class HistoryListViewHolder extends RecyclerView.ViewHolder {
        public TextView tvDate;
        public TextView tvAmount;
        public TextView tvTime;

        public HistoryListViewHolder(View v) {
            super(v);
            tvDate = (TextView) v.findViewById(R.id.calorie_date);
            tvAmount = (TextView) v.findViewById(R.id.calorie_amount);
            tvTime = (TextView) v.findViewById(R.id.calorie_time);
        }
    }

}
