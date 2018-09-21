package com.development.georgemcl.goaltracker.data.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.development.georgemcl.goaltracker.model.Action;

import java.util.List;

@Dao
public interface ActionDao {

    @Insert
    void insert(Action action);

    @Query("SELECT * FROM action_table")
    LiveData<List<Action>> getAllActions();

    @Update
    void updateActions(Action... actions);

    @Delete
    void deleteActions(Action... actions);

    @Query("DELETE FROM action_table")
    void deleteAll();

    @Query("SELECT * FROM action_table WHERE parentGoalId IS :parentGoalId")
    LiveData<List<Action>> getActions(int parentGoalId);
}
