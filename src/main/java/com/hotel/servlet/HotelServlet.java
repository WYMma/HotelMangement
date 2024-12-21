package com.hotel.servlet;

import com.hotel.dao.HotelDAO;
import com.hotel.dao.RoomTypeDAO;
import com.hotel.dao.impl.HotelDAOImpl;
import com.hotel.dao.impl.RoomTypeDAOImpl;
import com.hotel.model.Hotel;
import com.hotel.model.RoomType;
import com.hotel.util.CSRFUtil;
import com.hotel.util.LoggingUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/hotels/*")
public class HotelServlet extends HttpServlet {
    private final HotelDAO hotelDAO = new HotelDAOImpl();
    private final RoomTypeDAO roomTypeDAO = new RoomTypeDAOImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        String searchQuery = request.getParameter("search");
        
        try {
            if (searchQuery != null && !searchQuery.trim().isEmpty()) {
                // Search hotels
                LoggingUtil.logInfo("Searching for hotels with query: " + searchQuery);
                List<Hotel> hotels = hotelDAO.search(searchQuery);
                LoggingUtil.logInfo("Found " + hotels.size() + " hotels matching the search query");
                request.setAttribute("hotels", hotels);
                request.getRequestDispatcher("/WEB-INF/jsp/visitor/hotels.jsp").forward(request, response);
                return;
            }
            
            if (pathInfo == null || pathInfo.equals("/")) {
                // List all hotels
                List<Hotel> hotels = hotelDAO.findAll();
                request.setAttribute("hotels", hotels);
                request.getRequestDispatcher("/WEB-INF/jsp/visitor/hotels.jsp").forward(request, response);
            } else {
                // Show specific hotel
                int hotelId = Integer.parseInt(pathInfo.substring(1));
                Hotel hotel = hotelDAO.findById(hotelId);
                
                if (hotel == null) {
                    response.sendRedirect(request.getContextPath() + "/404.jsp");
                    return;
                }
                
                // Set CSRF token for the booking form
                CSRFUtil.setToken(request.getSession());
                
                List<RoomType> roomTypes = roomTypeDAO.findByHotelId(hotelId);
                request.setAttribute("hotel", hotel);
                request.setAttribute("roomTypes", roomTypes);
                request.getRequestDispatcher("/WEB-INF/jsp/visitor/hotel-details.jsp").forward(request, response);
            }
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/404.jsp");
        } catch (Exception e) {
            LoggingUtil.logError("HotelServlet", "Error processing hotel request", e);
            response.sendRedirect(request.getContextPath() + "/error");
        }
    }
}
