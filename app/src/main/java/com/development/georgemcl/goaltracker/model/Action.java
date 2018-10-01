package com.development.georgemcl.goaltracker.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.io.Serializable;

@Entity(tableName = "action_table")
public class Action implements Serializable{

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "actionName")
    private String actionName;

    @ColumnInfo(name = "parentGoalId")
    private int parentGoalId;

    @ColumnInfo(name = "repeatAction")
    private boolean repeatAction;

    @ColumnInfo(name = "repeatAmount")
    private int repeatAmount;

    @ColumnInfo(name = "repeatProgressAmount")
    private int repeatProgressAmount;

    @ColumnInfo(name = "repeatTimePeriod")
    private String repeatTimePeriod;

    @ColumnInfo(name = "repeatUnitOfMeasurement")
    private String repeatUnitOfMeasurement;


    @Ignore
    public Action(String actionName, int parentGoalId) {
        this.actionName = actionName;
        this.parentGoalId = parentGoalId;
        this.repeatAction = false;
    }

    public Action(String actionName, int parentGoalId, int repeatAmount, String repeatTimePeriod, String repeatUnitOfMeasurement) {
        this.actionName = actionName;
        this.parentGoalId = parentGoalId;
        this.repeatAction = true;
        this.repeatAmount = repeatAmount;
        this.repeatProgressAmount = 0;
        this.repeatTimePeriod = repeatTimePeriod;
        this.repeatUnitOfMeasurement = repeatUnitOfMeasurement;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public int getParentGoalId() {
        return parentGoalId;
    }

    public void setParentGoalId(int parentGoalId) {
        this.parentGoalId = parentGoalId;
    }

    public boolean isRepeatAction() {
        return repeatAction;
    }

    public void setRepeatAction(boolean repeatAction) {
        this.repeatAction = repeatAction;
    }

    public int getRepeatAmount() {
        return repeatAmount;
    }

    public void setRepeatAmount(int repeatAmount) {
        this.repeatAmount = repeatAmount;
    }

    public int getRepeatProgressAmount() {
        return repeatProgressAmount;
    }

    public void setRepeatProgressAmount(int repeatProgressAmount) {
        this.repeatProgressAmount = repeatProgressAmount;
    }

    public String getRepeatTimePeriod() {
        return repeatTimePeriod;
    }

    public void setRepeatTimePeriod(String repeatTimePeriod) {
        this.repeatTimePeriod = repeatTimePeriod;
    }

    public String getRepeatUnitOfMeasurement() {
        return repeatUnitOfMeasurement;
    }

    public void setRepeatUnitOfMeasurement(String repeatUnitOfMeasurement) {
        this.repeatUnitOfMeasurement = repeatUnitOfMeasurement;
    }


    @Override
    public String toString() {
        return "Action{" +
                "id=" + id +
                ", actionName='" + actionName + '\'' +
                ", parentGoalId=" + parentGoalId +
                ", repeatAction=" + repeatAction +
                ", repeatAmount=" + repeatAmount +
                ", repeatProgressAmount=" + repeatProgressAmount +
                ", repeatTimePeriod='" + repeatTimePeriod + '\'' +
                ", repeatUnitOfMeasurement='" + repeatUnitOfMeasurement + '\'' +
                '}';
    }
}
