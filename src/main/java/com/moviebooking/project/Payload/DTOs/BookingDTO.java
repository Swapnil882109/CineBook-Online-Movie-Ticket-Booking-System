package com.moviebooking.project.Payload.DTOs;

import lombok.Data;

import java.util.List;

@Data
public class BookingDTO {
    private Long showId;
    private List<Long> showSeat;
    String paymentType;
}
