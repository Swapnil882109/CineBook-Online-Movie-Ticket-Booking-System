package com.moviebooking.project.services;

import com.moviebooking.project.Payload.DTOs.BookingDTO;
import com.moviebooking.project.model.Booking;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface BookingService {
    String CreateBooking(@RequestBody BookingDTO bookingDTO);
    List<Booking> BookingHistory(Long userId);
    String CancelBooking(Long bookingId, Long userId);
}
