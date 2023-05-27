package com.github.tschalk.project_tracker.view;

import com.github.tschalk.project_tracker.controller.MainWindowController;
import com.github.tschalk.project_tracker.controller.StopwatchState;
import com.github.tschalk.project_tracker.model.Project;
import com.github.tschalk.project_tracker.utils.CustomTitleBar;
import com.github.tschalk.project_tracker.utils.SVGManager;
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

import static com.github.tschalk.project_tracker.utils.SceneManager.*;

public class MainWindowView extends BorderPane {

    private final MainWindowController mainWindowController;
    private final TableView<Project> projectTableView; // Sollte es eine lokale Variable sein?
    private final SimpleLongProperty secondsElapsed;
    private final Stage stage;
    private final SVGManager svgManager = SVGManager.getInstance();
    private Project selectedProject;
    private Project activeProject;
    private Label titleLabel;
    private Timeline updateTimeLabelTimeline;

    public MainWindowView(MainWindowController mainWindowController, Stage stage) {
        this.mainWindowController = mainWindowController;
        this.projectTableView = new TableView<>();
        this.secondsElapsed = new SimpleLongProperty(0);
        this.stage = stage;
        initializeUI();
    }

    private void initializeUI() {

        // This
        this.setPadding(new Insets(0, 0, 15, 0));

        // Top
        CustomTitleBar titleBar = new CustomTitleBar(this.stage, "Project Tracker");
        this.setTop(titleBar);

        // Center
        this.titleLabel = new Label("Project Tracker");

        initializeTableView();

        Button addButton = getAddButton();
        Button editButton = getEditButton();
        Button exportButton = getExportButton();
        Button startStopButton = getStartStopButton();
        //Button importButton = importButton();

        HBox buttonContainer = new HBox(10);
        buttonContainer.getChildren().addAll(addButton, editButton, exportButton, startStopButton/*, importButton*/);
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

        // Hier wird die Dauer berechnet
        TableColumn<Project, Number> durationColumn = new TableColumn<>("Duration [h]");
        durationColumn.setCellValueFactory(p -> { // callback p -> { ... }
            int durationInSeconds = mainWindowController.getProjectDuration(p.getValue());

            // Hier die Zeit in Stunden umrechnen und runden
            DoubleBinding durationInHours = Bindings.createDoubleBinding(
                    // * 100 um zwei Kommastellen zu verschieben, dann runden und danach / 100.0 um wieder zu teilen
                    () -> Math.round((double) durationInSeconds / 3600 * 100) / 100.0,
                    new SimpleIntegerProperty(durationInSeconds)
            );

            return durationInHours;
        });

        projectTableView.getColumns().addAll(descriptionColumn, costCenterColumn, responsibleColumn, durationColumn);
        projectTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedProject = newSelection;
            }
        });
    }

    private Button getAddButton() {
        Button addButton = new Button();
        addButton.getStyleClass().add("svg-button");
        addButton.setGraphic(svgManager.getSVGPath("addIcon"));
        addButton.setOnAction(e -> {
            getInstance().showNewWindowWithCustomScene(ADD_PROJECT_SCENE);
        });
        return addButton;
    }

    private Button getEditButton() {
        Button editButton = new Button();
        editButton.getStyleClass().add("svg-button");
        editButton.setGraphic(svgManager.getSVGPath("editIcon"));
        editButton.setOnAction(e -> {
            if (selectedProject != null) {
                System.out.println("Edit project: " + selectedProject.getDescription());
                getInstance().showNewWindowWithCustomScene(EDIT_PROJECT_SCENE, selectedProject);
            } else {
                System.out.println("No project selected!");
            }
        });
        return editButton;
    }

    private Button getStartStopButton() {
        Button startStopButton = new Button();
        startStopButton.getStyleClass().add("svg-button");
        startStopButton.setGraphic(svgManager.getSVGPath("startIcon"));
        startStopButton.setOnAction(e -> {

            if (selectedProject == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("No project selected!");
                alert.setContentText("Please select a project to start the stopwatch!");

                alert.showAndWait();
                return;
            }

            if (mainWindowController.getStopwatchState() == StopwatchState.STOPPED) {
                activeProject = selectedProject;
                mainWindowController.startStopwatch(selectedProject);
                startStopButton.setGraphic(svgManager.getSVGPath("stopIcon"));
                startStopButton.setStyle("-fx-background-color: rgba(199,78,79,0.9)");

                startUpdateTitleLabelTimeline();
            } else {
                mainWindowController.stopStopwatch();
                startStopButton.setGraphic(svgManager.getSVGPath("startIcon"));
                startStopButton.setStyle("");
                selectedProject = null;
                activeProject = null;

                // Stop updating the titleLabel
                stopUpdateTitleLabelTimeline();
                titleLabel.setText("Project Tracker");

                updateProjectTableView();
            }
        });
        return startStopButton;
    }

    private Button getExportButton() {
        Button exportButton = new Button();
        exportButton.getStyleClass().add("svg-button");
        exportButton.setGraphic(svgManager.getSVGPath("exportIcon"));
        exportButton.setOnAction(e -> {
            getInstance().showNewWindowWithCustomScene(EXPORT_SCENE);
        });
        return exportButton;
    }

    private Button importButton() {
        Button importButton = new Button();
        importButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));

            File selectedFile = fileChooser.showOpenDialog(stage);
            if (selectedFile != null) {
                // mainWindowController.importCsv(selectedFile.toPath().toString());
            }
        });
        return importButton;
    }

    private void handleAdminAction(HBox buttonContainer) {
        if (getMainWindowController().getCurrentUser().getRole().equals("admin")) {
            Button userManagementButton = new Button();
            userManagementButton.getStyleClass().add("svg-button");
            userManagementButton.setGraphic(svgManager.getSVGPath("userManagementIcon"));
            userManagementButton.setOnAction(e -> {
                getInstance().showNewWindowWithCustomScene(USER_MANAGEMENT_SCENE);
            });

            buttonContainer.getChildren().add(userManagementButton);
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
        // Hier wird die Zeit gestoppt
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
