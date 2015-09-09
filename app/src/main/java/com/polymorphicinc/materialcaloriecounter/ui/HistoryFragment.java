package com.polymorphicinc.materialcaloriecounter.ui;

import android.content.Context;
import android.os.Debug;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.polymorphicinc.materialcaloriecounter.CalorieHistory;
import com.polymorphicinc.materialcaloriecounter.R;
import com.polymorphicinc.materialcaloriecounter.adapters.HistoryListAdapter;
import com.polymorphicinc.materialcaloriecounter.presenters.HistoryPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class HistoryFragment extends Fragment implements IHistoryView {

    public HistoryPresenter historyPresenter;

    @Bind(R.id.rv_history)
        public RecyclerView listHistory;

    public HistoryFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        // Butterknife binds like this for fragments, requires the view of the fragment
        ButterKnife.bind(this, view);

        historyPresenter = new HistoryPresenter(this);
        historyPresenter.PullHistory();

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        listHistory.setLayoutManager(llm);

        return view;
    }

    @Override
    public void UpdateListView(List<CalorieHistory> list) {
        HistoryListAdapter historyListAdapter = new HistoryListAdapter(list);
        listHistory.setAdapter(historyListAdapter);
    }
}
