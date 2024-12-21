<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Edit Hotel</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <%@ include file="../../common/navbar.jsp" %>
    
    <div class="container mt-4">
        <h2>Edit Hotel</h2>

        <c:if test="${not empty error}">
            <div class="alert alert-danger">${error}</div>
        </c:if>

        <form action="${pageContext.request.contextPath}/admin/hotels/edit/${hotel.id}" method="post" class="needs-validation" novalidate>
            <input type="hidden" name="csrf_token" value="${sessionScope.csrf_token}">
            
            <div class="mb-3">
                <label for="name" class="form-label">Hotel Name</label>
                <input type="text" class="form-control" id="name" name="name" value="${hotel.name}" required>
            </div>

            <div class="mb-3">
                <label for="description" class="form-label">Description</label>
                <textarea class="form-control" id="description" name="description" rows="3" required>${hotel.description}</textarea>
            </div>

            <div class="mb-3">
                <label for="city" class="form-label">City</label>
                <input type="text" class="form-control" id="city" name="city" value="${hotel.city}" required>
            </div>

            <div class="mb-3">
                <label for="address" class="form-label">Address</label>
                <input type="text" class="form-control" id="address" name="address" value="${hotel.address}" required>
            </div>

            <div class="mb-3">
                <label for="stars" class="form-label">Stars</label>
                <select class="form-select" id="stars" name="stars" required>
                    <c:forEach var="i" begin="1" end="5">
                        <option value="${i}" ${hotel.stars == i ? 'selected' : ''}>${i} Star${i > 1 ? 's' : ''}</option>
                    </c:forEach>
                </select>
            </div>

            <div class="mb-3">
                <label for="image" class="form-label">Image URL</label>
                <input type="url" class="form-control" id="image" name="image" value="${hotel.image}">
            </div>

            <div class="mb-3">
                <label for="agentId" class="form-label">Agent</label>
                <select class="form-select" id="agentId" name="agentId" required>
                    <c:forEach var="agent" items="${agents}">
                        <option value="${agent.id}" ${hotel.agentId == agent.id ? 'selected' : ''}>
                            ${agent.firstName} ${agent.lastName}
                        </option>
                    </c:forEach>
                </select>
            </div>

            <button type="submit" class="btn btn-primary">Update Hotel</button>
            <a href="${pageContext.request.contextPath}/admin/hotels" class="btn btn-secondary">Cancel</a>
        </form>
    </div>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
