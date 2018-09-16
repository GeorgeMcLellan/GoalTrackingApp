package com.development.georgemcl.goaltracker.model;

public class Action {
    private String id;
    private String actionName;
    private String parentGoalId;
    private boolean repeated;

    public Action(String id, String actionName, String parentGoalId, boolean repeated) {
        this.id = id;
        this.actionName = actionName;
        this.parentGoalId = parentGoalId;
        this.repeated = repeated;
    }

    public Action(String actionName) {
        this.actionName = actionName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public String getParentGoalId() {
        return parentGoalId;
    }

    public void setParentGoalId(String parentGoalId) {
        this.parentGoalId = parentGoalId;
    }

    @Override
    public String toString() {
        return "Action{" +
                "id='" + id + '\'' +
                ", actionName='" + actionName + '\'' +
                ", parentGoalId='" + parentGoalId + '\'' +
                '}';
    }
}
