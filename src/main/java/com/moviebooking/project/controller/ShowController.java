package com.moviebooking.project.controller;


import com.moviebooking.project.Payload.DTOs.ShowDTO;
import com.moviebooking.project.Payload.Response.ShowResponse;
import com.moviebooking.project.Response.SeatingLayout;
import com.moviebooking.project.config.AppConstants;
import com.moviebooking.project.services.ShowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")

public class ShowController {


    @Autowired
    private ShowService showService;


    @PreAuthorize("hasRole('THEATRE_MANAGER')")
    @PostMapping("/screens/{screenId}/movies/{movieId}/shows/")
    public ResponseEntity<ShowDTO> CreateShow(
                                     @PathVariable long screenId,
                                     @PathVariable long movieId,
                                     @RequestBody ShowDTO showDTO){
        ShowDTO showDTOFromDB = showService.createShow(screenId, movieId, showDTO);
        return new ResponseEntity<>(showDTOFromDB, HttpStatus.CREATED);
    }

    @GetMapping("public/theatres/{theatreId}/shows/")
    public ResponseEntity<ShowResponse> getAllShowsByTheatreId(@PathVariable Long theatreId,@RequestParam(name = "pageNumber" ,defaultValue = AppConstants.PAGE_NUMBER) Integer pageNumber,
                                                               @RequestParam(name = "pageSize" ,defaultValue = AppConstants.PAGE_SIZE) Integer pageSize,
                                                               @RequestParam(name="sortBy" ,defaultValue = AppConstants.SORT_SHOWS_BY) String sortBy,
                                                               @RequestParam(name="sortOrder" ,defaultValue = AppConstants.SORT_DIR) String sortOrder){
        ShowResponse showResponse=showService.getAllShowsByTheatreId(theatreId,pageNumber,pageSize,sortBy,sortOrder);
        return new ResponseEntity<>(showResponse, HttpStatus.OK);
    }

    @GetMapping("public/movies/{movieId}/shows/")
    public ResponseEntity<ShowResponse> getAllShowsByMovieId(@PathVariable Long movieId,@RequestParam(name = "pageNumber" ,defaultValue = AppConstants.PAGE_NUMBER) Integer pageNumber,
                                                               @RequestParam(name = "pageSize" ,defaultValue = AppConstants.PAGE_SIZE) Integer pageSize,
                                                               @RequestParam(name="sortBy" ,defaultValue = AppConstants.SORT_SHOWS_BY) String sortBy,
                                                               @RequestParam(name="sortOrder" ,defaultValue = AppConstants.SORT_DIR) String sortOrder){
        ShowResponse showResponse=showService.getAllShowsByMovieId(movieId,pageNumber,pageSize,sortBy,sortOrder);
        return new ResponseEntity<>(showResponse, HttpStatus.OK);
    }

    @GetMapping("public/shows/{showId}/seats")
    public ResponseEntity<SeatingLayout> getAllSeatingByShowId(@PathVariable Long showId){
        SeatingLayout seatingLayout=showService.getShowSeatStatusById(showId);
        return new ResponseEntity<>(seatingLayout, HttpStatus.OK);
    }

    @GetMapping("public/shows/{showId}")
    public ResponseEntity<ShowDTO> getShowById(@PathVariable Long showId){
        ShowDTO showDTO=showService.getShowById(showId);
        return new ResponseEntity<>(showDTO,HttpStatus.OK);
    }

    @PreAuthorize("hasRole('THEATRE_MANAGER')")
    @PutMapping("/screens/{screenId}/shows/{showId}")
    public ResponseEntity<ShowDTO> UpdateShow(@PathVariable Long screenId,@PathVariable Long showId,@RequestBody ShowDTO showDTO){
        ShowDTO showDTOFromDB = showService.updateShow(screenId,showId,showDTO);
        return new ResponseEntity<>(showDTOFromDB, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('THEATRE_MANAGER')")
    @DeleteMapping("/shows/{showId}")
    public ResponseEntity<ShowDTO> DeleteShow(@PathVariable Long showId){
        ShowDTO showDTO=showService.deleteShow(showId);
        return new ResponseEntity<>(showDTO,HttpStatus.OK);
    }

}
