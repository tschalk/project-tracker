package com.github.tschalk.project_tracker.view;

import com.github.tschalk.project_tracker.controller.AdminChangePasswordController;
import com.github.tschalk.project_tracker.utils.CustomTitleBar;
import com.github.tschalk.project_tracker.utils.SceneManager;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import static com.github.tschalk.project_tracker.utils.SceneManager.MAIN_WINDOW_SCENE;

public class ChangePasswordView extends BorderPane {
    private final PasswordField newPasswordField;
    private final PasswordField confirmNewPasswordField;
    private final AdminChangePasswordController controller;
    private final Stage stage;

    public ChangePasswordView(AdminChangePasswordController controller, Stage stage) {
        this.controller = controller;
        this.stage = stage;
        this.newPasswordField = new PasswordField();
        this.confirmNewPasswordField = new PasswordField();
        initializeUI();
    }

    private void initializeUI() {
        // This
        this.setPadding(new Insets(0, 0, 15, 0));

        // Top
        CustomTitleBar customTitleBar = new CustomTitleBar(stage,"Edit Project");
        this.setTop(customTitleBar);

        // Center
        Label titleLabel = new Label("Change Password");

        GridPane gridPane = getGridPane();

        Button changePasswordButton = getChangePasswordButton();

        HBox actionButtonContainer = new HBox(10);
        actionButtonContainer.getChildren().addAll(changePasswordButton);
        actionButtonContainer.getStyleClass().add("button-container");

        VBox contentBox = new VBox(10);
        contentBox.setPadding(new Insets(10));
        contentBox.getChildren().addAll(titleLabel, gridPane, actionButtonContainer);

        this.setCenter(contentBox);
    }

    @NotNull
    private Button getChangePasswordButton() {
        Button changePasswordButton = new Button("Change Password");
        changePasswordButton.setOnAction(e -> changePassword());

        return changePasswordButton;
    }

    @NotNull
    private GridPane getGridPane() {
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        Label newPasswordLabel = new Label("New Password:");
        gridPane.add(newPasswordLabel, 0, 0); // column, row
        gridPane.add(newPasswordField, 1, 0);

        Label confirmNewPasswordLabel = new Label("Confirm Password:");
        gridPane.add(confirmNewPasswordLabel, 0, 1);
        gridPane.add(confirmNewPasswordField, 1, 1);

        return gridPane;
    }

    private void changePassword() {
        String newPassword = newPasswordField.getText();
        String confirmNewPassword = confirmNewPasswordField.getText();

        if(newPassword.isEmpty() || confirmNewPassword.isEmpty()) {
            showAlert("New password cannot be empty.");
            return;
        }

        if(!newPassword.equals(confirmNewPassword)) {
            showAlert("Passwords do not match.");
            return;
        }

        boolean passwordChangeSuccessful = controller.updatePassword(newPassword);
        if(passwordChangeSuccessful) {
            showAlert("Password changed successfully!");
            SceneManager.getInstance().showCustomScene(MAIN_WINDOW_SCENE, stage);
        } else {
            showAlert("Password change failed!");
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait();
    }
}
