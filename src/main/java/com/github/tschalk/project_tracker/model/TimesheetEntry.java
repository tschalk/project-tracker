package com.github.tschalk.project_tracker.model;

import java.time.LocalDateTime;

public class TimesheetEntry {
    private int id;
    private int projectId;
    private LocalDateTime startDateTime;
    private int duration;

    // getters and setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "TimesheetEntry{" +
                "id=" + id +
                ", projectId=" + projectId +
                ", startDateTime=" + startDateTime +
                ", duration=" + duration +
                '}';
    }
}
