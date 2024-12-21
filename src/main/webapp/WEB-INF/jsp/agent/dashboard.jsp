<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Agent Dashboard - Hotel Management System</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.7.2/font/bootstrap-icons.css" rel="stylesheet">
</head>
<body>
    <%@ include file="../common/navbar.jsp" %>
    
    <div class="container mt-4">
        <!-- Stats Cards -->
        <div class="row mb-4">
            <div class="col-md-6">
                <div class="card bg-primary text-white">
                    <div class="card-body">
                        <div class="d-flex justify-content-between align-items-center">
                            <div>
                                <h6 class="card-title mb-0">Total Hotels</h6>
                                <h2 class="display-4 mb-0">${totalHotels}</h2>
                            </div>
                            <i class="bi bi-building fs-1"></i>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-md-6">
                <div class="card bg-success text-white">
                    <div class="card-body">
                        <div class="d-flex justify-content-between align-items-center">
                            <div>
                                <h6 class="card-title mb-0">Active Bookings</h6>
                                <h2 class="display-4 mb-0">${activeBookings}</h2>
                            </div>
                            <i class="bi bi-calendar-check fs-1"></i>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Hotels Section -->
        <div class="card mb-4">
            <div class="card-header d-flex justify-content-between align-items-center">
                <h5 class="mb-0">My Hotels</h5>
                <a href="${pageContext.request.contextPath}/agent/hotels/create" class="btn btn-primary btn-sm">
                    <i class="bi bi-plus-lg"></i> Add New Hotel
                </a>
            </div>
            <div class="card-body">
                <c:if test="${not empty hotelError}">
                    <div class="alert alert-danger" role="alert">${hotelError}</div>
                </c:if>
                <div class="table-responsive">
                    <table class="table table-hover">
                        <thead>
                            <tr>
                                <th>Name</th>
                                <th>City</th>
                                <th>Stars</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="hotel" items="${hotels}">
                                <tr>
                                    <td>${hotel.name}</td>
                                    <td>${hotel.city}</td>
                                    <td>
                                        <c:forEach begin="1" end="${hotel.stars}">
                                            <i class="bi bi-star-fill text-warning"></i>
                                        </c:forEach>
                                    </td>
                                    <td>
                                        <div class="btn-group" role="group">
                                            <a href="${pageContext.request.contextPath}/agent/hotels/edit?id=${hotel.id}" 
                                               class="btn btn-sm btn-outline-primary">
                                                <i class="bi bi-pencil"></i>
                                            </a>
                                            <a href="${pageContext.request.contextPath}/agent/hotels/rooms?id=${hotel.id}" 
                                               class="btn btn-sm btn-outline-success">
                                                <i class="bi bi-door-open"></i>
                                            </a>
                                        </div>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
                
                <!-- Hotels Pagination -->
                <c:if test="${totalHotelPages > 1}">
                    <nav aria-label="Hotel pagination" class="mt-3">
                        <ul class="pagination justify-content-center">
                            <li class="page-item ${currentHotelPage == 0 ? 'disabled' : ''}">
                                <a class="page-link" href="?hotelPage=${currentHotelPage - 1}&reservationPage=${currentReservationPage}">Previous</a>
                            </li>
                            <c:forEach begin="0" end="${totalHotelPages - 1}" var="i">
                                <li class="page-item ${currentHotelPage == i ? 'active' : ''}">
                                    <a class="page-link" href="?hotelPage=${i}&reservationPage=${currentReservationPage}">${i + 1}</a>
                                </li>
                            </c:forEach>
                            <li class="page-item ${currentHotelPage + 1 >= totalHotelPages ? 'disabled' : ''}">
                                <a class="page-link" href="?hotelPage=${currentHotelPage + 1}&reservationPage=${currentReservationPage}">Next</a>
                            </li>
                        </ul>
                    </nav>
                </c:if>
            </div>
        </div>

        <!-- Reservations Section -->
        <div class="card">
            <div class="card-header">
                <h5 class="mb-0">Recent Reservations</h5>
            </div>
            <div class="card-body">
                <c:if test="${not empty reservationError}">
                    <div class="alert alert-danger" role="alert">${reservationError}</div>
                </c:if>
                <div class="table-responsive">
                    <table class="table table-hover">
                        <thead>
                            <tr>
                                <th>Hotel</th>
                                <th>Guest</th>
                                <th>Check-in</th>
                                <th>Check-out</th>
                                <th>Status</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="reservation" items="${reservations}">
                                <tr>
                                    <td>${reservation.hotelName}</td>
                                    <td>${reservation.guestName}</td>
                                    <td><fmt:formatDate value="${reservation.checkInDate}" pattern="MMM dd, yyyy"/></td>
                                    <td><fmt:formatDate value="${reservation.checkOutDate}" pattern="MMM dd, yyyy"/></td>
                                    <td>
                                        <span class="badge bg-${reservation.status == 'CONFIRMED' ? 'success' : 
                                            reservation.status == 'PENDING' ? 'warning' : 
                                            reservation.status == 'CANCELLED' ? 'danger' : 'secondary'}">
                                            ${reservation.status}
                                        </span>
                                    </td>
                                    <td>
                                        <div class="btn-group" role="group">
                                            <a href="${pageContext.request.contextPath}/agent/reservations/details?id=${reservation.id}" 
                                               class="btn btn-sm btn-outline-primary">
                                                <i class="bi bi-eye"></i>
                                            </a>
                                            <c:if test="${reservation.status == 'PENDING'}">
                                                <a href="${pageContext.request.contextPath}/agent/reservations/confirm?id=${reservation.id}" 
                                                   class="btn btn-sm btn-outline-success">
                                                    <i class="bi bi-check-lg"></i>
                                                </a>
                                                <a href="${pageContext.request.contextPath}/agent/reservations/cancel?id=${reservation.id}" 
                                                   class="btn btn-sm btn-outline-danger">
                                                    <i class="bi bi-x-lg"></i>
                                                </a>
                                            </c:if>
                                        </div>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
                
                <!-- Reservations Pagination -->
                <c:if test="${totalReservationPages > 1}">
                    <nav aria-label="Reservation pagination" class="mt-3">
                        <ul class="pagination justify-content-center">
                            <li class="page-item ${currentReservationPage == 0 ? 'disabled' : ''}">
                                <a class="page-link" href="?hotelPage=${currentHotelPage}&reservationPage=${currentReservationPage - 1}">Previous</a>
                            </li>
                            <c:forEach begin="0" end="${totalReservationPages - 1}" var="i">
                                <li class="page-item ${currentReservationPage == i ? 'active' : ''}">
                                    <a class="page-link" href="?hotelPage=${currentHotelPage}&reservationPage=${i}">${i + 1}</a>
                                </li>
                            </c:forEach>
                            <li class="page-item ${currentReservationPage + 1 >= totalReservationPages ? 'disabled' : ''}">
                                <a class="page-link" href="?hotelPage=${currentHotelPage}&reservationPage=${currentReservationPage + 1}">Next</a>
                            </li>
                        </ul>
                    </nav>
                </c:if>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
