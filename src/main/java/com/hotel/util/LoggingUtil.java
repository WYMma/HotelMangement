package com.hotel.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingUtil {
    private static final Logger securityLogger = LoggerFactory.getLogger("security");
    private static final Logger auditLogger = LoggerFactory.getLogger("audit");
    private static final Logger errorLogger = LoggerFactory.getLogger("error");

    public static void logSecurity(String username, String action, String details) {
        securityLogger.info("Security: User='{}' Action='{}' Details='{}'", username, action, details);
    }

    public static void logAudit(String username, String action, String details) {
        auditLogger.info("Audit: User='{}' Action='{}' Details='{}'", username, action, details);
    }

    public static void logError(String component, String error, Throwable throwable) {
        errorLogger.error("Component='{}' Error='{}' Exception='{}'", component, error, throwable.getMessage(), throwable);
    }

    public static void logWarning(String message) {
        errorLogger.warn(message);
    }

    public static void logInfo(String message) {
        auditLogger.info(message);
    }
}
