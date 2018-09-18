package com.development.georgemcl.goaltracker.view;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.development.georgemcl.goaltracker.R;
import com.development.georgemcl.goaltracker.model.Goal;
import com.development.georgemcl.goaltracker.view.adapters.GoalRecyclerViewAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainGoalViewFragment extends Fragment {

    private static final String TAG = "MainGoalViewFragment";

    @BindView(R.id.main_goal_add_goal_fab) FloatingActionButton mAddGoalFab;
    @BindView(R.id.main_goal_recycler_view) RecyclerView mGoalRecyclerView;

    private ArrayList<Goal> mGoals;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main_goal_view, container, false);
        ButterKnife.bind(this, view);

        mGoals = new ArrayList<>();
        mGoals.add(new Goal("333","Stop eating sugar", "End of 2018"));
        mGoals.add(new Goal("222","Publish an app to Google Play", "End of 2018"));

        mGoalRecyclerView.setAdapter(new GoalRecyclerViewAdapter(getContext(), mGoals));
        mGoalRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mAddGoalFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddGoalActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

}
