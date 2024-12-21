<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Search Results - Hotel Management System</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.7.2/font/bootstrap-icons.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet">
</head>
<body>
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
        <div class="container">
            <a class="navbar-brand" href="${pageContext.request.contextPath}/">Manita Reservation</a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav me-auto">
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/hotels">Hotels</a>
                    </li>
                </ul>
                <form class="d-flex" action="${pageContext.request.contextPath}/search" method="get">
                    <input class="form-control me-2" type="search" placeholder="Search hotels..." name="q" value="${query}">
                    <button class="btn btn-outline-light" type="submit">Search</button>
                </form>
            </div>
        </div>
    </nav>

    <div class="container mt-4">
        <div class="row">
            <!-- Search Filters -->
            <div class="col-md-3">
                <div class="card">
                    <div class="card-body">
                        <h5 class="card-title">Filters</h5>
                        <form action="${pageContext.request.contextPath}/search" method="get">
                            <input type="hidden" name="q" value="${query}">
                            
                            <div class="mb-3">
                                <label for="location" class="form-label">Location</label>
                                <input type="text" class="form-control" id="location" name="location" value="${location}">
                            </div>
                            
                            <div class="mb-3">
                                <label for="rating" class="form-label">Minimum Rating</label>
                                <select class="form-select" id="rating" name="rating">
                                    <option value="">Any Rating</option>
                                    <c:forEach begin="1" end="5" var="i">
                                        <option value="${i}" ${rating eq i ? 'selected' : ''}>
                                            ${i} Star${i > 1 ? 's' : ''} & Up
                                        </option>
                                    </c:forEach>
                                </select>
                            </div>
                            
                            <button type="submit" class="btn btn-primary w-100">Apply Filters</button>
                        </form>
                    </div>
                </div>
            </div>

            <!-- Search Results -->
            <div class="col-md-9">
                <h2 class="mb-4">Search Results</h2>
                <p class="text-muted">
                    Found ${hotels.size()} hotel${hotels.size() != 1 ? 's' : ''} 
                    ${query != null ? 'for "'.concat(query).concat('"') : ''}
                </p>

                <div class="row row-cols-1 row-cols-md-2 g-4">
                    <c:forEach var="hotel" items="${hotels}">
                        <div class="col">
                            <div class="card h-100">
                                <div class="card-body">
                                    <h5 class="card-title">${hotel.name}</h5>
                                    <div class="mb-2">
                                        <c:forEach begin="1" end="5" var="i">
                                            <i class="bi ${i <= hotel.rating ? 'bi-star-fill' : 'bi-star'} text-warning"></i>
                                        </c:forEach>
                                    </div>
                                    <p class="card-text">${hotel.description}</p>
                                    <p class="card-text">
                                        <small class="text-muted">
                                            <i class="bi bi-geo-alt"></i> ${hotel.address}<br>
                                            <i class="bi bi-door-open"></i> ${hotel.roomCount} rooms available
                                        </small>
                                    </p>
                                    <a href="${pageContext.request.contextPath}/hotel/${hotel.id}" class="btn btn-primary">View Details</a>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>

                <c:if test="${empty hotels}">
                    <div class="alert alert-info mt-4">
                        <i class="bi bi-info-circle"></i> No hotels found matching your criteria. Try adjusting your search filters.
                    </div>
                </c:if>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
