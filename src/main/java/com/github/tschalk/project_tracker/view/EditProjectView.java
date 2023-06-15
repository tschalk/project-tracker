package com.github.tschalk.project_tracker.view;

import com.github.tschalk.project_tracker.controller.EditProjectController;
import com.github.tschalk.project_tracker.model.CostCenter;
import com.github.tschalk.project_tracker.model.Project;
import com.github.tschalk.project_tracker.model.Responsible;
import com.github.tschalk.project_tracker.model.TimesheetEntry;
import com.github.tschalk.project_tracker.util.AlertUtils;
import com.github.tschalk.project_tracker.util.CustomTitleBar;
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
    private Project selectedProject;
    private TableView<TimesheetEntry> timesheetEntryTableView;
    private final ComboBox<CostCenter> costCenterComboBox;
    private final ComboBox<Responsible> responsibleComboBox;

    public EditProjectView(EditProjectController editProjectController) {
        this.editProjectController = editProjectController;
        this.costCenterComboBox = new ComboBox<>();
        this.responsibleComboBox = new ComboBox<>();
        initializeUI();
    }

    private void initializeUI() {
        // This
        this.setPadding(new Insets(0, 0, 15, 0));

        // Top
//        CustomTitleBar customTitleBar = new CustomTitleBar("Edit");
//        customTitleBar.showCloseButton(false);
//        this.setTop(customTitleBar);

        // Center
        Label titleLabel = new Label("Export project");

        setTimesheetTableView();
        initializeCostCenterComboBox();

        Button updateDescriptionButton = getUpdateDescriptionButton();

        Button removeDateTimeButton = getremoveDateTimeButton();
        Button removeProjectButton = getRemoveProjectButton();
        Button editButton = getEditButton();

        HBox actionButtonContainer1 = new HBox(10);
        actionButtonContainer1.getChildren().addAll(updateDescriptionButton, costCenterComboBox, responsibleComboBox);
        actionButtonContainer1.getStyleClass().add("button-container");

        HBox actionButtonContainer2 = new HBox(10);
        actionButtonContainer2.getChildren().addAll(removeDateTimeButton, removeProjectButton, editButton);
        actionButtonContainer2.getStyleClass().add("button-container");

        VBox contentBox = new VBox(10);
        contentBox.setPadding(new Insets(10));
        contentBox.getChildren().addAll(titleLabel, timesheetEntryTableView, actionButtonContainer1, actionButtonContainer2);

        this.setCenter(contentBox);
    }

    @NotNull
    private Button getremoveDateTimeButton() {
        Button removeDateTimeButton = new Button("Remove Time");
        removeDateTimeButton.setOnAction(event -> {
            // Wenn das ausgewählte Element null ist, dann wird nichts gemacht
            if (timesheetEntryTableView.getSelectionModel().getSelectedItem() == null) {
                return;
            }

            Optional<ButtonType> result = AlertUtils.showAlert(
                    Alert.AlertType.CONFIRMATION,
                    "Remove Time",
                    "Are you sure you want to remove this time?",
                    "This action cannot be undone."
            );

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

    @NotNull
    private Button getRemoveProjectButton() {
        Button removeProjectButton = new Button("Remove Project");
        removeProjectButton.setOnAction(event -> {

            Optional<ButtonType> result = AlertUtils.showAlert(
                    Alert.AlertType.CONFIRMATION,
                    "Remove Project " + this.selectedProject.getDescription(),
                    "Are you sure you want to remove the  Project " + this.selectedProject.getDescription() + "?",
                    "This action cannot be undone."
            );

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
    private Button getUpdateDescriptionButton() {
        Button updateDescriptionButton = new Button("Update Description");
        updateDescriptionButton.setOnAction(event -> {
            String newDescription = showInputDialog("Update Description", "Please enter the new description:");
            if (newDescription != null && selectedProject != null) {
                selectedProject.setDescription(newDescription);
                editProjectController.updateProject(selectedProject);
                editProjectController.getMainWindowView().updateProjectTableView();
            }
        });
        return updateDescriptionButton;
    }

    private String showInputDialog(String title, String headerText) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(title);
        dialog.setHeaderText(headerText);
        Optional<String> result = dialog.showAndWait();

        return result.orElse(null);
    }

    /**
     * In dieser Methode wird für CostCenter eine Combobox erstellt, in der alle CostCenter angezeigt werden.
     */
    private void initializeCostCenterComboBox() {
        costCenterComboBox.setPromptText("Select Cost Center");
        costCenterComboBox.showingProperty().addListener((observable, wasShowing, isNowShowing) -> {
            if (isNowShowing) {
                costCenterComboBox.setItems(editProjectController.getCostCenters());
            }
        });

        costCenterComboBox.setOnAction(event -> {
            CostCenter selectedCostCenter = costCenterComboBox.getValue();
            if (selectedCostCenter != null && selectedProject != null) {
                selectedProject.setCostCenter(selectedCostCenter.getName());
                editProjectController.updateCostCenter(selectedProject, selectedCostCenter);
                editProjectController.getMainWindowView().updateProjectTableView();
            }
        });

        responsibleComboBox.setPromptText("Select Responsible");
        responsibleComboBox.showingProperty().addListener((observable, wasShowing, isNowShowing) -> {
            if (isNowShowing) {
                responsibleComboBox.setItems(editProjectController.getResponsibles());
            }
        });

        responsibleComboBox.setOnAction(event -> {
            Responsible selectedResponsible = responsibleComboBox.getValue();
            if (selectedResponsible != null && selectedProject != null) {
                selectedProject.setResponsible(selectedResponsible.getName());
                editProjectController.updateResponsible(selectedProject, selectedResponsible);
                editProjectController.getMainWindowView().updateProjectTableView();
            }
        });

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
