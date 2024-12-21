<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Manage Agents</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <%@ include file="../../common/navbar.jsp" %>
    
    <div class="container mt-4">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <h2>Manage Agents</h2>
            <a href="${pageContext.request.contextPath}/admin/agents/create" class="btn btn-primary">Add New Agent</a>
        </div>

        <c:if test="${not empty error}">
            <div class="alert alert-danger">${error}</div>
        </c:if>

        <div class="table-responsive">
            <table class="table table-striped">
                <thead>
                    <tr>
                        <th>Username</th>
                        <th>Name</th>
                        <th>Email</th>
                        <th>Phone</th>
                        <th>Status</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="agent" items="${agents}">
                        <tr>
                            <td>${agent.username}</td>
                            <td>${agent.firstName} ${agent.lastName}</td>
                            <td>${agent.email}</td>
                            <td>${agent.phone}</td>
                            <td>
                                <span class="badge ${agent.active ? 'bg-success' : 'bg-danger'}">
                                    ${agent.active ? 'Active' : 'Inactive'}
                                </span>
                            </td>
                            <td>
                                <a href="${pageContext.request.contextPath}/admin/agents/edit/${agent.id}" class="btn btn-sm btn-warning">Edit</a>
                                <form action="${pageContext.request.contextPath}/admin/agents/delete/${agent.id}" method="post" style="display: inline;">
                                    <input type="hidden" name="csrf_token" value="${sessionScope.csrf_token}">
                                    <button type="submit" class="btn btn-sm btn-danger" onclick="return confirm('Are you sure you want to delete this agent?')">Delete</button>
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
