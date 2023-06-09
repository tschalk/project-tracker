package com.github.tschalk.project_tracker.view;

import com.github.tschalk.project_tracker.controller.ExportController;
import com.github.tschalk.project_tracker.model.Project;
import com.github.tschalk.project_tracker.util.AlertUtils;
import com.github.tschalk.project_tracker.util.CustomTitleBar;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class ExportView extends BorderPane {
    private static final String FROM_PROMPT_TEXT = "From";
    private static final String TO_PROMPT_TEXT = "To";
    private static final String CHOOSE_DIRECTORY_TEXT = "Choose Directory";
    private static final String EXPORT_TEXT = "Export";

    private final ExportController exportController;
    private final DirectoryChooser directoryChooser;
    private File directory;

    public ExportView(ExportController exportController) {
        this.exportController = exportController;
        this.directoryChooser = initializeDirectoryChooser();
        this.directory = directoryChooser.getInitialDirectory();
        initializeUI();
    }

    private void initializeUI() {
        // This
        this.setPadding(new Insets(0, 0, 15, 0));

        // Top
//        CustomTitleBar customTitleBar = new CustomTitleBar("Export");
//        customTitleBar.showCloseButton(false);
//        this.setTop(customTitleBar);

        // Center
        Label titleLabel = new Label("Export project");

        DatePicker fromDatePicker = getFromDatePicker();
        DatePicker toDatePicker = getToDatePicker();

        Button directoryButton = getDirectoryButton();
        Button exportButton = getExportButton(fromDatePicker, toDatePicker);

        GridPane gridPane = getGridPane(fromDatePicker, toDatePicker);

        HBox actionButtonContainer = new HBox(10);
        actionButtonContainer.getChildren().addAll(directoryButton, exportButton);
        actionButtonContainer.getStyleClass().add("button-container");

        VBox centerContainer = new VBox(10);
        centerContainer.setPadding(new Insets(10));
        centerContainer.getChildren().addAll(titleLabel, gridPane, actionButtonContainer);

        this.setCenter(centerContainer);

    }

    private static GridPane getGridPane(DatePicker fromDatePicker, DatePicker toDatePicker) {
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        Label fromLabel = new Label("From");
        gridPane.add(fromLabel, 0, 0);
        gridPane.add(fromDatePicker, 1, 0);

        Label toLabel = new Label("To");
        gridPane.add(toLabel, 0, 1);
        gridPane.add(toDatePicker, 1, 1);

        return gridPane;
    }

    private DirectoryChooser initializeDirectoryChooser() {
        DirectoryChooser chooser = new DirectoryChooser();

        return chooser;
    }

    private DatePicker getFromDatePicker() {
        DatePicker fromDatePicker = new DatePicker();
        fromDatePicker.setPromptText(FROM_PROMPT_TEXT);
        LocalDate now = LocalDate.now();
        while (now.getDayOfWeek() != DayOfWeek.MONDAY) {
            now = now.minusDays(1);
        }
        fromDatePicker.setValue(now);

        return fromDatePicker;
    }

    private DatePicker getToDatePicker() {
        DatePicker datePicker = new DatePicker();
        datePicker.setPromptText(TO_PROMPT_TEXT);
        datePicker.setValue(LocalDate.now());

        return datePicker;
    }

    private Button getDirectoryButton() {
        Button directoryButton = new Button(CHOOSE_DIRECTORY_TEXT);
    directoryButton.setOnAction(e -> {
        File selectedDirectory = directoryChooser.showDialog(getScene().getWindow());
        if (selectedDirectory != null) {
            directory = selectedDirectory;
        } else {
            directory = directoryChooser.getInitialDirectory();
        }
    });
        return directoryButton;
    }

    private Button getExportButton(DatePicker fromDatePicker, DatePicker toDatePicker) {
        Button exportButton = new Button(EXPORT_TEXT);
        exportButton.setOnAction(e -> {

            Optional<ButtonType> result = AlertUtils.showAlert(
                    Alert.AlertType.CONFIRMATION,
                    "Confirmation Dialog", "Exporting data",
                    "Are you sure you want to export the data?"
            );

            if (result.isPresent() && result.get() == ButtonType.NO) {
                return;
            }

            List<Project> projects = exportController.getProjects();
            Path directoryPath = Paths.get(directory.getPath());
            boolean hasExported = exportController.generateCSV(
                    projects,
                    fromDatePicker.getValue(),
                    toDatePicker.getValue(),
                    directoryPath
            );

            if(!hasExported) {
                AlertUtils.showAlert(Alert.AlertType.ERROR,
                        "Error Dialog", "Error while exporting data",
                        "Please proof the data and try again."
                );
                return;
            }
            Stage stage = (Stage) this.getScene().getWindow();
            stage.close();
        });
        return exportButton;
    }
}
