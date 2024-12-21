package com.hotel.util;

import com.hotel.model.Account;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class SecurityUtil {
    private static final String USER_SESSION_KEY = "user";

    /**
     * Check if a user is logged in
     */
    public static boolean isLoggedIn(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null && session.getAttribute(USER_SESSION_KEY) != null;
    }

    /**
     * Check if the logged-in user is an agent
     */
    public static boolean isAgentLoggedIn(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute(USER_SESSION_KEY) == null) {
            return false;
        }

        Account account = (Account) session.getAttribute(USER_SESSION_KEY);
        return "AGENT".equals(account.getRole());
    }

    /**
     * Get the currently logged-in user
     */
    public static Account getLoggedInUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute(USER_SESSION_KEY) == null) {
            return null;
        }
        return (Account) session.getAttribute(USER_SESSION_KEY);
    }

    /**
     * Set the logged-in user in the session
     */
    public static void setLoggedInUser(HttpServletRequest request, Account account) {
        HttpSession session = request.getSession();
        session.setAttribute(USER_SESSION_KEY, account);
    }

    /**
     * Clear the logged-in user from the session
     */
    public static void clearLoggedInUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute(USER_SESSION_KEY);
            session.invalidate();
        }
    }

    /**
     * Get the ID of the logged-in agent
     */
    public static int getLoggedInAgentId(HttpServletRequest request) {
        Account account = getLoggedInUser(request);
        if (account != null && "AGENT".equals(account.getRole())) {
            return account.getId();
        }
        return -1;
    }

    /**
     * Check if the logged-in agent owns the given hotel
     */
    public static boolean isAgentHotelOwner(HttpServletRequest request, int hotelId) {
        // This method should be implemented to check if the hotel belongs to the logged-in agent
        // You'll need to use HotelDAO to check the ownership
        return true; // Placeholder implementation
    }
}
