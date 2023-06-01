package com.github.tschalk.project_tracker.utils;

import javafx.scene.shape.SVGPath;
import java.util.HashMap;
import java.util.Map;

public class SVGManager {

    private static final String ADD_ICON = "M19,13H13V19H11V13H5V11H11V5H13V11H19V13Z";
    private static final String EXPORT_ICON = "M23,12L19,8V11H10V13H19V16M1,18V6C1,4.89 1.9,4 3,4H15A2,2 0 0,1 17," +
            "6V9H15V6H3V18H15V15H17V18A2,2 0 0,1 15,20H3A2,2 0 0,1 1,18Z";
    private static final String EDIT_ICON = "M19,19V5H5V19H19M19,3A2,2 0 0,1 21,5V19C21,20.11 20.1,21 19,21H5A2,2 " +
            "0 0,1 3,19V5A2,2 0 0,1 5,3H19M16.7,9.35L15.7,10.35L13.65,8.3L14.65,7.3C14.86,7.08 15.21,7.08 15.42,7.3L" +
            "16.7,8.58C16.92,8.79 16.92,9.14 16.7,9.35M7,14.94L13.06,8.88L15.12,10.94L9.06,17H7V14.94Z";
    private static final String START_ICON = "M12,20A7,7 0 0,1 5,13A7,7 0 0,1 12,6A7,7 0 0,1 19,13A7,7 0 0,1 12,20M" +
            "19.03,7.39L20.45,5.97C20,5.46 19.55,5 19.04,4.56L17.62,6C16.07,4.74 14.12,4 12,4A9,9 0 0,0 3,13A9,9 0 " +
            "0,0 12,22C17,22 21,17.97 21,13C21,10.88 20.26,8.93 19.03,7.39M11,14H13V8H11M15,1H9V3H15V1Z";
    private static final String STOP_ICON = "M11 8H13V14H11V8M12 20C8.13 20 5 16.87 5 13S8.13 6 12 6 19 9.13 19 13C" +
            "19.7 13 20.36 13.13 21 13.35C21 13.23 21 13.12 21 13C21 10.88 20.26 8.93 19.03 7.39L20.45 5.97C20 5.46" +
            " 19.55 5 19.04 4.56L17.62 6C16.07 4.74 14.12 4 12 4C7.03 4 3 8.03 3 13S7.03 22 12 22C12.59 22 13.16 21." +
            "94 13.71 21.83C13.4 21.25 13.18 20.6 13.08 19.91C12.72 19.96 12.37 20 12 20M15 1H9V3H15V1M16.5 16.5V21.5" +
            "H21.5V16.5H16.5Z";
    private static final String USER_MANAGEMENT_ICON = "M12,5.5A3.5,3.5 0 0,1 15.5,9A3.5,3.5 0 0,1 12,12.5A3.5,3.5 " +
            "0 0,1 8.5,9A3.5,3.5 0 0,1 12,5.5M5,8C5.56,8 6.08,8.15 6.53,8.42C6.38,9.85 6.8,11.27 7.66,12.38C7.16,13.3" +
            "4 6.16,14 5,14A3,3 0 0,1 2,11A3,3 0 0,1 5,8M19,8A3,3 0 0,1 22,11A3,3 0 0,1 19,14C17.84,14 16.84,13.34 16." +
            "34,12.38C17.2,11.27 17.62,9.85 17.47,8.42C17.92,8.15 18.44,8 19,8M5.5,18.25C5.5,16.18 8.41,14.5 12,14.5" +
            "C15.59,14.5 18.5,16.18 18.5,18.25V20H5.5V18.25M0,20V18.5C0,17.11 1.89,15.94 4.45,15.6C3.86,16.28 3.5,17" +
            ".22 3.5,18.25V20H0M24,20H20.5V18.25C20.5,17.22 20.14,16.28 19.55,15.6C22.11,15.94 24,17.11 24,18.5V20Z";
    private static final String WINDOW_CLOSE_ICON = "M13.46,12L19,17.54V19H17.54L12,13.46L6.46,19H5V17.54L10.54,12L5," +
            "6.46V5H6.46L12,10.54L17.54,5H19V6.46L13.46,12Z";
    private static final String REMOVE_ICON = "M19,13H5V11H19V13Z";
    private static final String SETTINGS_ICON = "M12,15.5A3.5,3.5 0 0,1 8.5,12A3.5,3.5 0 0,1 12,8.5A3.5,3.5 0 0,1 " +
            "15.5,12A3.5,3.5 0 0,1 12,15.5M19.43,12.97C19.47,12.65 19.5,12.33 19.5,12C19.5,11.67 19.47,11.34 19.43" +
            ",11L21.54,9.37C21.73,9.22 21.78,8.95 21.66,8.73L19.66,5.27C19.54,5.05 19.27,4.96 19.05,5.05L16.56," +
            "6.05C16.04,5.66 15.5,5.32 14.87,5.07L14.5,2.42C14.46,2.18 14.25,2 14,2H10C9.75,2 9.54,2.18 9.5,2.42L9.13" +
            ",5.07C8.5,5.32 7.96,5.66 7.44,6.05L4.95,5.05C4.73,4.96 4.46,5.05 4.34,5.27L2.34,8.73C2.21,8.95 2.27," +
            "9.22 2.46,9.37L4.57,11C4.53,11.34 4.5,11.67 4.5,12C4.5,12.33 4.53,12.65 4.57,12.97L2.46,14.63C2.27,14.78 " +
            "2.21,15.05 2.34,15.27L4.34,18.73C4.46,18.95 4.73,19.03 4.95,18.95L7.44,17.94C7.96,18.34 8.5,18.68 9.13," +
            "18.93L9.5,21.58C9.54,21.82 9.75,22 10,22H14C14.25,22 14.46,21.82 14.5,21.58L14.87,18.93C15.5,18.67 16.04," +
            "18.34 16.56,17.94L19.05,18.95C19.27,19.03 19.54,18.95 19.66,18.73L21.66,15.27C21.78,15.05 21.73,14.78 " +
            "21.54,14.63L19.43,12.97Z";
    private static final String DATABASE_RESTORE_ICON = "M12 14C9.58 14 7.3 13.4 6 12.45V9.64C7.47 10.47 9.61 11 12 " +
            "11S16.53 10.47 18 9.64V12.08C18.33 12.03 18.66 12 19 12C19.34 12 19.67 12.03 20 12.08V7C20 4.79 16.42 " +
            "3 12 3S4 4.79 4 7V17C4 19.21 7.59 21 12 21C12.1 21 12.2 21 12.29 21C12.11 20.36 12 19.69 12 19C8.13 19" +
            " 6 17.5 6 17V14.77C7.61 15.55 9.72 16 12 16C12.24 16 12.47 16 12.7 15.97C13.1 15.14 13.65 14.41 14.32 1" +
            "3.81C13.58 13.93 12.8 14 12 14M12 5C15.87 5 18 6.5 18 7S15.87 9 12 9 6 7.5 6 7 8.13 5 12 5M22.7 19.6V18" +
            ".6L23.8 17.8C23.9 17.7 24 17.6 23.9 17.5L22.9 15.8C22.9 15.7 22.7 15.7 22.6 15.7L21.4 16.2C21.1 16 20.8" +
            " 15.8 20.5 15.7L20.3 14.4C20.3 14.3 20.2 14.2 20.1 14.2H18.1C17.9 14.2 17.8 14.3 17.8 14.4L17.6 15.7C17." +
            "3 15.9 17.1 16 16.8 16.2L15.6 15.7C15.5 15.7 15.4 15.7 15.3 15.8L14.3 17.5C14.3 17.6 14.3 17.7 14.4 17.8" +
            "L15.5 18.6V19.6L14.4 20.4C14.3 20.5 14.2 20.6 14.3 20.7L15.3 22.4C15.4 22.5 15.5 22.5 15.6 22.5L16.8 22" +
            "C17 22.2 17.3 22.4 17.6 22.5L17.8 23.8C17.9 23.9 18 24 18.1 24H20.1C20.2 24 20.3 23.9 20.3 23.8L20.5 22" +
            ".5C20.8 22.3 21 22.2 21.3 22L22.5 22.4C22.6 22.4 22.7 22.4 22.8 22.3L23.8 20.6C23.9 20.5 23.9 20.4 23.8" +
            " 20.4L22.7 19.6M19 20.5C18.2 20.5 17.5 19.8 17.5 19S18.2 17.5 19 17.5 20.5 18.2 20.5 19 19.8 20.5 19 20.5Z";

