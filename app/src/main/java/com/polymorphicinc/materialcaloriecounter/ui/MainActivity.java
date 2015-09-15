package com.polymorphicinc.materialcaloriecounter.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.polymorphicinc.materialcaloriecounter.CalorieHistory;
import com.polymorphicinc.materialcaloriecounter.R;
import com.polymorphicinc.materialcaloriecounter.adapters.HistoryListAdapter;
import com.polymorphicinc.materialcaloriecounter.presenters.HistoryPresenter;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements IHistoryView {

    // UI
    public HistoryPresenter historyPresenter;

    @Bind(R.id.rv_history_2)
        public RecyclerView listHistory;
    @Bind(R.id.toolbar)
        public Toolbar toolbar;
    @Bind(R.id.tv_calorie_amount)
            public TextView tvCalorieAmount;

    // Adapters
    HistoryListAdapter historyListAdapter;
    List<CalorieHistory> calorieHistories = new ArrayList<CalorieHistory>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Make sure to bind ButterKnife otherwise Binding wont work.
        ButterKnife.bind(this);

        // Get the Presenter for this View.
        historyPresenter = new HistoryPresenter(this);
        historyPresenter.UpdateDailyCalorieAmount(historyPresenter.GetDailyCalroieAmountFromPrefs(this));

        // Initialize views that need it.
        InitToolbar();
        // Setup the adapter used for the RecyclerView.
        historyListAdapter = new HistoryListAdapter(calorieHistories);
        // Setup RecyclerView.
        InitHistoryRecyclerView();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent preferenceScreenIntent = new Intent(this, PreferenceScreenActivity.class);
            startActivity(preferenceScreenIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void UpdateDailyCalorieViewAmount(int amount) {
        // TODO Fix the bug of it adding the default calorie amount
        // Get the current value, if one hasn't been set make the value 0
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor editor = sp.edit();

        int currentValue;
        // Check if the current value has been set yet,
        // if not set it to 0 so we can add the daily calorie limit correctly.
        if(tvCalorieAmount.getText().toString().equals("") && sp.getString("prefs_key_current_calorie_amount", null) == null) {
            currentValue = 0;
        } else {
            currentValue = Integer.valueOf(sp.getString("prefs_key_current_calorie_amount", "1600"));
            Log.d("Current Value", String.valueOf(currentValue));
        }

        // Save current value
        editor.putString("prefs_key_current_calorie_amount", String.valueOf(currentValue));
        editor.apply();

        // The new amount will be the current + the new value
        int newAmount = currentValue + amount;

        // Save the new value over the older current value
        editor.putString("prefs_key_current_calorie_amount", String.valueOf(newAmount));
        editor.apply();

        tvCalorieAmount.setText(String.valueOf(newAmount));
    }

    private void InitHistoryRecyclerView() {
        // Set the linear layout manager
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        listHistory.setLayoutManager(llm);
        // Set the default item animator
        listHistory.setItemAnimator(new DefaultItemAnimator());
        listHistory.setAdapter(historyListAdapter);
    }

    @Override
    public void InsertNewListItem(CalorieHistory calorieHistoryItem) {
        // Add the new item
        calorieHistories.add(calorieHistoryItem);
        // Notify the insert so we get the animation from the default animator
        historyListAdapter.notifyItemInserted(0);
    }

    private void InitToolbar() {
        setSupportActionBar(toolbar);
        // Hide the title
        if(getSupportActionBar()!= null)
            getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @OnClick(R.id.fab_add_calorie)
    public void showCalorieDialog() {
        // EditText to be used in the dialog
        final EditText etAddCalorie = new EditText(this);
        etAddCalorie.setInputType(InputType.TYPE_CLASS_NUMBER);

        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setMessage("Test")
                .setView(etAddCalorie)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Notify the presenter of the calorie amount that the user entered.
                        historyPresenter.AddCalorieToHistoryList(Integer.valueOf(etAddCalorie.getText().toString()));
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
