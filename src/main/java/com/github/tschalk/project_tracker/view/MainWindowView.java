package com.github.tschalk.project_tracker.view;

import com.github.tschalk.project_tracker.controller.MainWindowController;
import com.github.tschalk.project_tracker.controller.StopwatchState;
import com.github.tschalk.project_tracker.database.DatabaseBackupManager;
import com.github.tschalk.project_tracker.model.Project;
import com.github.tschalk.project_tracker.util.CustomTitleBar;
import com.github.tschalk.project_tracker.util.SVGManager;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.util.Optional;

import static com.github.tschalk.project_tracker.util.AlertUtils.showAlert;
import static com.github.tschalk.project_tracker.util.SceneManager.*;

public class MainWindowView extends BorderPane {
    private final MainWindowController mainWindowController;
    private final TableView<Project> projectTableView;
    private final SimpleLongProperty secondsElapsed;
    private final Stage stage;
    private final SVGManager svgManager = SVGManager.getInstance();
    private final DatabaseBackupManager databaseBackupManager;
    private Project selectedProject;
    private Project activeProject;
    private Label titleLabel;
    private Timeline updateTimeLabelTimeline;

    public MainWindowView(MainWindowController mainWindowController, Stage stage, DatabaseBackupManager databaseBackupManager) {
        this.mainWindowController = mainWindowController;
        this.projectTableView = new TableView<>();
        this.secondsElapsed = new SimpleLongProperty(0);
        this.stage = stage;
        this.databaseBackupManager = databaseBackupManager;
        initializeUI();
    }

    private void initializeUI() {
        this.setPadding(new Insets(0, 0, 15, 0));

        CustomTitleBar titleBar = new CustomTitleBar(this.stage, "Project Tracker");
        this.setTop(titleBar);

        this.titleLabel = new Label(mainWindowController.getWelcomeMessage());

        initializeTableView();

        Button addButton = getAddButton();
        Button editButton = getEditButton();
        Button exportButton = getExportButton();
        Button startStopButton = getStartStopButton();
        Button userSettingsButton = getUserSettingsButton();

        HBox buttonContainer = new HBox(10);
        buttonContainer.getChildren().addAll(startStopButton, addButton, editButton, exportButton, userSettingsButton);
        buttonContainer.getStyleClass().add("button-container");
        handleAdminAction(buttonContainer);

        VBox contentBox = new VBox(10);
        contentBox.setPadding(new Insets(10));
        contentBox.getChildren().addAll(titleLabel, projectTableView, buttonContainer);
        this.setCenter(contentBox);

        updateProjectTableView();
    }

