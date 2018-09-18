package com.development.georgemcl.goaltracker.view;

import android.content.Intent;
import android.support.design.internal.NavigationMenu;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;

import com.development.georgemcl.goaltracker.Constants;
import com.development.georgemcl.goaltracker.R;
import com.development.georgemcl.goaltracker.model.Action;
import com.development.georgemcl.goaltracker.model.Goal;
import com.development.georgemcl.goaltracker.view.adapters.GoalRecyclerViewAdapter;
import com.development.georgemcl.goaltracker.view.adapters.ViewGoalRecyclerViewAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.yavski.fabspeeddial.FabSpeedDial;

public class ViewGoalActivity extends AppCompatActivity {

    private static final String TAG = "ViewGoalActivity";

    @BindView(R.id.view_goal_add_fab) FabSpeedDial mAddFab;
    @BindView(R.id.view_goal_recycler_view) RecyclerView mRecyclerView;

    private static final int ADD_ACTION_REQUEST_CODE = 204;
    private static final int ADD_SUBGOAL_REQUEST_CODE = 689;

    private ArrayList<Goal> mSubGoals;
    private ArrayList<Action> mActions;

    private String parentGoalId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_goal);
        ButterKnife.bind(this);

        if (parentGoalId == null && getIntent().hasExtra(Constants.KEY_PARENT_GOAL_ID)){
            parentGoalId = getIntent().getStringExtra(Constants.KEY_PARENT_GOAL_ID);
            Log.i(TAG, "onCreate: parent goal id = " + parentGoalId);
        }

        mSubGoals = new ArrayList<>();
        mSubGoals.add(new Goal("999","Improve Android knowledge", "October 30 2018"));
        mSubGoals.add(new Goal("888","Complete a mockup of app", "September 30 2018"));

        mActions = new ArrayList<>();
        mActions.add(new Action("Research other apps"));
        mActions.add(new Action("Read 30 mins a day"));



        mRecyclerView.setAdapter(new ViewGoalRecyclerViewAdapter(this, mSubGoals, mActions));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        mAddFab.setMenuListener(new FabSpeedDial.MenuListener() {
            @Override
            public boolean onPrepareMenu(NavigationMenu navigationMenu) {
                return true;
            }

            @Override
            public boolean onMenuItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.action_add_action : {
                        Intent intent = new Intent(ViewGoalActivity.this, AddActionActivity.class);
                        intent.putExtra(Constants.KEY_PARENT_GOAL_ID, parentGoalId);
                        startActivityForResult(intent, ADD_ACTION_REQUEST_CODE);
                        return true;
                    }
                    case R.id.action_add_subgoal : {
                        Intent intent = new Intent(ViewGoalActivity.this, AddGoalActivity.class);
                        intent.putExtra(Constants.KEY_PARENT_GOAL_ID, parentGoalId);
                        startActivityForResult(intent, ADD_SUBGOAL_REQUEST_CODE);
                        break;
                    }
                    default: break;
                }
                return true;
            }

            @Override
            public void onMenuClosed() {}
        });
    }
}