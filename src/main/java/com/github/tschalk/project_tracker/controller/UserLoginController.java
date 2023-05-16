package com.github.tschalk.project_tracker.controller;

import com.github.tschalk.project_tracker.dao.UserDAO;
import com.github.tschalk.project_tracker.model.User;

public class UserLoginController {

    private final UserDAO userDAO;
    private User currentUser;

    public UserLoginController(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public boolean login(String username, String password) {

            userDAO.setConnection(userDAO.getDatabaseManager().getConnection()); // Hier wird die Connection gesetzt, die in der UserDAO verwendet wird
            currentUser = userDAO.readUserByUsername(username);
            if (currentUser != null && currentUser.getPassword().equals(password)) {
                return true;
            } else {
                System.err.println("Login failed!");
                return false;
            }
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public UserDAO getUserDAO() {
        return userDAO;
    }
}
