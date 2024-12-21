package com.hotel.servlet.visitor;

import com.hotel.dao.HotelDAO;
import com.hotel.dao.ReservationDAO;
import com.hotel.dao.RoomTypeDAO;
import com.hotel.dao.impl.HotelDAOImpl;
import com.hotel.dao.impl.ReservationDAOImpl;
import com.hotel.dao.impl.RoomTypeDAOImpl;
import com.hotel.model.Hotel;
import com.hotel.model.Reservation;
import com.hotel.model.RoomType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.sql.Date;

@WebServlet("/booking")
public class BookingServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(BookingServlet.class);
    private final HotelDAO hotelDAO = new HotelDAOImpl();
    private final ReservationDAO reservationDAO = new ReservationDAOImpl();
    private final RoomTypeDAO roomTypeDAO = new RoomTypeDAOImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Get form parameters
            int hotelId = Integer.parseInt(request.getParameter("hotelId"));
            int roomTypeId = Integer.parseInt(request.getParameter("roomTypeId"));
            String guestName = request.getParameter("guestName");
            String guestEmail = request.getParameter("guestEmail");
            String guestPhone = request.getParameter("guestPhone");
            String checkInStr = request.getParameter("checkIn");
            String checkOutStr = request.getParameter("checkOut");
            int numberOfGuests = Integer.parseInt(request.getParameter("numberOfGuests"));
            String specialRequests = request.getParameter("specialRequests");

            logger.info("Received booking request - Hotel: {}, Room: {}, Guest: {}, Dates: {} to {}", 
                       hotelId, roomTypeId, guestName, checkInStr, checkOutStr);

            // Validate required fields
            if (guestName == null || guestEmail == null || guestPhone == null || 
                guestName.trim().isEmpty() || guestEmail.trim().isEmpty() || guestPhone.trim().isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing required guest information");
                return;
            }

            // Parse dates
            LocalDate checkInDate = LocalDate.parse(checkInStr);
            LocalDate checkOutDate = LocalDate.parse(checkOutStr);
            LocalDate today = LocalDate.now();

            // Validate dates
            if (checkInDate.isBefore(today)) {
                request.getSession().setAttribute("error", "Check-in date cannot be in the past");
                response.sendRedirect(request.getContextPath() + "/hotels/details/" + hotelId);
                return;
            }

            if (checkOutDate.isBefore(checkInDate) || checkOutDate.equals(checkInDate)) {
                request.getSession().setAttribute("error", "Check-out date must be after check-in date");
                response.sendRedirect(request.getContextPath() + "/hotels/details/" + hotelId);
                return;
            }

            // Validate hotel exists
            Hotel hotel = hotelDAO.findById(hotelId);
            if (hotel == null) {
                request.getSession().setAttribute("error", "Hotel not found");
                response.sendRedirect(request.getContextPath() + "/hotels");
                return;
            }

            // Validate room type exists and belongs to hotel
            RoomType roomType = roomTypeDAO.findById(roomTypeId);
            if (roomType == null || roomType.getHotelId() != hotelId) {
                request.getSession().setAttribute("error", "Invalid room type selected");
                response.sendRedirect(request.getContextPath() + "/hotels/details/" + hotelId);
                return;
            }

            // Validate number of guests against room capacity
            if (numberOfGuests > roomType.getCapacity()) {
                request.getSession().setAttribute("error", 
                    "Number of guests exceeds room capacity of " + roomType.getCapacity());
                response.sendRedirect(request.getContextPath() + "/hotels/details/" + hotelId);
                return;
            }

            // Create reservation
            Reservation reservation = new Reservation();
            reservation.setHotelId(hotelId);
            reservation.setRoomTypeId(roomTypeId);
            reservation.setGuestName(guestName);
            reservation.setGuestEmail(guestEmail);
            reservation.setGuestPhone(guestPhone);
            reservation.setCheckInDate(Date.valueOf(checkInDate));
            reservation.setCheckOutDate(Date.valueOf(checkOutDate));
            reservation.setNumberOfGuests(numberOfGuests);
            reservation.setSpecialRequests(specialRequests);
            reservation.setStatus("PENDING");

            // Save reservation
            reservationDAO.create(reservation);
            logger.info("Created reservation {} for guest {} at hotel {}", 
                       reservation.getId(), guestName, hotelId);

            // Set success message and redirect
            request.getSession().setAttribute("message", 
                "Booking confirmed! We'll send confirmation details to " + guestEmail);
            response.sendRedirect(request.getContextPath() + "/hotels");

        } catch (SQLException e) {
            logger.error("Database error while processing booking", e);
            request.getSession().setAttribute("error", "Error processing booking. Please try again.");
            response.sendRedirect(request.getHeader("Referer"));
        } catch (Exception e) {
            logger.error("Error processing booking", e);
            request.getSession().setAttribute("error", "Invalid booking data. Please check all fields.");
            response.sendRedirect(request.getHeader("Referer"));
        }
    }
}
