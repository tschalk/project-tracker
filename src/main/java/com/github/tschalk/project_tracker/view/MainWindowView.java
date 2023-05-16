package com.github.tschalk.project_tracker.view;

import com.github.tschalk.project_tracker.controller.MainWindowController;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainWindowView extends VBox {

    private final Stage stage;
    private final MainWindowController mainWindowController;
    private final TableView<Object> tableView;

    MainWindowView(MainWindowController mainWindowController, Stage stage) {
        this.stage = stage;
        this.mainWindowController = mainWindowController;
        this.tableView = new TableView<>();

        initUI();
    }

    private void initUI() {

        this.setSpacing(10);
        this.setPadding(new Insets(10));

        Label titleLabel = new Label("Project Tracker");

        Button addButton = new Button("Add");
        Button editButton = new Button("Edit");
        Button exportButton = new Button("Export to CSV");
        Button startStopButton = new Button("Start/Stop");

        HBox buttonContainer = new HBox(10);
        buttonContainer.getChildren().addAll(addButton, editButton, exportButton, startStopButton);
        buttonContainer.getStyleClass().add("button-container");

        this.getChildren().addAll(titleLabel, tableView, buttonContainer);
    }


}
