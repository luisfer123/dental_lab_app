-- ===================================================
-- Dental Lab Application Database (MySQL 8+)
-- Full schema + demo data
-- Database: dental_lab_app
-- ===================================================

DROP DATABASE IF EXISTS dental_lab_app;
CREATE DATABASE dental_lab_app
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_unicode_ci;
USE dental_lab_app;

-- ===================================================
-- 0) Security base (users, roles)
-- ===================================================

CREATE TABLE role (
  role_id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name    VARCHAR(50) UNIQUE NOT NULL
) ENGINE=InnoDB;
/* Table to store security related information of users.
   Only users that are going to login in the app need a
   record in this table.
*/
CREATE TABLE user_account (
  user_id           BIGINT PRIMARY KEY AUTO_INCREMENT,
  username          VARCHAR(100) UNIQUE NOT NULL,
  password_hash     VARCHAR(255) NOT NULL,
  enabled           BOOLEAN DEFAULT TRUE,
  locked            BOOLEAN DEFAULT FALSE,
  created_at        DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

CREATE TABLE user_role (
  user_id BIGINT NOT NULL,
  role_id BIGINT NOT NULL,
  PRIMARY KEY (user_id, role_id),
  FOREIGN KEY (user_id) REFERENCES user_account(user_id),
  FOREIGN KEY (role_id) REFERENCES role(role_id)
) ENGINE=InnoDB;

-- ===================================================
-- 1) Domain actors (dentist, technician, patient)
-- ===================================================

/* Represent a Dentist.
   As long as dentist cannot
   login in the app we do not need to add them to 
   the user_account table, we add them only in this
   table. If later we want to allow dentists to login
   in the app we add a record in the user_account 
   table a relate it with this table's record of such
   Dentist.
*/
CREATE TABLE dentist (
  dentist_id  BIGINT PRIMARY KEY AUTO_INCREMENT,
  name        VARCHAR(255) NOT NULL,
  clinic_name VARCHAR(255)
) ENGINE=InnoDB;

CREATE TABLE technician (
  technician_id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name          VARCHAR(255) NOT NULL,
  role          VARCHAR(50),
  address       VARCHAR(255)
) ENGINE=InnoDB;

CREATE TABLE patient (
  patient_id    BIGINT PRIMARY KEY AUTO_INCREMENT,
  identifier    VARCHAR(100) NOT NULL,
  date_of_birth DATE,
  dentist_id    BIGINT,
  FOREIGN KEY (dentist_id) REFERENCES dentist(dentist_id)
) ENGINE=InnoDB;

-- Persona linking tables
CREATE TABLE technician_user (
  user_id        BIGINT PRIMARY KEY,
  technician_id  BIGINT NOT NULL,
  created_at     DATETIME DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (user_id)       REFERENCES user_account(user_id),
  FOREIGN KEY (technician_id) REFERENCES technician(technician_id)
) ENGINE=InnoDB;

CREATE TABLE dentist_user (
  user_id     BIGINT PRIMARY KEY,
  dentist_id  BIGINT NOT NULL,
  created_at  DATETIME DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (user_id)    REFERENCES user_account(user_id),
  FOREIGN KEY (dentist_id) REFERENCES dentist(dentist_id)
) ENGINE=InnoDB;

CREATE TABLE patient_user (
  user_id     BIGINT PRIMARY KEY,
  patient_id  BIGINT NOT NULL,
  created_at  DATETIME DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (user_id)    REFERENCES user_account(user_id),
  FOREIGN KEY (patient_id) REFERENCES patient(patient_id)
) ENGINE=InnoDB;

-- Triggers to enforce one persona per user
DELIMITER //
CREATE TRIGGER trg_techuser_single_persona
BEFORE INSERT ON technician_user
FOR EACH ROW
BEGIN
  IF EXISTS (SELECT 1 FROM dentist_user WHERE user_id = NEW.user_id)
     OR EXISTS (SELECT 1 FROM patient_user WHERE user_id = NEW.user_id) THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'User already linked to another persona';
  END IF;
