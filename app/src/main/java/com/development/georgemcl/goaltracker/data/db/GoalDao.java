package com.development.georgemcl.goaltracker.data.db;

import androidx.lifecycle.LiveData;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.development.georgemcl.goaltracker.model.Goal;

import java.util.List;

/**
 * Data Access Object pattern for Room. Provides methods to be performed on the Goal table
 */
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

    @Query("SELECT * FROM goal_table WHERE parentGoalId IS -1")
    LiveData<List<Goal>> getMainGoals();

    @Query("SELECT * FROM goal_table WHERE parentGoalId IS :parentGoalId")
    LiveData<List<Goal>> getSubGoals(int parentGoalId);

    @Query("SELECT * FROM goal_table WHERE id IS :id")
    LiveData<Goal> getGoalById(int id);
}
