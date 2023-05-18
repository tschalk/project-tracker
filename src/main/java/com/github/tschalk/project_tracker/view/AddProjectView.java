package com.github.tschalk.project_tracker.view;

import com.github.tschalk.project_tracker.controller.AddProjectController;
import com.github.tschalk.project_tracker.model.CostCenter;
import com.github.tschalk.project_tracker.model.Responsible;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Optional;


public class AddProjectView extends VBox {

    private final AddProjectController addProjectController;
    private final Stage stage;
    private final TextField descriptionField;
    private final ComboBox<CostCenter> costCenterComboBox;
    private final ComboBox<Responsible> responsibleComboBox;

    public AddProjectView(AddProjectController addProjectController, Stage stage) {

        this.addProjectController = addProjectController;
        this.stage = stage;
        this.descriptionField = new TextField();
        this.costCenterComboBox = new ComboBox<>();
        this.responsibleComboBox = new ComboBox<>();

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

        // 1a. Combo Box - "Cost Center"
        Label costCenterLabel = new Label("Cost Center:");
        gridPane.add(costCenterLabel, 0, 1);
        gridPane.add(costCenterComboBox, 1, 1);


        costCenterComboBox.showingProperty().addListener((observable, wasShowing, isNowShowing) -> {
            if (isNowShowing) {
                costCenterComboBox.setItems(addProjectController.getCostCenters());
            }
        });

        // 1b. "Remove-Button" für Combo Box - "Cost Center"
        Button removeCostCenterButton = new Button("Remove Cost Center");
        removeCostCenterButton.setOnAction(event -> {

            CostCenter selectedCostCenter = costCenterComboBox.getValue();
            if (selectedCostCenter != null) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Dialog");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to remove the selected cost center?");

                Optional<ButtonType> result = alert.showAndWait();

                if (result.isPresent() && result.get().equals(ButtonType.OK)){
                    addProjectController.removeCostCenter(selectedCostCenter);
                    costCenterComboBox.getSelectionModel().clearSelection();
                }
            }
        });

        // 1c. Add-Button für Combo Box - "Cost Center"
        Button addCostCenterButton = new Button("Add Cost Center");
        addCostCenterButton.setOnAction(event -> {

            Dialog<String> dialog = new Dialog<>();
            dialog.setTitle("Add Cost Center");

            ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

            TextField textField = new TextField();
            textField.setPromptText("Cost Center");

            dialog.getDialogPane().setContent(textField);

            // Behandel den OK-Button
            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == okButtonType) {
                    return textField.getText();
                }
                return null;
            });

            Optional<String> result = dialog.showAndWait();

            result.ifPresent(name -> {
                addProjectController.addCostCenter(name);
                costCenterComboBox.setItems(addProjectController.getCostCenters());
            });
        });

        // 2a. Combobox - "Responsible"
        Label responsibleLabel = new Label("Responsible:");
        gridPane.add(responsibleLabel, 0, 2);
        gridPane.add(responsibleComboBox, 1, 2);

        // Aktualisiert die ComboBox-Elemente, wenn die ComboBox angezeigt wird
        responsibleComboBox.showingProperty().addListener((observable, wasShowing, isNowShowing) -> {
            if (isNowShowing) {
                responsibleComboBox.setItems(addProjectController.getResponsible());
            }
        });

        // 2b. "Remove-Button" für Combo Box - "Cost Center"
        Button removeResponsibleButton = new Button("Remove Responsible");
        removeResponsibleButton.setOnAction(event -> {

            Responsible selectedResponsible = responsibleComboBox.getValue();
            if (selectedResponsible != null) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Dialog");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to remove the selected Responsible?");

                Optional<ButtonType> result = alert.showAndWait();

                if (result.isPresent() && result.get().equals(ButtonType.OK)){
                    addProjectController.removeResponsible(selectedResponsible);
                    responsibleComboBox.getSelectionModel().clearSelection();
                }
            }
        });

        // 2c. Add-Button für Combo Box - "Responsible"
        Button addResponsibleButton = new Button("Add Responsible");
        addResponsibleButton.setOnAction(event -> {

            Dialog<String> dialog = new Dialog<>();
            dialog.setTitle("Add Responsible");

            ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

            TextField textField = new TextField();
            textField.setPromptText("Responsible");

            dialog.getDialogPane().setContent(textField);

            // Behandel den OK-Button
            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == okButtonType) {
                    return textField.getText();
                }
                return null;
            });

            Optional<String> result = dialog.showAndWait();

            result.ifPresent(name -> {
                addProjectController.addResponsible(name);
                responsibleComboBox.setItems(addProjectController.getResponsible());
            });
        });

        // 3. Buttons
        Button addButton = new Button("Ok");
        addButton.setOnAction(event -> {
            // TODO: add project to database if all fields are filled or selected
            // ...

            Stage currentStage = (Stage) addButton.getScene().getWindow();
            currentStage.close();
        });

        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(event -> {

            Stage currentStage = (Stage) cancelButton.getScene().getWindow();
            currentStage.close();
        });

        HBox addEntityButtonContainer = new HBox(10);
        addEntityButtonContainer.getChildren().addAll(addCostCenterButton, addResponsibleButton);
        addEntityButtonContainer.getStyleClass().add("button-container");

        HBox actionButtonContainer = new HBox(10);
        actionButtonContainer.getChildren().addAll(addButton, cancelButton);
        actionButtonContainer.getStyleClass().add("button-container");

        HBox removeButtonContainer = new HBox(10);
        removeButtonContainer.getChildren().addAll(removeCostCenterButton, removeResponsibleButton);
        removeButtonContainer.getStyleClass().add("button-container");

        setButtonSize(130, 25, addButton, cancelButton, addCostCenterButton, addResponsibleButton, removeCostCenterButton, removeResponsibleButton);

        this.getChildren().addAll(titleLabel, gridPane, addEntityButtonContainer, removeButtonContainer, actionButtonContainer);    }

    private void setButtonSize(double width, double height, Button... buttons) {

        for (Button button : buttons) {
            button.setMinWidth(width);
            button.setMaxWidth(width);
            button.setMinHeight(height);
            button.setMaxHeight(height);
        }
    }

}
