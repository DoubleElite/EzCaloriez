package com.polymorphicinc.ezcaloriez.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.polymorphicinc.ezcaloriez.CalorieHistory;
import com.polymorphicinc.ezcaloriez.R;
import com.polymorphicinc.ezcaloriez.adapters.HistoryListAdapter;
import com.polymorphicinc.ezcaloriez.presenters.HistoryPresenter;

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
    @Bind(R.id.fab_add_calorie)
        FloatingActionButton floatingActionButton;

    // Adapters
    HistoryListAdapter historyListAdapter;
    List<CalorieHistory> calorieHistories = new ArrayList<CalorieHistory>();

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Make sure to bind ButterKnife otherwise Binding wont work.
        ButterKnife.bind(this);
        context = this;

        // Always initialize your views before anything else.
        // Initialize views that need it.
        InitToolbar();
        // Check if the user changed the FAB location
        UpdateFABLocation();
        // Setup the adapter used for the RecyclerView.
        historyListAdapter = new HistoryListAdapter(calorieHistories);
        // Setup RecyclerView.
        InitHistoryRecyclerView();

        // Get the Presenter for this View.
        historyPresenter = new HistoryPresenter(this, context);
        // If it's the first launch this will let the user pick the limit.
        historyPresenter.InitializeCalorieLimit();
        // This will just get the current calorie amount and the history list and display them.
        historyPresenter.CheckForCalorieAmountFromPrefs();
        historyPresenter.CheckForCalorieHistoryItems();

        // Create the AlarmManger that will reset the current calorie amount once per day.
        historyPresenter.SetupDailyReset();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(historyPresenter.HasCurrentCalorieAmountChanged()) {
            Log.d("App", "True");
            historyPresenter.CheckForCalorieAmountFromPrefs();
            historyPresenter.ResetCalorieHasChangedPref();
        }
        // Check if FAB location has changed
        UpdateFABLocation();
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
        tvCalorieAmount.setText(String.valueOf(amount));
    }

    @Override
    public void InsertNewListItem(CalorieHistory calorieHistoryItem) {
        // Add the new item, make sure to add it to the start of the List
        calorieHistories.add(0, calorieHistoryItem);
        // Notify the insert so we get the animation from the default animator ands scroll to the correct position
        historyListAdapter.notifyItemInserted(0);
        listHistory.scrollToPosition(0);
    }

    @Override
    public void PickCalorieLimit() {
        // EditText to be used in the dialog
        final LinearLayout paddingLayout = new LinearLayout(context);
        final EditText etSetCalorieLimit = new EditText(context);
        etSetCalorieLimit.setInputType(InputType.TYPE_CLASS_NUMBER);

        paddingLayout.setOrientation(LinearLayout.VERTICAL);
        paddingLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        paddingLayout.setPadding(64, 0, 64, 0);
        paddingLayout.addView(etSetCalorieLimit);

        // TODO Prevent the user from putting nothing in.
        final AlertDialog alertDialog = new AlertDialog.Builder(this, R.style.AlertDialogCustom)
                .setTitle("First time here!")
                .setMessage("Before you can start logging meals, set your goal for your daily calorie limit. Don't worry, you can change this in the settings later!")
                .setView(paddingLayout)
                .setPositiveButton("Set", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing here because we are overriding it below
                    }
                })
                .create();

        // Prevent closing when the user taps outside the dialog
        alertDialog.setCanceledOnTouchOutside(false);

        // Show the keyboard as well
        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        // Now show the dialog
        alertDialog.show();

        // Override the positive button click so we can prevent the dialog from closing whenever the user presses the button
        // We need to run some checks before closing it instead.
        // Add a listener once the dialog has been shown
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // If the user has entered nothing don't let them close the dialog
                if(etSetCalorieLimit.getText().toString().equals("") || etSetCalorieLimit.getText().toString().equals("0")) {
                    // Do nothing until they enter data
                } else {
                    historyPresenter.SetDailyCalorieLimit(Integer.valueOf(etSetCalorieLimit.getText().toString()));
                    alertDialog.dismiss();
                }
            }
        });
    }

    private void InitHistoryRecyclerView() {
        // Set the linear layout manager
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        listHistory.setLayoutManager(llm);
        listHistory.setHasFixedSize(true);
        // Set the default item animator
        listHistory.setItemAnimator(new DefaultItemAnimator());
        listHistory.setAdapter(historyListAdapter);
    }

    private void InitToolbar() {
        setSupportActionBar(toolbar);
        // Hide the title
        if(getSupportActionBar()!= null)
            getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void UpdateFABLocation() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        // Get the add button locations from preferences
        String currentFabLocation = sp.getString("prefs_key_add_button_locations", "center");
        // Set the gravity
        switch (currentFabLocation) {
            case "center":
                CoordinatorLayout.LayoutParams paramsCenter = (CoordinatorLayout.LayoutParams) floatingActionButton.getLayoutParams();
                paramsCenter.anchorGravity = (Gravity.CENTER | Gravity.BOTTOM);
                floatingActionButton.setLayoutParams(paramsCenter);
                break;

            case "right":
                CoordinatorLayout.LayoutParams paramsRight = (CoordinatorLayout.LayoutParams) floatingActionButton.getLayoutParams();
                paramsRight.anchorGravity = (Gravity.END | Gravity.BOTTOM);
                floatingActionButton.setLayoutParams(paramsRight);
                break;

            default:
                CoordinatorLayout.LayoutParams paramsDefault = (CoordinatorLayout.LayoutParams) floatingActionButton.getLayoutParams();
                paramsDefault.anchorGravity = (Gravity.CENTER | Gravity.BOTTOM);
                floatingActionButton.setLayoutParams(paramsDefault);
                break;
        }
    }

    @OnClick(R.id.fab_add_calorie)
    public void showCalorieDialog() {
        // EditText to be used in the dialog
        final LinearLayout paddingLayout = new LinearLayout(context);
        final EditText etAddCalorie = new EditText(context);
        etAddCalorie.setInputType(InputType.TYPE_CLASS_NUMBER);

        paddingLayout.setOrientation(LinearLayout.VERTICAL);
        paddingLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        paddingLayout.setPadding(64, 0, 64, 0);
        paddingLayout.addView(etAddCalorie);

        final AlertDialog alertDialog = new AlertDialog.Builder(this, R.style.AlertDialogCustom)
                .setTitle("Add new item")
                .setView(paddingLayout)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNegativeButton("Cancel", null)
                .create();

        // Prevent closing when the user taps outside the dialog
        alertDialog.setCanceledOnTouchOutside(false);

        // Show the keyboard as well
        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        // Now show the dialog
        alertDialog.show();

        // Add a listener once the dialog has been shown
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // If the user has entered nothing don't let them close the dialog
                if (etAddCalorie.getText().toString().equals("") || etAddCalorie.getText().toString().equals("0")) {
                    // Do nothing until they enter data
                } else {
                    // Notify the presenter of the calorie amount that the user entered.
                    historyPresenter.AddCalorieToHistoryList(Integer.valueOf(etAddCalorie.getText().toString()), Integer.valueOf(tvCalorieAmount.getText().toString()));
                    alertDialog.dismiss();
                }
            }
        });
    }
}
