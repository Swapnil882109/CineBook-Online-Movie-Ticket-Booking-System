package com.moviebooking.project.Payload.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TheatreDTO {
    private Long theatreId;
    private String theatreName;
    private String city;
    private String state;
    private String country;
}
