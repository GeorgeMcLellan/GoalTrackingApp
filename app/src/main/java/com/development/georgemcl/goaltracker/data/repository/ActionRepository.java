package com.development.georgemcl.goaltracker.data.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.development.georgemcl.goaltracker.data.db.ActionDao;
import com.development.georgemcl.goaltracker.data.db.GoalRoomDatabase;
import com.development.georgemcl.goaltracker.model.Action;

import java.util.List;

/**
 * Clean API to access Action Data. Mediator between different data sources
 */
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

    public LiveData<List<Action>> getActionsByRepeatTimePeriod(String repeatTimePeriod) {
        return mActionDao.getActionsByRepeatTimePeriod(repeatTimePeriod);
    }

    public void insert(Action action) {
        new InsertAsyncTask(mActionDao).execute(action);
    }

    public void edit(Action action) { new EditAsyncTask(mActionDao).execute(action);}

    public void delete(Action action) { new DeleteAsyncTask(mActionDao).execute(action);}

    private static class InsertAsyncTask extends AsyncTask<Action, Void, Void> {

        private ActionDao mAsyncTaskDao;

        InsertAsyncTask(ActionDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Action... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class EditAsyncTask extends AsyncTask<Action, Void, Void> {

        private ActionDao mAsyncTaskDao;

        EditAsyncTask(ActionDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Action... params) {
            mAsyncTaskDao.updateActions(params[0]);
            return null;
        }
    }

    private static class DeleteAsyncTask extends AsyncTask<Action, Void, Void> {

        private ActionDao mAsyncTaskDao;

        DeleteAsyncTask(ActionDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Action... params) {
            mAsyncTaskDao.deleteActions(params[0]);
            return null;
        }
    }
}
