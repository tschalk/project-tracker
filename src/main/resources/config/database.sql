CREATE DATABASE IF NOT EXISTS ProjectTracker;
USE ProjectTracker;

CREATE TABLE User (
                      id INT(11) PRIMARY KEY AUTO_INCREMENT,
                      username VARCHAR(255) NOT NULL,
                      password VARCHAR(255) NOT NULL
);

CREATE TABLE CostCenter (
                            id INT(11) PRIMARY KEY AUTO_INCREMENT,
                            name VARCHAR(255) NOT NULL
);

CREATE TABLE Responsible (
                             id INT(11) PRIMARY KEY AUTO_INCREMENT,
                             name VARCHAR(255) NOT NULL
);

CREATE TABLE Project (
                         id INT(11) PRIMARY KEY AUTO_INCREMENT,
                         user_id INT(11),
                         cost_center_id INT(11),
                         responsible_id INT(11),
                         description VARCHAR(255),
                         FOREIGN KEY (user_id) REFERENCES User(id),
                         FOREIGN KEY (cost_center_id) REFERENCES CostCenter(id),
                         FOREIGN KEY (responsible_id) REFERENCES Responsible(id)
);

CREATE TABLE TimesheetEntry (
                                id INT(11) PRIMARY KEY AUTO_INCREMENT,
                                project_id INT(11),
                                start_time DATETIME,
                                duration INT(11),
                                FOREIGN KEY (project_id) REFERENCES Project(id)
);
