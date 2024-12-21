<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Manage Hotels</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <%@ include file="../../common/navbar.jsp" %>
    
    <div class="container mt-4">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <h2>Manage Hotels</h2>
            <a href="${pageContext.request.contextPath}/admin/hotels/create" class="btn btn-primary">Add New Hotel</a>
        </div>

        <c:if test="${not empty error}">
            <div class="alert alert-danger">${error}</div>
        </c:if>

        <div class="table-responsive">
            <table class="table table-striped">
                <thead>
                    <tr>
                        <th>Name</th>
                        <th>City</th>
                        <th>Stars</th>
                        <th>Agent</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="hotel" items="${hotels}">
                        <tr>
                            <td>${hotel.name}</td>
                            <td>${hotel.city}</td>
                            <td>${hotel.stars} Star${hotel.stars > 1 ? 's' : ''}</td>
                            <td>${hotel.agentName}</td>
                            <td>
                                <a href="${pageContext.request.contextPath}/admin/hotels/edit/${hotel.id}" class="btn btn-sm btn-warning">Edit</a>
                                <form action="${pageContext.request.contextPath}/admin/hotels/delete/${hotel.id}" method="post" style="display: inline;">
                                    <input type="hidden" name="csrf_token" value="${sessionScope.csrf_token}">
                                    <button type="submit" class="btn btn-sm btn-danger" onclick="return confirm('Are you sure you want to delete this hotel?')">Delete</button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
