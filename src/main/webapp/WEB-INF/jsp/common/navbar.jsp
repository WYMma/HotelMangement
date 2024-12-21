<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <div class="container">
        <a class="navbar-brand" href="${pageContext.request.contextPath}/">Manita Reservation</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav me-auto">
                <c:choose>
                    <%-- Admin Navigation --%>
                    <c:when test="${sessionScope.user.role.toLowerCase() == 'admin'}">
                        <li class="nav-item">
                            <a class="nav-link ${pageContext.request.servletPath == '/WEB-INF/jsp/admin/dashboard.jsp' ? 'active' : ''}" 
                               href="${pageContext.request.contextPath}/admin/dashboard">Dashboard</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link ${pageContext.request.servletPath == '/WEB-INF/jsp/admin/agent/list.jsp' ? 'active' : ''}" 
                               href="${pageContext.request.contextPath}/admin/agents">Manage Agents</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link ${pageContext.request.servletPath == '/WEB-INF/jsp/admin/hotel/list.jsp' ? 'active' : ''}" 
                               href="${pageContext.request.contextPath}/admin/hotels">Manage Hotels</a>
                        </li>
                    </c:when>
                    
                    <%-- Agent Navigation --%>
                    <c:when test="${sessionScope.user.role.toLowerCase() == 'agent'}">
                        <li class="nav-item">
                            <a class="nav-link ${pageContext.request.servletPath == '/WEB-INF/jsp/agent/dashboard.jsp' ? 'active' : ''}" 
                               href="${pageContext.request.contextPath}/agent/dashboard">Dashboard</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link ${pageContext.request.servletPath == '/WEB-INF/jsp/agent/hotel/list.jsp' ? 'active' : ''}" 
                               href="${pageContext.request.contextPath}/agent/hotels">My Hotels</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link ${pageContext.request.servletPath == '/WEB-INF/jsp/agent/reservation/list.jsp' ? 'active' : ''}" 
                               href="${pageContext.request.contextPath}/agent/reservations">Reservations</a>
                        </li>
                    </c:when>
                    
                    <%-- Default Navigation --%>
                    <c:otherwise>
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/hotels">Browse Hotels</a>
                        </li>
                    </c:otherwise>
                </c:choose>
            </ul>
            
            <%-- User Menu --%>
            <ul class="navbar-nav">
                <c:choose>
                    <c:when test="${not empty sessionScope.user}">
                        <li class="nav-item dropdown">
                            <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" 
                               data-bs-toggle="dropdown" aria-expanded="false">
                                ${sessionScope.user.firstName} ${sessionScope.user.lastName}
                            </a>
                            <ul class="dropdown-menu dropdown-menu-end" aria-labelledby="navbarDropdown">
                                <li><a class="dropdown-item" href="${pageContext.request.contextPath}/profile">Profile</a></li>
                                <li><hr class="dropdown-divider"></li>
                                <li>
                                    <form action="${pageContext.request.contextPath}/logout" method="post" style="display: inline;">
                                        <input type="hidden" name="csrf_token" value="${sessionScope.csrf_token}">
                                        <button type="submit" class="dropdown-item">Logout</button>
                                    </form>
                                </li>
                            </ul>
                        </li>
                    </c:when>
                    <c:otherwise>
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/login">Login</a>
                        </li>
                    </c:otherwise>
                </c:choose>
            </ul>
        </div>
    </div>
</nav>
