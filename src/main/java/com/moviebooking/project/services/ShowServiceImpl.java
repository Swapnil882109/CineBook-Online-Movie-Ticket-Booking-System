package com.moviebooking.project.services;

import com.moviebooking.project.Payload.DTOs.ShowDTO;
import com.moviebooking.project.Payload.Response.ShowResponse;
import com.moviebooking.project.Response.SeatingLayout;
import com.moviebooking.project.exception.APIException;
import com.moviebooking.project.exception.ResourceNotFoundException;
import com.moviebooking.project.model.*;
import com.moviebooking.project.repository.MovieRepository;
import com.moviebooking.project.repository.ScreenRepository;
import com.moviebooking.project.repository.ShowRepository;
import com.moviebooking.project.repository.ShowSeatRepository;
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
public class ShowServiceImpl implements ShowService {
    @Autowired
    private ShowRepository showRepository;

    @Autowired
    private ScreenRepository screenRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ShowSeatRepository showSeatRepository;

    @Override
    public ShowDTO createShow( Long screenId, Long movieId, ShowDTO showDTO) {
        Show show = modelMapper.map(showDTO,Show.class);
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(()->new ResourceNotFoundException("Movie","movieId",movieId));
        show.setEndTime(show.getStartTime().plus(movie.getDuration()));
        boolean showExists =showRepository.findWhereStartTimeOrEndTimeExists(screenId,show.getShowDate(),show.getStartTime(),show.getEndTime());
        if(showExists){
            throw new APIException("A show already exists between "+show.getStartTime()+" and "+show.getEndTime()+" on "+show.getShowDate());
        }
        Screen screen =screenRepository.findById(screenId)
                .orElseThrow(()->new ResourceNotFoundException("Screen","screenId",screenId));

        show.setScreen(screen);
        show.setMovie(movie);

        List<ShowSeat> showSeatList= createShowSeats(screen,show);

        show.setShowSeats(showSeatList);
        Show showFromDB=showRepository.save(show);
        return modelMapper.map(showFromDB,ShowDTO.class);
    }

    List<ShowSeat> createShowSeats(Screen screen, Show show) {
        List<Seat> seatList=screen.getSeats();
        List<ShowSeat> showSeatList=new ArrayList<>();
        for(Seat seat:seatList){
            ShowSeat showSeat=new ShowSeat(SeatStatus.Unlocked,show,seat);
            showSeatList.add(showSeat);
        }
        return showSeatList;
    }

    @Override
    public ShowResponse getAllShowsByTheatreId(Long theatreId,Integer pageNumber,Integer pageSize,String sortBy,String sortOrder) {
        Sort sortByAndOrder=sortOrder.equalsIgnoreCase("asc")?
                Sort.by(sortBy).ascending()
                :Sort.by(sortBy).descending();

        Pageable pageable= PageRequest.of(pageNumber,pageSize,sortByAndOrder);

        Page<Show> showPage=showRepository.findByTheatreId(theatreId,pageable);

        List<Show> showList=showPage.getContent();
        if(showList.isEmpty()){
            throw new APIException("There is no shows with the given theatreId");
        }

        List<ShowDTO> showDTOList=showList.stream()
                .map(s->modelMapper.map(s,ShowDTO.class)).toList();

        ShowResponse showResponse=new ShowResponse();
        showResponse.setContent(showDTOList);
        showResponse.setPageNumber(showPage.getNumber());
        showResponse.setPageSize(showPage.getSize());
        showResponse.setTotalPages(showPage.getTotalPages());
        showResponse.setTotalElements(showPage.getTotalElements());
        showResponse.setLastPage(showPage.isLast());
        return showResponse;
    }

    @Override
    public ShowResponse getAllShowsByMovieId(Long movieId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder=sortOrder.equalsIgnoreCase("asc")?
                Sort.by(sortBy).ascending()
                :Sort.by(sortBy).descending();

        Pageable pageable= PageRequest.of(pageNumber,pageSize,sortByAndOrder);

        Page<Show> showPage=showRepository.findByMovieId(movieId,pageable);

        List<Show> showList=showPage.getContent();
        if(showList.isEmpty()){
            throw new APIException("There is no shows with the given movieId");
        }



        List<ShowDTO> showDTOList=showList.stream()
                .map(s->modelMapper.map(s,ShowDTO.class)).toList();

        ShowResponse showResponse=new ShowResponse();
        showResponse.setContent(showDTOList);
        showResponse.setPageNumber(showPage.getNumber());
        showResponse.setPageSize(showPage.getSize());
        showResponse.setTotalPages(showPage.getTotalPages());
        showResponse.setTotalElements(showPage.getTotalElements());
        showResponse.setLastPage(showPage.isLast());
        return showResponse;


    }

    @Override
    public ShowDTO getShowById(Long showId) {
        Show show=showRepository.findById(showId)
                .orElseThrow(()->new ResourceNotFoundException("Show","showId",showId));
        return modelMapper.map(show,ShowDTO.class);
    }

    @Override
    public SeatingLayout getShowSeatStatusById(Long showId) {
        SeatingLayout seatingLayout=new SeatingLayout();
        Show show=showRepository.findById(showId)
                .orElseThrow(()->new ResourceNotFoundException("Show","showId",showId));

        List<ShowSeat> showSeatList=show.getShowSeats();
        seatingLayout.setShowSeats(showSeatList);
        seatingLayout.setTotalSeats((long)showSeatList.size());
        Long availableSeats=showSeatRepository.countByShowShowIdAndSeatStatus(showId,SeatStatus.Unlocked);
        seatingLayout.setAvailableSeats(availableSeats);
        return  seatingLayout;
    }

    @Override
    public ShowDTO updateShow(Long screenId, Long showId, ShowDTO showDTO) {
        Show showFromDB=showRepository.findById(showId)
                .orElseThrow(()->new ResourceNotFoundException("Show","showId",showId));

        Movie movieFromDB=movieRepository.findById(showFromDB.getMovie().getMovieId())
                .orElseThrow(()->new ResourceNotFoundException("Movie","movieId",showFromDB.getMovie().getMovieId()));

        showFromDB.setShowDate(showDTO.getShowDate());
        showFromDB.setStartTime(showDTO.getStartTime());
        showFromDB.setBasePrice(showDTO.getBasePrice());

        showFromDB.setEndTime(showFromDB.getStartTime().plus(movieFromDB.getDuration()));

        boolean showExists =showRepository.findWhereStartTimeOrEndTimeExistsExceptCurrent(screenId,showId,showFromDB.getShowDate(),showFromDB.getStartTime(),showFromDB.getEndTime());
        if(showExists){
            throw new APIException("A show already exists between "+showFromDB.getStartTime()+" and "+showFromDB.getEndTime()+" on "+showFromDB.getShowDate());
        }

        Show savedShow=showRepository.save(showFromDB);
        return modelMapper.map(savedShow,ShowDTO.class);
    }

    @Override
    public ShowDTO deleteShow(Long showId) {
        Show show=showRepository.findById(showId)
                .orElseThrow(()->new ResourceNotFoundException("Show","showId",showId));
        showRepository.delete(show);
        return modelMapper.map(show,ShowDTO.class);
    }
}
