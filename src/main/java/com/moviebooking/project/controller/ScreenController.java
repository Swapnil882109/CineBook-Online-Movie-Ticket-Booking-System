package com.moviebooking.project.controller;


import com.moviebooking.project.Payload.DTOs.ScreenDTO;
import com.moviebooking.project.Payload.Response.ScreenResponse;
import com.moviebooking.project.config.AppConstants;
import com.moviebooking.project.services.ScreenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")

public class ScreenController {

    @Autowired
    ScreenService screenService;


    @PreAuthorize("hasRole('THEATRE_MANAGER')")
    @PostMapping("/theatres/{theatreId}/screens")
    public ResponseEntity<ScreenDTO> createScreen(@PathVariable Long theatreId, @RequestBody ScreenDTO screenDTO) {
        ScreenDTO savedScreen=screenService.createScreen(theatreId,screenDTO);
        return new ResponseEntity<>(savedScreen, HttpStatus.CREATED);
    }


    @GetMapping("public/screens/")
    public ScreenResponse getAllScreens(@RequestParam(name = "pageNumber" ,defaultValue = AppConstants.PAGE_NUMBER) Integer pageNumber,
                                        @RequestParam(name = "pageSize" ,defaultValue = AppConstants.PAGE_SIZE) Integer pageSize,
                                        @RequestParam(name="sortBy" ,defaultValue = AppConstants.SORT_SCREENS_BY) String sortBy,
                                        @RequestParam(name="sortOrder" ,defaultValue = AppConstants.SORT_DIR) String sortOrder) {
        return screenService.getAllScreens(pageNumber,pageSize,sortBy,sortOrder);
    }

    @GetMapping("public/theatres/{theatreId}/screens")
    public ScreenResponse getScreenByTheatreId(@PathVariable Long theatreId,@RequestParam(name = "pageNumber" ,defaultValue = AppConstants.PAGE_NUMBER) Integer pageNumber,
                                    @RequestParam(name = "pageSize" ,defaultValue = AppConstants.PAGE_SIZE) Integer pageSize,
                                    @RequestParam(name="sortBy" ,defaultValue = AppConstants.SORT_SCREENS_BY) String sortBy,
                                    @RequestParam(name="sortOrder" ,defaultValue = "asc") String sortOrder) {

        return screenService.getAllScreensByTheatreId(theatreId,pageNumber,pageSize,sortBy,sortOrder);
    }


    @GetMapping("public/screens/{screenId}")
    public ResponseEntity<ScreenDTO> getScreenById(@PathVariable Long screenId) {
        ScreenDTO screenDTO=screenService.getScreenById(screenId);
        return new ResponseEntity<>(screenDTO, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('THEATRE_MANAGER')")
    @DeleteMapping("/screens/{screenId}")
    public ResponseEntity<ScreenDTO> deleteScreenById(@PathVariable Long screenId) {
        ScreenDTO screenDTO=screenService.deleteScreenById(screenId);
        return new ResponseEntity<>(screenDTO, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('THEATRE_MANAGER')")
    @PutMapping("/screens/{screenId}")
    public ResponseEntity<ScreenDTO> updateScreenById(@PathVariable Long screenId,@RequestBody ScreenDTO screenDTO) {
        ScreenDTO screenDTOFromDB=screenService.updateScreenById(screenId,screenDTO);
        return new ResponseEntity<>(screenDTOFromDB, HttpStatus.OK);
    }
}
