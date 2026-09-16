package com.moviebooking.project.Payload.DTOs;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class ShowDTO {
    private Long showId;

    private LocalDate showDate;

    private LocalTime startTime;

    private BigDecimal basePrice;
}
