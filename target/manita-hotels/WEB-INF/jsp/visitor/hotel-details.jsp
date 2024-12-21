<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${hotel.name} - Hotel Management System</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.7.2/font/bootstrap-icons.css" rel="stylesheet">
    <style>
        .notification {
            position: fixed;
            top: 20px;
            right: 20px;
            z-index: 1050;
            animation: slideIn 0.5s ease-out;
        }
        @keyframes slideIn {
            from {
                transform: translateX(100%);
                opacity: 0;
            }
            to {
                transform: translateX(0);
                opacity: 1;
            }
        }
        .room-type-card {
            height: 100%;
            transition: transform 0.2s;
        }
        .room-type-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 4px 15px rgba(0,0,0,0.1);
        }
    </style>
</head>
<body>
    <!-- Notifications -->
    <c:if test="${not empty sessionScope.message}">
        <div class="notification alert alert-success alert-dismissible fade show" role="alert">
            ${sessionScope.message}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
        <% session.removeAttribute("message"); %>
    </c:if>
    <c:if test="${not empty sessionScope.error}">
        <div class="notification alert alert-danger alert-dismissible fade show" role="alert">
            ${sessionScope.error}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
        <% session.removeAttribute("error"); %>
    </c:if>

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
                <div class="navbar-nav ms-auto">
                    <a class="nav-link" href="${pageContext.request.contextPath}/login">Login</a>
                </div>
            </div>
        </div>
    </nav>

    <div class="container mt-4">
        <nav aria-label="breadcrumb">
            <ol class="breadcrumb">
                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/hotels">Hotels</a></li>
                <li class="breadcrumb-item active" aria-current="page">${hotel.name}</li>
            </ol>
        </nav>

        <div class="row">
            <div class="col-md-8">
                <h1 class="mb-3">${hotel.name}</h1>
                <div class="mb-3">
                    <c:forEach begin="1" end="5" var="i">
                        <i class="bi ${i <= hotel.stars ? 'bi-star-fill' : 'bi-star'} text-warning"></i>
                    </c:forEach>
                </div>
                <p class="lead">
                    <i class="bi bi-geo-alt"></i> ${hotel.city}, ${hotel.address}
                </p>
                <p class="mb-4">${hotel.description}</p>

                <c:if test="${not empty hotel.image}">
                    <div class="mb-4">
                        <img src="${hotel.image}"
                             class="img-fluid rounded" 
                             alt="${hotel.name}"
                             onerror="this.src='${pageContext.request.contextPath}/images/default-hotel.jpg'">
                    </div>
                </c:if>

                <!-- Room Types Section -->
                <h2 class="mb-4">Available Room Types</h2>
                <div class="row row-cols-1 row-cols-md-2 g-4 mb-4">
                    <c:forEach var="roomType" items="${roomTypes}">
                        <div class="col">
                            <div class="card room-type-card" data-room-type-id="${roomType.id}" data-room-type-name="${roomType.name}" data-price-per-night="${roomType.pricePerNight}" data-capacity="${roomType.capacity}">
                                <div class="card-body">
                                    <h5 class="card-title">${roomType.name}</h5>
                                    <p class="card-text">${roomType.description}</p>
                                    <ul class="list-unstyled">
                                        <li><i class="bi bi-people"></i> Capacity: ${roomType.capacity} persons</li>
                                        <li><i class="bi bi-door-open"></i> Available Rooms: ${roomType.availableRooms}</li>
                                        <li><i class="bi bi-currency-dollar"></i> Price per Night: 
                                            <span class="text-primary fw-bold">
                                                <fmt:formatNumber value="${roomType.pricePerNight}" type="currency" currencySymbol="$"/>
                                            </span>
                                        </li>
                                    </ul>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                    
                    <c:if test="${empty roomTypes}">
                        <div class="col-12">
                            <div class="alert alert-info" role="alert">
                                <i class="bi bi-info-circle"></i> No room types available for this hotel at the moment.
                            </div>
                        </div>
                    </c:if>
                </div>
            </div>

            <div class="col-md-4">
                <div class="card sticky-top" style="top: 20px;">
                    <div class="card-body">
                        <h5 class="card-title">Book Now</h5>
                        <form action="${pageContext.request.contextPath}/booking" method="post" id="bookingForm">
                            <input type="hidden" name="hotelId" value="${hotel.id}">
                            <input type="hidden" name="roomTypeId" id="roomTypeId">
                            
                            <div class="mb-3">
                                <label for="selectedRoomType" class="form-label">Selected Room Type</label>
                                <input type="text" class="form-control" id="selectedRoomType" readonly>
                            </div>
                            
                            <div class="mb-3">
                                <label for="pricePerNight" class="form-label">Price per Night</label>
                                <div class="input-group">
                                    <span class="input-group-text">$</span>
                                    <input type="text" class="form-control" id="pricePerNight" readonly>
                                </div>
                            </div>

                            <div class="mb-3">
                                <label for="checkIn" class="form-label">Check-in Date</label>
                                <input type="date" class="form-control" id="checkIn" name="checkIn" required>
                            </div>

                            <div class="mb-3">
                                <label for="checkOut" class="form-label">Check-out Date</label>
                                <input type="date" class="form-control" id="checkOut" name="checkOut" required>
                            </div>

                            <div class="mb-3">
                                <label for="totalPrice" class="form-label">Total Price</label>
                                <div class="input-group">
                                    <span class="input-group-text">$</span>
                                    <input type="text" class="form-control" id="totalPrice" readonly>
                                </div>
                            </div>

                            <div class="mb-3">
                                <label for="guestName" class="form-label">Guest Name</label>
                                <input type="text" class="form-control" id="guestName" name="guestName" required>
                            </div>

                            <div class="mb-3">
                                <label for="guestEmail" class="form-label">Guest Email</label>
                                <input type="email" class="form-control" id="guestEmail" name="guestEmail" required>
                            </div>

                            <div class="mb-3">
                                <label for="guestPhone" class="form-label">Guest Phone</label>
                                <input type="text" class="form-control" id="guestPhone" name="guestPhone" required>
                            </div>

                            <div class="mb-3">
                                <label for="numberOfGuests" class="form-label">Number of Guests</label>
                                <input type="number" class="form-control" id="numberOfGuests" name="numberOfGuests" required>
                            </div>

                            <button type="submit" class="btn btn-primary w-100" disabled id="bookNowBtn">
                                Book Now
                            </button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        document.addEventListener('DOMContentLoaded', function() {
            const roomTypeCards = document.querySelectorAll('.room-type-card');
            const roomTypeIdInput = document.getElementById('roomTypeId');
            const selectedRoomTypeInput = document.getElementById('selectedRoomType');
            const pricePerNightInput = document.getElementById('pricePerNight');
            const checkInInput = document.getElementById('checkIn');
            const checkOutInput = document.getElementById('checkOut');
            const totalPriceInput = document.getElementById('totalPrice');
            const bookNowBtn = document.getElementById('bookNowBtn');
            const numberOfGuestsInput = document.getElementById('numberOfGuests');

            // Set minimum dates for check-in and check-out
            const today = new Date().toISOString().split('T')[0];
            checkInInput.min = today;
            checkOutInput.min = today;

            // Handle room type selection
            roomTypeCards.forEach(card => {
                card.addEventListener('click', function() {
                    // Remove active class from all cards
                    roomTypeCards.forEach(c => c.classList.remove('active'));
                    // Add active class to selected card
                    this.classList.add('active');

                    // Update form fields
                    const roomTypeId = this.dataset.roomTypeId;
                    const roomTypeName = this.dataset.roomTypeName;
                    const pricePerNight = this.dataset.pricePerNight;
                    const capacity = parseInt(this.dataset.capacity);

                    roomTypeIdInput.value = roomTypeId;
                    selectedRoomTypeInput.value = roomTypeName;
                    pricePerNightInput.value = pricePerNight;
                    numberOfGuestsInput.max = capacity;

                    // Enable book now button
                    bookNowBtn.disabled = false;

                    // Update total price if dates are selected
                    updateTotalPrice();
                });
            });

            // Update total price when dates change
            checkInInput.addEventListener('change', updateTotalPrice);
            checkOutInput.addEventListener('change', updateTotalPrice);

            function updateTotalPrice() {
                if (checkInInput.value && checkOutInput.value && pricePerNightInput.value) {
                    const checkIn = new Date(checkInInput.value);
                    const checkOut = new Date(checkOutInput.value);
                    const nights = Math.ceil((checkOut - checkIn) / (1000 * 60 * 60 * 24));

                    if (nights > 0) {
                        const pricePerNight = parseFloat(pricePerNightInput.value);
                        const total = (nights * pricePerNight).toFixed(2);
                        totalPriceInput.value = total;
                        bookNowBtn.disabled = false;
                    } else {
                        totalPriceInput.value = '';
                        bookNowBtn.disabled = true;
                    }
                }
            }

            // Form validation
            const bookingForm = document.getElementById('bookingForm');
            bookingForm.addEventListener('submit', function(e) {
                if (!validateForm()) {
                    e.preventDefault();
                }
            });

            function validateForm() {
                // Check if room type is selected
                if (!roomTypeIdInput.value) {
                    alert('Please select a room type');
                    return false;
                }

                // Check dates
                const checkIn = new Date(checkInInput.value);
                const checkOut = new Date(checkOutInput.value);
                if (checkIn >= checkOut) {
                    alert('Check-out date must be after check-in date');
                    return false;
                }

                // Validate guest details
                const guestName = document.getElementById('guestName').value.trim();
                const guestEmail = document.getElementById('guestEmail').value.trim();
                const guestPhone = document.getElementById('guestPhone').value.trim();
                const numberOfGuests = parseInt(numberOfGuestsInput.value);

                if (!guestName || !guestEmail || !guestPhone) {
                    alert('Please fill in all guest details');
                    return false;
                }

                if (numberOfGuests < 1) {
                    alert('Number of guests must be at least 1');
                    return false;
                }

                return true;
            }
        });
    </script>
</body>
</html>
