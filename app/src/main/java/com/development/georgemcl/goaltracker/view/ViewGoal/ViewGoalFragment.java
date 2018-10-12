package com.development.georgemcl.goaltracker.view.ViewGoal;

import android.app.AlertDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
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

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.yavski.fabspeeddial.FabSpeedDial;

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_goal, container, false);
        ButterKnife.bind(this, view);

        if (getArguments() != null) {
            mParentGoalId = getArguments().getInt(Constants.KEY_PARENT_GOAL_ID);
        }

        mViewGoalViewModel = ViewModelProviders.of(this).get(ViewGoalViewModel.class);

        mViewGoalViewModel.populateLists(mParentGoalId);

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
                if (goals != null) {
                    mSubGoalCount = goals.size();
                }
            }
        });

        mViewGoalViewModel.getGoalById(mParentGoalId).observe(this, new Observer<Goal>() {
            @Override
            public void onChanged(@Nullable Goal goal) {
                mGoalInView = goal;
                if (mGoalInView != null) {
                    mGoalNameTxt.setText(goal.getGoalName());
                    mGoalDescriptionTxt.setText(goal.getDescription());
                    mGoalCompletionDateTxt.setText(goal.getCompletionDate());
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


        mOptionsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(getContext(), mOptionsBtn);
                popupMenu.inflate(R.menu.menu_goal_options);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
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
                    }
                });
                popupMenu.show();
            }
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK){
            switch (requestCode){
                case ADD_ACTION_REQUEST_CODE : {
                    Log.d(TAG, "onActivityResult: add");
                    Action action = (Action) data.getSerializableExtra(AddActionActivity.EXTRA_ACTION_TO_ADD);
                    Log.d(TAG, "onActivityResult: action: " +action.toString());
                    mViewGoalViewModel.insertAction(action);
                    break;
                }
                case ADD_SUBGOAL_REQUEST_CODE : {
                    Goal goal = (Goal) data.getSerializableExtra(AddGoalActivity.EXTRA_GOAL_TO_ADD);
                    Log.d(TAG, "onActivityResult: goal: " + goal.toString());
                    mViewGoalViewModel.insertSubGoal(goal);
                    break;
                }
                case EDIT_ACTION_REQUEST_CODE : {
                    Log.d(TAG, "onActivityResult: edit");
                    Action action = (Action) data.getSerializableExtra(AddActionActivity.EXTRA_ACTION_TO_EDIT);
                    mViewGoalViewModel.editAction(action);
                    break;
                }
                case EDIT_GOAL_REQUEST_CODE : {
                    Goal goal = (Goal) data.getSerializableExtra(AddGoalActivity.EXTRA_GOAL_TO_EDIT);
                    Log.d(TAG, "onActivityResult: new edited goal : " + goal);
                    mViewGoalViewModel.editGoal(goal);
                    break;
                }

            }
        }

    }

    private void completeGoalConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                .setTitle("Complete Goal")
                .setMessage("Has this goal been reached?\n" +
                        "This will also remove all of its related actions")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteGoalAndActions();
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                ;
        builder.show();
    }

    private void deleteGoalAndActions() {
        boolean success = mViewGoalViewModel.deleteGoalAndActions(mGoalInView);
        if (success) {
            Toast.makeText(getContext(), "Goal and Actions deleted", Toast.LENGTH_SHORT).show();
            getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
            getActivity().getSupportFragmentManager().popBackStack();
        } else {
            Toast.makeText(getContext(), "Failed to delete", Toast.LENGTH_SHORT).show();

        }
    }

    private void deleteGoalConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), android.R.style.Theme_Material_Dialog_Alert)
                .setTitle("Delete Goal")
                .setMessage("Are you sure you want to delete this goal? \n" + mGoalInView.getGoalName() + "\n This will remove all of its related actions")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteGoalAndActions();
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                ;
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
        mViewGoalViewModel.editAction(action);
    }

    @Override
    public void deleteAction(Action action) {
        mViewGoalViewModel.deleteAction(action);
    }
}
