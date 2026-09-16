package com.moviebooking.project.Payload.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ScreenDTO {
    private Long screenId;
    private String screenName;
    private String screenType;
    private int rowsGold;
    private int rowsSilver;
    private int rowsBronze;
    private int seatsPerRow;
}
