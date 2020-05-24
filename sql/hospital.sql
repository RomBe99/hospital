DROP DATABASE IF EXISTS hospital;
CREATE DATABASE hospital;
USE hospital;

CREATE TABLE user_type
(
    id   INT         NOT NULL AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,

    PRIMARY KEY (id),
    UNIQUE KEY (name)
) ENGINE = INNODB
  DEFAULT CHARSET = utf8;

INSERT INTO user_type VALUES (0, 'ADMINISTRATOR');
INSERT INTO user_type VALUES (0, 'DOCTOR');
INSERT INTO user_type VALUES (0, 'PATIENT');

CREATE TABLE user
(
    id         INT         NOT NULL AUTO_INCREMENT,

    login      VARCHAR(30) NOT NULL,
    password   VARCHAR(30) NOT NULL,

    firstName  VARCHAR(30) NOT NULL,
    lastName   VARCHAR(30) NOT NULL,
    patronymic VARCHAR(30) DEFAULT NULL,
    userTypeId INT         DEFAULT NULL,

    PRIMARY KEY (id),
    UNIQUE KEY (login),
    FOREIGN KEY (userTypeId) REFERENCES user_type (id) ON DELETE SET NULL,
    KEY firstName (firstName),
    KEY lastName (lastName),
    KEY patronymic (patronymic),
    KEY password (password)
) ENGINE = INNODB
  DEFAULT CHARSET = utf8;

CREATE TABLE logged_in_users
(
    sessionId VARCHAR(36) NOT NULL,
    userId    INT         NOT NULL,

    PRIMARY KEY (sessionId),
    FOREIGN KEY (userId) REFERENCES user (id) ON DELETE CASCADE
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
    FOREIGN KEY (cabinetId) REFERENCES cabinet (id) ON DELETE SET NULL
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

CREATE TABLE schedule_cell
(
    id       INT  NOT NULL AUTO_INCREMENT,
    doctorId INT  NOT NULL,
    date     DATE NOT NULL,

    PRIMARY KEY (id),
    UNIQUE KEY (doctorId, date),
    FOREIGN KEY (doctorId) REFERENCES doctor (userId) ON DELETE CASCADE
) ENGINE = INNODB
  DEFAULT CHARSET = utf8;

CREATE TABLE time_cell
(
    ticketTime     TIME NOT NULL,
    scheduleCellId INT  NOT NULL,
    patientId      INT DEFAULT NULL,
    duration       INT  NOT NULL,

    PRIMARY KEY (ticketTime, scheduleCellId),
    UNIQUE KEY (scheduleCellId, patientId),
    FOREIGN KEY (scheduleCellId) REFERENCES schedule_cell (id) ON DELETE CASCADE,
    FOREIGN KEY (patientId) REFERENCES patient (userId) ON DELETE SET NULL
) ENGINE = INNODB
  DEFAULT CHARSET = utf8;

CREATE TABLE medical_commission
(
    id        INT AUTO_INCREMENT,
    date      DATE,
    time      TIME,
    patientId INT,
    duration  INT,

    PRIMARY KEY (id),
    UNIQUE KEY (date, time, patientId),
    FOREIGN KEY (patientId) REFERENCES patient (userId) ON DELETE CASCADE
) ENGINE = INNODB
  DEFAULT CHARSET = utf8;

CREATE TABLE commission_doctor
(
    commissionId INT NOT NULL,
    doctorId     INT NOT NULL,

    PRIMARY KEY (commissionId, doctorId),
    FOREIGN KEY (commissionId) REFERENCES medical_commission (id) ON DELETE CASCADE,
    FOREIGN KEY (doctorId) REFERENCES doctor (userId) ON DELETE CASCADE
) ENGINE = INNODB
  DEFAULT CHARSET = utf8;

# Insert doctors specialities

INSERT INTO doctor_specialty
VALUES (0, 'Dentist');

INSERT INTO doctor_specialty
VALUES (0, 'Surgeon');

INSERT INTO doctor_specialty
VALUES (0, 'Therapist');

INSERT INTO doctor_specialty
VALUES (0, 'Traumatologist');

# Insert hospital cabinets

INSERT INTO cabinet
VALUES (0, '104');

INSERT INTO cabinet
VALUES (0, '205');

INSERT INTO cabinet
VALUES (0, '306');

INSERT INTO cabinet
VALUES (0, '407');

# Insert root admin

INSERT INTO user
VALUES (0, 'admin', 'admin', 'Roman', 'Belinsky', NULL, (SELECT id FROM user_type WHERE name = 'Administrator'));
INSERT INTO administrator
VALUES (last_insert_id(), 'Root admin');