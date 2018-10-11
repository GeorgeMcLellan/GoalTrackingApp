package com.development.georgemcl.goaltracker.view.MainGoalView;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.development.georgemcl.goaltracker.data.repository.GoalRepository;
import com.development.georgemcl.goaltracker.model.Goal;

import java.util.List;

/**
 * ViewModel pattern for showing goals on the main screen
 */
public class MainGoalViewModel extends AndroidViewModel {

    private GoalRepository mRepository;
    private LiveData<List<Goal>> mMainGoals;

    public MainGoalViewModel (Application application) {
        super(application);
        mRepository = new GoalRepository(application);
        mMainGoals = mRepository.getMainGoals();
    }

    public LiveData<List<Goal>> getMainGoals() {
        return mMainGoals;
    }

    public void insert(Goal goal) {
        mRepository.insert(goal);
    }
}
