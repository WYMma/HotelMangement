package com.hotel.dao.impl;

import com.hotel.dao.ReservationDAO;
import com.hotel.model.Reservation;
import com.hotel.util.DatabaseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationDAOImpl implements ReservationDAO {
    private static final Logger logger = LoggerFactory.getLogger(ReservationDAOImpl.class);

    @Override
    public void create(Reservation reservation) throws SQLException {
        String sql = "INSERT INTO reservations (hotel_id, room_type_id, guest_name, guest_email, guest_phone, " +
                    "check_in_date, check_out_date, number_of_guests, special_requests, status) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, reservation.getHotelId());
            stmt.setInt(2, reservation.getRoomTypeId());
            stmt.setString(3, reservation.getGuestName());
            stmt.setString(4, reservation.getGuestEmail());
            stmt.setString(5, reservation.getGuestPhone());
            stmt.setDate(6, reservation.getCheckInDate());
            stmt.setDate(7, reservation.getCheckOutDate());
            stmt.setInt(8, reservation.getNumberOfGuests());
            stmt.setString(9, reservation.getSpecialRequests());
            stmt.setString(10, reservation.getStatus());
            
            stmt.executeUpdate();
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    reservation.setId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            logger.error("Error creating reservation", e);
            throw e;
        }
    }

    @Override
    public Reservation findById(int id) throws SQLException {
        String sql = "SELECT r.*, h.name as hotel_name, rt.name as room_type_name FROM reservations r " +
                    "LEFT JOIN hotel h ON r.hotel_id = h.id " +
                    "LEFT JOIN room_type rt ON r.room_type_id = rt.id " +
                    "WHERE r.id = ?";
                    
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToReservation(rs);
                }
            }
        } catch (SQLException e) {
            logger.error("Error finding reservation by id", e);
            throw e;
        }
        return null;
    }

    @Override
    public List<Reservation> findByHotelId(int hotelId) throws SQLException {
        String sql = "SELECT r.*, h.name as hotel_name, rt.name as room_type_name FROM reservations r " +
                    "LEFT JOIN hotel h ON r.hotel_id = h.id " +
                    "LEFT JOIN room_type rt ON r.room_type_id = rt.id " +
                    "WHERE r.hotel_id = ? " +
                    "ORDER BY r.created_at DESC";
                    
        List<Reservation> reservations = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, hotelId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    reservations.add(mapResultSetToReservation(rs));
                }
            }
        } catch (SQLException e) {
            logger.error("Error finding reservations by hotel id", e);
            throw e;
        }
        return reservations;
    }

    @Override
    public List<Reservation> findByGuestEmail(String email) throws SQLException {
        String sql = "SELECT r.*, h.name as hotel_name, rt.name as room_type_name FROM reservations r " +
                    "LEFT JOIN hotel h ON r.hotel_id = h.id " +
                    "LEFT JOIN room_type rt ON r.room_type_id = rt.id " +
                    "WHERE r.guest_email = ? " +
                    "ORDER BY r.created_at DESC";
                    
        List<Reservation> reservations = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    reservations.add(mapResultSetToReservation(rs));
                }
            }
        } catch (SQLException e) {
            logger.error("Error finding reservations by guest email", e);
            throw e;
        }
        return reservations;
    }

    @Override
    public void updateStatus(int id, String status) throws SQLException {
        String sql = "UPDATE reservations SET status = ? WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, status);
            stmt.setInt(2, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.error("Error updating reservation status", e);
            throw e;
        }
    }

    @Override
    public List<Reservation> findByAgentId(int agentId) throws SQLException {
        String sql = "SELECT r.*, h.name as hotel_name, rt.name as room_type_name FROM reservations r " +
                    "INNER JOIN hotel h ON r.hotel_id = h.id " +
                    "LEFT JOIN room_type rt ON r.room_type_id = rt.id " +
                    "WHERE h.agent_id = ? " +
                    "ORDER BY r.created_at DESC";
                    
        List<Reservation> reservations = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, agentId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    reservations.add(mapResultSetToReservation(rs));
                }
            }
        } catch (SQLException e) {
            logger.error("Error finding reservations for agent", e);
            throw e;
        }
        return reservations;
    }

    @Override
    public boolean isReservationForAgentHotel(int reservationId, int agentId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM reservations r " +
                    "JOIN hotel h ON r.hotel_id = h.id " +
                    "WHERE r.id = ? AND h.agent_id = ?";
                    
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, reservationId);
            stmt.setInt(2, agentId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            logger.error("Error checking if reservation is for agent hotel", e);
            throw e;
        }
        return false;
    }

    @Override
    public boolean confirmReservation(int reservationId) throws SQLException {
        return updateReservationStatus(reservationId, "CONFIRMED");
    }

    @Override
    public boolean cancelReservation(int reservationId) throws SQLException {
        return updateReservationStatus(reservationId, "CANCELLED");
    }

    @Override
    public int getTotalBookings() throws SQLException {
        String sql = "SELECT COUNT(*) FROM reservations";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            logger.error("Error getting total bookings", e);
            throw e;
        }
        return 0;
    }

    private boolean updateReservationStatus(int reservationId, String status) throws SQLException {
        String sql = "UPDATE reservations SET status = ? WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, status);
            stmt.setInt(2, reservationId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Error updating reservation status", e);
            throw e;
        }
    }

    private Reservation mapResultSetToReservation(ResultSet rs) throws SQLException {
        Reservation reservation = new Reservation();
        reservation.setId(rs.getInt("id"));
        reservation.setHotelId(rs.getInt("hotel_id"));
        reservation.setRoomTypeId(rs.getInt("room_type_id"));
        reservation.setGuestName(rs.getString("guest_name"));
        reservation.setGuestEmail(rs.getString("guest_email"));
        reservation.setGuestPhone(rs.getString("guest_phone"));
        reservation.setCheckInDate(rs.getDate("check_in_date"));
        reservation.setCheckOutDate(rs.getDate("check_out_date"));
        reservation.setNumberOfGuests(rs.getInt("number_of_guests"));
        reservation.setSpecialRequests(rs.getString("special_requests"));
        reservation.setStatus(rs.getString("status"));
        reservation.setCreatedAt(rs.getTimestamp("created_at"));
        
        // Set hotel name if available from join
        try {
            reservation.setHotelName(rs.getString("hotel_name"));
        } catch (SQLException e) {
            // Ignore if hotel_name is not in result set
        }
        
        // Set room type name if available from join
        try {
            reservation.setRoomTypeName(rs.getString("room_type_name"));
        } catch (SQLException e) {
            // Ignore if room_type_name is not in result set
        }
        
        return reservation;
    }
}
