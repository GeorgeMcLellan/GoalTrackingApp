package com.development.georgemcl.goaltracker.data.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.development.georgemcl.goaltracker.model.Goal;

import java.util.List;

@Dao
public interface GoalDao {

    @Insert
    void insert(Goal goal);

    @Query("SELECT * FROM goal_table")
    LiveData<List<Goal>> getAllGoals();

    @Update
    void updateGoals(Goal... goals);

    @Delete
    void deleteGoals(Goal... goals);

    @Query("DELETE FROM goal_table")
    void deleteAll();

    @Query("SELECT * FROM goal_table WHERE parentGoalId IS 0")
    LiveData<List<Goal>> getMainGoals();

    @Query("SELECT * FROM goal_table WHERE parentGoalId IS :parentGoalId")
    LiveData<List<Goal>> getSubGoals(int parentGoalId);
}
