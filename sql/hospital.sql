DROP DATABASE IF EXISTS hospital;
CREATE DATABASE hospital;
USE hospital;

CREATE TABLE user
(
    id         INT         NOT NULL AUTO_INCREMENT,

    login      VARCHAR(30) NOT NULL,
    password   VARCHAR(30) NOT NULL,

    firstName  VARCHAR(50) NOT NULL,
    lastName   VARCHAR(50) NOT NULL,
    patronymic VARCHAR(50) DEFAULT NULL,

    PRIMARY KEY (id),
    UNIQUE KEY (login),
    KEY firstName (firstName),
    KEY lastName (lastName),
    KEY patronymic (patronymic)
) ENGINE = INNODB
  DEFAULT CHARSET = utf8;

CREATE TABLE administrator
(
    userId   INT          NOT NULL,
    position VARCHAR(200) NOT NULL,

    PRIMARY KEY (userId),
    FOREIGN KEY (userId) REFERENCES user (id) ON DELETE CASCADE
) ENGINE = INNODB
  DEFAULT CHARSET = utf8;

CREATE TABLE cabinet
(
    id   INT         NOT NULL AUTO_INCREMENT,
    name VARCHAR(30) NOT NULL,

    PRIMARY KEY (id),
    UNIQUE KEY (name)
) ENGINE = INNODB
  DEFAULT CHARSET = utf8;

CREATE TABLE doctor_specialty
(
    id   INT         NOT NULL AUTO_INCREMENT,
    name VARCHAR(30) NOT NULL,

    PRIMARY KEY (id),
    UNIQUE KEY (name)
) ENGINE = INNODB
  DEFAULT CHARSET = utf8;

CREATE TABLE doctor
(
    userId      INT NOT NULL,

    specialtyId INT NOT NULL,
    cabinetId   INT DEFAULT NULL,

    PRIMARY KEY (userId),
    FOREIGN KEY (userId) REFERENCES user (id) ON DELETE CASCADE,
    FOREIGN KEY (specialtyId) REFERENCES doctor_specialty (id) ON DELETE CASCADE,
    FOREIGN KEY (cabinetId) REFERENCES cabinet (id) ON DELETE SET NULL,
    KEY specialtyName (specialtyId),
    KEY cabinetId (cabinetId)
) ENGINE = INNODB
  DEFAULT CHARSET = utf8;

CREATE TABLE patient
(
    userId  INT          NOT NULL,
    email   VARCHAR(320) NOT NULL,
    address VARCHAR(200) NOT NULL,
    phone   VARCHAR(12)  NOT NULL,

    PRIMARY KEY (userId),
    FOREIGN KEY (userId) REFERENCES user (id) ON DELETE CASCADE
) ENGINE = INNODB
  DEFAULT CHARSET = utf8;

SET @@time_zone = 'SYSTEM';

CREATE TABLE schedule_cell
(
    id       INT  NOT NULL AUTO_INCREMENT,
    doctorId INT  NOT NULL,
    date     DATE NOT NULL,

    PRIMARY KEY (id),
    UNIQUE KEY (date),
    FOREIGN KEY (doctorId) REFERENCES doctor (userId) ON DELETE CASCADE,
    KEY doctorId (doctorId)
) ENGINE = INNODB
  DEFAULT CHARSET = utf8;

CREATE TABLE time_cell
(
    ticketTime     TIME NOT NULL,
    scheduleCellId INT  NOT NULL,
    patientId      INT DEFAULT NULL,
    duration       INT  NOT NULL,

    PRIMARY KEY (ticketTime),
    FOREIGN KEY (scheduleCellId) REFERENCES schedule_cell (id) ON DELETE CASCADE,
    FOREIGN KEY (patientId) REFERENCES patient (userId) ON DELETE SET NULL
) ENGINE = INNODB
  DEFAULT CHARSET = utf8;

INSERT INTO user
VALUES (0, 'admin', 'admin', 'Petr', 'Petrov', NULL);
INSERT INTO administrator
VALUES (1, 'Root admin');