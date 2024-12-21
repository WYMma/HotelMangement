-- Add sample room types for hotels
-- Note: Replace hotel_id values with actual hotel IDs from your database
INSERT INTO room_type (hotel_id, name, description, capacity, price_per_night, available_rooms)
VALUES 
    -- Standard Rooms
    (1, 'Standard Single', 'Cozy room with a single bed, perfect for solo travelers', 1, 99.99, 5),
    (1, 'Standard Double', 'Comfortable room with a double bed', 2, 149.99, 8),
    (1, 'Standard Twin', 'Room with two single beds', 2, 159.99, 6),
    
    -- Deluxe Rooms
    (1, 'Deluxe Double', 'Spacious room with a queen-size bed and city view', 2, 199.99, 4),
    (1, 'Deluxe Suite', 'Luxury suite with separate living area and king-size bed', 3, 299.99, 3),
    
    -- Family Rooms
    (1, 'Family Room', 'Large room with one double and two single beds', 4, 259.99, 4),
    (1, 'Family Suite', 'Two-bedroom suite perfect for families', 6, 399.99, 2),
    
    -- Special Rooms
    (1, 'Honeymoon Suite', 'Romantic suite with king-size bed and jacuzzi', 2, 349.99, 1),
    (1, 'Executive Suite', 'Premium suite with office space and luxury amenities', 2, 449.99, 2),
    
    -- Add similar room types for other hotels
    (2, 'Standard Room', 'Comfortable room with queen-size bed', 2, 129.99, 10),
    (2, 'Deluxe Room', 'Spacious room with premium amenities', 2, 179.99, 8),
    (2, 'Family Room', 'Perfect for families with children', 4, 229.99, 5),
    
    (3, 'Economy Room', 'Budget-friendly comfortable room', 2, 89.99, 12),
    (3, 'Business Room', 'Ideal for business travelers', 1, 149.99, 8),
    (3, 'Luxury Suite', 'Top-floor suite with panoramic views', 2, 299.99, 4);
