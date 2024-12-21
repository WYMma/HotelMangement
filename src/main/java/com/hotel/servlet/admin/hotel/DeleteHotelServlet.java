package com.hotel.servlet.admin.hotel;

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

@WebServlet("/admin/hotels/delete/*")
public class DeleteHotelServlet extends HttpServlet {
    private final HotelDAO hotelDAO = new HotelDAOImpl();

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
                "Invalid CSRF token for POST request to /admin/hotels/delete");
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid CSRF token");
            return;
        }
        
        try {
            int hotelId = Integer.parseInt(request.getPathInfo().substring(1));
            Hotel hotel = hotelDAO.findById(hotelId);
            
            if (hotel != null) {
                hotelDAO.delete(hotelId);
                LoggingUtil.logInfo("Hotel deleted successfully: " + hotel.getName() + " by admin: " + user.getUsername());
            }
            
            response.sendRedirect(request.getContextPath() + "/admin/hotels");
        } catch (Exception e) {
            LoggingUtil.logError("DeleteHotelServlet", "Error deleting hotel", e);
            response.sendRedirect(request.getContextPath() + "/admin/hotels");
        }
    }
}
