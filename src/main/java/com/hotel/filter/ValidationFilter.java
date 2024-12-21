package com.hotel.filter;

import com.hotel.util.ValidationUtil;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

import java.io.IOException;
import java.util.*;

@WebFilter("/*")
public class ValidationFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        chain.doFilter(new SanitizedRequest((HttpServletRequest) request), response);
    }

    private static class SanitizedRequest extends HttpServletRequestWrapper {
        public SanitizedRequest(HttpServletRequest request) {
            super(request);
        }

        @Override
        public String getParameter(String name) {
            String value = super.getParameter(name);
            return value != null ? ValidationUtil.sanitizeInput(value) : null;
        }

        @Override
        public Map<String, String[]> getParameterMap() {
            Map<String, String[]> paramMap = super.getParameterMap();
            Map<String, String[]> sanitizedMap = new HashMap<>();

            for (Map.Entry<String, String[]> entry : paramMap.entrySet()) {
                String[] values = entry.getValue();
                String[] sanitizedValues = new String[values.length];
                for (int i = 0; i < values.length; i++) {
                    sanitizedValues[i] = ValidationUtil.sanitizeInput(values[i]);
                }
                sanitizedMap.put(entry.getKey(), sanitizedValues);
            }

            return Collections.unmodifiableMap(sanitizedMap);
        }

        @Override
        public Enumeration<String> getParameterNames() {
            return super.getParameterNames();
        }

        @Override
        public String[] getParameterValues(String name) {
            String[] values = super.getParameterValues(name);
            if (values == null) {
                return null;
            }

            String[] sanitizedValues = new String[values.length];
            for (int i = 0; i < values.length; i++) {
                sanitizedValues[i] = ValidationUtil.sanitizeInput(values[i]);
            }

            return sanitizedValues;
        }
    }
}
