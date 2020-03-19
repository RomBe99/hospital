DROP DATABASE IF EXISTS hospital;
CREATE DATABASE hospital;
USE hospital;

CREATE TABLE administrator (
    id INT NOT NULL AUTO_INCREMENT,
    login VARCHAR(30) NOT NULL,
    password VARCHAR(30) NOT NULL,

    PRIMARY KEY (id),
    UNIQUE KEY (login)
) ENGINE = INNODB
  DEFAULT CHARSET = utf8;

CREATE TABLE cabinet (
    id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(30) NOT NULL,

    PRIMARY KEY (id),
    UNIQUE KEY (name)
) ENGINE = INNODB
  DEFAULT CHARSET = utf8;

CREATE TABLE doctor_specialty (
    id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(30) NOT NULL,

    PRIMARY KEY (id),
    UNIQUE KEY (name)
) ENGINE = INNODB
  DEFAULT CHARSET = utf8;

CREATE TABLE reception_schedule (
    id INT NOT NULL AUTO_INCREMENT,

    PRIMARY KEY (id)
) ENGINE = INNODB
  DEFAULT CHARSET = utf8;

CREATE TABLE ticket_list (
    id INT NOT NULL AUTO_INCREMENT,

    PRIMARY KEY (id)
) ENGINE = INNODB
  DEFAULT CHARSET = utf8;

CREATE TABLE doctor (
    id INT NOT NULL AUTO_INCREMENT,
    login VARCHAR(30) NOT NULL,
    password VARCHAR(30) NOT NULL,
    firstName VARCHAR(50) NOT NULL,
    lastName VARCHAR(50) NOT NULL,

    specialtyId INT NOT NULL,
    receptionScheduleId INT DEFAULT NULL,
    cabinetId INT DEFAULT NULL,

    PRIMARY KEY (id),
    UNIQUE KEY (login),
    FOREIGN KEY (specialtyId) REFERENCES doctor_specialty (id) ON DELETE CASCADE,
    FOREIGN KEY (receptionScheduleId) REFERENCES reception_schedule (id) ON DELETE SET NULL,
    FOREIGN KEY (cabinetId) REFERENCES cabinet (id) ON DELETE SET NULL,
    KEY firstName (firstName),
    KEY lastName (lastName),
    KEY specialtyName (specialtyId),
    KEY receptionScheduleId (receptionScheduleId),
    KEY cabinetId (cabinetId)
) ENGINE = INNODB
  DEFAULT CHARSET = utf8;

SET @@time_zone = 'SYSTEM';

CREATE TABLE schedule_cell (
    id INT NOT NULL AUTO_INCREMENT,
    doctorId INT NOT NULL,
    cabinetName VARCHAR(30) NOT NULL,
    date DATETIME NOT NULL,

    PRIMARY KEY (id),
    UNIQUE KEY (date),
    FOREIGN KEY (doctorId) REFERENCES doctor (id) ON DELETE CASCADE,
    FOREIGN KEY (cabinetName) REFERENCES cabinet (name)  ON DELETE CASCADE,
    KEY doctorId (doctorId),
    KEY cabinetName (cabinetName)
) ENGINE = INNODB
  DEFAULT CHARSET = utf8;

CREATE TABLE reception_schedule_cell (
    receptionScheduleId INT NOT NULL,
    cellId INT NOT NULL,

    PRIMARY KEY (receptionScheduleId, cellId),
    FOREIGN KEY (receptionScheduleId) REFERENCES reception_schedule (id) ON DELETE CASCADE,
    FOREIGN KEY (cellId) REFERENCES schedule_cell (id) ON DELETE CASCADE
);

CREATE TABLE ticket_list_cell (
    ticketListId INT NOT NULL,
    cellId INT NOT NULL,

    PRIMARY KEY (ticketListId, cellId),
    FOREIGN KEY (ticketListId) REFERENCES ticket_list (id) ON DELETE CASCADE,
    FOREIGN KEY (cellId) REFERENCES schedule_cell (id) ON DELETE CASCADE
);

CREATE TABLE patient (
    id INT NOT NULL AUTO_INCREMENT,
    login VARCHAR(30) NOT NULL,
    password VARCHAR(30) NOT NULL,
    firstName VARCHAR(50) NOT NULL,
    lastName VARCHAR(50) NOT NULL,
    ticketListId INT DEFAULT NULL,

    PRIMARY KEY (id),
    UNIQUE KEY (login),
    FOREIGN KEY (ticketListId) REFERENCES ticket_list (id) ON DELETE SET NULL,
    KEY firstName (firstName),
    KEY lastName (lastName)
) ENGINE = INNODB
  DEFAULT CHARSET = utf8;

INSERT administrator(login, password) VALUES ('admin', 'admin');