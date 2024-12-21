package com.hotel.filter;

import com.hotel.model.Account;
import com.hotel.util.LoggingUtil;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@WebFilter("/*")
public class SecurityFilter implements Filter {
    private static final List<String> PUBLIC_PATHS = Arrays.asList(
        "/login", "/logout", "/register", "/static", "/css", "/js", "/images",
        "/error", "/403.jsp", "/404.jsp", "/500.jsp", "/hotels", "/hotel",
        "/", "/index.jsp", "/book"
    );

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);
        
        String path = httpRequest.getRequestURI().substring(httpRequest.getContextPath().length());
        
        // Allow public paths
        if (isPublicPath(path)) {
            chain.doFilter(request, response);
            return;
        }
        
        // Check if user is authenticated for protected paths (admin and agent)
        if (path.startsWith("/admin") || path.startsWith("/agent")) {
            if (session == null || session.getAttribute("user") == null) {
                LoggingUtil.logSecurity("anonymous", "UNAUTHORIZED", "Attempted to access protected path: " + path);
                httpResponse.sendRedirect(httpRequest.getContextPath() + "/login");
                return;
            }
            
            // Check role-based access
            Account user = (Account) session.getAttribute("user");
            if (!hasAccess(user, path)) {
                LoggingUtil.logSecurity(user.getUsername(), "UNAUTHORIZED", "Insufficient privileges for path: " + path);
                httpResponse.sendRedirect(httpRequest.getContextPath() + "/403.jsp");
                return;
            }
        }
        
        // Add security headers
        addSecurityHeaders(httpResponse);
        
        chain.doFilter(request, response);
    }

    private boolean isPublicPath(String path) {
        return PUBLIC_PATHS.stream().anyMatch(path::startsWith) || 
               path.matches("^/hotel/\\d+$") || // Allow paths like /hotel/1, /hotel/2, etc.
               path.equals("/");
    }

    private boolean hasAccess(Account user, String path) {
        if (path.startsWith("/admin") && !"admin".equals(user.getRole())) {
            return false;
        }
        if (path.startsWith("/agent") && !("agent".equals(user.getRole()) || "admin".equals(user.getRole()))) {
            return false;
        }
        return true;
    }

    private void addSecurityHeaders(HttpServletResponse response) {
        response.setHeader("X-Content-Type-Options", "nosniff");
        response.setHeader("X-Frame-Options", "DENY");
        response.setHeader("X-XSS-Protection", "1; mode=block");
        response.setHeader("Strict-Transport-Security", "max-age=31536000; includeSubDomains");
        response.setHeader("Content-Security-Policy", 
            "default-src 'self'; " +
            "script-src 'self' https://cdn.jsdelivr.net 'unsafe-inline' 'unsafe-eval'; " +
            "style-src 'self' https://cdn.jsdelivr.net 'unsafe-inline'; " +
            "img-src 'self' data: https:; " +
            "font-src 'self' https://cdn.jsdelivr.net; " +
            "connect-src 'self'");
    }
}
