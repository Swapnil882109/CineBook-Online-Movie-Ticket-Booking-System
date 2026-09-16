package com.moviebooking.project.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "Shows")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Show {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long showId;



    private LocalDate showDate;

    private LocalTime startTime;

    private LocalTime endTime;

    private BigDecimal basePrice;


    @ManyToOne()
    @JoinColumn(name="movieId")
    private Movie movie;

    @ManyToOne()
    @JoinColumn(name="screenId")
    private Screen screen;

    @OneToMany(
            mappedBy = "show",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<ShowSeat> showSeats;
}
