package com.moviebooking.project.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seatId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    SeatType seatType;

    @Min(1)
    @Column(name = "seat_row")
    int row;

    @Min(1)
    @Column(name = "seat_col")
    int col;

    @ManyToOne
    @JoinColumn(name="screenId")
    private Screen screen;

    @OneToMany(mappedBy = "seat")
    private List<ShowSeat> showSeats;

    public Seat(SeatType seatType, int row, int col, Screen screen) {
        this.screen=screen;
        this.row=row;
        this.col=col;
        this.seatType=seatType;
    }
}