    private void initializeTableView() {
        TableColumn<Project, String> descriptionColumn = new TableColumn<>("Description");
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        TableColumn<Project, String> costCenterColumn = new TableColumn<>("Cost Center");
        costCenterColumn.setCellValueFactory(new PropertyValueFactory<>("costCenter"));

        TableColumn<Project, String> responsibleColumn = new TableColumn<>("Responsible");
        responsibleColumn.setCellValueFactory(new PropertyValueFactory<>("responsible"));

        TableColumn<Project, Number> durationColumn = new TableColumn<>("Duration [h]");
        durationColumn.setCellValueFactory(p -> {
            int durationInSeconds = mainWindowController.getProjectDuration(p.getValue());

            DoubleBinding durationInHours = Bindings.createDoubleBinding(() -> Math.round((double) durationInSeconds / 3600 * 100) / 100.0, new SimpleIntegerProperty(durationInSeconds));

            return durationInHours;
        });

        projectTableView.getColumns().addAll(descriptionColumn, costCenterColumn, responsibleColumn, durationColumn);
        projectTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedProject = newSelection;
            }
        });
    }

    private Button createButton(String svgIcon, String tooltip, Runnable action) {
        Button button = new Button();
        button.setTooltip(new Tooltip(tooltip));
        button.getStyleClass().add("svg-button");
        button.setGraphic(svgManager.getSVGPath(svgIcon));
        button.setOnAction(e -> action.run());

        return button;
    }

    private Button getAddButton() {
        return createButton("addIcon", "Add new project", () -> getInstance().showNewWindowWithCustomScene(ADD_PROJECT_SCENE));
    }

    private Button getEditButton() {
        return createButton("editIcon", "Edit selected project", () -> {
            if (selectedProject != null) {
                System.out.println("Edit project: " + selectedProject.getDescription());
                getInstance().showNewWindowWithCustomScene(EDIT_PROJECT_SCENE, selectedProject);
            } else {
                showAlert(Alert.AlertType.INFORMATION, "Information", "No project selected!", "Please select a project to edit!");
            }
        });
    }

    private Button getStartStopButton() {
        final Button[] startStopButton = new Button[1];

        startStopButton[0] = createButton("startIcon", "Start/Stop stopwatch to track time for selected project", () -> {
            if (selectedProject == null) {
                showErrorAlert("No project selected!", "Please select a project to start the stopwatch!");
                return;
            }

            if (mainWindowController.getStopwatchState() == StopwatchState.STOPPED) {
                startStopwatch(startStopButton[0]);
            } else {
                stopStopwatch(startStopButton[0]);
            }
        });

        return startStopButton[0];
    }

    private void startStopwatch(Button startStopButton) {
        activeProject = selectedProject;
        mainWindowController.startStopwatch(selectedProject);
        startStopButton.setGraphic(svgManager.getSVGPath("stopIcon"));
        startStopButton.setStyle("-fx-background-color: rgba(199,78,79,0.9)");
        startUpdateTitleLabelTimeline();
    }

    private void stopStopwatch(Button startStopButton) {
        mainWindowController.stopStopwatch();
        startStopButton.setGraphic(svgManager.getSVGPath("startIcon"));
        startStopButton.setStyle("");
        selectedProject = null;
        activeProject = null;
        stopUpdateTitleLabelTimeline();
        titleLabel.setText(mainWindowController.getWelcomeMessage());
        updateProjectTableView();
    }

    private void showErrorAlert(String header, String content) {
        showAlert(Alert.AlertType.ERROR, "Error", header, content);
    }

    private Button getExportButton() {
        return createButton("exportIcon", "Export all projects to CSV with semicolon as delimiter", () -> getInstance().showNewWindowWithCustomScene(EXPORT_SCENE));
    }

    private Button getUserSettingsButton() {
        return createButton("settingsIcon", "Change password", () -> getInstance().showNewWindowWithCustomScene(CHANGE_PASSWORD_LOGGED_USER_SCENE));
    }

    private void handleAdminAction(HBox buttonContainer) {
        if (getMainWindowController().getCurrentUser().getRole().equals("admin")) {
            Button userManagementButton = createButton("userManagementIcon", "Manage users", () -> getInstance().showNewWindowWithCustomScene(USER_MANAGEMENT_SCENE));

            Button restoreDatabaseButton = createButton("databaseRestoreIcon", "Restore database", () -> {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Open Resource File");
                fileChooser.setInitialDirectory(new File(DatabaseBackupManager.MY_SQL_BACKUPS_PATH));
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("SQL Files", "*.sql"));

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Dialog");
                alert.setHeaderText("Restore database");
                alert.setContentText("Are you sure you want to restore the database?");

                databaseBackupManager.performDatabaseBackup();

                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() != ButtonType.OK) {
                    return;
                }
                File selectedFile = fileChooser.showOpenDialog(stage);
                if (selectedFile != null) {
                    databaseBackupManager.restoreDatabase(selectedFile.toPath());
                    updateProjectTableView();
                }
            });

            buttonContainer.getChildren().addAll(userManagementButton, restoreDatabaseButton);
        }
    }

    private void startUpdateTitleLabelTimeline() {
        setupTimeline();
        updateTimeLabelTimeline.play();
    }

    private void setupTimeline() {
        updateTimeLabelTimeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            secondsElapsed.set(secondsElapsed.get() + 1);
            String formattedTime = String.format("%02d:%02d:%02d", secondsElapsed.get() / 3600, (secondsElapsed.get() % 3600) / 60, secondsElapsed.get() % 60);
            titleLabel.setText("Selected Project: " + activeProject.getDescription() + " | Time: " + formattedTime);
        }));
        updateTimeLabelTimeline.setCycleCount(Timeline.INDEFINITE);
    }

    private void stopUpdateTitleLabelTimeline() {
        if (updateTimeLabelTimeline != null) {
            updateTimeLabelTimeline.stop();
            updateTimeLabelTimeline = null;
        }
        mainWindowController.updateProjectDuration(secondsElapsed.get());
        secondsElapsed.set(0);
    }

    public void updateProjectTableView() {
        ObservableList<Project> projectList = mainWindowController.getProjectList();
        projectTableView.setItems(projectList);
        System.out.println("Project list updated!");
    }

    public MainWindowController getMainWindowController() {
        return mainWindowController;
    }
}
