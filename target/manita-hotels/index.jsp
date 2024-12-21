<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Welcome - Hotel Management System</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .hero {
            background: linear-gradient(rgba(0, 0, 0, 0.5), rgba(0, 0, 0, 0.5)), url('https://digital.ihg.com/is/image/ihg/ckvcv-garner-clarksville-northeast-exterior-sunset');
            background-size: cover;
            background-position: center;
            height: 60vh;
            display: flex;
            align-items: center;
            justify-content: center;
            color: white;
            text-align: center;
        }
    </style>
</head>
<body>
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
        <div class="container">
            <a class="navbar-brand" href="${pageContext.request.contextPath}/">Manita Reservation</a>
            <div class="navbar-nav ms-auto">
                <a class="nav-link" href="${pageContext.request.contextPath}/login">Login</a>
            </div>
        </div>
    </nav>

    <div class="hero">
        <div class="container">
            <h1 class="display-4">Welcome to Manita Hotels</h1>
            <p class="lead">Discover our collection of luxurious hotels</p>
            <a href="${pageContext.request.contextPath}/hotels" class="btn btn-light btn-lg">View Hotels</a>
        </div>
    </div>

    <div class="container my-5">
        <div class="row">
            <div class="col-md-4">
                <h3>Luxury Hotels</h3>
                <p>Experience world-class hospitality in our carefully selected hotels.</p>
            </div>
            <div class="col-md-4">
                <h3>Best Locations</h3>
                <p>Find hotels in prime locations across the city.</p>
            </div>
            <div class="col-md-4">
                <h3>Great Service</h3>
                <p>Enjoy exceptional service and amenities during your stay.</p>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
