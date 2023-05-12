CREATE DATABASE IF NOT EXISTS ProjectTracker;
USE ProjectTracker;

CREATE TABLE User (
                      id INT(11) PRIMARY KEY AUTO_INCREMENT,
                      username VARCHAR(255) NOT NULL,
                      password VARCHAR(255) NOT NULL
);

CREATE TABLE Project (
                         id INT(11) PRIMARY KEY AUTO_INCREMENT,
                         user_id INT(11),
                         description TEXT,
                         account_code VARCHAR(255),
                         FOREIGN KEY (user_id) REFERENCES User(id)
);

CREATE TABLE TimesheetEntry (
                                id INT(11) PRIMARY KEY AUTO_INCREMENT,
                                project_id INT(11),
                                date DATE,
                                start_time TIME,
                                end_time TIME,
                                description TEXT,
                                FOREIGN KEY (project_id) REFERENCES Project(id)
);

CREATE TABLE CostCenter (
                            id INT(11) PRIMARY KEY AUTO_INCREMENT,
                            project_id INT(11),
                            name VARCHAR(255),
                            FOREIGN KEY (project_id) REFERENCES Project(id)
);

CREATE TABLE Responsible (
                             id INT(11) PRIMARY KEY AUTO_INCREMENT,
                             project_id INT(11),
                             name VARCHAR(255),
                             FOREIGN KEY (project_id) REFERENCES Project(id)
);
