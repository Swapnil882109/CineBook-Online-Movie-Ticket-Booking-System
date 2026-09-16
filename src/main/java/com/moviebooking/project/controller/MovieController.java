package com.moviebooking.project.controller;


import com.moviebooking.project.Payload.DTOs.MovieDTO;
import com.moviebooking.project.Payload.Response.MovieResponse;
import com.moviebooking.project.config.AppConstants;
import com.moviebooking.project.services.MovieService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("/api")

public class MovieController {

    @Autowired
    MovieService movieService;

    @PostMapping("/admin/movies")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MovieDTO> addMovie(@Valid @RequestBody MovieDTO movieDTO) {
        MovieDTO savedMovieDTO=movieService.createMovie(movieDTO);
        return new ResponseEntity<>(savedMovieDTO, HttpStatus.CREATED);
    }

    @GetMapping("public/movies")
    public ResponseEntity<MovieResponse> getAllMovies(
            @RequestParam(name="pageNumber",defaultValue = AppConstants.PAGE_NUMBER,required=false) Integer pageNumber,
            @RequestParam(name="pageSize",defaultValue = AppConstants.PAGE_SIZE,required=false) Integer pageSize,
            @RequestParam(name="sortBy",defaultValue = AppConstants.SORT_MOVIES_BY,required = false) String sortBy,
            @RequestParam(name="sortOrder",defaultValue = AppConstants.SORT_DIR,required = false) String sortOrder) {
        MovieResponse movieResponse=movieService.getAllMovies(pageNumber,pageSize,sortBy,sortOrder);
        return new ResponseEntity<>(movieResponse,HttpStatus.OK);
    }

    @GetMapping("public/movies/{movieId}")
    public ResponseEntity<MovieDTO> getMovieById(@PathVariable Long movieId) {
        MovieDTO movieDTO=movieService.getMovieById(movieId);
        return new ResponseEntity<>(movieDTO,HttpStatus.OK);
    }

    @GetMapping("public/movies/search")
    public ResponseEntity<List<MovieDTO>> getMoviesByKeyword(@RequestParam String keyword) {
        List<MovieDTO> movieResponse=movieService.getMoviesByKeyword(keyword);
        return new ResponseEntity<>(movieResponse,HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/admin/movies/{movieId}")
    public ResponseEntity<MovieDTO> updateMovie(@PathVariable Long movieId, @Valid @RequestBody MovieDTO movieDTO) {
        MovieDTO savedMovieDTO=movieService.updateMovie(movieId,movieDTO);
        return new ResponseEntity<>( savedMovieDTO, HttpStatus.ACCEPTED);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/admin/movies/{movieId}")
    public ResponseEntity<MovieDTO> deleteMovie(@PathVariable Long movieId) {
        MovieDTO savedMovieDTO=movieService.deleteMovie(movieId);
        return new ResponseEntity<>( savedMovieDTO, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/admin/movies/{movieId}/image")
    public ResponseEntity<MovieDTO> updateProductImage(@PathVariable Long movieId,
                                                         @RequestParam("Image") MultipartFile image) throws IOException {

        MovieDTO updatedMovie=movieService.updateMovieImage(movieId,image);
        return new ResponseEntity<>(updatedMovie, HttpStatus.OK);
    }
}
