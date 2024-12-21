package com.hotel.servlet.agent.roomtype;

import com.hotel.dao.RoomTypeDAO;
import com.hotel.dao.impl.RoomTypeDAOImpl;
import com.hotel.model.RoomType;
import com.hotel.util.SecurityUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/agent/hotel/room-type/delete")
public class DeleteRoomTypeServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(DeleteRoomTypeServlet.class);
    private final RoomTypeDAO roomTypeDAO = new RoomTypeDAOImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Check if user is logged in and is an agent
        if (!SecurityUtil.isAgentLoggedIn(request)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            // Get room type ID and hotel ID from request parameters
            int roomTypeId = Integer.parseInt(request.getParameter("id"));
            int hotelId = Integer.parseInt(request.getParameter("hotelId"));

            // Get room type to verify it exists and belongs to the hotel
            RoomType roomType = roomTypeDAO.findById(roomTypeId);
            if (roomType == null || roomType.getHotelId() != hotelId) {
                response.sendRedirect(request.getContextPath() + "/agent/dashboard");
                return;
            }

            // Delete room type
            roomTypeDAO.delete(roomTypeId);

            // Redirect back to hotel edit page
            response.sendRedirect(request.getContextPath() + "/agent/hotel/edit?id=" + hotelId);

        } catch (NumberFormatException e) {
            logger.error("Invalid room type or hotel ID", e);
            response.sendRedirect(request.getContextPath() + "/agent/dashboard");
        } catch (SQLException e) {
            logger.error("Database error while deleting room type", e);
            response.sendRedirect(request.getContextPath() + "/agent/dashboard");
        }
    }
}
