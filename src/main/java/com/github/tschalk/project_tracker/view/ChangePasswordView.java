package com.github.tschalk.project_tracker.view;

import com.github.tschalk.project_tracker.controller.AdminChangePasswordController;
import com.github.tschalk.project_tracker.utils.SceneManager;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ChangePasswordView extends VBox {
    private final PasswordField newPasswordField;
    private final PasswordField confirmNewPasswordField;
    private final AdminChangePasswordController controller;
    private final Stage stage;

    public ChangePasswordView(AdminChangePasswordController controller, Stage stage) {
        this.controller = controller;
        this.stage = stage;

        this.newPasswordField = new PasswordField();
        this.confirmNewPasswordField = new PasswordField();

        initUI();
    }

    private void initUI() {
        this.setSpacing(10);
        this.setPadding(new Insets(10));

        Label titleLabel = new Label("Change Password");

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        Label newPasswordLabel = new Label("New Password:");
        gridPane.add(newPasswordLabel, 0, 0);
        gridPane.add(newPasswordField, 1, 0);

        Label confirmNewPasswordLabel = new Label("Confirm Password:");
        gridPane.add(confirmNewPasswordLabel, 0, 1);
        gridPane.add(confirmNewPasswordField, 1, 1);

        Button changePasswordButton = new Button("Change Password");
        changePasswordButton.setOnAction(e -> changePassword());

        this.getChildren().addAll(titleLabel, gridPane, changePasswordButton);
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
            stage.close();
            SceneManager.getInstance().showNewWindowWithCustomScene("Main Window");
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
