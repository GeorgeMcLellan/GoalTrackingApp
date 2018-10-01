package com.development.georgemcl.goaltracker.data.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.development.georgemcl.goaltracker.data.db.ActionDao;
import com.development.georgemcl.goaltracker.data.db.ActionTargetProgressionDao;
import com.development.georgemcl.goaltracker.data.db.GoalRoomDatabase;
import com.development.georgemcl.goaltracker.model.Action;
import com.development.georgemcl.goaltracker.model.ActionTargetProgression;

import java.util.List;

public class ActionTargetProgressionRepository {
    private ActionTargetProgressionDao mDao;
    private LiveData<List<ActionTargetProgression>> mActionTargetProgressions;

    public ActionTargetProgressionRepository(Application application) {
        GoalRoomDatabase db = GoalRoomDatabase.getDatabase(application);
        mDao = db.actionTargetProgressionDao();
        mActionTargetProgressions = mDao.getAllActionTargetProgressions();
    }

    public LiveData<List<ActionTargetProgression>> getActionTargetProgressions() {
        return mActionTargetProgressions;
    }


    public LiveData<List<ActionTargetProgression>> getActionTargetProgressions(int actionId) {
        return mDao.getTargetProgressionsForAction(actionId);
    }

    public LiveData<ActionTargetProgression> getRelevantTargetProgression(int actionId, String finalDay) {
        return mDao.getTargetProgressionForActionByFinalDay(actionId, finalDay);
    }

    public void insert(ActionTargetProgression actionTargetProgression) {
        new InsertAsyncTask(mDao).execute(actionTargetProgression);
    }

    public void edit(ActionTargetProgression actionTargetProgression) { new EditAsyncTask(mDao).execute(actionTargetProgression);}

    private static class InsertAsyncTask extends AsyncTask<ActionTargetProgression, Void, Void> {

        private ActionTargetProgressionDao mAsyncTaskDao;

        InsertAsyncTask(ActionTargetProgressionDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final ActionTargetProgression... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class EditAsyncTask extends AsyncTask<ActionTargetProgression, Void, Void> {

        private ActionTargetProgressionDao mAsyncTaskDao;

        EditAsyncTask(ActionTargetProgressionDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final ActionTargetProgression... params) {
            mAsyncTaskDao.updateTargetProgression(params[0]);
            return null;
        }
    }
}
