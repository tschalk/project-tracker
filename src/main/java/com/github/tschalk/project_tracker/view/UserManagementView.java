package com.github.tschalk.project_tracker.view;

import com.github.tschalk.project_tracker.controller.UserManagementController;
import com.github.tschalk.project_tracker.model.User;
import com.github.tschalk.project_tracker.util.AlertUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.jetbrains.annotations.NotNull;

public class UserManagementView extends VBox {
    private final TextField usernameField;
    private final ComboBox<String> roleComboBox;
    private final UserManagementController userManagementController;
    private final ListView<User> userList;
    private final CheckBox activeUserChekBox;

    public UserManagementView(UserManagementController userManagementController) {
        this.userManagementController = userManagementController;
        this.usernameField = new TextField();
        this.roleComboBox = new ComboBox<>();
        this.userList = new ListView<>();
        this.activeUserChekBox = new CheckBox();
        updateUserList();
        initializeUI();
    }

    private void initializeUI() {
        Label titleLabel = new Label("User Management");

        GridPane gridPane = getGridPane();

        Button addButton = new Button("Add User");
        addButton.setOnAction(e -> addUser());

        Button updateButton = new Button("Update User");
        updateButton.setOnAction(e -> updateUser());

        Button deleteButton = new Button("Delete User");
        deleteButton.setOnAction(e -> deleteUser());

        HBox buttonContainer = new HBox(10);
        buttonContainer.getChildren().addAll(addButton, updateButton, deleteButton);
        buttonContainer.getStyleClass().add("button-container");

        userList.setPrefHeight(150);
        userList.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            if (newValue != null) {
                usernameField.setText(newValue.getName());
                roleComboBox.setValue(newValue.getRole());
                activeUserChekBox.setSelected(newValue.isActive());
            }
        });

        VBox contentBox = new VBox(10);
        contentBox.setPadding(new Insets(10, 10, 10, 10));
        contentBox.getChildren().addAll(titleLabel, gridPane, userList, buttonContainer);
        this.getChildren().add(contentBox);
    }

    @NotNull
    private GridPane getGridPane() {
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        Label usernameLabel = new Label("Username:");
        gridPane.add(usernameLabel, 0, 0);
        gridPane.add(usernameField, 1, 0);

        Label roleLabel = new Label("Role:");
        gridPane.add(roleLabel, 0, 1);
        gridPane.add(roleComboBox, 1, 1);
        roleComboBox.getItems().addAll("admin", "user");

        Label activeUserLabel = new Label("Active User:");
        gridPane.add(activeUserLabel, 0, 2);
        gridPane.add(activeUserChekBox, 1, 2);

        return gridPane;
    }

    private void addUser() {
        String username = usernameField.getText();
        String role = roleComboBox.getValue();

        if (username.isEmpty() || role == null) {
            AlertUtils.showAlert(Alert.AlertType.WARNING, "Warning", null, "Username or Role cannot be empty.");
            return;
        }

        boolean addUserSuccessful = userManagementController.addUser(username, role);
        if (addUserSuccessful) {
            System.out.println("User added successfully!");
            usernameField.clear();
            roleComboBox.setValue(null);
            User createdUser = userManagementController.getUser(username);
            String password = createdUser.getPassword();

            updateUserList();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText(null);

            Button button = new Button("Copy Password to Clipboard");
            button.setOnAction(e -> {
                final Clipboard clipboard = Clipboard.getSystemClipboard();
                final ClipboardContent content = new ClipboardContent();
                content.putString(password);
                clipboard.setContent(content);
            });

            VBox vbox = new VBox();
            vbox.getChildren().addAll(new Label("User created with password: " + password), button);
            alert.getDialogPane().setContent(vbox);

            alert.showAndWait();
        } else {
            AlertUtils.showAlert(Alert.AlertType.WARNING, "Warning", null, "Adding user failed! User already exists or you used special characters.");
        }
    }

    private void updateUser() {
        String username = usernameField.getText();
        String role = roleComboBox.getValue();

        if (username.isEmpty() || role == null) {
            AlertUtils.showAlert(Alert.AlertType.WARNING, "Warning", null, "Username or Role cannot be empty.");
            return;
        }

        User currentUser = userManagementController.getUser(username);
        String currentRole = currentUser.getRole();
        boolean currentIsActive = currentUser.isActive();

        if (currentUser.getName().equals("admin")) {
            AlertUtils.showAlert(Alert.AlertType.WARNING, "Warning", null, "This admin cannot be updated!");
            return;
        }

        boolean updateUserSuccessful = userManagementController.updateUser(username, role, activeUserChekBox.isSelected());
        if (updateUserSuccessful) {
            usernameField.clear();
            roleComboBox.setValue(null);
            User updatedUser = userManagementController.getUser(username);

            if (!currentRole.equals(updatedUser.getRole())) {
                AlertUtils.showAlert(Alert.AlertType.INFORMATION, "Information", null, "User role updated to: " + updatedUser.getRole());
            }

            if (currentIsActive != updatedUser.isActive()) {
                AlertUtils.showAlert(Alert.AlertType.INFORMATION, "Information", null, "User active status updated to: " + (updatedUser.isActive() ? "Active" : "Inactive"));
            }

            updateUserList();
        } else {
            AlertUtils.showAlert(Alert.AlertType.WARNING, "Warning", null, "Updating user failed! User already exists.");
        }
    }

    private void deleteUser() {
        String username = usernameField.getText();

        if (username.isEmpty()) {
            updateUserList();
            AlertUtils.showAlert(Alert.AlertType.WARNING, "Warning", null, "Username cannot be empty.");
            return;
        }

        boolean deleteUserSuccessful = userManagementController.deleteUser(username);
        if (deleteUserSuccessful) {
            updateUserList();
            System.out.println("User deleted successfully!");
            AlertUtils.showAlert(Alert.AlertType.INFORMATION, "Information", null, "User deleted successfully!");
            usernameField.clear();
            roleComboBox.setValue(null);
        } else {
            AlertUtils.showAlert(Alert.AlertType.WARNING, "Warning", null, "Deleting user failed!");
        }
    }

    private void updateUserList() {
        ObservableList<User> observableList = FXCollections.observableArrayList(userManagementController.getAllUsers());
        userList.setItems(observableList);
    }
}