END//
CREATE TRIGGER trg_dentuser_single_persona
BEFORE INSERT ON dentist_user
FOR EACH ROW
BEGIN
  IF EXISTS (SELECT 1 FROM technician_user WHERE user_id = NEW.user_id)
     OR EXISTS (SELECT 1 FROM patient_user WHERE user_id = NEW.user_id) THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'User already linked to another persona';
  END IF;
END//
CREATE TRIGGER trg_patuser_single_persona
BEFORE INSERT ON patient_user
FOR EACH ROW
BEGIN
  IF EXISTS (SELECT 1 FROM technician_user WHERE user_id = NEW.user_id)
     OR EXISTS (SELECT 1 FROM dentist_user   WHERE user_id = NEW.user_id) THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'User already linked to another persona';
  END IF;
END//
DELIMITER ;

-- Convenience view to resolve persona
CREATE OR REPLACE VIEW user_persona AS
SELECT tu.user_id, 'TECHNICIAN' AS persona_type, tu.technician_id AS persona_id FROM technician_user tu
UNION ALL
SELECT du.user_id, 'DENTIST',     du.dentist_id                    FROM dentist_user du
UNION ALL
SELECT pu.user_id, 'PATIENT',     pu.patient_id                    FROM patient_user pu;

-- ===================================================
-- 2) Case / Work workflow
-- ===================================================

CREATE TABLE case_order (
  case_id       BIGINT PRIMARY KEY AUTO_INCREMENT,
  dentist_id    BIGINT NOT NULL,
  patient_id    BIGINT NOT NULL,
  date_received DATE NOT NULL,
  due_date      DATE,
  status        VARCHAR(50),
  notes         TEXT,
  FOREIGN KEY (dentist_id) REFERENCES dentist(dentist_id),
  FOREIGN KEY (patient_id) REFERENCES patient(patient_id)
) ENGINE=InnoDB;

CREATE TABLE material (
  material_id     BIGINT PRIMARY KEY AUTO_INCREMENT,
  name            VARCHAR(100) NOT NULL,
  category        VARCHAR(50),
  unit            VARCHAR(20),
  price_per_unit  DECIMAL(10,2) NOT NULL,
  status          VARCHAR(50) DEFAULT 'ACTIVE',
  notes           TEXT
) ENGINE=InnoDB;

CREATE TABLE work (
  work_id     BIGINT PRIMARY KEY AUTO_INCREMENT,
  case_id     BIGINT NOT NULL,
  type        VARCHAR(50),
  material_id BIGINT,
  shade       VARCHAR(50),
  notes       TEXT,
  FOREIGN KEY (case_id)   REFERENCES case_order(case_id),
  FOREIGN KEY (material_id) REFERENCES material(material_id)
) ENGINE=InnoDB;

CREATE TABLE work_item (
  work_item_id BIGINT PRIMARY KEY AUTO_INCREMENT,
  work_id      BIGINT NOT NULL,
  tooth_number VARCHAR(10),
  sub_type     VARCHAR(50),
  status       VARCHAR(50),
  FOREIGN KEY (work_id) REFERENCES work(work_id)
) ENGINE=InnoDB;

CREATE TABLE work_step (
  step_id        BIGINT PRIMARY KEY AUTO_INCREMENT,
  work_item_id   BIGINT NOT NULL,
  technician_id  BIGINT,
  step_type      VARCHAR(50),
  date_started   DATETIME,
  date_completed DATETIME,
  notes          TEXT,
  FOREIGN KEY (work_item_id)  REFERENCES work_item(work_item_id),
  FOREIGN KEY (technician_id) REFERENCES technician(technician_id)
) ENGINE=InnoDB;

CREATE TABLE work_file (
  file_id      BIGINT PRIMARY KEY AUTO_INCREMENT,
  work_item_id BIGINT NOT NULL,
  file_type    VARCHAR(50),
  file_path    VARCHAR(500) NOT NULL,
  uploaded_at  DATETIME DEFAULT CURRENT_TIMESTAMP,
  description  TEXT,
  FOREIGN KEY (work_item_id) REFERENCES work_item(work_item_id)
) ENGINE=InnoDB;

