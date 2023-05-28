package com.github.tschalk.project_tracker.view;

import com.github.tschalk.project_tracker.controller.ChangePasswordController;
import com.github.tschalk.project_tracker.utils.CustomTitleBar;
import com.github.tschalk.project_tracker.utils.SceneManager;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import static com.github.tschalk.project_tracker.utils.SceneManager.MAIN_WINDOW_SCENE;

public class ChangePasswordView extends BorderPane {
    private final PasswordField newPasswordField;
    private final PasswordField confirmNewPasswordField;
    private final ChangePasswordController changePasswordController;
    private final Stage stage;

    boolean hasUppercase;
    boolean hasLowercase;
    boolean hasNumber;
    boolean hasSpecialChar;

    public ChangePasswordView(ChangePasswordController changePasswordController, Stage stage) {
        this.changePasswordController = changePasswordController;
        this.stage = stage;
        this.newPasswordField = new PasswordField();
        this.confirmNewPasswordField = new PasswordField();
        initializeUI();
    }

    private void initializeUI() {
        // This
        this.setPadding(new Insets(0, 0, 15, 0));

        // Top
        CustomTitleBar customTitleBar = new CustomTitleBar(stage, "Edit Project");
        this.setTop(customTitleBar);

        // Center
        Label titleLabel = new Label("Change Password:");

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

        if (newPassword == null || confirmNewPassword == null) {
            showAlert("New password and confirmation must not be null.", Alert.AlertType.ERROR);
            return;
        }

        if (newPassword.isEmpty() || confirmNewPassword.isEmpty()) {
            showAlert("New password and confirmation must not be empty.", Alert.AlertType.ERROR);
            return;
        }

        if (newPassword.length() < 8 || !isPasswordValid(newPassword)) {
            showAlert("Password must be at least 8 characters long and contain at least one uppercase letter," +
                    " one lowercase letter, one number, and one special character.", Alert.AlertType.ERROR);
            return;
        }

        if (!newPassword.equals(confirmNewPassword)) {
            showAlert("Passwords do not match.", Alert.AlertType.ERROR);
            return;
        }

        boolean passwordChangeSuccessful = changePasswordController.updatePassword(newPassword);
        showPasswordChangeResult(passwordChangeSuccessful);
    }

    private boolean isPasswordValid(String newPassword) {
        for (char c : newPassword.toCharArray()) {
            if (Character.isUpperCase(c)) {
                hasUppercase = true;
            } else if (Character.isLowerCase(c)) {
                hasLowercase = true;
            } else if (Character.isDigit(c)) {
                hasNumber = true;
            } else if (!Character.isLetterOrDigit(c)) {
                hasSpecialChar = true;
            }
        }
        return hasUppercase && hasLowercase && hasNumber && hasSpecialChar;
    }

    private void showPasswordChangeResult(boolean success) {
        if (success) {
            showAlert("Password changed successfully!", Alert.AlertType.INFORMATION);
            SceneManager.getInstance().showCustomScene(MAIN_WINDOW_SCENE, stage);
        } else {
            showAlert("Password change failed!", Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(alertType == Alert.AlertType.ERROR ? "Error" : "Success");

        if (alertType == Alert.AlertType.ERROR) {
            alert.setHeaderText("Error changing password");
        } else {
            alert.setHeaderText("Password changed successfully!");
        }

        TextArea textArea = new TextArea(message);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setMaxHeight(Double.MAX_VALUE);
        textArea.setMaxWidth(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(textArea, 0, 0);
        alert.getDialogPane().setContent(expContent);

        alert.showAndWait();
    }


}
