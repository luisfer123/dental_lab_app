-- V3__add_contact_tables.sql

-- 1) Remove old columns (optional, if you want to fully normalize)
ALTER TABLE Dentist DROP COLUMN phone, DROP COLUMN email;
ALTER TABLE Technician DROP COLUMN phone, DROP COLUMN email;

-- 2) Add new tables for Dentist contacts
CREATE TABLE DentistPhone (
    phone_id   BIGINT PRIMARY KEY AUTO_INCREMENT,
    dentist_id BIGINT NOT NULL,
    phone      VARCHAR(50) NOT NULL,
    type       VARCHAR(20),
    FOREIGN KEY (dentist_id) REFERENCES Dentist(dentist_id)
);

CREATE TABLE DentistEmail (
    email_id   BIGINT PRIMARY KEY AUTO_INCREMENT,
    dentist_id BIGINT NOT NULL,
    email      VARCHAR(255) NOT NULL,
    type       VARCHAR(20),
    FOREIGN KEY (dentist_id) REFERENCES Dentist(dentist_id)
);

-- 3) Add new tables for Technician contacts
CREATE TABLE TechnicianPhone (
    phone_id     BIGINT PRIMARY KEY AUTO_INCREMENT,
    technician_id BIGINT NOT NULL,
    phone        VARCHAR(50) NOT NULL,
    type         VARCHAR(20),
    FOREIGN KEY (technician_id) REFERENCES Technician(technician_id)
);

CREATE TABLE TechnicianEmail (
    email_id     BIGINT PRIMARY KEY AUTO_INCREMENT,
    technician_id BIGINT NOT NULL,
    email        VARCHAR(255) NOT NULL,
    type         VARCHAR(20),
    FOREIGN KEY (technician_id) REFERENCES Technician(technician_id)
);
