package com.moviebooking.project.request;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
public class BookingRequest {
    private Long showId;
    private List<Long> showSeat;
}
