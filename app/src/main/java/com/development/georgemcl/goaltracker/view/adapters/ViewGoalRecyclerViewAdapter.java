package com.development.georgemcl.goaltracker.view.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.development.georgemcl.goaltracker.R;
import com.development.georgemcl.goaltracker.model.Action;
import com.development.georgemcl.goaltracker.model.Goal;

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
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof GoalViewHolder) {
            final Goal goal = mSubGoals.get(position);
            ((GoalViewHolder) holder).populateFields(goal);
            ((GoalViewHolder) holder).parentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext, "clicked on goal" + goal.getGoalName(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        if (holder instanceof ActionViewHolder) {
            Action action = mActions.get(position - mSubGoals.size());
            ((ActionViewHolder) holder).populateFields(action);
        }

    }

    @Override
    public int getItemCount() {
        return mSubGoals.size() + mActions.size();
    }

    @Override
    public int getItemViewType(int position) {
        Log.d("RECYCLER", "getItemViewType: "+ position);
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
