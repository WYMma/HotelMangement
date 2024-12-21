package com.hotel.dao;

import java.sql.SQLException;
import java.util.List;
import com.hotel.model.Hotel;

public interface HotelDAO {
    Hotel findById(int id) throws SQLException;
    List<Hotel> findAll() throws SQLException;
    List<Hotel> findByAgentId(int agentId) throws SQLException;
    List<Hotel> findByCity(String city) throws SQLException;
    void create(Hotel hotel) throws SQLException;
    void update(Hotel hotel) throws SQLException;
    void delete(int id) throws SQLException;
    List<Hotel> search(String query) throws SQLException;
}