CREATE TABLE work_category (
  category_id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name        VARCHAR(100) NOT NULL UNIQUE,
  description TEXT
) ENGINE=InnoDB;

CREATE TABLE work_work_category (
  work_id     BIGINT NOT NULL,
  category_id BIGINT NOT NULL,
  PRIMARY KEY (work_id, category_id),
  FOREIGN KEY (work_id)     REFERENCES work(work_id),
  FOREIGN KEY (category_id) REFERENCES work_category(category_id)
) ENGINE=InnoDB;

-- ===================================================
-- 3) Materials: inventory, items, usage
-- ===================================================

CREATE TABLE material_inventory (
  inventory_id       BIGINT PRIMARY KEY AUTO_INCREMENT,
  material_id        BIGINT NOT NULL,
  quantity_available DECIMAL(10,2) NOT NULL,
  minimum_stock      DECIMAL(10,2),
  last_updated       DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (material_id) REFERENCES material(material_id)
) ENGINE=InnoDB;

CREATE TABLE material_item (
  material_item_id BIGINT PRIMARY KEY AUTO_INCREMENT,
  material_id      BIGINT NOT NULL,
  batch_number     VARCHAR(50),
  status           VARCHAR(50) DEFAULT 'IN_STORE',
  work_item_id     BIGINT NULL,
  quantity         DECIMAL(10,2) NOT NULL,
  date_received    DATE,
  date_used        DATE,
  FOREIGN KEY (material_id)  REFERENCES material(material_id),
  FOREIGN KEY (work_item_id) REFERENCES work_item(work_item_id)
) ENGINE=InnoDB;

CREATE TABLE material_usage (
  usage_id         BIGINT PRIMARY KEY AUTO_INCREMENT,
  material_item_id BIGINT NOT NULL,
  work_item_id     BIGINT NOT NULL,
  quantity_used    DECIMAL(10,2) NOT NULL,
  FOREIGN KEY (material_item_id) REFERENCES material_item(material_item_id),
  FOREIGN KEY (work_item_id)     REFERENCES work_item(work_item_id)
) ENGINE=InnoDB;

-- ===================================================
-- 4) Pricing
-- ===================================================

CREATE TABLE work_price (
  price_id    BIGINT PRIMARY KEY AUTO_INCREMENT,
  work_id     BIGINT NOT NULL,
  price       DECIMAL(10,2) NOT NULL,
  currency    CHAR(3) DEFAULT 'USD',
  valid_from  DATE NOT NULL,
  valid_to    DATE NULL,
  dentist_id  BIGINT NULL,
  notes       VARCHAR(255),
  FOREIGN KEY (work_id)    REFERENCES work(work_id),
  FOREIGN KEY (dentist_id) REFERENCES dentist(dentist_id)
) ENGINE=InnoDB;

CREATE TABLE work_item_price_override (
  override_id  BIGINT PRIMARY KEY AUTO_INCREMENT,
  work_item_id BIGINT NOT NULL,
  price        DECIMAL(10,2) NOT NULL,
  currency     CHAR(3) DEFAULT 'USD',
  reason       VARCHAR(255),
  valid_from   DATE NOT NULL,
  valid_to     DATE NULL,
  created_at   DATETIME DEFAULT CURRENT_TIMESTAMP,
  created_by   BIGINT NULL,
  FOREIGN KEY (work_item_id) REFERENCES work_item(work_item_id),
  FOREIGN KEY (created_by)  REFERENCES user_account(user_id)
) ENGINE=InnoDB;

-- ===================================================
-- 5) Invoicing
-- ===================================================

CREATE TABLE invoice (
  invoice_id   BIGINT PRIMARY KEY AUTO_INCREMENT,
  case_id      BIGINT NOT NULL,
  dentist_id   BIGINT NOT NULL,
  issue_date   DATE,
  total_amount DECIMAL(10,2),
  status       VARCHAR(50),
  FOREIGN KEY (case_id)   REFERENCES case_order(case_id),
  FOREIGN KEY (dentist_id) REFERENCES dentist(dentist_id)
) ENGINE=InnoDB;

