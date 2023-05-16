package com.github.tschalk.project_tracker.controller;

import com.github.tschalk.project_tracker.dao.UserDAO;
import com.github.tschalk.project_tracker.model.User;

public class UserLoginController {

    private final UserDAO userDAO;

    public UserLoginController(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public boolean login(String username, String password) {
        try {
            User user = userDAO.readUserByUsername(username);
            if (user != null && user.getPassword().equals(password)) {
                // Login successful
                return true;
            } else {
                // Login failed
                return false;
            }
        } finally {
            userDAO.closeConnection(); // Da kein try-with-resources
        }
    }

    public UserDAO getUserDAO() {
        return userDAO;
    }
}
