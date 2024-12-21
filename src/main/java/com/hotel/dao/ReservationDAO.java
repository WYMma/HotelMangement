package com.hotel.dao;

import com.hotel.model.Reservation;
import java.sql.SQLException;
import java.util.List;

public interface ReservationDAO {
    void create(Reservation reservation) throws SQLException;
    Reservation findById(int id) throws SQLException;
    List<Reservation> findByHotelId(int hotelId) throws SQLException;
    List<Reservation> findByGuestEmail(String email) throws SQLException;
    void updateStatus(int id, String status) throws SQLException;
    List<Reservation> findByAgentId(int agentId) throws SQLException;
    boolean isReservationForAgentHotel(int reservationId, int agentId) throws SQLException;
    boolean confirmReservation(int reservationId) throws SQLException;
    boolean cancelReservation(int reservationId) throws SQLException;
    int getTotalBookings() throws SQLException;  // Added method for total bookings
}
