package com.moviebooking.project.Response;

import lombok.Data;

import java.util.Map;

@Data
public class UnavailableSeatsResponse {
    Map<Long,String> unaviableSeats;
}
