package com.github.tschalk.project_tracker.model;

public class User {
    int id;
    String name;
    String password;
    String role;

    public User(int id, String username, String password) {
        this(id, username, password, null);
    }

    public User(int id, String username, String password, String role) {
        this.id = id;
        this.name = username;
        this.password = password;
        this.role = role;
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

    @Override
    public String toString() {
        return name + " (" + role + ")";
    }
}
