package com.github.tschalk.project_tracker.util;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.Optional;

public class CustomTitleBar extends HBox {
    private double xOffset = 0;
    private double yOffset = 0;
    private final Button closeButton;

    public CustomTitleBar(String titleText) {
        this(null, titleText);
    }

    public CustomTitleBar(Stage stage, String titleText) {

        this.setPadding(new Insets(10));
        this.setStyle("-fx-background-color: #3c3f41;");
        this.setId("title-bar");

        Label title = new Label(titleText);
        title.setTextFill(Color.WHITE);
        title.setStyle("-fx-font-size: 16;");

        closeButton = new Button();
        closeButton.setGraphic(SVGManager.getInstance().getSVGPath("windowCloseIcon"));
        closeButton.setTextFill(Color.WHITE);
        closeButton.getStyleClass().add("window-close-button");
        closeButton.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Exit");
            alert.setHeaderText(null);
            alert.setContentText("Do you want to exit?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                Platform.exit();
            }
        });

        this.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        this.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        this.getChildren().addAll(title, spacer, closeButton);
    }

    public void showCloseButton(boolean setVisible) {
        closeButton.setVisible(setVisible);
    }
}
