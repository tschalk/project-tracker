package com.github.tschalk.project_tracker.view;

import com.github.tschalk.project_tracker.controller.ChangePasswordController;
import com.github.tschalk.project_tracker.util.AlertUtils;
import com.github.tschalk.project_tracker.util.CustomTitleBar;
import com.github.tschalk.project_tracker.util.SceneManager;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import static com.github.tschalk.project_tracker.util.SceneManager.MAIN_WINDOW_SCENE;

public class ChangePasswordView extends BorderPane {
    private final PasswordField newPasswordField;
    private final PasswordField confirmNewPasswordField;
    private final ChangePasswordController changePasswordController;
    private final Stage stage;
    private boolean hasUppercase;
    private boolean hasLowercase;
    private boolean hasNumber;
    private boolean hasSpecialChar;
    private boolean isUserLoggedIn;

    public ChangePasswordView(ChangePasswordController changePasswordController, Stage stage) {
        this.changePasswordController = changePasswordController;
        this.stage = stage;
        this.newPasswordField = new PasswordField();
        this.confirmNewPasswordField = new PasswordField();
        initializeUI();
    }

    public ChangePasswordView(ChangePasswordController changePasswordController, Stage stage, boolean isUserLoggedIn) {
        this.changePasswordController = changePasswordController;
        this.stage = stage;
        this.newPasswordField = new PasswordField();
        this.confirmNewPasswordField = new PasswordField();
        this.isUserLoggedIn = isUserLoggedIn;
        initializeUI();
    }

    private void initializeUI() {
        // This
        this.setPadding(new Insets(0, 0, 15, 0));

        // Top
//        CustomTitleBar customTitleBar = new CustomTitleBar(/*stage,*/ "Password");
//        this.setTop(customTitleBar);
//        if (isUserLoggedIn) customTitleBar.showCloseButton(false);

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

        if (isUserLoggedIn) {
            Label currentPasswordLabel = new Label("Current Password:");
            PasswordField currentPasswordField = new PasswordField();
            gridPane.add(currentPasswordLabel, 0, 0); // column, row
            gridPane.add(currentPasswordField, 1, 0);
        }

        Label newPasswordLabel = new Label("New Password:");
        gridPane.add(newPasswordLabel, 0, isUserLoggedIn ? 1 : 0);
        gridPane.add(newPasswordField, 1, isUserLoggedIn ? 1 : 0);

        Label confirmNewPasswordLabel = new Label("Confirm Password:");
        gridPane.add(confirmNewPasswordLabel, 0, isUserLoggedIn ? 2 : 1);
        gridPane.add(confirmNewPasswordField, 1, isUserLoggedIn ? 2 : 1);

        return gridPane;
    }

    private void changePassword() {
        String newPassword = newPasswordField.getText();
        String confirmNewPassword = confirmNewPasswordField.getText();

        if (newPassword == null || confirmNewPassword == null) {
            AlertUtils.showAlert(Alert.AlertType.ERROR, "Error", "Error changing password", "New password and confirmation must not be null.");
            return;
        }

        if (newPassword.isEmpty() || confirmNewPassword.isEmpty()) {
            AlertUtils.showAlert(Alert.AlertType.ERROR, "Error", "Error changing password", "New password and confirmation must not be empty.");
            return;
        }

        if (newPassword.length() < 8 || !isPasswordValid(newPassword)) {
            AlertUtils.showAlert(Alert.AlertType.ERROR, "Error", "Error changing password", "Password must be at least 8 characters long and contain at least one uppercase letter, one lowercase letter, one number, and one special character.");
            return;
        }

        if (!newPassword.equals(confirmNewPassword)) {
            AlertUtils.showAlert(Alert.AlertType.ERROR, "Error", "Error changing password", "Passwords do not match.");
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
            AlertUtils.showAlert(Alert.AlertType.INFORMATION, "Success", "Password changed successfully!", "Password changed successfully!");
            if (isUserLoggedIn) {
                Stage stage = (Stage) this.getScene().getWindow();
                stage.close();
            } else {
                SceneManager.getInstance().showCustomScene(MAIN_WINDOW_SCENE, stage);
            }
        } else {
            AlertUtils.showAlert(Alert.AlertType.ERROR, "Error", "Error changing password", "Password change failed!");
        }
    }
}