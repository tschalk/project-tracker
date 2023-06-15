package com.github.tschalk.project_tracker.controller;

import com.github.tschalk.project_tracker.dao.UserDAO;
import com.github.tschalk.project_tracker.model.User;
import com.github.tschalk.project_tracker.util.SimplePasswordEncryption;

public class ChangePasswordController {
    private final UserDAO userDAO;
    private final User currentUser;

    public ChangePasswordController(UserLoginController userLoginController) {
        this.userDAO = userLoginController.getUserDAO();
        this.currentUser = userLoginController.getCurrentUser();
    }

    public boolean updatePassword(String newPassword) {
        return this.currentUser != null && userDAO.updatePassword(currentUser.getId(), newPassword);
    }
}
