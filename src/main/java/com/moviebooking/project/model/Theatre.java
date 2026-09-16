package com.moviebooking.project.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Theatre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long theatreId;

//    @NotBlank
    @Size(min = 3, max = 100)
    private String theatreName;

//    @NotBlank
    @Size(min = 3, max = 100)
    private String city;

//    @NotBlank
    @Size(min = 3, max = 100)
    private String state;

//    @NotBlank
    @Size(min = 3, max = 100)
    private String country;


    @OneToMany(mappedBy = "theatre",orphanRemoval = true,cascade = CascadeType.ALL)
    List<Screen> screens;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id")
    private User manager;


}
