package com.development.georgemcl.goaltracker.view.adapters;

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
import android.widget.TextView;
import android.widget.Toast;

import com.development.georgemcl.goaltracker.Constants;
import com.development.georgemcl.goaltracker.R;
import com.development.georgemcl.goaltracker.model.Action;
import com.development.georgemcl.goaltracker.model.Goal;
import com.development.georgemcl.goaltracker.view.ViewGoalActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ViewGoalRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final int VIEW_TYPE_SUBGOAL = 10;
    private static final int VIEW_TYPE_ACTION = 20;

    private Context mContext;
    private ArrayList<Goal> mSubGoals;
    private ArrayList<Action> mActions;

    public ViewGoalRecyclerViewAdapter(Context context, ArrayList<Goal> goals, ArrayList<Action> actions) {
        mContext = context;
        mSubGoals = goals;
        mActions = actions;
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
                    Intent intent = new Intent(mContext, ViewGoalActivity.class);
                    intent.putExtra(Constants.KEY_PARENT_GOAL_ID, goal.getId());
                    mContext.startActivity(intent);
                }
            });
        }
        if (holder instanceof ActionViewHolder) {
            Action action = mActions.get(position - mSubGoals.size());
            final ActionViewHolder actionViewHolder = (ActionViewHolder) holder;
            actionViewHolder.populateFields(action);

            actionViewHolder.doneImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext, "Action completed", Toast.LENGTH_SHORT).show();
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
                                    return true;
                                }
                                case R.id.action_delete_action : {
                                    return true;
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

        public ActionViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void populateFields(Action action) {
            nameTxt.setText(action.getActionName());
        }
    }

    public class GoalViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.goal_item_name_textview) TextView nameTxt;
        @BindView(R.id.goal_item_parent_layout) View parentLayout;

        public GoalViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void populateFields(Goal goal) {
            nameTxt.setText(goal.getGoalName());
        }
    }

}
