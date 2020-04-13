package com.development.georgemcl.goaltracker.data.db;

import androidx.lifecycle.LiveData;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.development.georgemcl.goaltracker.model.Action;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

/**
 * Data Access Object pattern for Room. Provides methods to be performed on the Action table
 */
@Dao
public interface ActionDao {

    @Insert
    void insert(Action action);

    @Query("SELECT * FROM action_table")
    LiveData<List<Action>> getAllActions();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable updateActions(List<Action> actions);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable updateAction(Action action);

    @Delete
    void deleteActions(Action... actions);

    @Delete
    Completable deleteAction(Action action);


    @Query("SELECT * FROM action_table WHERE parentGoalId IS :parentGoalId")
    LiveData<List<Action>> getActions(int parentGoalId);

    @Query("SELECT * FROM action_table WHERE repeatTimePeriod IS :repeatTimePeriod")
    LiveData<List<Action>> getActionsByRepeatTimePeriod(String repeatTimePeriod);

    @Query("SELECT * FROM action_table WHERE repeatTimePeriod IS :repeatTimePeriod")
    Single<List<Action>> getRxActionsByRepeatTimePeriod(String repeatTimePeriod);
}
