package com.github.tschalk.project_tracker.view;

import com.github.tschalk.project_tracker.controller.AddProjectController;
import com.github.tschalk.project_tracker.model.CostCenter;
import com.github.tschalk.project_tracker.model.Responsible;
import com.github.tschalk.project_tracker.util.CustomTitleBar;
import com.github.tschalk.project_tracker.util.SVGManager;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Objects;
import java.util.Optional;

import static com.github.tschalk.project_tracker.Main.STYLESHEET_PATH;

public class AddProjectView extends BorderPane {
    private final AddProjectController addProjectController;
    private final TextField descriptionField;
    private final ComboBox<CostCenter> costCenterComboBox;
    private final ComboBox<Responsible> responsibleComboBox;
    private final SVGManager svgManager;
    private Button addCostCenterButton;
    private Button addResponsibleButton;
    private Button removeCostCenterButton;
    private Button removeResponsibleButton;

    public AddProjectView(AddProjectController addProjectController) {
        this.addProjectController = addProjectController;
        this.descriptionField = new TextField();
        this.costCenterComboBox = new ComboBox<>();
        this.responsibleComboBox = new ComboBox<>();
        svgManager = SVGManager.getInstance();
        initializeUI();
    }

    private void initializeUI() {

        // This
        this.setPadding(new Insets(0, 0, 15, 0));

        // Top
//        CustomTitleBar customTitleBar = new CustomTitleBar("Add new project");
//        customTitleBar.showCloseButton(false);
//        this.setTop(customTitleBar);

        // Center
        Label titleLabel = new Label("Add new project");

        Button addOkButton = getOkButton();
        Button cancelButton = getCancelButton();
        setButtonStyle(addOkButton, cancelButton);

        // Buttons für das GridPane
        addCostCenterButton = getAddCostcenterButton();
        addResponsibleButton = getAddResponsibleButton();
        removeCostCenterButton = getRemoveCostcenterButton();
        removeResponsibleButton = getRemoveResponsibleButton();
        setButtonSVGStyle(addCostCenterButton, addResponsibleButton, removeCostCenterButton, removeResponsibleButton);

        GridPane gridPane = getGridPane();

        HBox actionButtonContainer = new HBox(10);
        actionButtonContainer.getChildren().addAll(addOkButton, cancelButton);
        actionButtonContainer.getStyleClass().add("button-container");

        VBox contentBox = new VBox(10);
        contentBox.setPadding(new Insets(10));
        contentBox.getChildren().addAll(titleLabel, gridPane, actionButtonContainer);

        this.setCenter(contentBox);

    }

    private GridPane getGridPane() {
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        Label descriptionLabel = new Label("Description:");
        gridPane.add(descriptionLabel, 0, 0);
        gridPane.add(descriptionField, 1, 0);

        Label costCenterLabel = new Label("Cost Center:");
        gridPane.add(costCenterLabel, 0, 1);
        gridPane.add(costCenterComboBox, 1, 1);
        gridPane.add(addCostCenterButton, 2, 1);
        gridPane.add(removeCostCenterButton, 3, 1);

        Label responsibleLabel = new Label("Responsible:");
        gridPane.add(responsibleLabel, 0, 2);
        gridPane.add(responsibleComboBox, 1, 2);
        gridPane.add(addResponsibleButton, 2, 2);
        gridPane.add(removeResponsibleButton, 3, 2);

        costCenterComboBox.showingProperty().addListener((observable, wasShowing, isNowShowing) -> {
            if (isNowShowing) {
                costCenterComboBox.setItems(addProjectController.getCostCenters());
            }
        });

        responsibleComboBox.showingProperty().addListener((observable, wasShowing, isNowShowing) -> {
            if (isNowShowing) {
                responsibleComboBox.setItems(addProjectController.getResponsible());
            }
        });
        return gridPane;
    }

    private Button getAddCostcenterButton() {
        Button addCostCenterButton = new Button("Add Cost Center");
        addCostCenterButton.setTooltip(new Tooltip("Add a new cost center"));
        addCostCenterButton.setGraphic(svgManager.getSVGPath("addIcon"));

        addCostCenterButton.setOnAction(event -> {
            Dialog<String> dialog = new Dialog<>();
            dialog.setTitle("Add cost center");

            ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

            TextField textField = new TextField();
            textField.setPromptText("Cost Center");

            dialog.getDialogPane().setContent(textField);
            dialog.getDialogPane().getStylesheets().add(Objects.requireNonNull(getClass().getResource(STYLESHEET_PATH)).toExternalForm());

            handleDialogInput(dialog, okButtonType, textField);

            Optional<String> result = dialog.showAndWait();

            result.ifPresentOrElse(name -> {
                addProjectController.addCostCenter(name);
                costCenterComboBox.setItems(addProjectController.getCostCenters());
            }, () -> {
                // Hier wird der Code ausgeführt, wenn der Wert in result null ist
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("An error occurred while adding the responsible. Semi-colons are not allowed. Please try again.");
                alert.showAndWait();
            });
        });
        return addCostCenterButton;
    }

