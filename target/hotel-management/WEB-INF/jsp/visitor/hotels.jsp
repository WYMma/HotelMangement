<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Hotels - Hotel Management System</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.7.2/font/bootstrap-icons.css">
    <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet">
    <style>
        .hotel-image {
            width: 100%;
            height: 200px;
            object-fit: cover;
            border-top-left-radius: calc(0.375rem - 1px);
            border-top-right-radius: calc(0.375rem - 1px);
        }
        .card {
            transition: transform 0.2s;
        }
        .card:hover {
            transform: translateY(-5px);
            box-shadow: 0 4px 15px rgba(0,0,0,0.1);
        }
        .stars {
            color: #ffc107;
        }
        .location {
            color: #6c757d;
            font-size: 0.9rem;
        }
        .card-text {
            overflow: hidden;
            text-overflow: ellipsis;
            display: -webkit-box;
            -webkit-line-clamp: 3;
            -webkit-box-orient: vertical;
        }
        .toast-container {
            position: fixed;
            top: 20px;
            right: 20px;
            z-index: 1050;
        }
        .toast {
            background-color: #fff;
            border-radius: 8px;
            box-shadow: 0 4px 12px rgba(0,0,0,0.15);
            opacity: 0;
            transition: opacity 0.3s ease-in-out;
        }
        .toast.show {
            opacity: 1;
        }
        .toast-success {
            border-left: 4px solid #28a745;
        }
        .toast-error {
            border-left: 4px solid #dc3545;
        }
    </style>
</head>
<body>
    <!-- Toast Container -->
    <div class="toast-container">
        <c:if test="${not empty sessionScope.message}">
            <div class="toast toast-success show" role="alert" aria-live="assertive" aria-atomic="true">
                <div class="toast-header">
                    <i class="bi bi-check-circle-fill text-success me-2"></i>
                    <strong class="me-auto">Success</strong>
                    <button type="button" class="btn-close" data-bs-dismiss="toast" aria-label="Close"></button>
                </div>
                <div class="toast-body">
                    ${sessionScope.message}
                </div>
            </div>
            <% session.removeAttribute("message"); %>
        </c:if>
        <c:if test="${not empty sessionScope.error}">
            <div class="toast toast-error show" role="alert" aria-live="assertive" aria-atomic="true">
                <div class="toast-header">
                    <i class="bi bi-exclamation-circle-fill text-danger me-2"></i>
                    <strong class="me-auto">Error</strong>
                    <button type="button" class="btn-close" data-bs-dismiss="toast" aria-label="Close"></button>
                </div>
                <div class="toast-body">
                    ${sessionScope.error}
                </div>
            </div>
            <% session.removeAttribute("error"); %>
        </c:if>
    </div>

    <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
        <div class="container">
            <a class="navbar-brand" href="${pageContext.request.contextPath}/">Manita Reservation</a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav me-auto">
                    <li class="nav-item">
                        <a class="nav-link active" href="${pageContext.request.contextPath}/hotels">Hotels</a>
                    </li>
                </ul>
                <ul class="navbar-nav">
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/login">Login</a>
                    </li>
                </ul>
            </div>
        </div>
    </nav>

    <div class="container mt-4">
        <h1 class="mb-4">Our Hotels</h1>
        
        <form action="${pageContext.request.contextPath}/hotels" method="get" class="mb-4">
            <div class="input-group">
                <input type="text" name="search" class="form-control" placeholder="Search hotels..." value="${param.search}">
                <button class="btn btn-outline-primary" type="submit">Search</button>
            </div>
        </form>

        <div class="row row-cols-1 row-cols-md-2 row-cols-lg-3 g-4">
            <c:forEach var="hotel" items="${hotels}">
                <div class="col">
                    <div class="card h-100">
                        <c:choose>
                            <c:when test="${not empty hotel.image}">
                                <img src="${hotel.image}"
                                     class="hotel-image" 
                                     alt="${hotel.name}"
                                     onerror="this.src='${pageContext.request.contextPath}/images/default-hotel.jpg'">
                            </c:when>
                            <c:otherwise>
                                <img src="${pageContext.request.contextPath}/images/default-hotel.jpg" 
                                     class="hotel-image" 
                                     alt="Default hotel image">
                            </c:otherwise>
                        </c:choose>
                        
                        <div class="card-body">
                            <h5 class="card-title">${hotel.name}</h5>
                            <div class="stars mb-2">
                                <c:forEach begin="1" end="${hotel.stars}" var="i">
                                    <i class="bi bi-star-fill"></i>
                                </c:forEach>
                                <c:forEach begin="${hotel.stars + 1}" end="5" var="i">
                                    <i class="bi bi-star"></i>
                                </c:forEach>
                            </div>
                            <p class="location">
                                <i class="bi bi-geo-alt-fill"></i> ${hotel.city}, ${hotel.address}
                            </p>
                            <p class="card-text">${hotel.description}</p>
                        </div>
                        <div class="card-footer bg-transparent border-top-0">
                            <a href="${pageContext.request.contextPath}/hotels/details/${hotel.id}" 
                               class="btn btn-primary w-100">View Details</a>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>

        <c:if test="${empty hotels}">
            <div class="alert alert-info mt-4" role="alert">
                No hotels found. Please try a different search.
            </div>
        </c:if>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        document.addEventListener('DOMContentLoaded', function() {
            // Initialize all toasts
            var toasts = document.querySelectorAll('.toast');
            toasts.forEach(function(toast) {
                new bootstrap.Toast(toast, {
                    autohide: true,
                    delay: 5000
                }).show();
            });

            // Add click event to close buttons
            document.querySelectorAll('.toast .btn-close').forEach(function(button) {
                button.addEventListener('click', function() {
                    var toast = this.closest('.toast');
                    toast.classList.remove('show');
                    setTimeout(function() {
                        toast.remove();
                    }, 300);
                });
            });
        });
    </script>
</body>
</html>
