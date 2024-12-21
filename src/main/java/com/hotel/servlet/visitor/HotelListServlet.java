package com.hotel.servlet.visitor;

import com.hotel.dao.HotelDAO;
import com.hotel.dao.impl.HotelDAOImpl;
import com.hotel.model.Hotel;
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

@WebServlet("/hotels")
public class HotelListServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(HotelListServlet.class);
    private final HotelDAO hotelDAO = new HotelDAOImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<Hotel> hotels = hotelDAO.findAll();
            request.setAttribute("hotels", hotels);
            request.getRequestDispatcher("/WEB-INF/jsp/visitor/hotels.jsp").forward(request, response);
        } catch (SQLException e) {
            logger.error("Error retrieving hotels", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error retrieving hotels");
        }
    }
}
