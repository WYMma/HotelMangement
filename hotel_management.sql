-- Create database
DROP DATABASE IF EXISTS hotel_management;
CREATE DATABASE hotel_management;
USE hotel_management;

-- Create tables
CREATE TABLE account (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    phone VARCHAR(20),
    role VARCHAR(20) NOT NULL,
    active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE hotel (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    city VARCHAR(50) NOT NULL,
    address TEXT,
    stars INT NOT NULL CHECK (stars BETWEEN 1 AND 5),
    image VARCHAR(255),
    agent_id INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (agent_id) REFERENCES account(id) ON DELETE SET NULL
);

CREATE TABLE room_type (
    id INT PRIMARY KEY AUTO_INCREMENT,
    hotel_id INT,
    name VARCHAR(50) NOT NULL,
    description TEXT,
    price DECIMAL(10,2) NOT NULL,
    capacity INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (hotel_id) REFERENCES hotel(id) ON DELETE CASCADE
);

-- Insert sample data

-- Admin account (password: admin123)
INSERT INTO account (username, password, email, first_name, last_name, phone, role, active) VALUES
('admin', '$2a$10$xLqX7UCs4OxdO8.vzRwX8.YUOXp6YwZXJqYtqHV.F9tNz8q3RvzFi', 'admin@hotelmanagement.com', 'System', 'Administrator', '+1234567890', 'admin', true);

-- Agent accounts (password: agent123)
INSERT INTO account (username, password, email, first_name, last_name, phone, role, active) VALUES
('agent1', '$2a$10$8K1p/9jbqp1QD6UP9gG3puRvyZ9o1h3zqM3qaYMMzn5ImhZ7CH9.S', 'agent1@hotels.com', 'John', 'Smith', '+1234567891', 'agent', true),
('agent2', '$2a$10$8K1p/9jbqp1QD6UP9gG3puRvyZ9o1h3zqM3qaYMMzn5ImhZ7CH9.S', 'agent2@hotels.com', 'Jane', 'Doe', '+1234567892', 'agent', true),
('agent3', '$2a$10$8K1p/9jbqp1QD6UP9gG3puRvyZ9o1h3zqM3qaYMMzn5ImhZ7CH9.S', 'agent3@hotels.com', 'Mike', 'Johnson', '+1234567893', 'agent', true);

-- Hotels
INSERT INTO hotel (name, description, city, address, stars, image, agent_id) VALUES
('Grand Plaza Hotel', 'Experience luxury at its finest in the heart of the city. Featuring spectacular views and world-class amenities.', 'New York', '123 Broadway St, New York, NY 10013', 5, 'grand-plaza.jpg', 2),
('Seaside Resort', 'A beautiful beachfront resort offering stunning ocean views and private beach access.', 'Miami', '456 Ocean Drive, Miami Beach, FL 33139', 4, 'seaside-resort.jpg', 2),
('Mountain Lodge', 'Cozy mountain retreat with scenic views and outdoor activities.', 'Denver', '789 Mountain View Rd, Denver, CO 80202', 3, 'mountain-lodge.jpg', 2),
('City Lights Hotel', 'Modern urban hotel with easy access to shopping and entertainment.', 'Chicago', '321 Michigan Ave, Chicago, IL 60601', 4, 'city-lights.jpg', 3),
('Desert Oasis Resort', 'Luxury desert resort with spa facilities and golf course.', 'Las Vegas', '555 Paradise Rd, Las Vegas, NV 89169', 5, 'desert-oasis.jpg', 3),
('Riverside Inn', 'Charming riverside property with traditional architecture and gardens.', 'San Antonio', '777 River Walk, San Antonio, TX 78205', 3, 'riverside-inn.jpg', 3),
('Harbor View Hotel', 'Elegant waterfront hotel with panoramic harbor views.', 'Boston', '888 Harbor St, Boston, MA 02110', 4, 'harbor-view.jpg', 4),
('Forest Retreat', 'Peaceful forest setting with nature trails and spa services.', 'Portland', '999 Forest Path, Portland, OR 97201', 4, 'forest-retreat.jpg', 4),
('Metropolitan Suites', 'Luxurious all-suite hotel in the business district.', 'Los Angeles', '444 Wilshire Blvd, Los Angeles, CA 90024', 5, 'metropolitan.jpg', 4);

-- Room Types
INSERT INTO room_type (hotel_id, name, description, price, capacity) VALUES
-- Grand Plaza Hotel
(1, 'Presidential Suite', 'Luxurious suite with city views and private terrace', 899.99, 4),
(1, 'Deluxe Room', 'Spacious room with modern amenities', 299.99, 2),
(1, 'Standard Room', 'Comfortable room with essential amenities', 199.99, 2),

-- Seaside Resort
(2, 'Ocean View Suite', 'Suite with panoramic ocean views', 599.99, 3),
(2, 'Beach Front Room', 'Room with direct beach access', 399.99, 2),
(2, 'Garden View Room', 'Peaceful room overlooking the gardens', 249.99, 2),

-- Mountain Lodge
(3, 'Mountain Suite', 'Suite with mountain views and fireplace', 399.99, 4),
(3, 'Cabin Room', 'Cozy room with rustic decor', 199.99, 2),
(3, 'Family Room', 'Spacious room for families', 299.99, 4),

-- City Lights Hotel
(4, 'Executive Suite', 'Business suite with office space', 499.99, 2),
(4, 'City View Room', 'Room with skyline views', 299.99, 2),
(4, 'Standard Room', 'Modern room with city convenience', 199.99, 2),

-- Desert Oasis Resort
(5, 'Royal Suite', 'Luxury suite with private pool', 999.99, 4),
(5, 'Pool View Room', 'Room overlooking the oasis pool', 399.99, 2),
(5, 'Desert View Room', 'Room with scenic desert views', 299.99, 2),

-- Riverside Inn
(6, 'River Suite', 'Suite with river views and balcony', 399.99, 3),
(6, 'Traditional Room', 'Room with classic decor', 199.99, 2),
(6, 'Family Room', 'Spacious room for families', 299.99, 4),

-- Harbor View Hotel
(7, 'Harbor Suite', 'Luxury suite with harbor views', 599.99, 3),
(7, 'Ocean View Room', 'Room with water views', 399.99, 2),
(7, 'City View Room', 'Room with city views', 299.99, 2),

-- Forest Retreat
(8, 'Forest Suite', 'Suite surrounded by nature', 499.99, 3),
(8, 'Treehouse Room', 'Unique elevated room experience', 399.99, 2),
(8, 'Garden Room', 'Room with forest garden views', 299.99, 2),

-- Metropolitan Suites
(9, 'Penthouse Suite', 'Luxury penthouse with city views', 1299.99, 4),
(9, 'Executive Suite', 'Business-friendly luxury suite', 699.99, 2),
(9, 'Deluxe Suite', 'Spacious suite with living area', 499.99, 2);
