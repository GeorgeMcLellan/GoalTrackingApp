package com.development.georgemcl.goaltracker.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * Represents goal_table in the Room Database
 * Properties represent columns in the Room Database
 * Data Transfer Object pattern
 */
@Entity(tableName = "goal_table")
public class Goal implements Serializable{

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "id")
    private int id;

    @NonNull
    @ColumnInfo(name = "goalName")
    private String goalName;

    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "completionDate")
    private String completionDate;

    /**
     * ParentGoalId is null if the goal has no parent.
     * Otherwise, it links to the parent of which it is a sub-goal
     */
    @ColumnInfo(name = "parentGoalId")
    private int parentGoalId;


    public Goal(String goalName, String description, String completionDate, int parentGoalId) {
        this.goalName = goalName;
        this.description = description;
        this.completionDate = completionDate;
        this.parentGoalId = parentGoalId;
    }

    public String getGoalName() {
        return goalName;
    }

    public void setGoalName(String goalName) {
        this.goalName = goalName;
    }

    public String getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(String completionDate) {
        this.completionDate = completionDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getParentGoalId() {
        return parentGoalId;
    }

    public void setParentGoalId(int parentGoalId) {
        this.parentGoalId = parentGoalId;
    }

    @Override
    public String toString() {
        return "Goal{" +
                "id='" + id + '\'' +
                ", goalName='" + goalName + '\'' +
                ", description='" + description + '\'' +
                ", completionDate='" + completionDate + '\'' +
                ", parentGoalId='" + parentGoalId + '\'' +
                '}';
    }
}
