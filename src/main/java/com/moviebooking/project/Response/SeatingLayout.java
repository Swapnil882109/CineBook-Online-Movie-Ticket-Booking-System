package com.moviebooking.project.Response;


import com.moviebooking.project.model.ShowSeat;
import lombok.Data;

import java.time.LocalTime;
import java.util.List;

@Data
public class SeatingLayout {
    private Long showId;
    private Long totalSeats;
    private Long AvailableSeats;
    private List<ShowSeat> showSeats;
}
