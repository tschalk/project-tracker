package com.github.tschalk.project_tracker.view;

import com.github.tschalk.project_tracker.controller.MainWindowController;
import com.github.tschalk.project_tracker.model.Project;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import static com.github.tschalk.project_tracker.utils.SceneManager.getInstance;
import static com.github.tschalk.project_tracker.view.UserLoginView.ADD_PROJECT_SCENE;
import static com.github.tschalk.project_tracker.view.UserLoginView.EDIT_PROJECT_SCENE;

public class MainWindowView extends VBox {

    private static MainWindowView instance = null;

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
        TableColumn<Project, Integer> durationColumn = new TableColumn<>("Duration");
        durationColumn.setCellValueFactory(new PropertyValueFactory<>("duration"));
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

        HBox buttonContainer = new HBox(10);
        buttonContainer.getChildren().addAll(addButton, editButton, exportButton, startStopButton);
        buttonContainer.getStyleClass().add("button-container");

        this.getChildren().addAll(titleLabel, projectTableView, buttonContainer);

        updateProjectTableView();
    }

    private void openEditProjectWindow(Project selectedProject) {
        // Öffnen Sie das Bearbeitungsfenster und übergeben Sie das ausgewählte Projekt
        getInstance().showNewWindowWithCustomScene(EDIT_PROJECT_SCENE, selectedProject);
    }

    public void updateProjectTableView() {
        ObservableList<Project> projectList = mainWindowController.getProjectList();
        projectTableView.setItems(projectList);
        System.out.println("Project list updated!");
    }

    public Project getSelectedProject() {
        return selectedProject;
    }
}
