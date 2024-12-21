package com.hotel.servlet.admin.hotel;

import com.hotel.dao.HotelDAO;
import com.hotel.dao.impl.HotelDAOImpl;
import com.hotel.model.Hotel;
import com.hotel.util.LoggingUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/admin/hotels")
public class ListHotelsServlet extends HttpServlet {
    private final HotelDAO hotelDAO = new HotelDAOImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<Hotel> hotels = hotelDAO.findAll();
            request.setAttribute("hotels", hotels);
            request.getRequestDispatcher("/WEB-INF/jsp/admin/hotel/list.jsp").forward(request, response);
        } catch (Exception e) {
            LoggingUtil.logError("ListHotelsServlet", "Error listing hotels", e);
            request.setAttribute("error", "Failed to load hotels: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/admin/dashboard");
        }
    }
}
