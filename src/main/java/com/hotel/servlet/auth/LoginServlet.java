package com.hotel.servlet.auth;

import com.hotel.dao.AccountDAO;
import com.hotel.dao.impl.AccountDAOImpl;
import com.hotel.model.Account;
import com.hotel.util.CSRFUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(LoginServlet.class);
    private final AccountDAO accountDAO = new AccountDAOImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        if (session != null && session.getAttribute("user") != null) {
            // If already logged in, redirect to appropriate dashboard
            Account user = (Account) session.getAttribute("user");
            String contextPath = request.getContextPath();
            redirectToDashboard(response, user.getRole(), contextPath);
            return;
        }

        // Set CSRF token for the login form
        CSRFUtil.setToken(session);
        request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        HttpSession session = request.getSession();

        try {
            // First, verify CSRF token
            if (!CSRFUtil.validateToken(request)) {
                session.setAttribute("error", "Invalid request token. Please try again.");
                response.sendRedirect(request.getContextPath() + "/login");
                return;
            }

            // Attempt authentication
            if (accountDAO.authenticate(username, password)) {
                Account account = accountDAO.findByUsername(username);
                if (account != null && account.isActive()) {
                    // Store user in session
                    session.setAttribute("user", account);
                    
                    // Log successful login
                    logger.info("Successful login for user: {}", username);
                    
                    // Redirect to appropriate dashboard
                    String contextPath = request.getContextPath();
                    redirectToDashboard(response, account.getRole(), contextPath);
                    return;
                }
            }

            // If we get here, authentication failed
            logger.warn("Failed login attempt for username: {}", username);
            session.setAttribute("error", "Invalid username or password");
            response.sendRedirect(request.getContextPath() + "/login");

        } catch (SQLException e) {
            logger.error("Database error during login", e);
            session.setAttribute("error", "System error. Please try again later.");
            response.sendRedirect(request.getContextPath() + "/login");
        }
    }

    private void redirectToDashboard(HttpServletResponse response, String role, String contextPath) throws IOException {
        String dashboard = switch (role.toLowerCase()) {
            case "admin" -> contextPath + "/admin/dashboard";
            case "agent" -> contextPath + "/agent/dashboard";
            default -> "/hotels";
        };
        response.sendRedirect(dashboard);
    }
}