    private static SVGManager instance;
    private final Map<String, SVGPath> svgPaths;

    private SVGManager() {
        svgPaths = new HashMap<>();
        initializeSVGPaths();
    }

    public static SVGManager getInstance() {
        if (instance == null) {
            instance = new SVGManager();
        }
        return instance;
    }

    private void initializeSVGPaths() {
        addSVGPath("addIcon", ADD_ICON, "plus-icon");
        addSVGPath("removeIcon", REMOVE_ICON, "remove-icon");
        addSVGPath("exportIcon", EXPORT_ICON, "export-icon");
        addSVGPath("editIcon", EDIT_ICON, "edit-icon");
        addSVGPath("startIcon", START_ICON, "stopwatch-icon");
        addSVGPath("stopIcon", STOP_ICON, "stop-icon");
        addSVGPath("userManagementIcon", USER_MANAGEMENT_ICON, "user-management-icon");
        addSVGPath("windowCloseIcon", WINDOW_CLOSE_ICON, "window-close-icon");
        addSVGPath("settingsIcon", SETTINGS_ICON, "settings-icon");
        addSVGPath("databaseRestoreIcon", DATABASE_RESTORE_ICON, "database-restore-icon");
    }
    private void addSVGPath(String name, String content, String styleClass) {
        SVGPath svgPath = new SVGPath();
        svgPath.setContent(content);
        svgPath.getStyleClass().add(styleClass);
        svgPaths.put(name, svgPath);
    }

    public SVGPath getSVGPath(String name) {
        SVGPath original = svgPaths.get(name);
        return original != null ? cloneSVGPath(original) : null;
    }

    private SVGPath cloneSVGPath(SVGPath original) {
        SVGPath clone = new SVGPath();
        clone.setContent(original.getContent());
        clone.getStyleClass().addAll(original.getStyleClass());
        return clone;
    }
}
