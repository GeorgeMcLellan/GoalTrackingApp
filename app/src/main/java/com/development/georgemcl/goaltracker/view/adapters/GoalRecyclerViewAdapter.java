package com.development.georgemcl.goaltracker.view.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.development.georgemcl.goaltracker.R;
import com.development.georgemcl.goaltracker.model.Goal;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GoalRecyclerViewAdapter extends RecyclerView.Adapter<GoalRecyclerViewAdapter.ViewHolder> {

    private Context mContext;
    private List<Goal> mGoals = Collections.emptyList();
    private OnGoalSelectedListener mGoalSelectedListener;

    public GoalRecyclerViewAdapter(Context context, OnGoalSelectedListener listener) {
        mContext = context;
        mGoalSelectedListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(mContext).inflate(R.layout.goal_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull GoalRecyclerViewAdapter.ViewHolder holder, int position) {

        final Goal goal = mGoals.get(position);
        holder.nameTxt.setText(goal.getGoalName());
        holder.completionDateTxt.setText(goal.getCompletionDate());
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mGoalSelectedListener.onGoalSelected(goal.getId());
//                Intent intent = new Intent(mContext, ViewGoalActivity.class);
//                intent.putExtra(Constants.KEY_PARENT_GOAL_ID, goal.getId());
//                mContext.startActivity(intent);
            }
        });
    }

    public void setGoals(List<Goal> goals) {
        mGoals = goals;
        notifyDataSetChanged();
    }

    public void setOnGoalSelectedListener(OnGoalSelectedListener listener) {
        mGoalSelectedListener = listener;
    }

    @Override
    public int getItemCount() {
        return mGoals.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.goal_item_name_textview) TextView nameTxt;
        @BindView(R.id.goal_item_completion_date_textview) TextView completionDateTxt;
        @BindView(R.id.goal_item_parent_layout) View parentLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    /**
     * Communicates to parent view when a goal has been selected
     */
    public interface OnGoalSelectedListener {
        /**
         * Called when a goal is selected
         * @param goalId
         */
        void onGoalSelected(int goalId);
    }
}
