package com.github.tschalk.project_tracker.view;

import com.github.tschalk.project_tracker.controller.EditProjectController;
import com.github.tschalk.project_tracker.model.Project;
import com.github.tschalk.project_tracker.model.TimesheetEntry;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDateTime;

public class EditProjectView extends VBox {

    EditProjectController editProjectController;
    Stage stage;
    Project selectedProject;
    private TableView<TimesheetEntry> timesheetEntryTableView;

    public EditProjectView(EditProjectController editProjectController, Stage stage) {
        this.editProjectController = editProjectController;
        this.stage = stage;
        initUI();
//        System.out.println("EditProjectView: " +   selectedProject.getDescription());
    }


    private void initUI() {
        this.setSpacing(10);
        this.setPadding(new Insets(10));;

        timesheetEntryTableView = new TableView<>();

        TableColumn<TimesheetEntry, LocalDateTime> startDateTimeColumn = new TableColumn<>("Start Date/Time");
        startDateTimeColumn.setCellValueFactory(new PropertyValueFactory<>("startDateTime"));


        TableColumn<TimesheetEntry, Number> durationColumn = new TableColumn<>("Duration");
        durationColumn.setCellValueFactory(new PropertyValueFactory<>("duration"));

        timesheetEntryTableView.getColumns().addAll(startDateTimeColumn, durationColumn);

        this.getChildren().add(timesheetEntryTableView);
    }

    public void setSelectedProject(Project selectedProject) {
        this.selectedProject = selectedProject;
        ObservableList<TimesheetEntry> timesheetEntries = FXCollections.observableArrayList(editProjectController.getTimesheetEntriesForProject(selectedProject));

        timesheetEntryTableView.setItems(timesheetEntries);
    }
}
