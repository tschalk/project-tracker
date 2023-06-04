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

        return currentUser != null && currentUser.isActive() && currentUser.getPassword().equals(password);
    }

    public UserDAO getUserDAO() {
        return userDAO;
    }

    public User getCurrentUser() {
        return currentUser != null ?
                userDAO.getUser(currentUser.getName()) :
                null;
    }

    public DatabaseConnectionManager getDatabaseConnectionManager() {
        return userDAO.getDatabaseManager();
    }
}
