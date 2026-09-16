package com.moviebooking.project.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;


    @NotBlank
    @Size(min=2, max=50)
    @Email
    @Column(name = "email")
    private String email;


    @NotBlank
    @Size(min=2, max=50)
    @Column(name = "username")
    private String userName;

    @NotBlank
    @Size(min=2, max=100)
    @Column(name = "password")
    private String password;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name="user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles=new HashSet<>();

    @OneToMany(mappedBy = "user")
    private List<Booking> bookings=new ArrayList<>();

    @OneToMany(mappedBy = "manager")
    private List<Theatre> theatres=new ArrayList<>();

    public User(@NotBlank @Size(min = 3, max = 20) String username, @NotBlank @Size(max = 50) @Email String email, @Nullable String encode) {
        this.userName = username;
        this.email = email;
        this.password = encode;
    }
}
