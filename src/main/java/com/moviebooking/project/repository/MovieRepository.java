package com.moviebooking.project.repository;

import com.moviebooking.project.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<Movie,Long> {

    Optional<Movie> findByMovieName(String movieName);

    List<Movie> findByMovieNameLikeIgnoreCase(String keyword);
}
