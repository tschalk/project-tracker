package com.github.tschalk.project_tracker.controller;

import com.github.tschalk.project_tracker.dao.UserDAO;
import com.github.tschalk.project_tracker.model.User;

public class ChangePasswordController {

    private final UserDAO userDAO;
    private final User currentUser;

    public ChangePasswordController(UserLoginController userLoginController) {
        this.userDAO = userLoginController.getUserDAO();
        this.currentUser = userLoginController.getCurrentUser();
    }

    public ChangePasswordController(UserDAO userDAO, User currentUser) {
        this.userDAO = userDAO;
        this.currentUser = currentUser;
    }

    public boolean updatePassword(String newPassword) {
        return this.currentUser != null
                ? userDAO.updatePassword(currentUser.getId(), newPassword)
                : false;
    }

    public boolean updatePassword(String newPassword, User currentUser) {
        return currentUser != null
                ? userDAO.updatePassword(currentUser.getId(), newPassword)
                : false;
    }

    public User getCurrentUser() {
        return this.currentUser;
    }
}