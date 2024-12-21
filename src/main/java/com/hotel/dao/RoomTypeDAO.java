package com.hotel.dao;

import com.hotel.model.RoomType;
import java.sql.SQLException;
import java.util.List;

public interface RoomTypeDAO {
    RoomType findById(int id) throws SQLException;
    List<RoomType> findByHotelId(int hotelId) throws SQLException;
    void create(RoomType roomType) throws SQLException;
    void update(RoomType roomType) throws SQLException;
    void delete(int id) throws SQLException;
    List<RoomType> findByPriceRange(double minPrice, double maxPrice) throws SQLException;
    List<RoomType> findByCapacity(int capacity) throws SQLException;
}
