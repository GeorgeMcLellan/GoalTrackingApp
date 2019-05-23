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

import io.reactivex.Completable;
import io.reactivex.disposables.CompositeDisposable;

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
    private CompositeDisposable disposables = new CompositeDisposable();


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

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
    }

    public LiveData<List<Goal>> getSubGoals() {
        return mSubGoals;
    }

    public LiveData<List<Action>> getActions() {
        return mActions;
    }

    public LiveData<Goal> getGoalById(int id) { return mGoalRepository.getGoalById(id);}

    public Completable insertSubGoal(Goal goal) {
        return mGoalRepository.insert(goal);
    }

    public Completable insertAction(Action action) {
        return mActionRepository.insert(action);
    }

    public Completable editAction(Action action) { return mActionRepository.edit(action); }

    public Completable editGoal(Goal goal) { return mGoalRepository.edit(goal);}

    public Completable deleteAction(Action action){ return mActionRepository.delete(action);}

    public Completable deleteGoal(Goal goal) { return mGoalRepository.delete(goal);}

    public boolean deleteGoalAndActions (Goal goal) {
        if (mActions.getValue() != null) {
            for (Action action : mActions.getValue()) {
                disposables.add(mActionRepository.delete(action)
                        .subscribe(
                                () -> Log.i(TAG, "deleteAction onComplete: "),
                                (e) -> Log.e(TAG, "deleteAction error: " + e.getMessage())
                        ));
            }
            disposables.add(mGoalRepository.delete(goal)
                    .subscribe(
                            () -> Log.i(TAG, "deleteGoal onComplete: "),
                            (e) -> Log.e(TAG, "deleteGoal error: " + e.getMessage())
                    ));
            return true;
        }
        return false;
    }
}

