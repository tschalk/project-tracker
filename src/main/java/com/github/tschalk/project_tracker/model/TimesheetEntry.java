package com.github.tschalk.project_tracker.model;

import java.time.LocalDate;

public class TimesheetEntry {
    int id;
    int projectId;
    LocalDate date;
    int duration;
}
