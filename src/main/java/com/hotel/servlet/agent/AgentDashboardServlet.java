package com.hotel.servlet.agent;

import com.hotel.dao.HotelDAO;
import com.hotel.dao.ReservationDAO;
import com.hotel.dao.impl.HotelDAOImpl;
import com.hotel.dao.impl.ReservationDAOImpl;
import com.hotel.model.Account;
import com.hotel.model.Hotel;
import com.hotel.model.Reservation;
import com.hotel.util.LoggingUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@WebServlet("/agent/dashboard")
public class AgentDashboardServlet extends HttpServlet {
    private static final int ITEMS_PER_PAGE = 10;
    private final HotelDAO hotelDAO = new HotelDAOImpl();
    private final ReservationDAO reservationDAO = new ReservationDAOImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Validate agent session
            Account agent = validateAgentSession(request, response);
            if (agent == null) {
                return; // Response has been redirected
            }

            // Get page parameters
            int hotelPage = getPageParameter(request, "hotelPage");
            int reservationPage = getPageParameter(request, "reservationPage");

            // Load dashboard data
            loadHotelsData(request, agent.getId(), hotelPage);
            loadReservationsData(request, agent.getId(), reservationPage);

            // Forward to dashboard
            request.getRequestDispatcher("/WEB-INF/jsp/agent/dashboard.jsp").forward(request, response);

        } catch (Exception e) {
            LoggingUtil.logError("AgentDashboardServlet", "Error loading dashboard", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error loading dashboard");
        }
    }

    private Account validateAgentSession(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            LoggingUtil.logWarning("Agent session not found, redirecting to login");
            response.sendRedirect(request.getContextPath() + "/login");
            return null;
        }

        Account agent = (Account) session.getAttribute("user");
        if (!"agent".equalsIgnoreCase(agent.getRole())) {
            LoggingUtil.logWarning("Unauthorized access attempt to agent dashboard by user: " + agent.getUsername());
            response.sendRedirect(request.getContextPath() + "/403");
            return null;
        }

        return agent;
    }

    private void loadHotelsData(HttpServletRequest request, int agentId, int page) {
        try {
            // Get paginated hotels
            List<Hotel> allHotels = hotelDAO.findByAgentId(agentId);
            int totalHotels = allHotels.size();
            
            int startIndex = page * ITEMS_PER_PAGE;
            int endIndex = Math.min(startIndex + ITEMS_PER_PAGE, totalHotels);
            
            List<Hotel> pagedHotels = startIndex < totalHotels ? 
                allHotels.subList(startIndex, endIndex) : 
                Collections.emptyList();

            // Set attributes
            request.setAttribute("hotels", pagedHotels);
            request.setAttribute("totalHotels", totalHotels);
            request.setAttribute("totalHotelPages", (int) Math.ceil(totalHotels / (double) ITEMS_PER_PAGE));
            request.setAttribute("currentHotelPage", page);

        } catch (Exception e) {
            LoggingUtil.logError("AgentDashboardServlet", "Error loading hotels data", e);
            request.setAttribute("hotelError", "Unable to load hotels. Please try again later.");
        }
    }

    private void loadReservationsData(HttpServletRequest request, int agentId, int page) {
        try {
            // Get paginated reservations
            List<Reservation> allReservations = reservationDAO.findByAgentId(agentId);
            int totalReservations = allReservations.size();
            
            int startIndex = page * ITEMS_PER_PAGE;
            int endIndex = Math.min(startIndex + ITEMS_PER_PAGE, totalReservations);
            
            List<Reservation> pagedReservations = startIndex < totalReservations ? 
                allReservations.subList(startIndex, endIndex) : 
                Collections.emptyList();

            // Calculate statistics
            long activeBookings = allReservations.stream()
                .filter(r -> "CONFIRMED".equals(r.getStatus()))
                .count();

            // Set attributes
            request.setAttribute("reservations", pagedReservations);
            request.setAttribute("totalReservations", totalReservations);
            request.setAttribute("totalReservationPages", (int) Math.ceil(totalReservations / (double) ITEMS_PER_PAGE));
            request.setAttribute("currentReservationPage", page);
            request.setAttribute("activeBookings", activeBookings);

        } catch (Exception e) {
            LoggingUtil.logError("AgentDashboardServlet", "Error loading reservations data", e);
            request.setAttribute("reservationError", "Unable to load reservations. Please try again later.");
        }
    }

    private int getPageParameter(HttpServletRequest request, String paramName) {
        try {
            String pageStr = request.getParameter(paramName);
            int page = pageStr != null ? Integer.parseInt(pageStr) : 0;
            return Math.max(0, page); // Ensure page is not negative
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
