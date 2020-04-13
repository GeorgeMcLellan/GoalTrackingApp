package com.development.georgemcl.goaltracker.data.repository

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import com.development.georgemcl.goaltracker.data.db.ActionDao
import com.development.georgemcl.goaltracker.data.db.GoalRoomDatabase
import com.development.georgemcl.goaltracker.model.Action
import com.development.georgemcl.goaltracker.utils.isProgressCompleted
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Clean API to access Action Data. Mediator between different data sources
 */
class ActionRepository(application: Application?) {
    private val mActionDao: ActionDao
    val allActions: LiveData<List<Action>>

    init {
        val db = GoalRoomDatabase.getDatabase(application)
        mActionDao = db.actionDao()
        allActions = mActionDao.allActions
    }
    /**
     * Get actions for a relevent goal
     *
     * @param parentGoalId the parent goal
     * @return the actions for that goal
     */
    fun getActions(parentGoalId: Int): LiveData<List<Action>> {
        return mActionDao.getActions(parentGoalId)
    }

    fun getActionsByRepeatTimePeriod(repeatTimePeriod: String?): LiveData<List<Action>> {
        return mActionDao.getActionsByRepeatTimePeriod(repeatTimePeriod)
    }

    fun insert(action: Action?): Completable {
        return Completable.fromAction { mActionDao.insert(action) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun edit(action: Action?): Completable {
        return mActionDao.updateAction(action)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun delete(action: Action?): Completable {
        return mActionDao.deleteAction(action)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun resetAllDailyActionProgress(): Single<Int> {
        return mActionDao.getRxActionsByRepeatTimePeriod("per day")
                .subscribeOn(Schedulers.io())
                .flatMap { actions ->
                    val numberOfCompletedActions = actions.filter { it.isProgressCompleted() }.size
                    actions.map { it.repeatProgressAmount = 0 }
                    return@flatMap mActionDao.updateActions(actions).toSingleDefault(numberOfCompletedActions)
                }
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun resetAllWeeklyActionProgress(): Single<Int> {
        return mActionDao.getRxActionsByRepeatTimePeriod("per week")
                .subscribeOn(Schedulers.io())
                .flatMap { actions ->
                    val numberOfCompletedActions = actions.filter { it.isProgressCompleted() }.size
                    actions.map { it.repeatProgressAmount = 0 }
                    return@flatMap mActionDao.updateActions(actions).toSingleDefault(numberOfCompletedActions)
                }
    }

    fun resetAllMonthlyActionProgress(): Single<Int> {
        return mActionDao.getRxActionsByRepeatTimePeriod("per month")
                .subscribeOn(Schedulers.io())
                .flatMap { actions ->
                    val numberOfCompletedActions = actions.filter { it.isProgressCompleted() }.size
                    actions.map { it.repeatProgressAmount = 0 }
                    return@flatMap mActionDao.updateActions(actions).toSingleDefault(numberOfCompletedActions)
                }
    }

    companion object {
        private const val TAG = "ActionRepository"
    }
}