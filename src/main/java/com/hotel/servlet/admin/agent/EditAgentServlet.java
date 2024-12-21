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

@WebServlet("/admin/agents/edit/*")
public class EditAgentServlet extends HttpServlet {
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

        // Set CSRF token
        CSRFUtil.setToken(session);
        
        try {
            int agentId = Integer.parseInt(request.getPathInfo().substring(1));
            Account agent = accountDAO.findById(agentId);
            
            if (agent == null || !"agent".equals(agent.getRole())) {
                response.sendRedirect(request.getContextPath() + "/admin/agents");
                return;
            }
            
            request.setAttribute("agent", agent);
            request.getRequestDispatcher("/WEB-INF/jsp/admin/agent/edit.jsp").forward(request, response);
        } catch (Exception e) {
            LoggingUtil.logError("EditAgentServlet", "Error loading agent for editing", e);
            response.sendRedirect(request.getContextPath() + "/admin/agents");
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
                "Invalid CSRF token for POST request to /admin/agents/edit");
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid CSRF token");
            return;
        }
        
        try {
            int agentId = Integer.parseInt(request.getPathInfo().substring(1));
            Account agent = accountDAO.findById(agentId);
            
            if (agent == null || !"agent".equals(agent.getRole())) {
                response.sendRedirect(request.getContextPath() + "/admin/agents");
                return;
            }

            agent.setEmail(request.getParameter("email"));
            agent.setFirstName(request.getParameter("firstName"));
            agent.setLastName(request.getParameter("lastName"));
            agent.setPhone(request.getParameter("phone"));
            agent.setActive("on".equals(request.getParameter("active")));

            String newPassword = request.getParameter("password");
            if (newPassword != null && !newPassword.trim().isEmpty()) {
                agent.setPassword(newPassword); // Note: In production, hash the password
            }

            accountDAO.update(agent);
            LoggingUtil.logInfo("Agent updated successfully: " + agent.getUsername() + " by admin: " + user.getUsername());
            
            response.sendRedirect(request.getContextPath() + "/admin/agents");
        } catch (Exception e) {
            LoggingUtil.logError("EditAgentServlet", "Error updating agent", e);
            request.setAttribute("error", "Failed to update agent: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/jsp/admin/agent/edit.jsp").forward(request, response);
        }
    }
}
