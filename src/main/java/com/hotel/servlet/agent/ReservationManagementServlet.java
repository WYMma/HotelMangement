package com.hotel.servlet.agent;

import com.hotel.dao.ReservationDAO;
import com.hotel.dao.impl.ReservationDAOImpl;
import com.hotel.model.Account;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet({"/agent/reservations/confirm/*", "/agent/reservations/cancel/*"})
public class ReservationManagementServlet extends HttpServlet {
    private final ReservationDAO reservationDAO = new ReservationDAOImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String pathInfo = request.getPathInfo();
            String[] pathParts = pathInfo.split("/");
            
            if (pathParts.length != 3) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            String action = pathParts[1];
            int reservationId = Integer.parseInt(pathParts[2]);

            // Get the current agent
            Account agent = (Account) request.getSession().getAttribute("user");
            if (agent == null || !"AGENT".equals(agent.getRole())) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }

            // Verify CSRF token
            String sessionToken = (String) request.getSession().getAttribute("csrf_token");
            String requestToken = request.getParameter("csrf_token");
            if (sessionToken == null || !sessionToken.equals(requestToken)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid CSRF token");
                return;
            }

            // Verify that the reservation belongs to one of the agent's hotels
            if (!reservationDAO.isReservationForAgentHotel(reservationId, agent.getId())) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }

            boolean success = false;
            switch (action.toLowerCase()) {
                case "confirm":
                    success = reservationDAO.confirmReservation(reservationId);
                    break;
                case "cancel":
                    success = reservationDAO.cancelReservation(reservationId);
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                    return;
            }

            if (success) {
                response.sendRedirect(request.getContextPath() + "/agent/dashboard");
            } else {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
