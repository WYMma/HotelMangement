package com.hotel.servlet.visitor;

import com.hotel.dao.HotelDAO;
import com.hotel.dao.RoomTypeDAO;
import com.hotel.dao.impl.HotelDAOImpl;
import com.hotel.dao.impl.RoomTypeDAOImpl;
import com.hotel.model.Hotel;
import com.hotel.model.RoomType;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/hotels/details/*")
public class HotelDetailsServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(HotelDetailsServlet.class);
    private final HotelDAO hotelDAO = new HotelDAOImpl();
    private final RoomTypeDAO roomTypeDAO = new RoomTypeDAOImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Get hotel ID from path
            String pathInfo = request.getPathInfo();
            if (pathInfo == null || pathInfo.equals("/")) {
                response.sendRedirect(request.getContextPath() + "/hotels");
                return;
            }

            // Remove leading slash and parse ID
            String hotelIdStr = pathInfo.substring(1);
            logger.info("Fetching details for hotel ID: {}", hotelIdStr);

            int hotelId = Integer.parseInt(hotelIdStr);
            Hotel hotel = hotelDAO.findById(hotelId);
            
            if (hotel == null) {
                logger.warn("Hotel not found with ID: {}", hotelId);
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Hotel not found");
                return;
            }

            // Get room types for this hotel
            List<RoomType> roomTypes = roomTypeDAO.findByHotelId(hotelId);
            logger.info("Found {} room types for hotel {}", roomTypes.size(), hotelId);

            request.setAttribute("hotel", hotel);
            request.setAttribute("roomTypes", roomTypes);
            request.getRequestDispatcher("/WEB-INF/jsp/visitor/hotel-details.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            logger.error("Invalid hotel ID format", e);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid hotel ID");
        } catch (SQLException e) {
            logger.error("Error retrieving hotel details", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error retrieving hotel details");
        }
    }
}
