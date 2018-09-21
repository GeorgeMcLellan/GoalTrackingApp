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

    public GoalRepository(Application application) {
        GoalRoomDatabase db = GoalRoomDatabase.getDatabase(application);
        mGoalDao = db.goalDao();
        mAllGoals = mGoalDao.getAllGoals();
    }

    public LiveData<List<Goal>> getAllGoals() {
        return mAllGoals;
    }

    public void insert(Goal goal) {
        new insertAsyncTask(mGoalDao).execute(goal);
    }

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
}
