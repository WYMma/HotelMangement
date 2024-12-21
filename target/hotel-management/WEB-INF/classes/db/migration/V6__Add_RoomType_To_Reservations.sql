ALTER TABLE reservations
ADD COLUMN room_type_id INT NOT NULL AFTER hotel_id,
ADD FOREIGN KEY (room_type_id) REFERENCES room_type(id);
