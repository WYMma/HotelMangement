<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Edit Room Type</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet">
</head>
<body>
    <jsp:include page="../../includes/agent_header.jsp" />
    
    <div class="container mt-5">
        <div class="row justify-content-center">
            <div class="col-md-8">
                <div class="card">
                    <div class="card-header">
                        <h2 class="text-center">Edit Room Type</h2>
                    </div>
                    <div class="card-body">
                        <c:if test="${not empty error}">
                            <div class="alert alert-danger" role="alert">
                                ${error}
                            </div>
                        </c:if>
                        
                        <form action="${pageContext.request.contextPath}/agent/hotel/room-type/edit" method="post">
                            <input type="hidden" name="id" value="${roomType.id}">
                            <input type="hidden" name="hotelId" value="${roomType.hotelId}">
                            
                            <div class="mb-3">
                                <label for="name" class="form-label">Room Type Name</label>
                                <input type="text" class="form-control" id="name" name="name" value="${roomType.name}" required>
                            </div>
                            
                            <div class="mb-3">
                                <label for="description" class="form-label">Description</label>
                                <textarea class="form-control" id="description" name="description" rows="3" required>${roomType.description}</textarea>
                            </div>
                            
                            <div class="mb-3">
                                <label for="capacity" class="form-label">Capacity (persons)</label>
                                <input type="number" class="form-control" id="capacity" name="capacity" min="1" value="${roomType.capacity}" required>
                            </div>
                            
                            <div class="mb-3">
                                <label for="pricePerNight" class="form-label">Price per Night</label>
                                <div class="input-group">
                                    <span class="input-group-text">$</span>
                                    <input type="number" class="form-control" id="pricePerNight" name="pricePerNight" min="0" step="0.01" value="${roomType.pricePerNight}" required>
                                </div>
                            </div>
                            
                            <div class="mb-3">
                                <label for="availableRooms" class="form-label">Number of Available Rooms</label>
                                <input type="number" class="form-control" id="availableRooms" name="availableRooms" min="0" value="${roomType.availableRooms}" required>
                            </div>
                            
                            <div class="d-grid gap-2">
                                <button type="submit" class="btn btn-primary">Update Room Type</button>
                                <a href="${pageContext.request.contextPath}/agent/hotel/edit?id=${roomType.hotelId}" class="btn btn-secondary">Cancel</a>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
