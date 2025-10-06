-- ===================================================
-- Dental Lab App - Seed Data (Spanish, snake_case, using TRUNCATE)
-- ===================================================

USE dental_lab_app;

-- Clear previous demo data
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE payment_allocation;
TRUNCATE TABLE payment;
TRUNCATE TABLE invoice_item;
TRUNCATE TABLE invoice;
TRUNCATE TABLE work_item_price_override;
TRUNCATE TABLE work_price;
TRUNCATE TABLE work_work_category;
TRUNCATE TABLE work_category;
TRUNCATE TABLE work_file;
TRUNCATE TABLE work_step;
TRUNCATE TABLE material_usage;
TRUNCATE TABLE material_item;
TRUNCATE TABLE material_inventory;
TRUNCATE TABLE work_item;
TRUNCATE TABLE work;
TRUNCATE TABLE case_order;
TRUNCATE TABLE patient_user;
TRUNCATE TABLE dentist_user;
TRUNCATE TABLE technician_user;
TRUNCATE TABLE patient;
TRUNCATE TABLE technician_email;
TRUNCATE TABLE technician_phone;
TRUNCATE TABLE technician;
TRUNCATE TABLE dentist_address;
TRUNCATE TABLE dentist_email;
TRUNCATE TABLE dentist_phone;
TRUNCATE TABLE dentist;
TRUNCATE TABLE user_role;
TRUNCATE TABLE user_account;
-- keep role
SET FOREIGN_KEY_CHECKS = 1;

-- Roles
INSERT INTO role(name) VALUES ('ROLE_ADMIN'), ('ROLE_TECHNICIAN'), ('ROLE_DENTIST');

-- Users
INSERT INTO user_account (user_id, username, password_hash, enabled, locked)
VALUES 
  (1, 'ana',     '$2a$10$demo_bcrypt_hash_ana', TRUE, FALSE),
  (2, 'drlopez', '$2a$10$demo_bcrypt_hash_doc', TRUE, FALSE),
  (3, 'admin', '$2b$10$ObqAhRNHq54Ki6dzC/l.7OmPv1Kd0uqYD1ZREvAVhqkxIMReKC46m', TRUE, FALSE);

INSERT INTO user_role (user_id, role_id) SELECT 1, role_id FROM role WHERE name='ROLE_TECHNICIAN';
INSERT INTO user_role (user_id, role_id) SELECT 2, role_id FROM role WHERE name='ROLE_DENTIST';
INSERT INTO user_role (user_id, role_id) SELECT 3, role_id FROM role WHERE name='ROLE_ADMIN';

-- Dentist
INSERT INTO dentist (dentist_id, name, clinic_name) 
VALUES (1, 'Dr. López', 'Clínica Centro');

-- Dentist contacts
INSERT INTO dentist_phone (dentist_id, phone, type) VALUES (1, '555-0101', 'OFICINA');
INSERT INTO dentist_email (dentist_id, email, type) VALUES (1, 'dr.lopez@clinic.local', 'TRABAJO');
INSERT INTO dentist_address (dentist_id, address, type) VALUES (1, 'Av. Reforma 123, Ciudad de México', 'PRINCIPAL');

-- Technician
INSERT INTO technician (technician_id, name, role, address) 
VALUES (1, 'Ana Pérez', 'Ceramista', 'Laboratorio Central');

-- Technician contacts
INSERT INTO technician_phone (technician_id, phone, type) VALUES (1, '555-0202', 'MÓVIL');
INSERT INTO technician_email (technician_id, email, type) VALUES (1, 'ana@lab.local', 'TRABAJO');

-- Patient
INSERT INTO patient (patient_id, identifier, date_of_birth, dentist_id)
VALUES (1, 'PAC-0001', '1990-05-12', 1);

-- Persona links
INSERT INTO technician_user (user_id, technician_id) VALUES (1, 1);
INSERT INTO dentist_user    (user_id, dentist_id)    VALUES (2, 1);

