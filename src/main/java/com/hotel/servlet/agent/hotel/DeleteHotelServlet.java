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

@WebServlet("/agent/hotels/delete/*")
public class DeleteHotelServlet extends HttpServlet {
    private final HotelDAO hotelDAO = new HotelDAOImpl();

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
                "Invalid CSRF token for POST request to /agent/hotels/delete");
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid CSRF token");
            return;
        }
        
        try {
            int hotelId = Integer.parseInt(request.getPathInfo().substring(1));
            Hotel hotel = hotelDAO.findById(hotelId);
            
            // Verify that the hotel belongs to this agent
            if (hotel != null && hotel.getAgentId() == agent.getId()) {
                hotelDAO.delete(hotelId);
                LoggingUtil.logInfo("Hotel deleted successfully: " + hotel.getName() + " by agent: " + agent.getUsername());
            } else {
                LoggingUtil.logSecurity(agent.getUsername(), "UNAUTHORIZED_ACCESS", 
                    "Attempted to delete hotel " + hotelId + " belonging to another agent");
            }
            
            response.sendRedirect(request.getContextPath() + "/agent/dashboard");
        } catch (Exception e) {
            LoggingUtil.logError("DeleteHotelServlet", "Error deleting hotel", e);
            response.sendRedirect(request.getContextPath() + "/agent/dashboard");
        }
    }
}
