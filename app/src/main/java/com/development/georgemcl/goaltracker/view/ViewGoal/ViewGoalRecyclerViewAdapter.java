package com.development.georgemcl.goaltracker.view.ViewGoal;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.development.georgemcl.goaltracker.Constants;
import com.development.georgemcl.goaltracker.R;
import com.development.georgemcl.goaltracker.model.Action;
import com.development.georgemcl.goaltracker.model.ActionTargetProgression;
import com.development.georgemcl.goaltracker.model.Goal;
import com.development.georgemcl.goaltracker.view.AddActionActivity;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ViewGoalRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private static final String TAG = "ViewGoalRecyclerViewAda";

    private static final int VIEW_TYPE_SUBGOAL = 10;
    private static final int VIEW_TYPE_ACTION = 20;

    private Context mContext;
    private OnItemSelectedListener mOnItemSelectedListener;
    private List<Goal> mSubGoals = Collections.emptyList();
    private List<Action> mActions = Collections.emptyList();

    public ViewGoalRecyclerViewAdapter(Context context, OnItemSelectedListener listener) {
        mContext = context;
        mOnItemSelectedListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_SUBGOAL) {
            View itemView = LayoutInflater.from(mContext).inflate(R.layout.goal_item, parent, false);
            return new GoalViewHolder(itemView);
        }

        if (viewType == VIEW_TYPE_ACTION) {
            final View itemView = LayoutInflater.from(mContext).inflate(R.layout.action_item, parent, false);
            return new ActionViewHolder(itemView);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof GoalViewHolder) {
            final Goal goal = mSubGoals.get(position);
            ((GoalViewHolder) holder).populateFields(goal);
            ((GoalViewHolder) holder).parentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemSelectedListener.onSubGoalSelected(goal.getId());
//                    Intent intent = new Intent(mContext, ViewGoalActivity.class);
//                    intent.putExtra(Constants.KEY_PARENT_GOAL_ID, goal.getId());
//                    mContext.startActivity(intent);
                }
            });
        }
        if (holder instanceof ActionViewHolder) {
            final Action action = mActions.get(position - mSubGoals.size());
            final ActionViewHolder actionViewHolder = (ActionViewHolder) holder;
            actionViewHolder.populateFields(action);

            actionViewHolder.doneImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "onClick: setting repeat progress amount to "+ action.getRepeatAmount());
                    action.setRepeatProgressAmount(action.getRepeatAmount());
                    mOnItemSelectedListener.updateAction(action);
                    Toast.makeText(mContext, "Action completed", Toast.LENGTH_SHORT).show();
                }
            });

            actionViewHolder.parentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemSelectedListener.onActionSelected(action.getId());
                }
            });

            actionViewHolder.optionsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popupMenu = new PopupMenu(mContext, actionViewHolder.optionsButton);
                    popupMenu.inflate(R.menu.menu_action_options);
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()){
                                case R.id.action_edit_action : {
                                    mOnItemSelectedListener.onActionEdit(action);
                                    return true;
                                }
                                case R.id.action_delete_action : {
                                    return true;
                                }
                                case R.id.action_reset_progress : {
                                    action.setRepeatAmount(0);
                                    mOnItemSelectedListener.updateAction(action);
                                }
                                default:
                                    return false;
                            }
                        }
                    });
                    popupMenu.show();
                }
            });
        }

    }

    public void setSubGoals(List<Goal> goals) {
        mSubGoals = goals;
        notifyDataSetChanged();
    }

    public void setActions(List<Action> actions) {
        mActions = actions;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mSubGoals.size() + mActions.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position < mSubGoals.size()) {
            return VIEW_TYPE_SUBGOAL;
        }

        if (position - mSubGoals.size() < mActions.size()) {
            return VIEW_TYPE_ACTION;
        }

        return -1;
    }

    public class ActionViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.action_item_name_textview) TextView nameTxt;
        @BindView(R.id.action_item_parent_layout) View parentLayout;
        @BindView(R.id.action_item_done_imageview) ImageView doneImageView;
        @BindView(R.id.action_item_options_button) Button optionsButton;
        @BindView(R.id.action_item_repeat_textview) TextView repeatTxt;
        @BindView(R.id.action_item_progressbar) ProgressBar completionProgressBar;

        public ActionViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void populateFields(Action action) {
            nameTxt.setText(action.getActionName());
            String repeatString = action.getRepeatAmount() + " " + action.getRepeatUnitOfMeasurement() + " " + action.getRepeatTimePeriod();
            repeatTxt.setText(repeatString);
            if (action.getRepeatProgressAmount() > 0 && action.getRepeatAmount() > 0){
                double amount = (action.getRepeatProgressAmount() / action.getRepeatAmount()) * 100;
                Log.d(TAG, "populateFields: "+amount);
                completionProgressBar.setProgress((int)amount);

            }
        }
    }

    public class GoalViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.goal_item_name_textview) TextView nameTxt;
        @BindView(R.id.goal_item_completion_date_textview) TextView completionDateTxt;
        @BindView(R.id.goal_item_parent_layout) View parentLayout;

        public GoalViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void populateFields(Goal goal) {
            nameTxt.setText(goal.getGoalName());
            completionDateTxt.setText(goal.getCompletionDate());
        }
    }

    public interface OnItemSelectedListener {
        /**
         * Called when a sub goal is selected
         * @param goalId
         */
        void onSubGoalSelected(int goalId);

        /**
         * Called when an action is selected
         * @param actionId
         */
        void onActionSelected(int actionId);

        void onActionEdit(Action action);

        void updateAction(Action action);
    }

}
