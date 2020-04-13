package com.development.georgemcl.goaltracker.data.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;

import com.development.georgemcl.goaltracker.data.db.GoalDao;
import com.development.georgemcl.goaltracker.data.db.GoalRoomDatabase;
import com.development.georgemcl.goaltracker.model.Goal;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Clean API to access Goal Data. Mediator between different data sources.
 */
public class GoalRepository {
    private GoalDao mGoalDao;
    private LiveData<List<Goal>> mAllGoals;
    //Goals that are shown on the main screen, which have no parentGoalId. They are not sub-goals.
    private LiveData<List<Goal>> mMainGoals;

    public GoalRepository(Application application) {
        GoalRoomDatabase db = GoalRoomDatabase.getDatabase(application);
        mGoalDao = db.goalDao();
        mAllGoals = mGoalDao.getAllGoals();
        mMainGoals = mGoalDao.getMainGoals();
    }

    public LiveData<List<Goal>> getAllGoals() {
        return mAllGoals;
    }

    public LiveData<List<Goal>> getMainGoals() { return mMainGoals;}

    public LiveData<List<Goal>> getSubGoals(int parentGoalId) {
        return mGoalDao.getSubGoals(parentGoalId);
    }

    public LiveData<Goal> getGoalById(int id) {
        return mGoalDao.getGoalById(id);
    }

    public Completable insert(Goal goal) {
        return Completable.fromAction(() -> mGoalDao.insert(goal))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable edit(Goal goal) {
        return Completable.fromAction(() -> mGoalDao.updateGoals(goal))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable delete(Goal goal) {
        return Completable.fromAction(() -> mGoalDao.deleteGoals(goal))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
