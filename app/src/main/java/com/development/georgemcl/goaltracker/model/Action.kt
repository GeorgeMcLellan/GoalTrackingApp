package com.development.georgemcl.goaltracker.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey

import java.io.Serializable

/**
 * Represents an action for specific goal.
 * Represents action_table in the Room Database
 * Properties represent columns in the Room Database
 * Data Transfer Object pattern
 */

@Entity(tableName = "action_table")
data class Action (
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "id")
        var id: Int = 0,

        @ColumnInfo(name = "actionName")
        var actionName: String = "",

        @ColumnInfo(name = "parentGoalId")
        var parentGoalId: Int = 0,

        @ColumnInfo(name = "repeatAction")
        var isRepeatAction: Boolean = false,

        @ColumnInfo(name = "repeatAmount")
        var repeatAmount: Int = 0,

        @ColumnInfo(name = "repeatProgressAmount")
        var repeatProgressAmount: Int = 0,

        @ColumnInfo(name = "repeatTimePeriod")
        var repeatTimePeriod: String = "",

        @ColumnInfo(name = "repeatUnitOfMeasurement")
        var repeatUnitOfMeasurement: String = ""
): Serializable