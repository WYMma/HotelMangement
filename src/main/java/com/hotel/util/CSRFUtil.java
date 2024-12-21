package com.hotel.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.security.SecureRandom;
import java.util.Base64;

public class CSRFUtil {
    private static final SecureRandom secureRandom = new SecureRandom();
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder();
    
    public static String generateToken() {
        byte[] randomBytes = new byte[32];
        secureRandom.nextBytes(randomBytes);
        return base64Encoder.encodeToString(randomBytes);
    }
    
    public static void setToken(HttpSession session) {
        if (session.getAttribute("csrf_token") == null) {
            session.setAttribute("csrf_token", generateToken());
        }
    }
    
    public static String getToken(HttpSession session) {
        return (String) session.getAttribute("csrf_token");
    }
    
    public static boolean validateToken(HttpServletRequest request) {
        String sessionToken = getToken(request.getSession());
        String requestToken = request.getParameter("csrf_token");
        
        if (sessionToken == null || requestToken == null) {
            return false;
        }
        
        return sessionToken.equals(requestToken);
    }
}
