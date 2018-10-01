package com.development.georgemcl.goaltracker.view.ActionTab;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.development.georgemcl.goaltracker.Constants;
import com.development.georgemcl.goaltracker.R;
import com.development.georgemcl.goaltracker.model.Action;
import com.development.georgemcl.goaltracker.view.ViewGoal.ViewGoalRecyclerViewAdapter;
import com.development.georgemcl.goaltracker.view.ViewGoal.ViewGoalViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class ActionsTabFragment extends Fragment implements ViewGoalRecyclerViewAdapter.OnItemSelectedListener{

    private static final String TAG = "ActionsTabFragment";

    private String mActionTabCategory;
    private ActionTabViewModel mActionsTabViewModel;
    private ViewGoalRecyclerViewAdapter mRecyclerViewAdapter;

    @BindView(R.id.actions_tab_recycler_view)
    RecyclerView mRecyclerView;


    public ActionsTabFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_actions_tab, container, false);

        ButterKnife.bind(this, view);

        mRecyclerViewAdapter = new ViewGoalRecyclerViewAdapter(getContext(), this);
        mRecyclerView.setAdapter(mRecyclerViewAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mActionTabCategory = getArguments().getString(Constants.KEY_TAB_SELECTED);

        String actionTab = "";
        if (mActionTabCategory.equals(getString(R.string.daily))){
            actionTab = "per day";
        }
        else if (mActionTabCategory.equals(getString(R.string.weekly))){
            actionTab = "per week";

        }
        else if (mActionTabCategory.equals(getString(R.string.monthly))) {
            actionTab = "per month";
        }


        mActionsTabViewModel = ViewModelProviders.of(this).get(ActionTabViewModel.class);
        mActionsTabViewModel.setActionRepeatCategory(actionTab);

        mActionsTabViewModel.getActionsForRepeatCategory().observe(this, new Observer<List<Action>>() {
            @Override
            public void onChanged(@Nullable List<Action> actions) {
                Log.d(TAG, "onChanged: actions size = "+ actions.size());
                mRecyclerViewAdapter.setActions(actions);
            }
        });

        return view;
    }

    @Override
    public void onSubGoalSelected(int goalId) {

    }

    @Override
    public void onActionSelected(int actionId) {

    }

    @Override
    public void onActionEdit(Action action) {

    }

    @Override
    public void updateAction(Action action) {

    }
}
