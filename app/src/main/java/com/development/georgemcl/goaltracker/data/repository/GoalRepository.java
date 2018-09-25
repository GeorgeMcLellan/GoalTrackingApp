package com.development.georgemcl.goaltracker.data.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.development.georgemcl.goaltracker.data.db.GoalDao;
import com.development.georgemcl.goaltracker.data.db.GoalRoomDatabase;
import com.development.georgemcl.goaltracker.model.Goal;

import java.util.List;

public class GoalRepository {
    private GoalDao mGoalDao;
    private LiveData<List<Goal>> mAllGoals;
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

    public void insert(Goal goal) {
        new insertAsyncTask(mGoalDao).execute(goal);
    }

    public void edit(Goal goal) { new EditAsyncTask(mGoalDao).execute(goal);}


    private static class insertAsyncTask extends AsyncTask<Goal, Void, Void> {

        private GoalDao mAsyncTaskDao;

        insertAsyncTask(GoalDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Goal... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class EditAsyncTask extends AsyncTask<Goal, Void, Void> {

        private GoalDao mAsyncTaskDao;

        EditAsyncTask(GoalDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Goal... params) {
            mAsyncTaskDao.updateGoals(params[0]);
            return null;
        }
    }
}
