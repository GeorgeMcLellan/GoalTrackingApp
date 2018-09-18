package com.development.georgemcl.goaltracker.model;

public class Goal {
    private String id;
    private String goalName;
    private String description;
    private String completionDate;

    /**
     * ParentGoalId is null if the goal has no parent.
     * Otherwise, it links to the parent of which it is a sub-goal
     */
    private String parentGoalId;

    public Goal(String goalName, String completionDate) {
        this.goalName = goalName;
        this.completionDate = completionDate;
    }
    public Goal(String id, String goalName, String completionDate) {
        this.id = id;
        this.goalName = goalName;
        this.completionDate = completionDate;
    }

    public Goal(String goalName, String description, String completionDate, String parentGoalId) {
        this.id = "1234";
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getParentGoalId() {
        return parentGoalId;
    }

    public void setParentGoalId(String parentGoalId) {
        this.parentGoalId = parentGoalId;
    }

    @Override
    public String toString() {
        return "Goal{" +
                "id='" + id + '\'' +
                ", goalName='" + goalName + '\'' +
                ", completionDate='" + completionDate + '\'' +
                ", parentGoalId='" + parentGoalId + '\'' +
                '}';
    }
}
