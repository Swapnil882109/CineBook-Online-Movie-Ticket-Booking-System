package com.moviebooking.project.controller;


import com.moviebooking.project.Payload.DTOs.TheatreDTO;
import com.moviebooking.project.Payload.Response.TheatreResponse;
import com.moviebooking.project.config.AppConstants;
import com.moviebooking.project.model.User;
import com.moviebooking.project.services.TheatreService;
import com.moviebooking.project.utils.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@EnableMethodSecurity
public class TheatreController {
    @Autowired
    TheatreService theatreService;

    @Autowired
    private AuthUtil authUtil;


    @PreAuthorize("hasRole('THEATRE_MANAGER')")
    @PostMapping("/theatres")
    public ResponseEntity<TheatreDTO> createTheatre(@RequestBody TheatreDTO theatreDTO) {
        User theatreManager=authUtil.loggedInUser();
        TheatreDTO createdTheatre =theatreService.createTheatre(theatreDTO,theatreManager);
        return new ResponseEntity<>(createdTheatre, HttpStatus.OK);
    }

    @GetMapping("public/theatres")
    public TheatreResponse getAllTheatres(@RequestParam(name="pageNumber",defaultValue = AppConstants.PAGE_NUMBER) Integer pageNumber,
                                          @RequestParam(name="pageSize",defaultValue = AppConstants.PAGE_SIZE) Integer pageSize,
                                          @RequestParam(name="sortBy",defaultValue = AppConstants.SORT_THEATRES_BY) String sortBy,
                                          @RequestParam(name="sortOrder",defaultValue = AppConstants.SORT_DIR) String sortOrder) {
        TheatreResponse theatreResponse=theatreService.getAllTheatres(pageNumber,pageSize,sortBy,sortOrder);
        return theatreResponse;
    }

    @GetMapping("public/theatres/{theatreId}")
    public ResponseEntity<TheatreDTO> getTheatreById(@PathVariable("theatreId") Long theatreId) {
        TheatreDTO theatreDTO=theatreService.getTheatreById(theatreId);
        return new ResponseEntity<>(theatreDTO, HttpStatus.OK);
    }

    @GetMapping("public/theatres/name/{theatreName}")
    public ResponseEntity<TheatreDTO> getTheatreByName(@PathVariable("theatreName") String theatreName) {
        TheatreDTO theatreDTO=theatreService.getTheatreByName(theatreName);
        return new ResponseEntity<>(theatreDTO, HttpStatus.OK);
    }


    @GetMapping("public/theatres/city/{city}")
    public TheatreResponse getTheatresByCity(@PathVariable String city,
                                             @RequestParam(name="pageNumber",defaultValue = AppConstants.PAGE_NUMBER) Integer pageNumber,
                                             @RequestParam(name="pageSize",defaultValue = AppConstants.PAGE_SIZE) Integer pageSize,
                                             @RequestParam(name="sortBy",defaultValue = AppConstants.SORT_THEATRES_BY) String sortBy,
                                             @RequestParam(name="sortOrder",defaultValue = AppConstants.SORT_DIR) String sortOrder
                                             )  {
        TheatreResponse theatreResponse=theatreService.getTheatresByCity(city,pageNumber,pageSize,sortBy,sortOrder);
        return theatreResponse;
    }
    @GetMapping("public/theatres/state/{state}")
    public TheatreResponse getTheatresByState(@PathVariable String state,
                                          @RequestParam(name="pageNumber",defaultValue = AppConstants.PAGE_NUMBER) Integer pageNumber,
                                          @RequestParam(name="pageSize",defaultValue = AppConstants.PAGE_SIZE) Integer pageSize,
                                          @RequestParam(name="sortBy",defaultValue = AppConstants.SORT_THEATRES_BY) String sortBy,
                                          @RequestParam(name="sortOrder",defaultValue = AppConstants.SORT_DIR) String sortOrder) {
        TheatreResponse theatreResponse=theatreService.getTheatresByState(state,pageNumber,pageSize,sortBy,sortOrder);
        return theatreResponse;
    }


    @PreAuthorize("hasRole('THEATRE_MANAGER')")
    @PutMapping("/theatres/{theatreId}")
    public ResponseEntity<TheatreDTO> updateTheatre(@PathVariable  Long theatreId, @RequestBody TheatreDTO theatreDTO) {
        Long theatreManagerId=authUtil.loggedInUserId();
        TheatreDTO updatedTheatre=theatreService.updateTheatre(theatreId,theatreDTO,theatreManagerId);
        return new ResponseEntity<>(updatedTheatre,HttpStatus.OK);
    }


    @PreAuthorize("hasRole('THEATRE_MANAGER')")
    @DeleteMapping("/theatres/{theatreId}")
    public ResponseEntity<TheatreDTO> deleteTheatre(@PathVariable Long theatreId) {
        Long theatreManagerId=authUtil.loggedInUserId();
        TheatreDTO theatreDTO=theatreService.deleteTheatre(theatreId,theatreManagerId);
        return new ResponseEntity<>(theatreDTO,HttpStatus.OK);
    }


}
