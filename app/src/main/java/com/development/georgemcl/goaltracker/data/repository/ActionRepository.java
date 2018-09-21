package com.development.georgemcl.goaltracker.data.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.development.georgemcl.goaltracker.data.db.ActionDao;
import com.development.georgemcl.goaltracker.data.db.GoalRoomDatabase;
import com.development.georgemcl.goaltracker.model.Action;

import java.util.List;

public class ActionRepository {
    private ActionDao mActionDao;
    private LiveData<List<Action>> mAllActions;

    public ActionRepository(Application application) {
        GoalRoomDatabase db = GoalRoomDatabase.getDatabase(application);
        mActionDao = db.actionDao();
        mAllActions = mActionDao.getAllActions();
    }

    public LiveData<List<Action>> getAllActions() {
        return mAllActions;
    }


    public LiveData<List<Action>> getActions(int parentGoalId) {
        return mActionDao.getActions(parentGoalId);
    }

    public void insert(Action action) {
        new insertAsyncTask(mActionDao).execute(action);
    }

    private static class insertAsyncTask extends AsyncTask<Action, Void, Void> {

        private ActionDao mAsyncTaskDao;

        insertAsyncTask(ActionDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Action... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }
}
