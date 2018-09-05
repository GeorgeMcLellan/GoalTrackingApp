package com.development.georgemcl.goaltracker.model;

public class Goal {
    private String goalName;
    private String completionDate;

    public Goal(String goalName, String completionDate) {
        this.goalName = goalName;
        this.completionDate = completionDate;
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

    @Override
    public String toString() {
        return "Goal{" +
                "goalName='" + goalName + '\'' +
                ", completionDate='" + completionDate + '\'' +
                '}';
    }
}
