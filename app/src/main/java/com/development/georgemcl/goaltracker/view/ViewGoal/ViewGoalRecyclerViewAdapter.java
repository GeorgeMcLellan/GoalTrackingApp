package com.development.georgemcl.goaltracker.view.ViewGoal;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.development.georgemcl.goaltracker.R;
import com.development.georgemcl.goaltracker.model.Action;
import com.development.georgemcl.goaltracker.model.Goal;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Combines both sub-goals and actions into one recyclerview that can be acted upon
 */
public class ViewGoalRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "ViewGoalRecyclerViewAda";

    private static final int VIEW_TYPE_SUBGOAL = 10;
    private static final int VIEW_TYPE_ACTION = 20;

    private static final int AMOUNT_TO_INCREMEMNT = 1;
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
                }
            });

        }
        if (holder instanceof ActionViewHolder) {
            final Action action = mActions.get(position - mSubGoals.size());
            final ActionViewHolder actionViewHolder = (ActionViewHolder) holder;
            actionViewHolder.populateFields(action);

            actionViewHolder.doneImageView.setOnClickListener(v -> handleActionDoneSelected(action));

            actionViewHolder.parentLayout.setOnClickListener(v -> mOnItemSelectedListener.onActionSelected(action.getId()));

            actionViewHolder.optionsButton.setOnClickListener(v -> {
                PopupMenu popupMenu = new PopupMenu(mContext, actionViewHolder.optionsButton);
                popupMenu.inflate(R.menu.menu_action_options);
                popupMenu.getMenu().findItem(R.id.action_reset_progress).setVisible(action.isRepeatAction());
                popupMenu.setOnMenuItemClickListener(item -> {
                    switch (item.getItemId()) {
                        case R.id.action_edit_action: {
                            mOnItemSelectedListener.onActionEdit(action);
                            return true;
                        }
                        case R.id.action_delete_action: {
                            confirmDeleteAction(action);
                            return true;
                        }
                        case R.id.action_reset_progress: {
                            action.setRepeatProgressAmount(0);
                            mOnItemSelectedListener.updateAction(action);
                        }
                        default:
                            return false;
                    }
                });
                popupMenu.show();
            });
        }

    }

    private void handleActionDoneSelected(final Action action) {
        if (action.isRepeatAction()) {
            if (action.getRepeatUnitOfMeasurement().equals(mContext.getString(R.string.repeat_times))) {
                if (action.getRepeatAmount() == 1) {
                    updateActionProgress(action, 1);
                } else {
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mContext);
                    dialogBuilder.setTitle("Enter progress " + action.getRepeatUnitOfMeasurement());
                    EditText editText = new EditText(mContext);
                    editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                    editText.setGravity(Gravity.CENTER);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT);
                    editText.setLayoutParams(lp);
                    dialogBuilder.setView(editText)
                            .setPositiveButton("Ok", ((dialog, which) -> {
                                try {
                                    int progress = Integer.parseInt(editText.getText().toString());
                                    updateActionProgress(action, progress);

                                } catch (NumberFormatException e) {
                                    Log.e(TAG, "askUserForActionProgress: " + e.getLocalizedMessage());
                                    Toast.makeText(mContext, "Invalid data entered", Toast.LENGTH_SHORT).show();
                                }
                            }))
                            .setNegativeButton("Cancel", ((dialog, which) -> { }));
                    dialogBuilder.show();
                }

            } else {
                TimePickerDialog timePickerDialog = new TimePickerDialog(mContext,
                        (view, hourOfDay, minute) -> {
                            int totalMinutes = (hourOfDay * 60) + minute;
                            updateActionProgress(action, totalMinutes);
                        }, 0, 0, true);
                timePickerDialog.show();
            }
        } else {
            ///Might want to rethink this
            Toast.makeText(mContext, "Action completed, well done!", Toast.LENGTH_SHORT).show();
            mOnItemSelectedListener.deleteAction(action);
        }

    }

    private void updateActionProgress(Action action, int progress) {
        action.setRepeatProgressAmount(action.getRepeatProgressAmount() + progress);
        if (action.getRepeatProgressAmount() >= action.getRepeatAmount()) {
            Toast.makeText(mContext, "Action completed, well done!", Toast.LENGTH_SHORT).show();
        }
        mOnItemSelectedListener.updateAction(action);
    }

    private void confirmDeleteAction(final Action action) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, android.R.style.Theme_Material_Dialog_Alert)
                .setTitle("Delete action")
                .setMessage("Are you sure you want to delete this action? \n " + action.getActionName())
                .setPositiveButton(android.R.string.yes, (dialog, which) -> mOnItemSelectedListener.deleteAction(action))
                .setNegativeButton(android.R.string.cancel, (dialog, which) -> { })
                .setIcon(android.R.drawable.ic_dialog_alert)
                ;
        builder.show();
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

    /**
     * Represents an action in the recycler view
     */
    public class ActionViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.action_item_name_textview)
        TextView nameTxt;
        @BindView(R.id.action_item_parent_layout)
        View parentLayout;
        @BindView(R.id.action_item_done_imageview)
        ImageView doneImageView;
        @BindView(R.id.action_item_options_button)
        Button optionsButton;
        @BindView(R.id.action_item_repeat_layout)
        LinearLayout repeatLayout;
        @BindView(R.id.action_item_repeat_textview)
        TextView repeatTxt;
        @BindView(R.id.action_item_progressbar)
        ProgressBar completionProgressBar;

        public ActionViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void populateFields(Action action) {
            nameTxt.setText(action.getActionName());
            if (action.isRepeatAction()) {
                repeatLayout.setVisibility(View.VISIBLE);
                completionProgressBar.setProgress(calculateProgress(action.getRepeatProgressAmount(), action.getRepeatAmount()));
                StringBuilder repeatString = new StringBuilder();
                if (action.getRepeatUnitOfMeasurement().equals(mContext.getString(R.string.repeat_times))) {
                    repeatString.append(action.getRepeatAmount()).append(" ");
                    repeatString.append("time");
                    //only append with an 's' if it requires a plural
                    if (action.getRepeatAmount() > 1) {
                        repeatString.append("s ");
                    } else{
                        repeatString.append(" ");
                    }

                } else {
                    int totalTime = action.getRepeatAmount();
                    int hours = totalTime / 60;
                    int minutes = totalTime % 60;
                    repeatString.append(hours).append("h ");
                    repeatString.append(minutes).append("m ");
                }

                repeatString.append(action.getRepeatTimePeriod());
                repeatTxt.setText(repeatString);
                doneImageView.setImageResource(android.R.drawable.ic_input_add);
            } else {
                doneImageView.setImageResource(R.drawable.baseline_done_24);
                repeatLayout.setVisibility(View.GONE);
            }
        }
    }

    /**
     * Represents a goal in the recycler view
     */
    public class GoalViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.goal_item_name_textview)
        TextView nameTxt;
        @BindView(R.id.goal_item_completion_date_textview)
        TextView completionDateTxt;
        @BindView(R.id.goal_item_parent_layout)
        View parentLayout;

        public GoalViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void populateFields(Goal goal) {
            nameTxt.setText(goal.getGoalName());
            completionDateTxt.setText(goal.getCompletionDate());
        }


    }

    /**
     * Interface to update the data in the database through communicating with the view
     */
    public interface OnItemSelectedListener {
        /**
         * Called when a sub goal is selected
         *
         * @param goalId id of sub-goal selected
         */
        void onSubGoalSelected(int goalId);

        /**
         * Called when an action is selected
         *
         * @param actionId id of action selected
         */
        void onActionSelected(int actionId);

        /**
         * Called when action is to be edited
         * @param action action to be edited
         */
        void onActionEdit(Action action);

        /**
         * Called when action is needs to be updated in the database
         * @param action updated action
         */
        void updateAction(Action action);

        /**
         * Called when action is needs to be deleted in the database
         * @param action updated action
         */
        void deleteAction(Action action);
    }

    /**
     * Calculates progress to be shown in progress bar
     * @param progress amount progressed
     * @param aim goal amount
     * @return progress from 0-100
     */
    private int calculateProgress(int progress, int aim) {
        Log.d(TAG, "calculateProgress: progress = " + progress + ", aim = " + aim);
        if (aim != 0) {
            double progressPercentage = (((double)progress / (double)aim)) * 100;
            return (int) progressPercentage;
        }
        Log.e(TAG, "calculateProgress: division by 0: " + progress + " / " + aim);
        return 0;
    }

}
