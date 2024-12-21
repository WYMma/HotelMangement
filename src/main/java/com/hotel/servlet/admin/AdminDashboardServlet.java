package com.hotel.servlet.admin;

import com.hotel.dao.AccountDAO;
import com.hotel.dao.HotelDAO;
import com.hotel.dao.ReservationDAO;
import com.hotel.dao.impl.AccountDAOImpl;
import com.hotel.dao.impl.HotelDAOImpl;
import com.hotel.dao.impl.ReservationDAOImpl;
import com.hotel.model.Account;
import com.hotel.model.Hotel;
import com.hotel.util.LoggingUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/admin/dashboard")
public class AdminDashboardServlet extends HttpServlet {
    private final AccountDAO accountDAO = new AccountDAOImpl();
    private final HotelDAO hotelDAO = new HotelDAOImpl();
    private final ReservationDAO reservationDAO = new ReservationDAOImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            LoggingUtil.logInfo("Accessing dashboard with context path: " + request.getContextPath() +
                               ", URI: " + request.getRequestURI() +
                               ", Servlet Path: " + request.getServletPath());
            
            List<Account> agents = accountDAO.findAllAgents();
            List<Hotel> hotels = hotelDAO.findAll();
            
            // Get hotel count for each agent
            for (Account agent : agents) {
                List<Hotel> agentHotels = hotelDAO.findByAgentId(agent.getId());
                agent.setHotelCount(agentHotels.size());
            }
            
            request.setAttribute("agents", agents);
            request.setAttribute("hotels", hotels);
            request.setAttribute("totalAgents", agents.size());
            request.setAttribute("totalHotels", hotels.size());
            request.setAttribute("totalBookings", reservationDAO.getTotalBookings());
            
            request.getRequestDispatcher("/WEB-INF/jsp/admin/dashboard.jsp").forward(request, response);
        } catch (Exception e) {
            LoggingUtil.logError("AdminDashboardServlet", "Error loading dashboard", e);
            response.sendRedirect(request.getContextPath() + "/error");
        }
    }
}
