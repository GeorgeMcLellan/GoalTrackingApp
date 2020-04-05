package com.development.georgemcl.goaltracker.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey

import java.io.Serializable

/**
 * Represents goal_table in the Room Database
 * Properties represent columns in the Room Database
 * Data Transfer Object pattern
 */
@Entity(tableName = "goal_table")
data class Goal (
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "id")
        var id: Int = 0,
        @ColumnInfo(name = "goalName")
        var goalName: String = "",
        @ColumnInfo(name = "description")
        var description: String = "",
        @ColumnInfo(name = "completionDate")
        var completionDate: String = "",
        /**
         * ParentGoalId is -1 if the goal has no parent.
         * Otherwise, it links to the parent of which it is a sub-goal
         */
        @ColumnInfo(name = "parentGoalId")
        var parentGoalId: Int = -1
) : Serializable
