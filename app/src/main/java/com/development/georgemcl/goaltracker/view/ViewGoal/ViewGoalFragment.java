package com.development.georgemcl.goaltracker.view.ViewGoal;

import android.app.AlertDialog;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.development.georgemcl.goaltracker.Constants;
import com.development.georgemcl.goaltracker.R;
import com.development.georgemcl.goaltracker.model.Action;
import com.development.georgemcl.goaltracker.model.Goal;
import com.development.georgemcl.goaltracker.view.AddAction.AddActionActivity;
import com.development.georgemcl.goaltracker.view.AddGoal.AddGoalActivity;
import com.development.georgemcl.goaltracker.view.MainActivity.MainActivity;
import com.google.android.material.internal.NavigationMenu;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.yavski.fabspeeddial.FabSpeedDial;
import io.reactivex.disposables.CompositeDisposable;

import static android.app.Activity.RESULT_OK;

/**
 * For viewing a goal and its related actions and sub-goals using ViewGoalRecyclerViewAdapter
 */
public class ViewGoalFragment extends Fragment implements ViewGoalRecyclerViewAdapter.OnItemSelectedListener{
    private static final String TAG = "ViewGoalFragment";
    @BindView(R.id.view_goal_add_fab) FabSpeedDial mAddFab;
    @BindView(R.id.view_goal_recycler_view) RecyclerView mRecyclerView;
    @BindView(R.id.view_goal_name_textview) TextView mGoalNameTxt;
    @BindView(R.id.view_goal_description_textview) TextView mGoalDescriptionTxt;
    @BindView(R.id.view_goal_completion_date_textview) TextView mGoalCompletionDateTxt;
    @BindView(R.id.view_goal_options_button) Button mOptionsBtn;
    @BindView(R.id.view_goal_compelete_imageview) ImageView mGoalCompletedImageView;

    private static final int ADD_ACTION_REQUEST_CODE = 204;
    private static final int EDIT_ACTION_REQUEST_CODE = 689;
    private static final int ADD_SUBGOAL_REQUEST_CODE = 23;
    private static final int EDIT_GOAL_REQUEST_CODE = 231;


    private ViewGoalRecyclerViewAdapter mRecyclerViewAdapter;

    private ViewGoalViewModel mViewGoalViewModel;

    private int mParentGoalId;

    private Goal mGoalInView;
    
    private int mSubGoalCount = -1;

