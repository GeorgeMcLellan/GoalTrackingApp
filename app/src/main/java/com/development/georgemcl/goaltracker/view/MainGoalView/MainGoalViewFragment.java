package com.development.georgemcl.goaltracker.view.MainGoalView;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.development.georgemcl.goaltracker.Constants;
import com.development.georgemcl.goaltracker.R;
import com.development.georgemcl.goaltracker.model.Goal;
import com.development.georgemcl.goaltracker.view.AddGoalActivity;
import com.development.georgemcl.goaltracker.view.adapters.GoalRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_OK;


public class MainGoalViewFragment extends Fragment {

    private static final String TAG = "MainGoalViewFragment";

    private static final int ADD_GOAL_ACTIVITY_REQUEST_CODE = 5;

    @BindView(R.id.main_goal_add_goal_fab) FloatingActionButton mAddGoalFab;
    @BindView(R.id.main_goal_recycler_view) RecyclerView mGoalRecyclerView;

    private ArrayList<Goal> mGoals;
    private GoalRecyclerViewAdapter mRecyclerViewAdapter;
    private MainGoalViewModel mMainGoalViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main_goal_view, container, false);
        ButterKnife.bind(this, view);

        mMainGoalViewModel = ViewModelProviders.of(this).get(MainGoalViewModel.class);

        mGoals = new ArrayList<>();
        mGoals.add(new Goal("Stop eating sugar", "End of 2018"));
        mGoals.add(new Goal("Publish an app to Google Play", "End of 2018"));

        mRecyclerViewAdapter = new GoalRecyclerViewAdapter(getContext(), mGoals);
        mGoalRecyclerView.setAdapter(mRecyclerViewAdapter);
        mGoalRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mAddGoalFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddGoalActivity.class);
                startActivityForResult(intent, ADD_GOAL_ACTIVITY_REQUEST_CODE);
            }
        });

        mMainGoalViewModel.getAllGoals().observe(this, new Observer<List<Goal>>() {
            @Override
            public void onChanged(@Nullable List<Goal> goals) {
                mRecyclerViewAdapter.setGoals(goals);
                Log.d(TAG, "onChanged: goals size = "+ goals.size());
            }
        });

        return view;
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_GOAL_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK){
            Goal goal = (Goal) data.getSerializableExtra(AddGoalActivity.EXTRA_GOAL_TO_ADD);
            Log.d(TAG, "onActivityResult: Recieved goal : " + goal.toString());
            mMainGoalViewModel.insert(goal);
        }
    }
}
