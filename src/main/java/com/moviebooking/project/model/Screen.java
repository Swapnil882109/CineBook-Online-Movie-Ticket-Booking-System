package com.moviebooking.project.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Screen {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long screenId;

    @NotBlank
    @Size(min = 3, max = 100)
    private String screenName;

    @NotBlank
    @Size(min=3,max=100)
    private String screenType;

    @Min(1)
    @Max(10)
    private int rowsGold;

    @Min(1)
    @Max(10)
    private int rowsSilver;

    @Min(1)
    @Max(10)
    private int rowsBronze;

    @Min(1)
    @Max(30)
    private int seatsPerRow;

    @ManyToOne()
    @JoinColumn(name="theatreId")
    private Theatre theatre;

    @OneToMany(mappedBy = "screen",orphanRemoval = true,cascade = CascadeType.ALL)
    private List<Seat>  seats;
}
