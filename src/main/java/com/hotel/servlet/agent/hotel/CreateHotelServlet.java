package com.hotel.servlet.agent.hotel;

import com.hotel.dao.HotelDAO;
import com.hotel.dao.impl.HotelDAOImpl;
import com.hotel.model.Hotel;
import com.hotel.model.Account;
import com.hotel.util.LoggingUtil;
import com.hotel.util.CSRFUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/agent/hotels/create")
public class CreateHotelServlet extends HttpServlet {
    private final HotelDAO hotelDAO = new HotelDAOImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Account agent = (Account) session.getAttribute("user");
        
        if (session == null || agent == null || !"agent".equals(agent.getRole())) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Set CSRF token
        CSRFUtil.setToken(session);
        
        request.getRequestDispatcher("/WEB-INF/jsp/agent/hotel/create.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Account agent = (Account) session.getAttribute("user");
        
        if (session == null || agent == null || !"agent".equals(agent.getRole())) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Verify CSRF token
        if (!CSRFUtil.validateToken(request)) {
            LoggingUtil.logSecurity(agent.getUsername(), "CSRF_VIOLATION", 
                "Invalid CSRF token for POST request to /agent/hotels/create");
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid CSRF token");
            return;
        }
        
        try {
            Hotel hotel = new Hotel();
            hotel.setName(request.getParameter("name"));
            hotel.setDescription(request.getParameter("description"));
            hotel.setCity(request.getParameter("city"));
            hotel.setAddress(request.getParameter("address"));
            hotel.setStars(Integer.parseInt(request.getParameter("stars")));
            hotel.setImage(request.getParameter("image"));
            hotel.setAgentId(agent.getId()); // Set the current agent as the hotel's agent

            hotelDAO.create(hotel);
            LoggingUtil.logInfo("Hotel created successfully: " + hotel.getName() + " by agent: " + agent.getUsername());
            
            response.sendRedirect(request.getContextPath() + "/agent/dashboard");
        } catch (Exception e) {
            LoggingUtil.logError("CreateHotelServlet", "Error creating hotel", e);
            request.setAttribute("error", "Failed to create hotel: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/jsp/agent/hotel/create.jsp").forward(request, response);
        }
    }
}
