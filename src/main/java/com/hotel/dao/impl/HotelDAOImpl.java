package com.hotel.dao.impl;

import com.hotel.dao.HotelDAO;
import com.hotel.model.Hotel;
import com.hotel.util.DatabaseUtil;
import com.hotel.util.LoggingUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HotelDAOImpl implements HotelDAO {
    private static final Logger logger = LoggerFactory.getLogger(HotelDAOImpl.class);

    @Override
    public List<Hotel> findAll() throws SQLException {
        List<Hotel> hotels = new ArrayList<>();
        String sql = "SELECT h.*, CONCAT(a.first_name, ' ', a.last_name) as agent_name " +
                    "FROM hotel h " +
                    "LEFT JOIN account a ON h.agent_id = a.id " +
                    "ORDER BY h.name";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Hotel hotel = mapResultSetToHotel(rs);
                hotel.setAgentName(rs.getString("agent_name"));
                hotels.add(hotel);
            }
        }
        return hotels;
    }

    @Override
    public Hotel findById(int id) throws SQLException {
        String sql = "SELECT * FROM hotel WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToHotel(rs);
                }
            }
        }
        return null;
    }

    @Override
    public List<Hotel> findByAgentId(int agentId) throws SQLException {
        List<Hotel> hotels = new ArrayList<>();
        String sql = "SELECT * FROM hotel WHERE agent_id = ? ORDER BY name";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, agentId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    hotels.add(mapResultSetToHotel(rs));
                }
            }
        }
        return hotels;
    }

    @Override
    public List<Hotel> findByCity(String city) throws SQLException {
        List<Hotel> hotels = new ArrayList<>();
        String sql = "SELECT * FROM hotel WHERE city = ? ORDER BY name";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, city);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    hotels.add(mapResultSetToHotel(rs));
                }
            }
        }
        return hotels;
    }

    @Override
    public void create(Hotel hotel) throws SQLException {
        String sql = "INSERT INTO hotel (name, description, city, address, stars, image, agent_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, hotel.getName());
            stmt.setString(2, hotel.getDescription());
            stmt.setString(3, hotel.getCity());
            stmt.setString(4, hotel.getAddress());
            stmt.setInt(5, hotel.getStars());
            stmt.setString(6, hotel.getImage());
            stmt.setInt(7, hotel.getAgentId());

            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    hotel.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    @Override
    public void update(Hotel hotel) throws SQLException {
        String sql = "UPDATE hotel SET name = ?, description = ?, city = ?, address = ?, stars = ?, image = ?, agent_id = ? WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, hotel.getName());
            stmt.setString(2, hotel.getDescription());
            stmt.setString(3, hotel.getCity());
            stmt.setString(4, hotel.getAddress());
            stmt.setInt(5, hotel.getStars());
            stmt.setString(6, hotel.getImage());
            stmt.setInt(7, hotel.getAgentId());
            stmt.setInt(8, hotel.getId());

            stmt.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM hotel WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    @Override
    public List<Hotel> search(String query) throws SQLException {
        List<Hotel> hotels = new ArrayList<>();
        String sql = "SELECT h.*, CONCAT(a.first_name, ' ', a.last_name) as agent_name " +
                     "FROM hotel h " +
                     "LEFT JOIN account a ON h.agent_id = a.id " +
                     "WHERE LOWER(h.name) LIKE LOWER(?) OR LOWER(h.description) LIKE LOWER(?) OR LOWER(h.city) LIKE LOWER(?) " +
                     "ORDER BY h.name";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            String searchQuery = "%" + query.trim() + "%";
            stmt.setString(1, searchQuery);
            stmt.setString(2, searchQuery);
            stmt.setString(3, searchQuery);
            
            LoggingUtil.logInfo("Executing search query: " + sql.replace("?", "'" + searchQuery + "'"));
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Hotel hotel = mapResultSetToHotel(rs);
                    hotel.setAgentName(rs.getString("agent_name"));
                    hotels.add(hotel);
                }
            }
        } catch (SQLException e) {
            LoggingUtil.logError("HotelDAO", "Error executing search query", e);
            throw e;
        }
        return hotels;
    }

    private Hotel mapResultSetToHotel(ResultSet rs) throws SQLException {
        Hotel hotel = new Hotel();
        hotel.setId(rs.getInt("id"));
        hotel.setName(rs.getString("name"));
        hotel.setDescription(rs.getString("description"));
        hotel.setCity(rs.getString("city"));
        hotel.setAddress(rs.getString("address"));
        hotel.setStars(rs.getInt("stars"));
        hotel.setImage(rs.getString("image"));
        hotel.setAgentId(rs.getInt("agent_id"));
        hotel.setCreatedAt(rs.getTimestamp("created_at"));
        return hotel;
    }
}
