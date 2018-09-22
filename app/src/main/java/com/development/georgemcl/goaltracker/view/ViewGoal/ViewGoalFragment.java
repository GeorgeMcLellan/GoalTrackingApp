package com.development.georgemcl.goaltracker.view.ViewGoal;

import android.app.ActionBar;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.internal.NavigationMenu;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.development.georgemcl.goaltracker.Constants;
import com.development.georgemcl.goaltracker.R;
import com.development.georgemcl.goaltracker.model.Action;
import com.development.georgemcl.goaltracker.model.Goal;
import com.development.georgemcl.goaltracker.view.AddActionActivity;
import com.development.georgemcl.goaltracker.view.AddGoalActivity;
import com.development.georgemcl.goaltracker.view.adapters.ViewGoalRecyclerViewAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.yavski.fabspeeddial.FabSpeedDial;

import static android.app.Activity.RESULT_OK;

public class ViewGoalFragment extends Fragment implements ViewGoalRecyclerViewAdapter.OnItemSelectedListener{
    private static final String TAG = "ViewGoalFragment";
    @BindView(R.id.view_goal_add_fab) FabSpeedDial mAddFab;
    @BindView(R.id.view_goal_recycler_view) RecyclerView mRecyclerView;
    @BindView(R.id.view_goal_name_textview) TextView mGoalNameTxt;
    @BindView(R.id.view_goal_description_textview) TextView mGoalDescriptionTxt;
    @BindView(R.id.view_goal_completion_date_textview) TextView mGoalCompletionDateTxt;

    private static final int ADD_ACTION_REQUEST_CODE = 204;
    private static final int ADD_SUBGOAL_REQUEST_CODE = 689;

    private ViewGoalRecyclerViewAdapter mRecyclerViewAdapter;

    private ViewGoalViewModel mViewGoalViewModel;

    private int parentGoalId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_goal, container, false);
        ButterKnife.bind(this, view);
//        if (getIntent().hasExtra(Constants.KEY_PARENT_GOAL_ID)){
//            parentGoalId = getIntent().getIntExtra(Constants.KEY_PARENT_GOAL_ID, -1);
//            Log.i(TAG, "onCreate: parent goal id = " + parentGoalId);
//        }
        
        parentGoalId = getArguments().getInt(Constants.KEY_PARENT_GOAL_ID);

        mViewGoalViewModel = ViewModelProviders.of(this).get(ViewGoalViewModel.class);

        mViewGoalViewModel.populateLists(parentGoalId);


//        mSubGoals = new ArrayList<>();
////        mSubGoals.add(new Goal("Improve Android knowledge", "October 30 2018"));
////        mSubGoals.add(new Goal("Complete a mockup of app", "September 30 2018"));
//
//        mActions = new ArrayList<>();
//        mActions.add(new Action("Research other apps"));
//        mActions.add(new Action("Read 30 mins a day"));



        mRecyclerViewAdapter = new ViewGoalRecyclerViewAdapter(getContext(), this);
        mRecyclerView.setAdapter(mRecyclerViewAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        mViewGoalViewModel.getActions().observe(this, new Observer<List<Action>>() {
            @Override
            public void onChanged(@Nullable List<Action> actions) {
                mRecyclerViewAdapter.setActions(actions);
            }
        });

        mViewGoalViewModel.getSubGoals().observe(this, new Observer<List<Goal>>() {
            @Override
            public void onChanged(@Nullable List<Goal> goals) {
                mRecyclerViewAdapter.setSubGoals(goals);
            }
        });

        mViewGoalViewModel.getGoalById(parentGoalId).observe(this, new Observer<Goal>() {
            @Override
            public void onChanged(@Nullable Goal goal) {
                mGoalNameTxt.setText(goal.getGoalName());
                mGoalDescriptionTxt.setText(goal.getDescription());
                mGoalCompletionDateTxt.setText(goal.getCompletionDate());
            }
        });


        mAddFab.setMenuListener(new FabSpeedDial.MenuListener() {
            @Override
            public boolean onPrepareMenu(NavigationMenu navigationMenu) {
                return true;
            }

            @Override
            public boolean onMenuItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.action_add_action : {
                        Intent intent = new Intent(getContext(), AddActionActivity.class);
                        intent.putExtra(Constants.KEY_PARENT_GOAL_ID, parentGoalId);
                        startActivityForResult(intent, ADD_ACTION_REQUEST_CODE);
                        return true;
                    }
                    case R.id.action_add_subgoal : {
                        Intent intent = new Intent(getContext(), AddGoalActivity.class);
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
        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_ACTION_REQUEST_CODE && resultCode == RESULT_OK){
            Action action = (Action) data.getSerializableExtra(AddActionActivity.EXTRA_ACTION_TO_ADD);
            Log.d(TAG, "onActivityResult: action: " +action.toString());
            mViewGoalViewModel.insertAction(action);
        }
        else if (requestCode == ADD_SUBGOAL_REQUEST_CODE && resultCode == RESULT_OK) {
            Goal goal = (Goal) data.getSerializableExtra(AddGoalActivity.EXTRA_GOAL_TO_ADD);
            Log.d(TAG, "onActivityResult: goal: " +goal.toString());
            mViewGoalViewModel.insertSubGoal(goal);
        }
    }

    @Override
    public void onSubGoalSelected(int goalId) {
        Fragment fragment = new ViewGoalFragment();
        Bundle args = new Bundle();
        args.putInt(Constants.KEY_PARENT_GOAL_ID, goalId);
        fragment.setArguments(args);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_activity_fragment_container, fragment).addToBackStack(null).commit();
    }

    @Override
    public void onActionSelected(int actionId) {

    }
}
