package com.github.tschalk.project_tracker.model;

public class Project {

    // benötige ich immeer beides Resposnible und ResponsibleId??
    private int id;
    private String description;
    private String responsible;
    private String costCenter;
    private int duration;

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getResponsible() {
        return this.responsible;
    }

    public void setResponsible(String responsible) {
        this.responsible = responsible;
    }

    public String getCostCenter() {
        return this.costCenter;
    }

    public void setCostCenter(String costCenter) {
        this.costCenter = costCenter;
    }

    public int getDuration() {
        return this.duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "Project{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", responsible='" + responsible + '\'' +
                ", costCenter='" + costCenter + '\'' +
                ", duration=" + duration +
                '}';
    }
}
