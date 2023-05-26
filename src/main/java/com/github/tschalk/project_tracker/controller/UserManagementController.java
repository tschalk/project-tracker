package com.github.tschalk.project_tracker.controller;

import com.github.tschalk.project_tracker.model.User;

import java.util.List;

public class UserManagementController {
    UserLoginController userLoginController;
    User curretnUser;

    public UserManagementController(UserLoginController userLoginController) {
        this.userLoginController = userLoginController;
        this.curretnUser = userLoginController.getCurrentUser();
    }

    public boolean addUser(String username, String role) {
        return userLoginController.getUserDAO().addUser(username, role);
    }

        public List<User> getAllUsers() {
            return userLoginController.getUserDAO().getAllUsers();
        }

    public boolean deleteUser(String username) {
        User user = userLoginController.getUserDAO().getUser(username);
        if (user.getId() == 1) {
            System.out.println("Admin user cannot be deleted.");
            return false;
        }

        return userLoginController.getUserDAO().deleteUser(username);
    }

}
