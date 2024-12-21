package com.hotel.dao.impl;

import com.hotel.dao.RoomTypeDAO;
import com.hotel.model.RoomType;
import com.hotel.util.DatabaseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoomTypeDAOImpl implements RoomTypeDAO {
    private static final Logger logger = LoggerFactory.getLogger(RoomTypeDAOImpl.class);

    @Override
    public RoomType findById(int id) throws SQLException {
        String sql = "SELECT * FROM room_type WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToRoomType(rs);
                }
            }
        } catch (SQLException e) {
            logger.error("Error finding room type by id: " + id, e);
            throw e;
        }
        return null;
    }

    @Override
    public List<RoomType> findByHotelId(int hotelId) throws SQLException {
        String sql = "SELECT * FROM room_type WHERE hotel_id = ? ORDER BY price_per_night ASC";
        List<RoomType> roomTypes = new ArrayList<>();
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, hotelId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    roomTypes.add(mapResultSetToRoomType(rs));
                }
            }
        } catch (SQLException e) {
            logger.error("Error finding room types for hotel: " + hotelId, e);
            throw e;
        }
        return roomTypes;
    }

    @Override
    public void create(RoomType roomType) throws SQLException {
        String sql = "INSERT INTO room_type (hotel_id, name, description, capacity, price_per_night, available_rooms) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
                    
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, roomType.getHotelId());
            stmt.setString(2, roomType.getName());
            stmt.setString(3, roomType.getDescription());
            stmt.setInt(4, roomType.getCapacity());
            stmt.setBigDecimal(5, roomType.getPricePerNight());
            stmt.setInt(6, roomType.getAvailableRooms());
            
            stmt.executeUpdate();
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    roomType.setId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            logger.error("Error creating room type", e);
            throw e;
        }
    }

    @Override
    public void update(RoomType roomType) throws SQLException {
        String sql = "UPDATE room_type SET name = ?, description = ?, capacity = ?, " +
                    "price_per_night = ?, available_rooms = ? WHERE id = ?";
                    
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, roomType.getName());
            stmt.setString(2, roomType.getDescription());
            stmt.setInt(3, roomType.getCapacity());
            stmt.setBigDecimal(4, roomType.getPricePerNight());
            stmt.setInt(5, roomType.getAvailableRooms());
            stmt.setInt(6, roomType.getId());
            
            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.error("Error updating room type: " + roomType.getId(), e);
            throw e;
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM room_type WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.error("Error deleting room type: " + id, e);
            throw e;
        }
    }

    @Override
    public List<RoomType> findByPriceRange(double minPrice, double maxPrice) throws SQLException {
        String sql = "SELECT * FROM room_type WHERE price_per_night BETWEEN ? AND ? ORDER BY price_per_night ASC";
        List<RoomType> roomTypes = new ArrayList<>();
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setDouble(1, minPrice);
            stmt.setDouble(2, maxPrice);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    roomTypes.add(mapResultSetToRoomType(rs));
                }
            }
        } catch (SQLException e) {
            logger.error("Error finding room types by price range", e);
            throw e;
        }
        return roomTypes;
    }

    @Override
    public List<RoomType> findByCapacity(int capacity) throws SQLException {
        String sql = "SELECT * FROM room_type WHERE capacity >= ? ORDER BY capacity ASC";
        List<RoomType> roomTypes = new ArrayList<>();
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, capacity);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    roomTypes.add(mapResultSetToRoomType(rs));
                }
            }
        } catch (SQLException e) {
            logger.error("Error finding room types by capacity", e);
            throw e;
        }
        return roomTypes;
    }

    private RoomType mapResultSetToRoomType(ResultSet rs) throws SQLException {
        RoomType roomType = new RoomType();
        roomType.setId(rs.getInt("id"));
        roomType.setHotelId(rs.getInt("hotel_id"));
        roomType.setName(rs.getString("name"));
        roomType.setDescription(rs.getString("description"));
        roomType.setCapacity(rs.getInt("capacity"));
        roomType.setPricePerNight(rs.getBigDecimal("price_per_night"));
        roomType.setAvailableRooms(rs.getInt("available_rooms"));
        roomType.setCreatedAt(rs.getTimestamp("created_at"));
        return roomType;
    }
}
