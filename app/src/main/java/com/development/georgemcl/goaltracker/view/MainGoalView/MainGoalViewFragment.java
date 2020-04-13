package com.development.georgemcl.goaltracker.view.MainGoalView;


import android.app.ActionBar;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.development.georgemcl.goaltracker.Constants;
import com.development.georgemcl.goaltracker.R;
import com.development.georgemcl.goaltracker.model.Goal;
import com.development.georgemcl.goaltracker.view.AddGoal.AddGoalActivity;
import com.development.georgemcl.goaltracker.view.MainActivity.MainActivity;
import com.development.georgemcl.goaltracker.view.ViewGoal.ViewGoalFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;

import static android.app.Activity.RESULT_OK;

/**
 * Main Goal screen that shows all goals which are not sub-goals
 */
public class MainGoalViewFragment extends Fragment implements GoalRecyclerViewAdapter.OnGoalSelectedListener{

    private static final String TAG = "MainGoalViewFragment";

    private static final int ADD_GOAL_ACTIVITY_REQUEST_CODE = 5;

    @BindView(R.id.main_goal_add_goal_fab) FloatingActionButton mAddGoalFab;
    @BindView(R.id.main_goal_recycler_view) RecyclerView mGoalRecyclerView;

    private GoalRecyclerViewAdapter mRecyclerViewAdapter;
    private MainGoalViewModel mMainGoalViewModel;

    private CompositeDisposable disposables = new CompositeDisposable();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_goal_view, container, false);
        ButterKnife.bind(this, view);

        ActionBar actionbar = getActivity().getActionBar();
        if (actionbar != null) {
            actionbar.setTitle("Actions");
        }

        mMainGoalViewModel = new ViewModelProvider(this).get(MainGoalViewModel.class);

        MainActivity activity = ((MainActivity) getActivity());
        if (activity != null) {
            activity.setActionBarTitle("Goals");
            activity.showBackButton(false);
        }


        mRecyclerViewAdapter = new GoalRecyclerViewAdapter(getContext(), this);
        mGoalRecyclerView.setAdapter(mRecyclerViewAdapter);
        mGoalRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mAddGoalFab.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), AddGoalActivity.class);
            startActivityForResult(intent, ADD_GOAL_ACTIVITY_REQUEST_CODE);
        });

        mMainGoalViewModel.getMainGoals().observe(getViewLifecycleOwner(), goals -> mRecyclerViewAdapter.setGoals(goals));

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        disposables.clear();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_GOAL_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK){
            Goal goal = (Goal) data.getSerializableExtra(AddGoalActivity.EXTRA_GOAL_TO_ADD);
            disposables.add(mMainGoalViewModel.insert(goal)
                    .subscribe(
                            () -> Log.d(TAG, "insertGoal onComplete: "),
                            (e) -> Log.e(TAG, "insertGoal: error" + e.getMessage() )
                    ));
        }
    }

    @Override
    public void onGoalSelected(int goalId) {
        Fragment fragment = new ViewGoalFragment();
        Bundle args = new Bundle();
        args.putInt(Constants.KEY_PARENT_GOAL_ID, goalId);
        fragment.setArguments(args);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_activity_fragment_container, fragment).addToBackStack(null).commit();
        if (getActivity() instanceof MainActivity){
            ((MainActivity) getActivity()).showBackButton(true);
        }
    }


}
