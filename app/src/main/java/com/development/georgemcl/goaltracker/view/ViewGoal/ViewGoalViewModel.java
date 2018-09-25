package com.development.georgemcl.goaltracker.view.ViewGoal;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.development.georgemcl.goaltracker.data.repository.ActionRepository;
import com.development.georgemcl.goaltracker.data.repository.GoalRepository;
import com.development.georgemcl.goaltracker.model.Action;
import com.development.georgemcl.goaltracker.model.Goal;

import java.util.Collections;
import java.util.List;

public class ViewGoalViewModel extends AndroidViewModel {

    private GoalRepository mGoalRepository;
    private ActionRepository mActionRepository;
    private LiveData<List<Goal>> mSubGoals;
    private LiveData<List<Action>> mActions;
    private int mParentGoalId;

    public ViewGoalViewModel (Application application) {
        super(application);
        mGoalRepository = new GoalRepository(application);
        mActionRepository = new ActionRepository(application);
    }

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
}
