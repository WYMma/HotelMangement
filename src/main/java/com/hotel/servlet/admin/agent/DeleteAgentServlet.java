package com.hotel.servlet.admin.agent;

import com.hotel.dao.AccountDAO;
import com.hotel.dao.impl.AccountDAOImpl;
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

@WebServlet("/admin/agents/delete/*")
public class DeleteAgentServlet extends HttpServlet {
    private final AccountDAO accountDAO = new AccountDAOImpl();

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
                "Invalid CSRF token for POST request to /admin/agents/delete");
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid CSRF token");
            return;
        }
        
        try {
            int agentId = Integer.parseInt(request.getPathInfo().substring(1));
            Account agent = accountDAO.findById(agentId);
            
            if (agent != null && "agent".equals(agent.getRole())) {
                accountDAO.delete(agentId);
                LoggingUtil.logInfo("Agent deleted successfully: " + agent.getUsername() + " by admin: " + user.getUsername());
            }
            
            response.sendRedirect(request.getContextPath() + "/admin/agents");
        } catch (Exception e) {
            LoggingUtil.logError("DeleteAgentServlet", "Error deleting agent", e);
            response.sendRedirect(request.getContextPath() + "/admin/agents");
        }
    }
}
