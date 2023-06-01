package com.github.tschalk.project_tracker.model;

public class User {
    private int id;
    private String name;
    private String password;
    private String role;
    private boolean isActive;

    public User(int id, String username, String password, String role, boolean isActive) {
        this.id = id;
        this.name = username;
        this.password = password;
        this.role = role;
        this.isActive = isActive;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public String toString() {
        return name + " (" + role + ")" + (isActive ? "" : " [Inactive]");
    }
}
