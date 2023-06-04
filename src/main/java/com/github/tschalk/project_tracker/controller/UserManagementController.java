package com.github.tschalk.project_tracker.controller;

import com.github.tschalk.project_tracker.model.User;
import com.github.tschalk.project_tracker.dao.UserDAO;

import java.util.List;
import java.util.Random;

public class UserManagementController {
    private final UserDAO userDAO;

    public UserManagementController(UserLoginController userLoginController) {
        this.userDAO = userLoginController.getUserDAO();
    }

    public boolean addUser(String username, String role) {
        if (userDAO.getUser(username) != null) {
            System.out.println("User already exists.");
            return false;
        }
        String password = generatePassword(username);
        return userDAO.addUser(username, role, password);
    }

    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }

    public boolean deleteUser(String username) {
        User user = userDAO.getUser(username);
        if (user.getId() == 1) {
            System.out.println("Admin user cannot be deleted.");
            return false;
        }
        return userDAO.deleteUser(username);
    }

    public User getUser(String username) {
        User user = userDAO.getUser(username);
        if (user == null) {
            System.out.println("User not found.");
        }
        return user;
    }

    public boolean updateUser(String username, String role, boolean isActive) {
        return userDAO.updateUser(username, role, isActive);
    }

    public boolean resetPassword(String username) {
        User user = getUser(username);

        // Der Benutzer existiert nicht oder ist der (Super)-Admin-Benutzer.
        if (user == null || user.getId() == 1) {
            System.out.println("Password cannot be reset.");
            return false;
        }
        String newPassword = generatePassword(username);
        return userDAO.updatePassword(user.getId(), newPassword);
    }

    private String generatePassword(String username) {
        return String.format(username + "%04d", new Random().nextInt(10000));
    }
}