    private CompositeDisposable disposables = new CompositeDisposable();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_goal, container, false);
        ButterKnife.bind(this, view);

        if (getArguments() != null) {
            mParentGoalId = getArguments().getInt(Constants.KEY_PARENT_GOAL_ID);
        }

        MainActivity activity = ((MainActivity) getActivity());
        if (activity != null) {
            activity.setActionBarTitle("");
            activity.showBackButton(true);
        }

        mViewGoalViewModel = new ViewModelProvider(this).get(ViewGoalViewModel.class);

        mViewGoalViewModel.populateLists(mParentGoalId);

        mRecyclerViewAdapter = new ViewGoalRecyclerViewAdapter(getContext(), this);
        mRecyclerView.setAdapter(mRecyclerViewAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mViewGoalViewModel.getActions().observe(getViewLifecycleOwner(), actions -> mRecyclerViewAdapter.setActions(actions));

        mViewGoalViewModel.getSubGoals().observe(getViewLifecycleOwner(), goals -> {
            mRecyclerViewAdapter.setSubGoals(goals);
            if (goals != null) {
                mSubGoalCount = goals.size();
            }
        });

        mViewGoalViewModel.getGoalById(mParentGoalId).observe(getViewLifecycleOwner(), goal -> {
            mGoalInView = goal;
            if (mGoalInView != null) {
                mGoalNameTxt.setText(goal.getGoalName());
                mGoalDescriptionTxt.setText(goal.getDescription());
                if (goal.getDescription().isEmpty()) {
                    mGoalDescriptionTxt.setVisibility(View.GONE);
                } else {
                    //temporarily hide while i figure out UI
//                    mGoalDescriptionTxt.setVisibility(View.VISIBLE);
                }
                mGoalCompletionDateTxt.setText(goal.getCompletionDate());
                if (goal.getCompletionDate().isEmpty()) {
                    mGoalCompletionDateTxt.setVisibility(View.GONE);
                } else {
                    mGoalCompletionDateTxt.setVisibility(View.VISIBLE);
                }
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
                        intent.putExtra(Constants.KEY_PARENT_GOAL_ID, mParentGoalId);
                        startActivityForResult(intent, ADD_ACTION_REQUEST_CODE);
                        return true;
                    }
                    case R.id.action_add_subgoal : {
                        Intent intent = new Intent(getContext(), AddGoalActivity.class);
                        intent.putExtra(Constants.KEY_PARENT_GOAL_ID, mParentGoalId);
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


        mOptionsBtn.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(getContext(), mOptionsBtn);
            popupMenu.inflate(R.menu.menu_goal_options);
            popupMenu.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()){
                    case R.id.goal_edit_action : {
                        Intent intent = new Intent(getContext(), AddGoalActivity.class);
                        intent.putExtra(Constants.KEY_PARENT_GOAL_ID, mGoalInView.getParentGoalId());
                        intent.putExtra(Constants.KEY_GOAL_TO_EDIT, mGoalInView);
                        startActivityForResult(intent, EDIT_GOAL_REQUEST_CODE);
                        return true;
                    }
                    case R.id.goal_delete_action : {
                        if (mSubGoalCount == 0) {
                            deleteGoalConfirmationDialog();
                        } else {
                            Toast.makeText(getContext(), "You need to delete sub-goals first", Toast.LENGTH_SHORT).show();
                        }
                    }
                    default:
                        return false;
                }
            });
            popupMenu.show();
        });

        mGoalCompletedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSubGoalCount == 0) {
                    completeGoalConfirmationDialog();
                } else {
                    Toast.makeText(getContext(), "You need to complete sub-goals first", Toast.LENGTH_SHORT).show();
                }
            }
        });

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

        if (resultCode == RESULT_OK){
            switch (requestCode){
                case ADD_ACTION_REQUEST_CODE : {
                    Log.d(TAG, "onActivityResult: add");
                    Action action = (Action) data.getSerializableExtra(AddActionActivity.EXTRA_ACTION_TO_ADD);
                    Log.d(TAG, "onActivityResult: action: " +action.toString());
                    disposables.add(mViewGoalViewModel.insertAction(action)
                            .subscribe(
                                    () -> Log.d(TAG, "insertAction onComplete: "),
                                    (e) -> Log.e(TAG, "insertAction error: " + e.getMessage() )
                            ));
                    break;
                }
                case ADD_SUBGOAL_REQUEST_CODE : {
                    Goal goal = (Goal) data.getSerializableExtra(AddGoalActivity.EXTRA_GOAL_TO_ADD);
                    Log.d(TAG, "onActivityResult: goal: " + goal.toString());
                    disposables.add(mViewGoalViewModel.insertSubGoal(goal)
                            .subscribe(
                                    () -> Log.d(TAG, "insertSubGoal onComplete: "),
                                    (e) -> Log.e(TAG, "insertSubGoal: error" + e.getMessage() )
                            ));
                    break;
                }
                case EDIT_ACTION_REQUEST_CODE : {
                    Log.d(TAG, "onActivityResult: edit");
                    Action action = (Action) data.getSerializableExtra(AddActionActivity.EXTRA_ACTION_TO_EDIT);
                    updateAction(action);
                    break;
                }
                case EDIT_GOAL_REQUEST_CODE : {
                    Goal goal = (Goal) data.getSerializableExtra(AddGoalActivity.EXTRA_GOAL_TO_EDIT);
                    Log.d(TAG, "onActivityResult: new edited goal : " + goal);
                    disposables.add(mViewGoalViewModel.editGoal(goal)
                            .subscribe(
                                    () -> Log.d(TAG, "editGoal onComplete: "),
                                    (e) -> Log.e(TAG, "editGoal: error" + e.getMessage() )
                            ));
                    break;
                }

            }
        }

    }

    private void completeGoalConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                .setTitle("Goal Completed?")
                .setMessage("This will also remove all of its related actions")
                .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    Toast.makeText(getContext(), "Congratulations on achieving your goal!", Toast.LENGTH_SHORT).show();
                    deleteGoalAndActions(false);
                })
                .setNegativeButton(android.R.string.cancel, (dialog, which) -> {

                })
                ;
        builder.show();
    }

    private void deleteGoalAndActions(boolean showConfirmationToast) {
        boolean success = mViewGoalViewModel.deleteGoalAndActions(mGoalInView);
        if (success) {
            if (showConfirmationToast) {
                Toast.makeText(getContext(), "Goal and Actions deleted", Toast.LENGTH_SHORT).show();
            }
            getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
            getActivity().getSupportFragmentManager().popBackStack();
        } else {
            Toast.makeText(getContext(), "Failed to delete", Toast.LENGTH_SHORT).show();

        }
    }

    private void deleteGoalConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), android.R.style.Theme_Material_Dialog_Alert)
                .setTitle("Delete Goal")
                .setMessage("Are you sure you want to delete this goal?\nThis will remove all of its related actions")
                .setPositiveButton(android.R.string.yes, (dialog, which) -> deleteGoalAndActions(true))
                .setNegativeButton(android.R.string.cancel, (dialog, which) -> {})
                .setIcon(android.R.drawable.ic_dialog_alert);
        builder.show();
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
    public void onActionSelected(int actionId) {}

    @Override
    public void onActionEdit(Action action) {
        Intent intent = new Intent(getContext(), AddActionActivity.class);
        intent.putExtra(Constants.KEY_PARENT_GOAL_ID, mParentGoalId);
        intent.putExtra(Constants.KEY_ACTION_TO_EDIT, action);
        startActivityForResult(intent, EDIT_ACTION_REQUEST_CODE);
    }

    @Override
    public void updateAction(Action action) {
        disposables.add(mViewGoalViewModel.editAction(action)
                .subscribe(
                        () -> Log.i(TAG, "updateAction: onCompelte()"),
                        (e) -> Log.e(TAG, "updateAction: error: " +e.getMessage())
        ));
    }

    @Override
    public void deleteAction(Action action) {
        disposables.add(mViewGoalViewModel.deleteAction(action)
                .subscribe(
                        () -> Log.i(TAG, "deleteAction: onComplete()"),
                        (e) -> Log.e(TAG, "deleteAction: error: " + e.getMessage())
                ));
    }
}
