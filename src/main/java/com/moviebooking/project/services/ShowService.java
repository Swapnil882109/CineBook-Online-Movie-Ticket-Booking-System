package com.moviebooking.project.services;

import com.moviebooking.project.Payload.DTOs.ShowDTO;
import com.moviebooking.project.Payload.Response.ShowResponse;
import com.moviebooking.project.Response.SeatingLayout;

public interface ShowService {
    ShowDTO createShow(Long screenId,Long movieId,ShowDTO showDTO);

    ShowResponse getAllShowsByTheatreId(Long theatreId,Integer pageNumber,Integer pageSize,String sortBy,String sortOrder);

    ShowResponse getAllShowsByMovieId(Long movieId,Integer pageNumber,Integer pageSize,String sortBy,String sortOrder);

    ShowDTO getShowById(Long showId);

    SeatingLayout getShowSeatStatusById(Long showId);

    ShowDTO updateShow(Long screenId,Long showId,ShowDTO showDTO);

    ShowDTO deleteShow(Long showId);
}
