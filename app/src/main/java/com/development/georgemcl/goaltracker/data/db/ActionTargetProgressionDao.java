package com.development.georgemcl.goaltracker.data.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;


import com.development.georgemcl.goaltracker.model.ActionTargetProgression;

import java.util.List;

@Dao
public interface ActionTargetProgressionDao {
    @Insert
    void insert(ActionTargetProgression actionTargetProgression);

    @Query("SELECT * FROM action__target_progression_table")
    LiveData<List<ActionTargetProgression>> getAllActionTargetProgressions();

    @Update
    void updateTargetProgression(ActionTargetProgression... actionTargetProgressions);

    @Delete
    void deleteTargetProgression(ActionTargetProgression... actionTargetProgressions);

    @Query("DELETE FROM action__target_progression_table")
    void deleteAll();

    @Query("SELECT * FROM action__target_progression_table WHERE actionId IS :actionId")
    LiveData<List<ActionTargetProgression>> getTargetProgressionsForAction(int actionId);

    @Query("SELECT * FROM action__target_progression_table WHERE actionId IS :actionId AND repeatTargetFinalDay IS :finalDay")
    LiveData<ActionTargetProgression> getTargetProgressionForActionByFinalDay(int actionId, String finalDay);
}
