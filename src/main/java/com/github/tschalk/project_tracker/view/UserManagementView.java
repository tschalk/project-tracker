package com.github.tschalk.project_tracker.view;

import com.github.tschalk.project_tracker.controller.UserManagementController;
import com.github.tschalk.project_tracker.model.User;
import com.github.tschalk.project_tracker.utils.SimplePasswordEncryption;
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
    //    private final TextField roleField;
    private final ComboBox<String> roleComboBox;

    private final UserManagementController userManagementController;
    private final ListView<User> userList;
    private final CheckBox activeUserChekBox;

    public UserManagementView(UserManagementController userManagementController) {
        this.userManagementController = userManagementController;
        this.usernameField = new TextField();
//        this.roleField = new TextField();
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
            showAlert("Username or Role cannot be empty.");
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
            showAlert("Adding user failed! User already exists or you used special characters.");
        }
    }

    private void updateUser() {
        String username = usernameField.getText();
        String role = roleComboBox.getValue();

        if (username.isEmpty() || role == null) {
            showAlert("Username or Role cannot be empty.");
            return;
        }

        // Hier wird der aktuelle User geholt um zu prüfen, ob sich etwas geändert hat
        User currentUser = userManagementController.getUser(username);
        String currentRole = currentUser.getRole();
        boolean currentIsActive = currentUser.isActive();

        if (currentUser.getName().equals("admin")) {
            showAlert("This admin cannot be updated!");
            return;
        }

        boolean updateUserSuccessful = userManagementController.updateUser(username, role, activeUserChekBox.isSelected());
        if (updateUserSuccessful) {
            usernameField.clear();
            roleComboBox.setValue(null);
            User updatedUser = userManagementController.getUser(username);

            // Check, ob die Rolle geändert wurde
            if (!currentRole.equals(updatedUser.getRole())) {
                showAlert("User role updated to: " + updatedUser.getRole());
            }
            // Check, ob der Status geändert wurde
            if (currentIsActive != updatedUser.isActive()) {
                showAlert("User active status updated to: " + (updatedUser.isActive() ? "Active" : "Inactive"));
            }
            updateUserList();
        } else {
            showAlert("Updating user failed! User already exists.");
        }
    }


    private void deleteUser() {
        String username = usernameField.getText();

        if (username.isEmpty()) {
            updateUserList();
            showAlert("Username cannot be empty.");
            return;
        }

        boolean deleteUserSuccessful = userManagementController.deleteUser(username);
        if (deleteUserSuccessful) {
            updateUserList();
            System.out.println("User deleted successfully!");
            showAlert("User deleted successfully!");
            usernameField.clear();
            roleComboBox.setValue(null);
        } else {
            showAlert("Deleting user failed!");
        }
    }

    private void updateUserList() {
        ObservableList<User> observableList = FXCollections.observableArrayList(userManagementController.getAllUsers());
        userList.setItems(observableList);
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
