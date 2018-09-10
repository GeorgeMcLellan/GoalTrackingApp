package com.development.georgemcl.goaltracker.view;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.development.georgemcl.goaltracker.R;
import com.development.georgemcl.goaltracker.model.Action;
import com.development.georgemcl.goaltracker.model.Goal;
import com.development.georgemcl.goaltracker.view.adapters.GoalRecyclerViewAdapter;
import com.development.georgemcl.goaltracker.view.adapters.ViewGoalRecyclerViewAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ViewGoalActivity extends AppCompatActivity {

    @BindView(R.id.view_goal_add_fab) FloatingActionButton mAddFab;
    @BindView(R.id.view_goal_recycler_view) RecyclerView mRecyclerView;

    private ArrayList<Goal> mSubGoals;
    private ArrayList<Action> mActions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_goal);
        ButterKnife.bind(this);

        mSubGoals = new ArrayList<>();
        mSubGoals.add(new Goal("Eat more food", "End of year"));
        mSubGoals.add(new Goal("Heal UC", "End of year"));

        mActions = new ArrayList<>();
        mActions.add(new Action("Perform this action"));


        mRecyclerView.setAdapter(new ViewGoalRecyclerViewAdapter(this, mSubGoals, mActions));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

    }
}
