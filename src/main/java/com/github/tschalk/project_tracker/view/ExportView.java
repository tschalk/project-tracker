package com.github.tschalk.project_tracker.view;

import com.github.tschalk.project_tracker.controller.ExportController;
import com.github.tschalk.project_tracker.model.Project;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

public class ExportView extends VBox {

    private static final String FROM_PROMPT_TEXT = "From";
    private static final String TO_PROMPT_TEXT = "To";
    private static final String CHOOSE_DIRECTORY_TEXT = "Choose Directory";
    private static final String EXPORT_TEXT = "Export";

    private ExportController exportController;
    private DirectoryChooser directoryChooser;
    private File directory;

    public ExportView(ExportController exportController) {
        this.exportController = exportController;
        this.directoryChooser = initializeDirectoryChooser();
        this.directory = directoryChooser.getInitialDirectory();
        initializeUI();
    }

    private DirectoryChooser initializeDirectoryChooser() {
        DirectoryChooser chooser = new DirectoryChooser();
        Path desktopPath = getDesktopPath();
        chooser.setInitialDirectory(desktopPath.toFile());

        return chooser;
    }

    private Path getDesktopPath() {
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains("win")) {
            return Paths.get(System.getProperty("user.home"), "Desktop");
        } else {
            return Paths.get(System.getProperty("user.home"));
        }
    }

    private void initializeUI() {

        this.setSpacing(10);
        this.setPadding(new Insets(10));

        DatePicker fromDatePicker = createFromDatePicker();
        DatePicker toDatePicker = createToDatePicker();

        Button directoryButton = createDirectoryButton();
        Button exportButton = createExportButton(fromDatePicker, toDatePicker);

        this.getChildren().addAll(fromDatePicker, toDatePicker, directoryButton, exportButton);
    }

    private DatePicker createFromDatePicker() {
        DatePicker fromDatePicker = new DatePicker();
        fromDatePicker.setPromptText(FROM_PROMPT_TEXT);
        LocalDate now = LocalDate.now();
        while (now.getDayOfWeek() != DayOfWeek.MONDAY) {
            now = now.minusDays(1);
        }
        fromDatePicker.setValue(now);

        return fromDatePicker;
    }

    private DatePicker createToDatePicker() {
        DatePicker datePicker = new DatePicker();
        datePicker.setPromptText(TO_PROMPT_TEXT);
        datePicker.setValue(LocalDate.now());

        return datePicker;
    }

    private Button createDirectoryButton() {
        Button directoryButton = new Button(CHOOSE_DIRECTORY_TEXT);
        directoryButton.setOnAction(e -> directory = directoryChooser.showDialog(getScene().getWindow()));

        return directoryButton;
    }

    private Button createExportButton(DatePicker fromDatePicker, DatePicker toDatePicker) {
        Button exportButton = new Button(EXPORT_TEXT);
        exportButton.setOnAction(e -> {
            List<Project> projects = exportController.getProjects();
            Path directoryPath = Paths.get(directory.getPath());
            exportController.generateCSV(projects, fromDatePicker.getValue(), toDatePicker.getValue(), directoryPath);
        });
        return exportButton;
    }
}
