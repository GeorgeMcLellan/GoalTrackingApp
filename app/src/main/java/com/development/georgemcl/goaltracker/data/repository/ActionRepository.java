package com.development.georgemcl.goaltracker.data.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;

import com.development.georgemcl.goaltracker.data.db.ActionDao;
import com.development.georgemcl.goaltracker.data.db.GoalRoomDatabase;
import com.development.georgemcl.goaltracker.model.Action;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * Clean API to access Action Data. Mediator between different data sources
 */
public class ActionRepository {
    private ActionDao mActionDao;
    private LiveData<List<Action>> mAllActions;
    private static final String TAG = "ActionRepository";

    public ActionRepository(Application application) {
        GoalRoomDatabase db = GoalRoomDatabase.getDatabase(application);
        mActionDao = db.actionDao();
        mAllActions = mActionDao.getAllActions();
    }

    public LiveData<List<Action>> getAllActions() {
        return mAllActions;
    }

    /**
     * Get actions for a relevent goal
     * @param parentGoalId the parent goal
     * @return the actions for that goal
     */
    public LiveData<List<Action>> getActions(int parentGoalId) {
        return mActionDao.getActions(parentGoalId);
    }

    public LiveData<List<Action>> getActionsByRepeatTimePeriod(String repeatTimePeriod) {
        return mActionDao.getActionsByRepeatTimePeriod(repeatTimePeriod);
    }

    public Completable insert(final Action action) {
        return Completable.fromAction(() -> mActionDao.insert(action))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable edit(final Action action) {
        return Completable.fromAction(() -> mActionDao.updateActions(action))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable delete(final Action action) {
        return Completable.fromAction(() -> mActionDao.deleteActions(action))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
