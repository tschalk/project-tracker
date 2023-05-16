package com.github.tschalk.project_tracker.controller;

import com.github.tschalk.project_tracker.dao.UserDAO;

public class UserLoginController {
    UserDAO userDAO;

    public UserLoginController(UserDAO userDAO) {
        this.userDAO = userDAO;
    }
}
