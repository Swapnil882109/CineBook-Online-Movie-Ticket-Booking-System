package com.moviebooking.project.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShowSeat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long showSeatId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    SeatStatus seatStatus;

    @Version
    private Long version;


    @ManyToOne
    @JoinColumn(name = "showId")
    private Show show;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinColumn(name = "seatId")
    private Seat seat;

    public ShowSeat(SeatStatus seatStatus, Show show, Seat seat) {
        this.seatStatus=seatStatus;
        this.seat=seat;
        this.show=show;
    }
}
