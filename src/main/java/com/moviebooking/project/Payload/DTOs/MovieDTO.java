package com.moviebooking.project.Payload.DTOs;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovieDTO {
    private Long movieId;
    private String movieName;
    private Duration duration;
    private Double basePrice;
    private Double movieRating;
    private String movieDescription;
    private String movieImage;
    private List<String> movieTags;
    private List<String> movieActors;
}
