package com.moviebooking.project.services;

import com.moviebooking.project.Payload.DTOs.MovieDTO;
import com.moviebooking.project.Payload.Response.MovieResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public interface MovieService {
    MovieDTO createMovie(MovieDTO movieDTO);
    MovieResponse getAllMovies(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
    MovieDTO getMovieById(Long movieId);
    List<MovieDTO> getMoviesByKeyword(String keyword);
    MovieDTO updateMovie(Long movieId,MovieDTO movieDTO);
    MovieDTO deleteMovie(Long id);
    MovieDTO updateMovieImage(Long movieId, MultipartFile image) throws IOException;
}
