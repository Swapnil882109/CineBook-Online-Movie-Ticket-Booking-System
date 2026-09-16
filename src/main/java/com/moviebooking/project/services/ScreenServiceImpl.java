package com.moviebooking.project.services;

import com.moviebooking.project.Payload.DTOs.ScreenDTO;
import com.moviebooking.project.Payload.Response.ScreenResponse;
import com.moviebooking.project.exception.APIException;
import com.moviebooking.project.exception.ResourceNotFoundException;
import com.moviebooking.project.model.Screen;
import com.moviebooking.project.model.Seat;
import com.moviebooking.project.model.SeatType;
import com.moviebooking.project.model.Theatre;
import com.moviebooking.project.repository.ScreenRepository;
import com.moviebooking.project.repository.TheatreRepository;
import org.jspecify.annotations.NonNull;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class ScreenServiceImpl implements ScreenService {
    @Autowired
    TheatreRepository theatreRepository;

    @Autowired
    ScreenRepository screenRepository;


    @Autowired
    ModelMapper modelMapper;


    @Override
    public ScreenDTO createScreen(Long theatreId, ScreenDTO screenDTO) {
        Theatre theatre=theatreRepository.findById(theatreId)
                .orElseThrow(()->new ResourceNotFoundException("Theatre","TheatreId",theatreId));
        Screen screen=modelMapper.map(screenDTO,Screen.class);
        screen.setTheatre(theatre);
        List<Seat> seatList = createSeats(screenDTO, screen);
        screen.setSeats(seatList);
        Screen screenFromDB=screenRepository.save(screen);
        List<Screen> screenList=theatre.getScreens();
        screenList.add(screen);
        return modelMapper.map(screenFromDB,ScreenDTO.class);
    }

    @Override
    public ScreenResponse getAllScreens(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder=sortOrder.equalsIgnoreCase("asc")?
                Sort.by(sortBy).ascending()
                :Sort.by(sortBy).descending();

        Pageable pageable=PageRequest.of(pageNumber,pageSize,sortByAndOrder);

        Page<Screen> screenPage=screenRepository.findAll(pageable);
        List<Screen> screenList=screenPage.getContent();
        if(screenList.isEmpty()){
            throw  new APIException("No screens found");
        }
        List<ScreenDTO> screenDTOList=screenList.stream()

                .map(screen -> modelMapper.map(screen,ScreenDTO.class)).toList();
        ScreenResponse screenResponse=new ScreenResponse();
        screenResponse.setContent(screenDTOList);
        screenResponse.setPageNumber(pageable.getPageNumber());
        screenResponse.setPageSize(pageable.getPageSize());
        screenResponse.setTotalPages(screenPage.getTotalPages());
        screenResponse.setTotalElements(screenPage.getTotalElements());
        screenResponse.setLastPage(screenPage.isLast());
        return screenResponse;
    }

    @Override
    public ScreenResponse getAllScreensByTheatreId(Long theatreId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder=sortOrder.equalsIgnoreCase("asc")?
                Sort.by(sortBy).ascending():
                Sort.by(sortBy).descending();

        Pageable pageable=PageRequest.of(pageNumber,pageSize,sortByAndOrder);
         Page<Screen> screenPage=screenRepository.findWhereTheatreId(theatreId,pageable);
         List<Screen> screenList=screenPage.getContent();
        if(screenList.isEmpty()){
            throw  new APIException("No screens found");
        }
        List<ScreenDTO> screenDTOList=screenList.stream()
                .map(screen -> modelMapper.map(screen,ScreenDTO.class)).toList();
        ScreenResponse screenResponse=new ScreenResponse();
        screenResponse.setContent(screenDTOList);
        screenResponse.setPageNumber(pageable.getPageNumber());
        screenResponse.setPageSize(pageable.getPageSize());
        screenResponse.setTotalPages(screenPage.getTotalPages());
        screenResponse.setTotalElements(screenPage.getTotalElements());
        screenResponse.setLastPage(screenPage.isLast());
        return screenResponse;
    }


    @Override
    public ScreenDTO getScreenById(Long screenId) {
        Screen screen=screenRepository.findById(screenId)
                .orElseThrow(()->new ResourceNotFoundException("Screen","id",screenId));
        return modelMapper.map(screen,ScreenDTO.class);
    }

    @Override
    public ScreenDTO deleteScreenById( Long screenId) {
        Screen screen=screenRepository.findById(screenId)
                .orElseThrow(() ->new ResourceNotFoundException("Screen","ScreenId",screenId));
        screen.getTheatre().getScreens().remove(screen);
        screen.setTheatre(null);
        screenRepository.delete(screen);
        return modelMapper.map(screen,ScreenDTO.class);
    }

    @Override
    public ScreenDTO updateScreenById( Long screenId, ScreenDTO screenDTO) {
        Screen screenFromDB=screenRepository.findById(screenId)
                .orElseThrow(() ->new ResourceNotFoundException("Screen","ScreenId",screenId));

        screenFromDB.setRowsGold(screenDTO.getRowsGold());
        screenFromDB.setRowsSilver(screenDTO.getRowsSilver());
        screenFromDB.setRowsBronze(screenDTO.getRowsBronze());
        screenFromDB.setScreenName(screenDTO.getScreenName());
        screenFromDB.setScreenType(screenDTO.getScreenType());
        screenFromDB.setSeatsPerRow(screenDTO.getSeatsPerRow());

        List<Seat> seatList = createSeats(screenDTO, screenFromDB);
        screenFromDB.setSeats(seatList);


        Screen screen=screenRepository.save(screenFromDB);
        return modelMapper.map(screen,ScreenDTO.class);
    }

    private static @NonNull List<Seat> createSeats(ScreenDTO screenDTO, Screen screenFromDB) {
        int seatPerRow= screenDTO.getSeatsPerRow();
        int goldRows= screenDTO.getRowsGold();
        int silverRows= screenDTO.getRowsSilver();
        int bronzeRows= screenDTO.getRowsBronze();
        int rows=goldRows+silverRows+bronzeRows;
        SeatType seatType;

        List<Seat> seatList=new ArrayList<Seat>();
        for (int i = 1; i <= rows; i++) {
            if (i <= goldRows) {
                seatType = SeatType.Gold;
            } else if (i <= goldRows + silverRows) {
                seatType = SeatType.Silver;
            } else {
                seatType = SeatType.Bronze;
            }
            for (int j = 1; j <= seatPerRow; j++) {
                Seat seat = new Seat(seatType, i, j, screenFromDB);
                seatList.add(seat);
            }
        }
        return seatList;
    }
}
