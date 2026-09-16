package com.moviebooking.project.services;


import com.moviebooking.project.Payload.DTOs.ScreenDTO;
import com.moviebooking.project.Payload.Response.ScreenResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public interface ScreenService {
    ScreenDTO createScreen(Long theatreId,@RequestBody ScreenDTO screenDTO);
    ScreenResponse getAllScreens(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
    ScreenResponse getAllScreensByTheatreId(Long theatreId,Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
    ScreenDTO getScreenById(Long id);
    ScreenDTO deleteScreenById(Long id);
    ScreenDTO updateScreenById(Long id,ScreenDTO screenDTO);
}
