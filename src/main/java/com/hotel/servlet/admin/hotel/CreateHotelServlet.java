package com.hotel.servlet.admin.hotel;

import com.hotel.dao.AccountDAO;
import com.hotel.dao.HotelDAO;
import com.hotel.dao.impl.AccountDAOImpl;
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
import java.util.List;

@WebServlet("/admin/hotels/create")
public class CreateHotelServlet extends HttpServlet {
    private final HotelDAO hotelDAO = new HotelDAOImpl();
    private final AccountDAO accountDAO = new AccountDAOImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Account user = (Account) session.getAttribute("user");
        
        if (session == null || user == null || !"admin".equals(user.getRole())) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            // Get all active agents for the dropdown
            List<Account> agents = accountDAO.findAllAgents();
            request.setAttribute("agents", agents);

            // Set CSRF token
            CSRFUtil.setToken(session);
            
            request.getRequestDispatcher("/WEB-INF/jsp/admin/hotel/create.jsp").forward(request, response);
        } catch (Exception e) {
            LoggingUtil.logError("CreateHotelServlet", "Error loading create hotel page", e);
            response.sendRedirect(request.getContextPath() + "/admin/hotels");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Account user = (Account) session.getAttribute("user");
        
        if (session == null || user == null || !"admin".equals(user.getRole())) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Verify CSRF token
        if (!CSRFUtil.validateToken(request)) {
            LoggingUtil.logSecurity(user.getUsername(), "CSRF_VIOLATION", 
                "Invalid CSRF token for POST request to /admin/hotels/create");
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
            hotel.setAgentId(Integer.parseInt(request.getParameter("agentId")));

            hotelDAO.create(hotel);
            LoggingUtil.logInfo("Hotel created successfully: " + hotel.getName() + " by admin: " + user.getUsername());
            
            response.sendRedirect(request.getContextPath() + "/admin/hotels");
        } catch (Exception e) {
            LoggingUtil.logError("CreateHotelServlet", "Error creating hotel", e);
            request.setAttribute("error", "Failed to create hotel: " + e.getMessage());
            doGet(request, response);
        }
    }
}
