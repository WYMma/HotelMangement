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
import java.math.BigDecimal;
import java.sql.SQLException;

@WebServlet("/agent/hotel/room-type/edit")
public class EditRoomTypeServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(EditRoomTypeServlet.class);
    private final RoomTypeDAO roomTypeDAO = new RoomTypeDAOImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Check if user is logged in and is an agent
        if (!SecurityUtil.isAgentLoggedIn(request)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            // Get room type ID from request parameter
            int roomTypeId = Integer.parseInt(request.getParameter("id"));
            RoomType roomType = roomTypeDAO.findById(roomTypeId);

            if (roomType == null) {
                response.sendRedirect(request.getContextPath() + "/agent/dashboard");
                return;
            }

            request.setAttribute("roomType", roomType);
            request.getRequestDispatcher("/WEB-INF/jsp/agent/roomtype/edit.jsp").forward(request, response);

        } catch (NumberFormatException | SQLException e) {
            logger.error("Error retrieving room type", e);
            response.sendRedirect(request.getContextPath() + "/agent/dashboard");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Check if user is logged in and is an agent
        if (!SecurityUtil.isAgentLoggedIn(request)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            // Get form parameters
            int id = Integer.parseInt(request.getParameter("id"));
            int hotelId = Integer.parseInt(request.getParameter("hotelId"));
            String name = request.getParameter("name");
            String description = request.getParameter("description");
            int capacity = Integer.parseInt(request.getParameter("capacity"));
            BigDecimal pricePerNight = new BigDecimal(request.getParameter("pricePerNight"));
            int availableRooms = Integer.parseInt(request.getParameter("availableRooms"));

            // Validate input
            if (name == null || name.trim().isEmpty() || 
                description == null || description.trim().isEmpty() || 
                capacity <= 0 || pricePerNight.compareTo(BigDecimal.ZERO) <= 0 || 
                availableRooms < 0) {
                request.setAttribute("error", "Please fill in all fields correctly");
                doGet(request, response);
                return;
            }

            // Create room type object
            RoomType roomType = new RoomType();
            roomType.setId(id);
            roomType.setHotelId(hotelId);
            roomType.setName(name);
            roomType.setDescription(description);
            roomType.setCapacity(capacity);
            roomType.setPricePerNight(pricePerNight);
            roomType.setAvailableRooms(availableRooms);

            // Update in database
            roomTypeDAO.update(roomType);

            // Redirect to hotel details page
            response.sendRedirect(request.getContextPath() + "/agent/hotel/edit?id=" + hotelId);

        } catch (NumberFormatException e) {
            logger.error("Invalid number format in form submission", e);
            request.setAttribute("error", "Please enter valid numbers for capacity, price, and available rooms");
            doGet(request, response);
        } catch (SQLException e) {
            logger.error("Database error while updating room type", e);
            request.setAttribute("error", "Error updating room type. Please try again.");
            doGet(request, response);
        }
    }
}
