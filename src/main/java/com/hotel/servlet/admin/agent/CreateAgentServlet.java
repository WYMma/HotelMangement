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

@WebServlet("/admin/agents/create")
public class CreateAgentServlet extends HttpServlet {
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

        // Set CSRF token using the existing utility method
        CSRFUtil.setToken(session);
        
        request.getRequestDispatcher("/WEB-INF/jsp/admin/agent/create.jsp").forward(request, response);
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

        // Verify CSRF token using the existing utility method
        if (!CSRFUtil.validateToken(request)) {
            LoggingUtil.logSecurity(user.getUsername(), "CSRF_VIOLATION", 
                "Invalid CSRF token for POST request to /admin/agents/create");
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid CSRF token");
            return;
        }
        
        try {
            Account agent = new Account();
            agent.setUsername(request.getParameter("username"));
            agent.setPassword(request.getParameter("password")); // Note: In production, hash the password
            agent.setEmail(request.getParameter("email"));
            agent.setFirstName(request.getParameter("firstName"));
            agent.setLastName(request.getParameter("lastName"));
            agent.setPhone(request.getParameter("phone"));
            agent.setRole("agent");
            agent.setActive(true);

            accountDAO.create(agent);
            LoggingUtil.logInfo("Agent created successfully: " + agent.getUsername() + " by admin: " + user.getUsername());
            
            response.sendRedirect(request.getContextPath() + "/admin/agents");
        } catch (Exception e) {
            LoggingUtil.logError("CreateAgentServlet", "Error creating agent", e);
            request.setAttribute("error", "Failed to create agent: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/jsp/admin/agent/create.jsp").forward(request, response);
        }
    }
}
