package com.github.tschalk.project_tracker.view;

import com.github.tschalk.project_tracker.controller.AddProjectController;
import com.github.tschalk.project_tracker.controller.MainWindowController;
import com.github.tschalk.project_tracker.model.Project;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainWindowView extends VBox {

    private final Stage stage;
    private final MainWindowController mainWindowController;
    private final TableView<Project> projectTableView;

    MainWindowView(MainWindowController mainWindowController, Stage stage) {

        this.stage = stage;
        this.mainWindowController = mainWindowController;
        this.projectTableView = new TableView<>();

        initUI();
        updateProjectTableView();
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
        addButton.setOnAction(event -> {
            AddProjectController addProjectController = new AddProjectController(mainWindowController.getProjectDAO()); // ProjectDAO
            AddProjectView addProjectView = new AddProjectView(addProjectController, stage);

            Scene scene = new Scene(addProjectView, 500, 400);
            Stage addStage = new Stage();
            addStage.setScene(scene);
            addStage.setTitle("Project Tracker");
            addStage.setResizable(false);
            addStage.centerOnScreen();
            addStage.show();

        });
        Button editButton = new Button("Edit");
        Button exportButton = new Button("Export to CSV");
        Button startStopButton = new Button("Start/Stop");

        HBox buttonContainer = new HBox(10);
        buttonContainer.getChildren().addAll(addButton, editButton, exportButton, startStopButton);
        buttonContainer.getStyleClass().add("button-container");

        this.getChildren().addAll(titleLabel, projectTableView, buttonContainer);
    }

    private void updateProjectTableView() {
        ObservableList<Project> projectList = FXCollections.observableArrayList(mainWindowController.getProjectList());
        projectTableView.setItems(projectList);
        System.out.println("Project list updated!");
    }



}
