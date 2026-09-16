package com.moviebooking.project.controller;

import com.moviebooking.project.Payload.DTOs.BookingDTO;
import com.moviebooking.project.model.Booking;
import com.moviebooking.project.security.jwt.JwtUtils;
import com.moviebooking.project.services.BookingService;
import com.moviebooking.project.utils.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")

public class BookingController {

    @Autowired
    AuthUtil  authUtil;
    @Autowired
    BookingService bookingService;


    @PostMapping("/bookings")
    public ResponseEntity<String> booking(@RequestBody BookingDTO bookingDTO) {
        String bookingStatus=bookingService.CreateBooking(bookingDTO);
        return ResponseEntity.ok(bookingStatus);
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/bookings/history")
    public ResponseEntity<List<Booking>> BookingHistory(){
        Long userId=authUtil.loggedInUserId();
        List<Booking> bookings=bookingService.BookingHistory(userId);
        return ResponseEntity.ok(bookings);
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/bookings/{bookingId}")
    public ResponseEntity<String> cancelBooking(@PathVariable Long bookingId){
        Long userId=authUtil.loggedInUserId();
        String cancelBooking=bookingService.CancelBooking(bookingId,userId);
        return ResponseEntity.ok(cancelBooking);
    }

}
