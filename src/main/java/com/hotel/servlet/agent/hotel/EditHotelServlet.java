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

@WebServlet("/agent/hotels/edit")
public class EditHotelServlet extends HttpServlet {
    private final HotelDAO hotelDAO = new HotelDAOImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Validate agent session
            HttpSession session = request.getSession(false);
            Account agent = (Account) session.getAttribute("user");
            
            if (session == null || agent == null || !"agent".equalsIgnoreCase(agent.getRole())) {
                response.sendRedirect(request.getContextPath() + "/login");
                return;
            }

            // Get hotel ID from query parameter
            String hotelIdStr = request.getParameter("id");
            if (hotelIdStr == null || hotelIdStr.trim().isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/agent/dashboard");
                return;
            }

            try {
                int hotelId = Integer.parseInt(hotelIdStr);
                Hotel hotel = hotelDAO.findById(hotelId);
                
                // Verify that the hotel belongs to this agent
                if (hotel == null || hotel.getAgentId() != agent.getId()) {
                    LoggingUtil.logWarning("Unauthorized attempt to edit hotel " + hotelId + " by agent " + agent.getUsername());
                    response.sendRedirect(request.getContextPath() + "/403");
                    return;
                }
                
                // Set CSRF token and forward to edit page
                CSRFUtil.setToken(session);
                request.setAttribute("hotel", hotel);
                request.getRequestDispatcher("/WEB-INF/jsp/agent/hotel/edit.jsp").forward(request, response);
                
            } catch (NumberFormatException e) {
                LoggingUtil.logError("EditHotelServlet", "Invalid hotel ID format: " + hotelIdStr, e);
                response.sendRedirect(request.getContextPath() + "/agent/dashboard");
            }
        } catch (Exception e) {
            LoggingUtil.logError("EditHotelServlet", "Error loading hotel for editing", e);
            response.sendRedirect(request.getContextPath() + "/error");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Validate agent session
            HttpSession session = request.getSession(false);
            Account agent = (Account) session.getAttribute("user");
            
            if (session == null || agent == null || !"agent".equalsIgnoreCase(agent.getRole())) {
                response.sendRedirect(request.getContextPath() + "/login");
                return;
            }

            // Validate CSRF token
            if (!CSRFUtil.validateToken(request)) {
                LoggingUtil.logSecurity(agent.getUsername(), "INVALID_CSRF", "Invalid CSRF token in hotel edit");
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid CSRF token");
                return;
            }

            // Get hotel data
            int hotelId = Integer.parseInt(request.getParameter("id"));
            Hotel existingHotel = hotelDAO.findById(hotelId);
            
            // Verify ownership
            if (existingHotel == null || existingHotel.getAgentId() != agent.getId()) {
                LoggingUtil.logWarning("Unauthorized attempt to update hotel " + hotelId + " by agent " + agent.getUsername());
                response.sendRedirect(request.getContextPath() + "/403");
                return;
            }

            // Update hotel data
            Hotel hotel = new Hotel();
            hotel.setId(hotelId);
            hotel.setName(request.getParameter("name"));
            hotel.setDescription(request.getParameter("description"));
            hotel.setCity(request.getParameter("city"));
            hotel.setAddress(request.getParameter("address"));
            hotel.setStars(Integer.parseInt(request.getParameter("stars")));
            hotel.setImage(request.getParameter("image"));
            hotel.setAgentId(agent.getId());

            hotelDAO.update(hotel);
            LoggingUtil.logInfo("Hotel " + hotelId + " updated successfully by agent " + agent.getUsername());
            
            response.sendRedirect(request.getContextPath() + "/agent/dashboard");
            
        } catch (Exception e) {
            LoggingUtil.logError("EditHotelServlet", "Error updating hotel", e);
            response.sendRedirect(request.getContextPath() + "/error");
        }
    }
}
