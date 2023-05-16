package com.github.tschalk.project_tracker.model;

public class Project {
    int id;
    String name;
    int userId;
    int responsibleUserId;
    int costCenterId;
    int workHours;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getResponsibleUserId() {
        return responsibleUserId;
    }

    public void setResponsibleUserId(int responsibleUserId) {
        this.responsibleUserId = responsibleUserId;
    }

    public int getCostCenterId() {
        return costCenterId;
    }

    public void setCostCenterId(int costCenterId) {
        this.costCenterId = costCenterId;
    }

    public int getWorkHours() {
        return workHours;
    }

    public void setWorkHours(int workHours) {
        this.workHours = workHours;
    }
}
