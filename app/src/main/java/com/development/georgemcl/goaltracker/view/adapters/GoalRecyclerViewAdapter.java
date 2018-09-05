package com.development.georgemcl.goaltracker.view.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.development.georgemcl.goaltracker.R;
import com.development.georgemcl.goaltracker.model.Goal;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GoalRecyclerViewAdapter extends RecyclerView.Adapter<GoalRecyclerViewAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<Goal> mGoals;

    public GoalRecyclerViewAdapter(Context context, ArrayList<Goal> goals) {
        mContext = context;
        mGoals = goals;
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
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "clicked on goal" + goal.getGoalName(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mGoals.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.goal_item_name_textview) TextView nameTxt;
        @BindView(R.id.goal_item_parent_layout) View parentLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
