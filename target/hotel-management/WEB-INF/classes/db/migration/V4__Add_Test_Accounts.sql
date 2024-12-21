-- Add admin account (password in plain text)
INSERT INTO account (username, password, email, first_name, last_name, phone, role, active)
VALUES ('admin', 'admin123', 'admin@hotel.com', 'Admin', 'User', '1234567890', 'admin', true);

-- Add agent account (password in plain text)
INSERT INTO account (username, password, email, first_name, last_name, phone, role, active)
VALUES ('agent', 'agent123', 'agent@hotel.com', 'Agent', 'User', '0987654321', 'agent', true);
