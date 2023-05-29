package com.github.tschalk.project_tracker.view;

import com.github.tschalk.project_tracker.controller.EditProjectController;
import com.github.tschalk.project_tracker.model.Project;
import com.github.tschalk.project_tracker.model.TimesheetEntry;
import com.github.tschalk.project_tracker.utils.CustomTitleBar;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.Optional;

public class EditProjectView extends BorderPane {

    private final EditProjectController editProjectController;
    private Stage stage;
    private Project selectedProject;
    private TableView<TimesheetEntry> timesheetEntryTableView;

    public EditProjectView(EditProjectController editProjectController) {
        this.editProjectController = editProjectController;
        initializeUI();
    }

    private void initializeUI() {

        // This
        this.setPadding(new Insets(0, 0, 15, 0));

        // Top
        CustomTitleBar customTitleBar = new CustomTitleBar("Edit Project");
        customTitleBar.showCloseButton(false);
        this.setTop(customTitleBar);

        // Center
        Label titleLabel = new Label("Edit project");

        setTimesheetTableView();

        Button removeDateTimeButton = getremoveDateTimeButton();
        Button removeProjectButton = getRemoveProjectButton();
        Button editButton = getEditButton();

        HBox actionButtonContainer = new HBox(10);
        actionButtonContainer.getChildren().addAll(removeDateTimeButton, removeProjectButton, editButton);
        actionButtonContainer.getStyleClass().add("button-container");

        VBox contentBox = new VBox(10);
        contentBox.setPadding(new Insets(10));
        contentBox.getChildren().addAll(titleLabel, timesheetEntryTableView, actionButtonContainer);

        this.setCenter(contentBox);
    }

    @NotNull
    private Button getEditButton() {
        Button editButton = new Button("Edit Duration");
        editButton.setOnAction(event -> {
            TimesheetEntry selectedEntry = timesheetEntryTableView.getSelectionModel().getSelectedItem();
            if (selectedEntry != null) {
                editDuration(selectedEntry);
                editProjectController.getMainWindowView().updateProjectTableView();
            }
        });
        return editButton;
    }

    @NotNull
    private Button getRemoveProjectButton() {
        Button removeProjectButton = new Button("Remove Project");
        removeProjectButton.setOnAction(event -> {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Remove Project " + this.selectedProject.getDescription());
            alert.setHeaderText("Are you sure you want to remove the  Project " + this.selectedProject.getDescription() + "?");
            alert.setContentText("This action cannot be undone.");
            alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.NO) {
                return;
            }

            if (selectedProject != null) {
                editProjectController.deleteProject(selectedProject);
                selectedProject = null;
                editProjectController.getMainWindowView().updateProjectTableView();

                Stage stage = (Stage) this.getScene().getWindow();
                stage.close();
            }
        });
        return removeProjectButton;
    }

    @NotNull
    private Button getremoveDateTimeButton() {
        Button removeDateTimeButton = new Button("Remove Time");
        removeDateTimeButton.setOnAction(event -> {
            // Wenn das ausgewählte Element null ist, dann wird nichts gemacht
            if (timesheetEntryTableView.getSelectionModel().getSelectedItem() == null) {
                return;
            }
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Remove Time");
            alert.setHeaderText("Are you sure you want to remove this time?");
            alert.setContentText("This action cannot be undone.");
            alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.NO) {
                return;
            }

            TimesheetEntry selectedEntry = timesheetEntryTableView.getSelectionModel().getSelectedItem();
            if (selectedEntry != null) {
                timesheetEntryTableView.getItems().remove(selectedEntry);
                removeEntryFromDatabase(selectedEntry);
            }
        });
        return removeDateTimeButton;
    }

    private void setTimesheetTableView() {
        timesheetEntryTableView = new TableView<>();

        TableColumn<TimesheetEntry, LocalDateTime> startDateTimeColumn = new TableColumn<>("Start Date/Time");
        startDateTimeColumn.setCellValueFactory(new PropertyValueFactory<>("startDateTime"));

        TableColumn<TimesheetEntry, Number> durationColumn = new TableColumn<>("Duration");
        durationColumn.setCellValueFactory(new PropertyValueFactory<>("duration"));

        timesheetEntryTableView.getColumns().addAll(startDateTimeColumn, durationColumn);
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
