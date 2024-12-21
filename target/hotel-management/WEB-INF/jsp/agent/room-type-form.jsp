<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>${empty roomType ? 'Add' : 'Edit'} Room Type</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.7.2/font/bootstrap-icons.css" rel="stylesheet">
    <style>
        .form-container {
            max-width: 800px;
            margin: 0 auto;
        }
        .required::after {
            content: "*";
            color: red;
            margin-left: 4px;
        }
    </style>
</head>
<body>
    <jsp:include page="../includes/agent-navbar.jsp" />

    <div class="container mt-4">
        <div class="form-container">
            <h2 class="mb-4">${empty roomType ? 'Add New' : 'Edit'} Room Type</h2>

            <form action="${pageContext.request.contextPath}/agent/room-types" 
                  method="post" 
                  class="needs-validation" 
                  novalidate>
                
                <input type="hidden" name="action" value="${empty roomType ? 'add' : 'edit'}">
                <c:if test="${not empty roomType}">
                    <input type="hidden" name="roomTypeId" value="${roomType.id}">
                </c:if>

                <div class="mb-3">
                    <label for="hotel" class="form-label required">Hotel</label>
                    <c:choose>
                        <c:when test="${empty roomType}">
                            <select class="form-select" id="hotel" name="hotelId" required>
                                <option value="">Select a hotel</option>
                                <c:forEach items="${hotels}" var="hotel">
                                    <option value="${hotel.id}">${hotel.name}</option>
                                </c:forEach>
                            </select>
                        </c:when>
                        <c:otherwise>
                            <input type="text" class="form-control" value="${hotel.name}" readonly>
                            <input type="hidden" name="hotelId" value="${hotel.id}">
                        </c:otherwise>
                    </c:choose>
                    <div class="invalid-feedback">Please select a hotel.</div>
                </div>

                <div class="mb-3">
                    <label for="name" class="form-label required">Room Type Name</label>
                    <input type="text" 
                           class="form-control" 
                           id="name" 
                           name="name" 
                           value="${roomType.name}"
                           required>
                    <div class="invalid-feedback">Please enter a room type name.</div>
                </div>

                <div class="mb-3">
                    <label for="description" class="form-label required">Description</label>
                    <textarea class="form-control" 
                              id="description" 
                              name="description" 
                              rows="3" 
                              required>${roomType.description}</textarea>
                    <div class="invalid-feedback">Please enter a description.</div>
                </div>

                <div class="row">
                    <div class="col-md-4 mb-3">
                        <label for="capacity" class="form-label required">Capacity (guests)</label>
                        <input type="number" 
                               class="form-control" 
                               id="capacity" 
                               name="capacity" 
                               value="${roomType.capacity}"
                               min="1" 
                               required>
                        <div class="invalid-feedback">Please enter a valid capacity.</div>
                    </div>

                    <div class="col-md-4 mb-3">
                        <label for="pricePerNight" class="form-label required">Price per Night ($)</label>
                        <input type="number" 
                               class="form-control" 
                               id="pricePerNight" 
                               name="pricePerNight" 
                               value="${roomType.pricePerNight}"
                               min="0.01" 
                               step="0.01" 
                               required>
                        <div class="invalid-feedback">Please enter a valid price.</div>
                    </div>

                    <div class="col-md-4 mb-3">
                        <label for="availableRooms" class="form-label required">Available Rooms</label>
                        <input type="number" 
                               class="form-control" 
                               id="availableRooms" 
                               name="availableRooms" 
                               value="${roomType.availableRooms}"
                               min="0" 
                               required>
                        <div class="invalid-feedback">Please enter the number of available rooms.</div>
                    </div>
                </div>

                <div class="d-flex justify-content-between">
                    <a href="${pageContext.request.contextPath}/agent/room-types" 
                       class="btn btn-secondary">
                        <i class="bi bi-arrow-left"></i> Back
                    </a>
                    <button type="submit" class="btn btn-primary">
                        <i class="bi bi-save"></i> ${empty roomType ? 'Add' : 'Update'} Room Type
                    </button>
                </div>
            </form>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        // Form validation
        (function() {
            'use strict';
            var forms = document.querySelectorAll('.needs-validation');
            Array.prototype.slice.call(forms).forEach(function(form) {
                form.addEventListener('submit', function(event) {
                    if (!form.checkValidity()) {
                        event.preventDefault();
                        event.stopPropagation();
                    }
                    form.classList.add('was-validated');
                }, false);
            });
        })();
    </script>
</body>
</html>
