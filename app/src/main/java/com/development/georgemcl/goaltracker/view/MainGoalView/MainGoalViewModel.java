package com.development.georgemcl.goaltracker.view.MainGoalView;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.development.georgemcl.goaltracker.data.repository.GoalRepository;
import com.development.georgemcl.goaltracker.model.Goal;

import java.util.List;

public class MainGoalViewModel extends AndroidViewModel {

    private GoalRepository mRepository;
    private LiveData<List<Goal>> mAllGoals;

    public MainGoalViewModel (Application application) {
        super(application);
        mRepository = new GoalRepository(application);
        mAllGoals = mRepository.getAllGoals();
    }

    public LiveData<List<Goal>> getAllGoals() {
        return mAllGoals;
    }

    public void insert(Goal goal) {
        mRepository.insert(goal);
    }
}
