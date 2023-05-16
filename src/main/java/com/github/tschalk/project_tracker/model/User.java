package com.github.tschalk.project_tracker.model;

public class User {
    int id;
    String name;
    String password;

    public User(int id, String username, String password) { // Für: Login
        this.id = id;
        this.name = username;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }
}