CREATE TABLE invoice_item (
  item_id     BIGINT PRIMARY KEY AUTO_INCREMENT,
  invoice_id  BIGINT NOT NULL,
  work_id     BIGINT,
  description VARCHAR(255),
  amount      DECIMAL(10,2),
  unit_price  DECIMAL(10,2),
  currency    CHAR(3) DEFAULT 'USD',
  FOREIGN KEY (invoice_id) REFERENCES invoice(invoice_id),
  FOREIGN KEY (work_id)    REFERENCES work(work_id)
) ENGINE=InnoDB;

-- ===================================================
-- 6) Payments
-- ===================================================

CREATE TABLE payment (
  payment_id   BIGINT PRIMARY KEY AUTO_INCREMENT,
  dentist_id   BIGINT NOT NULL,
  received_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  method       VARCHAR(40),
  amount_total DECIMAL(12,2) NOT NULL,
  currency     CHAR(3) DEFAULT 'USD',
  reference    VARCHAR(100),
  notes        VARCHAR(255),
  FOREIGN KEY (dentist_id) REFERENCES dentist(dentist_id)
) ENGINE=InnoDB;

CREATE TABLE payment_allocation (
  allocation_id   BIGINT PRIMARY KEY AUTO_INCREMENT,
  payment_id      BIGINT NOT NULL,
  invoice_item_id BIGINT NOT NULL,
  amount_applied  DECIMAL(12,2) NOT NULL,
  created_at      DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (payment_id)      REFERENCES payment(payment_id),
  FOREIGN KEY (invoice_item_id) REFERENCES invoice_item(item_id)
) ENGINE=InnoDB;

-- ===================================================
-- 7) Security (Refresh Tokens)
-- ===================================================

CREATE TABLE refresh_token (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    token VARCHAR(500) NOT NULL,
    user_id BIGINT NOT NULL,
    expiry_date DATETIME NOT NULL,
    revoked BOOLEAN DEFAULT FALSE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user_account(user_id)
) ENGINE=InnoDB;

-- ===================================================
-- 8) Contact tables
-- ===================================================

-- Dentist phones (1–many)
CREATE TABLE dentist_phone (
    phone_id   BIGINT PRIMARY KEY AUTO_INCREMENT,
    dentist_id BIGINT NOT NULL,
    phone      VARCHAR(50) NOT NULL,
    type       VARCHAR(20),   -- e.g., MOBILE, OFFICE, HOME
    FOREIGN KEY (dentist_id) REFERENCES dentist(dentist_id)
);

-- Dentist emails (1–many)
CREATE TABLE dentist_email (
    email_id   BIGINT PRIMARY KEY AUTO_INCREMENT,
    dentist_id BIGINT NOT NULL,
    email      VARCHAR(255) NOT NULL,
    type       VARCHAR(20),   -- WORK, PERSONAL
    FOREIGN KEY (dentist_id) REFERENCES dentist(dentist_id)
);

-- Dentist addresses (1–many)
CREATE TABLE dentist_address (
    address_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    dentist_id BIGINT NOT NULL,
    address    VARCHAR(255) NOT NULL,
    type       VARCHAR(20),   -- MAIN, BRANCH, BILLING, etc.
    FOREIGN KEY (dentist_id) REFERENCES dentist(dentist_id)
);

-- Technician phones (1–many)
CREATE TABLE technician_phone (
    phone_id      BIGINT PRIMARY KEY AUTO_INCREMENT,
    technician_id BIGINT NOT NULL,
    phone         VARCHAR(50) NOT NULL,
    type          VARCHAR(20),
    FOREIGN KEY (technician_id) REFERENCES technician(technician_id)
);

-- Technician emails (1–many)
CREATE TABLE technician_email (
    email_id      BIGINT PRIMARY KEY AUTO_INCREMENT,
    technician_id BIGINT NOT NULL,
    email         VARCHAR(255) NOT NULL,
    type          VARCHAR(20),
    FOREIGN KEY (technician_id) REFERENCES technician(technician_id)
);

