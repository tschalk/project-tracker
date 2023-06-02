package com.github.tschalk.project_tracker.util;

@FunctionalInterface
public interface Sanitizer {
    String sanitize(String sqlInput);

    static Sanitizer getDefault() {
        return (input) -> input == null ? null :
                input.replaceAll("[<>&\"'\n\r\t]|--", "");
    }
}
