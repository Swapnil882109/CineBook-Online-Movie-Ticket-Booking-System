package com.moviebooking.project.model;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@Data
public class Booking{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long bookingId;

    Double totalPrice;

    String paymentType;

    LocalDate bookingDate;

    LocalTime bookingTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(fetch = FetchType.LAZY)
    List<ShowSeat>  showSeats;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "showId")
    Show show;
}
