package com.github.tschalk.project_tracker.controller;

import com.github.tschalk.project_tracker.dao.UserDAO;
import com.github.tschalk.project_tracker.database.DatabaseConnectionManager;
import com.github.tschalk.project_tracker.model.User;

public class UserLoginController {

    private final UserDAO userDAO;
    private User currentUser;

    public UserLoginController(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public boolean login(String username, String password) {

        userDAO.setConnection(userDAO.getDatabaseManager().getConnection());
        currentUser = userDAO.getUser(username);

        if (currentUser != null && currentUser.isActive() && currentUser.getPassword().equals(password)) {
            return true;
        } else {
            System.err.println("Login failed!");
            return false;
        }
    }


    public UserDAO getUserDAO() {
        return userDAO;
    }

    public User getCurrentUser() {
        currentUser = userDAO.getUser(currentUser.getName());
        System.out.println("Current User: " + currentUser);
        return currentUser;
    }

    public DatabaseConnectionManager getDatabaseConnectionManager() {
        return userDAO.getDatabaseManager();
    }

}
