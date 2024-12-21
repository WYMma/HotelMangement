package com.hotel.filter;

import com.hotel.util.CSRFUtil;
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
public class CSRFFilter implements Filter {
    private static final List<String> PROTECTED_METHODS = Arrays.asList("POST", "PUT", "DELETE", "PATCH");
    private static final List<String> EXCLUDED_PATHS = Arrays.asList("/static", "/css", "/js", "/images", "/book");

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);

        String path = httpRequest.getRequestURI().substring(httpRequest.getContextPath().length());
        
        // Skip CSRF check for excluded paths
        if (isExcludedPath(path)) {
            chain.doFilter(request, response);
            return;
        }

        // Generate CSRF token for all requests
        if (session != null) {
            CSRFUtil.setToken(session);
        }

        // Verify CSRF token for protected methods
        if (PROTECTED_METHODS.contains(httpRequest.getMethod())) {
            if (!CSRFUtil.validateToken(httpRequest)) {
                LoggingUtil.logSecurity("unknown", "CSRF_VIOLATION", 
                    "Invalid CSRF token for " + httpRequest.getMethod() + " request to " + path);
                httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid CSRF Token");
                return;
            }
        }

        chain.doFilter(request, response);
    }

    private boolean isExcludedPath(String path) {
        return EXCLUDED_PATHS.stream().anyMatch(path::startsWith);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }
}
