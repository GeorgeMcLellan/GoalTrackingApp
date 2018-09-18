package com.development.georgemcl.goaltracker.model;

/**
 * Created by george on 18/09/18.
 */

public class ActionTarget {
    private String id;
    private int repeatAmount;
    private String timePeriod;
    private String unitOfMeasurement;
    private String actionId;

    public ActionTarget(String id, int repeatAmount, String timePeriod, String unitOfMeasurement, String actionId) {
        this.id = id;
        this.repeatAmount = repeatAmount;
        this.timePeriod = timePeriod;
        this.unitOfMeasurement = unitOfMeasurement;
        this.actionId = actionId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTimePeriod() {
        return timePeriod;
    }

    public void setTimePeriod(String timePeriod) {
        this.timePeriod = timePeriod;
    }

    public String getUnitOfMeasurement() {
        return unitOfMeasurement;
    }

    public void setUnitOfMeasurement(String unitOfMeasurement) {
        this.unitOfMeasurement = unitOfMeasurement;
    }

    public int getRepeatAmount() {
        return repeatAmount;
    }

    public void setRepeatAmount(int repeatAmount) {
        this.repeatAmount = repeatAmount;
    }

    public String getActionId() {
        return actionId;
    }

    public void setActionId(String actionId) {
        this.actionId = actionId;
    }

    @Override
    public String toString() {
        return "ActionTarget{" +
                "id='" + id + '\'' +
                ", repeatAmount=" + repeatAmount +
                ", timePeriod='" + timePeriod + '\'' +
                ", unitOfMeasurement='" + unitOfMeasurement + '\'' +
                ", actionId='" + actionId + '\'' +
                '}';
    }

}
