package com.github.tschalk.project_tracker.controller;

import com.github.tschalk.project_tracker.dao.UserDAO;
import com.github.tschalk.project_tracker.model.User;

public class UserLoginController {

    private final UserDAO userDAO;

    public UserLoginController(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public boolean login(String username, String password) {

            userDAO.setConnection(userDAO.getDatabaseManager().getConnection()); // Hier wird die Connection gesetzt, die in der UserDAO verwendet wird
            User user = userDAO.readUserByUsername(username);
            if (user != null && user.getPassword().equals(password)) {
                return true;
            } else {
                System.err.println("Login failed!");
                return false;
            }
    }

    public UserDAO getUserDAO() {
        return userDAO;
    }
}
