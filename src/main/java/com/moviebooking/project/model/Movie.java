package com.moviebooking.project.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long movieId;

    @NotBlank
    @Size(min = 3, max = 100)
    private String movieName;
    @NotNull
    private Duration duration;

    @NotNull
    private Double basePrice;

    private Double movieRating;
    private String movieDescription;
    private String movieImage;
    private List<String> movieTags;
    private List<String> movieActors;

    @OneToMany(mappedBy = "movie")
    private List<Show> showList;

}
