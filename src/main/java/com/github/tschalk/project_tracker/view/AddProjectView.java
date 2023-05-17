package com.github.tschalk.project_tracker.view;

import com.github.tschalk.project_tracker.controller.AddProjectController;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class AddProjectView extends VBox {

    private final AddProjectController addProjectController;
    private final Stage stage;
    private final TextField descriptionField;
    private final TextField costCenterField;
    private final TextField responsibleField;

    public AddProjectView(AddProjectController addProjectController, Stage stage) {

        this.addProjectController = addProjectController;
        this.stage = stage;
        this.descriptionField = new TextField();
        this.costCenterField = new TextField();
        this.responsibleField = new TextField();

        initUI();
    }

    private void initUI() {

        this.setSpacing(10);
        this.setPadding(new Insets(10));

        Label titleLabel = new Label("Add new project");

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        Label descriptionLabel = new Label("Description:");
        gridPane.add(descriptionLabel, 0, 0);
        gridPane.add(descriptionField, 1, 0);

        Label costCenterLabel = new Label("Cost Center:");
        gridPane.add(costCenterLabel, 0, 1);
        gridPane.add(costCenterField, 1, 1);

        Label responsibleLabel = new Label("Responsible:");
        gridPane.add(responsibleLabel, 0, 2);
        gridPane.add(responsibleField, 1, 2);

        Button addButton = new Button("Ok");
        addButton.setOnAction(event -> {
            // TODO: add project to database if all fields are filled
            // ...

            Stage currentStage = (Stage) addButton.getScene().getWindow();
            currentStage.close();
        });
        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(event -> stage.close());

        HBox buttonContainer = new HBox(10);
        buttonContainer.getChildren().addAll(addButton, cancelButton);
        buttonContainer.getStyleClass().add("button-container");

        this.getChildren().addAll(titleLabel, gridPane, buttonContainer);
    }
}