-- Material
INSERT INTO material (material_id, name, category, unit, price_per_unit, status)
VALUES (1, 'Disco de zirconia 98x10', 'Libre de metal', 'pieza', 1800.00, 'ACTIVO');

INSERT INTO material_inventory (material_id, quantity_available, minimum_stock)
VALUES (1, 0.00, 1.00);

INSERT INTO material_item (material_item_id, material_id, batch_number, status, work_item_id, quantity, date_received, date_used)
VALUES (1, 1, 'ZIR-2025-0001', 'TERMINADO', NULL, 1.00, '2025-09-01', '2025-09-16');

-- Case and work
INSERT INTO case_order (case_id, dentist_id, patient_id, date_received, due_date, status, notes)
VALUES (1, 1, 1, '2025-09-10', '2025-09-18', 'EN_PROCESO', 'Estética anterior');

INSERT INTO work (work_id, case_id, type, material_id, shade, notes)
VALUES (1, 1, 'corona', 1, 'A2', 'Corona 21, zirconia con caracterización cerámica');

INSERT INTO work_item (work_item_id, work_id, tooth_number, sub_type, status)
VALUES (1, 1, '21', 'corona_unitaria', 'LISTO_PARA_FACTURAR');

-- Link material to work item
UPDATE material_item SET work_item_id = 1 WHERE material_item_id = 1;
INSERT INTO material_usage (usage_id, material_item_id, work_item_id, quantity_used)
VALUES (1, 1, 1, 1.00);

-- Work step
INSERT INTO work_step (step_id, work_item_id, technician_id, step_type, date_started, date_completed, notes)
VALUES (1, 1, 1, 'estratificación_cerámica', '2025-09-14 10:00:00', '2025-09-15 17:30:00', 'Aplicación de incisal translúcido');

-- Work file
INSERT INTO work_file (file_id, work_item_id, file_type, file_path, description)
VALUES (1, 1, 'FOTO', '/files/caso1/work1/item1/final.jpg', 'Vista vestibular final');

-- Categories
INSERT INTO work_category (category_id, name, description)
VALUES (1, 'Libre de metal', 'Prótesis sin metal'),
       (2, 'Monolítico', 'Restauración monolítica');

INSERT INTO work_work_category (work_id, category_id) VALUES (1, 1), (1, 2);

-- Pricing
INSERT INTO work_price (price_id, work_id, price, currency, valid_from, valid_to, notes)
VALUES (1, 1, 220.00, 'MXN', '2025-07-01', NULL, 'Precio estándar de corona');

INSERT INTO work_item_price_override (override_id, work_item_id, price, currency, reason, valid_from, valid_to, created_by)
VALUES (1, 1, 260.00, 'MXN', 'Complejidad estética anterior', '2025-09-01', NULL, 1);

-- Invoice
INSERT INTO invoice (invoice_id, case_id, dentist_id, issue_date, total_amount, status)
VALUES (1, 1, 1, '2025-09-18', 260.00, 'EMITIDA');

INSERT INTO invoice_item (item_id, invoice_id, work_id, description, amount, unit_price, currency)
VALUES (1, 1, 1, 'Corona 21 (precio especial)', 260.00, 260.00, 'MXN');

-- Payments
INSERT INTO payment (payment_id, dentist_id, received_at, method, amount_total, currency, reference)
VALUES 
  (1, 1, '2025-09-19 09:00:00', 'TRANSFERENCIA', 100.00, 'MXN', 'TRX-100-A'),
  (2, 1, '2025-09-20 11:30:00', 'TRANSFERENCIA', 160.00, 'MXN', 'TRX-160-B');

INSERT INTO payment_allocation (allocation_id, payment_id, invoice_item_id, amount_applied)
VALUES 
  (1, 1, 1, 100.00),
  (2, 2, 1, 160.00);
