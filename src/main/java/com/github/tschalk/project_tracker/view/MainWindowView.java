package com.github.tschalk.project_tracker.view;

import com.github.tschalk.project_tracker.controller.MainWindowController;
import com.github.tschalk.project_tracker.controller.StopwatchState;
import com.github.tschalk.project_tracker.model.Project;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import static com.github.tschalk.project_tracker.utils.SceneManager.getInstance;
import static com.github.tschalk.project_tracker.view.UserLoginView.ADD_PROJECT_SCENE;
import static com.github.tschalk.project_tracker.view.UserLoginView.EDIT_PROJECT_SCENE;

public class MainWindowView extends VBox {

    // TODO: Label, dass beim starten der Stoppuhr die Projektbschreibung und die Zeit im format hh:mm:ss angezeigt. [ Selected Project: <Project Description> | Time: <hh:mm:ss> ]

    private final Stage stage;
    private final MainWindowController mainWindowController;
    private final TableView<Project> projectTableView;
    private Project selectedProject;

    public MainWindowView(MainWindowController mainWindowController, Stage stage) {
        this.stage = stage;
        this.mainWindowController = mainWindowController;
        this.projectTableView = new TableView<>();

        initUI();
    }

    private void initUI() {
        this.setSpacing(10);
        this.setPadding(new Insets(10));

        Label titleLabel = new Label("Project Tracker");

        TableColumn<Project, String> descriptionColumn = new TableColumn<>("Description");
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        TableColumn<Project, String> costCenterColumn = new TableColumn<>("Cost Center");
        costCenterColumn.setCellValueFactory(new PropertyValueFactory<>("costCenter"));

        TableColumn<Project, String> responsibleColumn = new TableColumn<>("Responsible");
        responsibleColumn.setCellValueFactory(new PropertyValueFactory<>("responsible"));

        // Hier wird die Dauer berechnet
        TableColumn<Project, Number> durationColumn = new TableColumn<>("Duration");
        durationColumn.setCellValueFactory(p -> { // callback p -> { ... }
            int durationInSeconds = mainWindowController.getProjectDuration(p.getValue());

            // Hier die Zeit in Stunden umrechnen und runden
            DoubleBinding durationInHours = Bindings.createDoubleBinding(
                    // * 100 um zwei kommastellen zu verschieben, dann runden und danach / 100.0 um wieder zu teilen
                    () -> Math.round((double) durationInSeconds / 3600 * 100) / 100.0,
                    new SimpleIntegerProperty(durationInSeconds)
            );
            return durationInHours;
        });

        projectTableView.getColumns().addAll(descriptionColumn, costCenterColumn, responsibleColumn, durationColumn);

        Button addButton = new Button("Add");
        addButton.setOnAction(e -> {
            getInstance().showNewWindowWithCustomScene(ADD_PROJECT_SCENE);
        });

        projectTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedProject = newSelection;
            }
        });

        Button editButton = new Button("Edit");
        editButton.setOnAction(e -> {
            if (selectedProject != null) {
                System.out.println("Edit project: " + selectedProject.getDescription());
                openEditProjectWindow(selectedProject);
            } else {
                System.out.println("No project selected!");
            }
        });

        Button exportButton = new Button("Export to CSV");

        Button startStopButton = new Button("Start/Stop");

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
                mainWindowController.startStopwatch(selectedProject);
                startStopButton.setText("Stop");
                startStopButton.setStyle("-fx-background-color: rgba(255,0,0,0.5)");
            } else {
                mainWindowController.stopStopwatch(/*selectedProject*/);
                startStopButton.setText("Start");
                startStopButton.setStyle("");
                selectedProject = null;
                updateProjectTableView();
            }
        });

        HBox buttonContainer = new HBox(10);
        buttonContainer.getChildren().addAll(addButton, editButton, exportButton, startStopButton);
        buttonContainer.getStyleClass().add("button-container");

        this.getChildren().addAll(titleLabel, projectTableView, buttonContainer);

        updateProjectTableView();
    }

    private void openEditProjectWindow(Project selectedProject) {

        getInstance().showNewWindowWithCustomScene(EDIT_PROJECT_SCENE, selectedProject);
    }

    public void updateProjectTableView() {
        ObservableList<Project> projectList = mainWindowController.getProjectList();
        projectTableView.setItems(projectList);
        System.out.println("Project list updated!");
    }

}