    private Button getAddResponsibleButton() {
        Button addResponsibleButton = new Button("Add Responsible");
        addResponsibleButton.setTooltip(new Tooltip("Add a new responsible"));
        addResponsibleButton.setGraphic(svgManager.getSVGPath("addIcon"));

        addResponsibleButton.setOnAction(event -> {
            Dialog<String> dialog = new Dialog<>();
            dialog.setTitle("Add Responsible");

            ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

            TextField textField = new TextField();
            textField.setPromptText("Responsible");

            dialog.getDialogPane().setContent(textField);
            dialog.getDialogPane().getStylesheets().add(Objects.requireNonNull(getClass().getResource(STYLESHEET_PATH)).toExternalForm());

            handleDialogInput(dialog, okButtonType, textField);

            Optional<String> result = dialog.showAndWait();

            result.ifPresentOrElse(name -> {
                addProjectController.addResponsible(name);
                responsibleComboBox.setItems(addProjectController.getResponsible());
            }, () -> {
                // Hier wird der Code ausgeführt, wenn der Wert in result null ist
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("An error occurred while adding the responsible. Semi-colons are not allowed. Please try again.");
                alert.showAndWait();
            });
        });
        return addResponsibleButton;
    }

    private void handleDialogInput(Dialog<String> dialog, ButtonType okButtonType, TextField textField) {
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                String text = textField.getText();
                int startIndex = 0;
                int endIndex = text.length() - 1;

                // Leerzeichen am Anfang der Zeichenkette entfernen
                while (startIndex <= endIndex && Character.isWhitespace(text.charAt(startIndex))) {
                    startIndex++;
                }

                // Leerzeichen am Ende der Zeichenkette entfernen
                while (endIndex >= startIndex && Character.isWhitespace(text.charAt(endIndex))) {
                    endIndex--;
                }

                // Kein sichtbarer Text vorhanden
                if (startIndex > endIndex) {
                    return null;
                }

                // Neue Zeichenkette ohne Leerzeichen am Anfang und am Ende erstellen
                String trimmedText = text.substring(startIndex, endIndex + 1);

                if (trimmedText.contains(";")) {
//                    return trimmedText.replace(";", ",");
                    return null;
                } else {
                    return trimmedText;
                }
            }
            return null;
        });
    }

    private Button getRemoveCostcenterButton() {
        Button removeCostCenterButton = new Button("Remove Cost Center");
        removeCostCenterButton.getStyleClass().add("remove-button");
        removeCostCenterButton.setTooltip(new Tooltip("Remove the selected cost center"));
        removeCostCenterButton.setGraphic(svgManager.getSVGPath("removeIcon"));
        removeCostCenterButton.setOnAction(event -> {

            CostCenter selectedCostCenter = costCenterComboBox.getValue();
            if (selectedCostCenter != null) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Dialog");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to remove the selected cost center?");

                Optional<ButtonType> result = alert.showAndWait();

                if (result.isPresent() && result.get().equals(ButtonType.OK)) {
                    addProjectController.removeCostCenter(selectedCostCenter);
                    costCenterComboBox.getSelectionModel().clearSelection();
                }
            }
        });
        return removeCostCenterButton;
    }

    private Button getRemoveResponsibleButton() {
        Button removeResponsibleButton = new Button("Remove Responsible");
        removeResponsibleButton.getStyleClass().add("remove-button");
        removeResponsibleButton.setTooltip(new Tooltip("Remove the selected Responsible"));
        removeResponsibleButton.setGraphic(svgManager.getSVGPath("removeIcon"));
        removeResponsibleButton.setOnAction(event -> {

            Responsible selectedResponsible = responsibleComboBox.getValue();
            if (selectedResponsible != null) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Dialog");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to remove the selected responsible?");

                Optional<ButtonType> result = alert.showAndWait();

                if (result.isPresent() && result.get().equals(ButtonType.OK)) {
                    addProjectController.removeResponsible(selectedResponsible);
                    responsibleComboBox.getSelectionModel().clearSelection();
                }
            }
        });
        return removeResponsibleButton;
    }

    private Button getOkButton() {
        Button okButton = new Button("Ok");
        okButton.setOnAction(event -> {
            String description = descriptionField.getText();
            if (description != null && costCenterComboBox.getValue() != null && responsibleComboBox.getValue() != null) {
                if (description.contains(";")) {
                    // Display error message if description contains a semicolon
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Dialog");
                    alert.setHeaderText(null);
                    alert.setContentText("Description cannot contain a semicolon (;).");
                    alert.showAndWait();
                    return;
                }

                addProjectController.getProjectDAO().addProject(
                        description,
                        costCenterComboBox.getValue(),
                        responsibleComboBox.getValue(),
                        addProjectController.getCurrentUser());

                Stage currentStage = (Stage) okButton.getScene().getWindow();

                addProjectController.getMainWindowView().updateProjectTableView();

                currentStage.close();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setHeaderText(null);
                alert.setContentText("Please make sure all fields are filled and selected!");

                alert.showAndWait();
            }
        });
        return okButton;
    }

    private Button getCancelButton() {
        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(event -> {
            Stage currentStage = (Stage) cancelButton.getScene().getWindow();
            currentStage.close();
        });
        return cancelButton;
    }

    private void setButtonStyle(Button... buttons) {
        for (Button button : buttons) {
            button.setMinWidth(130);
            button.setMaxWidth(130);
            button.setMinHeight(25);
            button.setMaxHeight(25);
        }
    }

    private void setButtonSVGStyle(Button... buttons) {
        for (Button button : buttons) {
            button.getStyleClass().add("svg-button");
        }
    }
}
