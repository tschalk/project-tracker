module com.github.tschalk.projecttracker {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.github.tschalk.projecttracker to javafx.fxml;
    exports com.github.tschalk.projecttracker;
}