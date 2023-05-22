package com.github.tschalk.project_tracker.view;

import com.github.tschalk.project_tracker.controller.EditProjectController;
import com.github.tschalk.project_tracker.model.Project;
import com.github.tschalk.project_tracker.model.TimesheetEntry;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.util.Optional;

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
        this.setPadding(new Insets(10));

        timesheetEntryTableView = new TableView<>();

        TableColumn<TimesheetEntry, LocalDateTime> startDateTimeColumn = new TableColumn<>("Start Date/Time");
        startDateTimeColumn.setCellValueFactory(new PropertyValueFactory<>("startDateTime"));

        TableColumn<TimesheetEntry, Number> durationColumn = new TableColumn<>("Duration");
        durationColumn.setCellValueFactory(new PropertyValueFactory<>("duration"));

        timesheetEntryTableView.getColumns().addAll(startDateTimeColumn, durationColumn);

        Button removeDateTimeButton = new Button("Remove Time");
        removeDateTimeButton.setOnAction(event -> {
            TimesheetEntry selectedEntry = timesheetEntryTableView.getSelectionModel().getSelectedItem();
            if (selectedEntry != null) {
                timesheetEntryTableView.getItems().remove(selectedEntry);
                removeEntryFromDatabase(selectedEntry);
            }
        });

        Button removeProjectButton = new Button("Remove Project");
        removeProjectButton.setOnAction(event -> {
            if (selectedProject != null) {
                editProjectController.deleteProject(selectedProject);
                selectedProject = null;
                editProjectController.getMainWindowView().updateProjectTableView();
            }
        });

        Button editButton = new Button("Edit Duration");
        editButton.setOnAction(event -> {
            TimesheetEntry selectedEntry = timesheetEntryTableView.getSelectionModel().getSelectedItem();
            if (selectedEntry != null) {
                editDuration(selectedEntry);
                editProjectController.getMainWindowView().updateProjectTableView();
            }
        });


        this.getChildren().addAll(timesheetEntryTableView, removeDateTimeButton, removeProjectButton, editButton);
    }

    private void editDuration(TimesheetEntry entry) {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Edit Duration");

        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

        TextField textField = new TextField();
        textField.setText(String.valueOf(entry.getDuration()));
        dialog.getDialogPane().setContent(textField);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == okButtonType) {
                return textField.getText();
            }
            return null;
        });

        Optional<String> result = dialog.showAndWait();

        if (result.isPresent()) {
            int newDuration;
            try {
                newDuration = Integer.parseInt(result.get());
                entry.setDuration(newDuration);
                editProjectController.getTimesheetEntryDAO().updateTimesheetEntry(entry);
                timesheetEntryTableView.refresh();
            } catch (NumberFormatException e) {
                System.out.println("Invalid input.");
            }
        }
    }


    public void setSelectedProject(Project selectedProject) {
        this.selectedProject = selectedProject;
        ObservableList<TimesheetEntry> timesheetEntries = FXCollections.observableArrayList(editProjectController.getTimesheetEntriesForProject(selectedProject));

        timesheetEntryTableView.setItems(timesheetEntries);
    }
    private void removeEntryFromDatabase(TimesheetEntry selectedEntry) {
        editProjectController.removeTimesheetEntry(selectedEntry);
        editProjectController.getMainWindowView().updateProjectTableView();
    }


}
