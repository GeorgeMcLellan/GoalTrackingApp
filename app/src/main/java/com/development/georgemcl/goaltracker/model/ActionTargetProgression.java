package com.development.georgemcl.goaltracker.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.io.Serializable;

@Entity(tableName = "action__target_progression_table")
public class ActionTargetProgression implements Serializable{

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "actionId")
    private int actionId;


    @ColumnInfo(name = "repeatTargetAmount")
    private int repeatTargetAmount;

    @ColumnInfo (name = "repeatTargetProgress")
    private int repeatTargetProgress;

    /**
     * The last day that this applies. If for a daily repeat action, then this is the current day
     */
    @ColumnInfo(name = "repeatTargetFinalDay")
    private String repeatTargetFinalDay;

    public ActionTargetProgression(@NonNull int id, int actionId, int repeatTargetAmount, int repeatTargetProgress, String repeatTargetFinalDay) {
        this.id = id;
        this.actionId = actionId;
        this.repeatTargetAmount = repeatTargetAmount;
        this.repeatTargetProgress = repeatTargetProgress;
        this.repeatTargetFinalDay = repeatTargetFinalDay;
    }

    @NonNull
    public int getId() {
        return id;
    }

    public void setId(@NonNull int id) {
        this.id = id;
    }

    public int getActionId() {
        return actionId;
    }

    public void setActionId(int actionId) {
        this.actionId = actionId;
    }

    public int getRepeatTargetAmount() {
        return repeatTargetAmount;
    }

    public void setRepeatTargetAmount(int repeatTargetAmount) {
        this.repeatTargetAmount = repeatTargetAmount;
    }

    public int getRepeatTargetProgress() {
        return repeatTargetProgress;
    }

    public void setRepeatTargetProgress(int repeatTargetProgress) {
        this.repeatTargetProgress = repeatTargetProgress;
    }

    public String getRepeatTargetFinalDay() {
        return repeatTargetFinalDay;
    }

    public void setRepeatTargetFinalDay(String repeatTargetFinalDay) {
        this.repeatTargetFinalDay = repeatTargetFinalDay;
    }

    @Override
    public String toString() {
        return "ActionTargetProgression{" +
                "id=" + id +
                ", actionId=" + actionId +
                ", repeatTargetAmount=" + repeatTargetAmount +
                ", repeatTargetProgress=" + repeatTargetProgress +
                ", repeatTargetFinalDay='" + repeatTargetFinalDay + '\'' +
                '}';
    }
}
