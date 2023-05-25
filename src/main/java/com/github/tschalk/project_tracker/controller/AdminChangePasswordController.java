package com.github.tschalk.project_tracker.controller;

import com.github.tschalk.project_tracker.dao.UserDAO;
import com.github.tschalk.project_tracker.model.User;

public class AdminChangePasswordController {

    private final UserDAO userDAO;
    private User currentUser;

    public AdminChangePasswordController( UserLoginController userLoginController) {
        this.userDAO = userLoginController.getUserDAO();
        this.currentUser = userLoginController.getCurrentUser();
    }

    public boolean updatePassword(String newPassword) {
        return currentUser != null
                ? userDAO.updatePassword(currentUser.getId(), newPassword)
                : false;
    }
}
