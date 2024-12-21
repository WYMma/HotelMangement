package com.hotel.servlet.admin.agent;

import com.hotel.dao.AccountDAO;
import com.hotel.dao.impl.AccountDAOImpl;
import com.hotel.model.Account;
import com.hotel.util.LoggingUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet("/admin/agents")
public class ListAgentsServlet extends HttpServlet {
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
            List<Account> agents = accountDAO.findAllAgents();
            request.setAttribute("agents", agents);
            request.getRequestDispatcher("/WEB-INF/jsp/admin/agent/list.jsp").forward(request, response);
        } catch (Exception e) {
            LoggingUtil.logError("ListAgentsServlet", "Error listing agents", e);
            request.setAttribute("error", "Failed to load agents: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/admin/dashboard");
        }
    }
}
