package com.github.tschalk.project_tracker.controller;

import com.github.tschalk.project_tracker.model.User;

import java.util.List;
import java.util.Random;

public class UserManagementController {
    UserLoginController userLoginController;
    User curretnUser;

    public UserManagementController(UserLoginController userLoginController) {
        this.userLoginController = userLoginController;
        this.curretnUser = userLoginController.getCurrentUser();
    }

    public boolean addUser(String username, String role) {
        if (userLoginController.getUserDAO().getUser(username) != null) {
            System.out.println("User already exists.");
            return false;
        }
        String password = String.format(username + "%04d", new Random().nextInt(10000));
        return userLoginController.getUserDAO().addUser(username, role, password);
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

    public User getUser(String username) {
        User user = userLoginController.getUserDAO().getUser(username);
        if (user == null) {
            System.out.println("User not found.");
        }
        return user;
    }
}
