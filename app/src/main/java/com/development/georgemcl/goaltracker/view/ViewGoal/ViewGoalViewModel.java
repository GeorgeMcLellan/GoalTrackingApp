package com.development.georgemcl.goaltracker.view.ViewGoal;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.util.Log;

import com.development.georgemcl.goaltracker.data.repository.ActionRepository;
import com.development.georgemcl.goaltracker.data.repository.GoalRepository;
import com.development.georgemcl.goaltracker.model.Action;
import com.development.georgemcl.goaltracker.model.Goal;

import java.util.Collections;
import java.util.List;
/**
 * ViewModel pattern for sub-goals and actions that fall under the selected goal (mParentGoalId)
 */
public class ViewGoalViewModel extends AndroidViewModel {
    private static final String TAG = "ViewGoalViewModel";

    private GoalRepository mGoalRepository;
    private ActionRepository mActionRepository;
    private LiveData<List<Goal>> mSubGoals;
    private LiveData<List<Action>> mActions;
    //The selected goal's goal id
    private int mParentGoalId;

    public ViewGoalViewModel (Application application) {
        super(application);
        mGoalRepository = new GoalRepository(application);
        mActionRepository = new ActionRepository(application);
    }

    /**
     * Populates lists with relevant data based on the goal being viewed
     * @param parentGoalId
     */
    public void populateLists(int parentGoalId){
        mParentGoalId = parentGoalId;
        mSubGoals = mGoalRepository.getSubGoals(mParentGoalId);
        mActions = mActionRepository.getActions(mParentGoalId);
    }

    public LiveData<List<Goal>> getSubGoals() {
        return mSubGoals;
    }

    public LiveData<List<Action>> getActions() {
        return mActions;
    }

    public LiveData<Goal> getGoalById(int id) { return mGoalRepository.getGoalById(id);}

    public void insertSubGoal(Goal goal) {
        mGoalRepository.insert(goal);
    }

    public void insertAction(Action action) {
        mActionRepository.insert(action);
    }

    public void editAction(Action action) { mActionRepository.edit(action); }

    public void editGoal(Goal goal) { mGoalRepository.edit(goal);}

    public void deleteAction(Action action){ mActionRepository.delete(action);}

    public void deleteGoal(Goal goal) {mGoalRepository.delete(goal);}

    public boolean deleteGoalAndActions (Goal goal) {
        if (mActions.getValue() != null) {
            for (Action action : mActions.getValue()) {
                mActionRepository.delete(action);
            }
            mGoalRepository.delete(goal);
            //deletegoal
            return true;
        }
        return false;
    }
}

