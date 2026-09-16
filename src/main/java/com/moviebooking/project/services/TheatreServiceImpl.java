package com.moviebooking.project.services;

import com.moviebooking.project.Payload.DTOs.TheatreDTO;
import com.moviebooking.project.Payload.Response.TheatreResponse;
import com.moviebooking.project.exception.APIException;
import com.moviebooking.project.exception.ResourceNotFoundException;
import com.moviebooking.project.model.Theatre;
import com.moviebooking.project.model.User;
import com.moviebooking.project.repository.TheatreRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class TheatreServiceImpl implements TheatreService {

    @Autowired
    TheatreRepository theatreRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public TheatreDTO createTheatre(TheatreDTO theatreDTO, User theatreManager) {
        Theatre theatre=modelMapper.map(theatreDTO,Theatre.class);
        theatre.setManager(theatreManager);
        Theatre savedTheatre=theatreRepository.findWhereTheatreIdCityStateAndCountry(theatre.getTheatreName(),theatre.getCity()
                ,theatre.getState(),theatre.getCountry());
        if(savedTheatre!=null){
            throw new APIException("theatre already exists");
        }
        Theatre theatreFromDB=theatreRepository.save(theatre);
        return modelMapper.map(theatreFromDB,TheatreDTO.class);
    }

    @Override
    public TheatreResponse getAllTheatres(Integer pageNumber, Integer pageSize,String sortBy,String sortOrder) {
        Sort sortByAndOrder=sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                :Sort.by(sortBy).descending();
        Pageable pageDetails= PageRequest.of(pageNumber,pageSize,sortByAndOrder);
        Page<Theatre> theatrePage=theatreRepository.findAll(pageDetails);

        List<Theatre> theatreList=theatrePage.getContent();
        if(theatreList.isEmpty()){
            throw new APIException("No theatres found");
        }

        List<TheatreDTO> theatres=theatreList.stream().map
                (t->modelMapper.map(t,TheatreDTO.class)).toList();

        TheatreResponse theatreResponse=new TheatreResponse();
        theatreResponse.setContent(theatres);
        theatreResponse.setPageNumber(pageDetails.getPageNumber());
        theatreResponse.setPageSize(pageDetails.getPageSize());
        theatreResponse.setTotalPages(theatrePage.getTotalPages());
        theatreResponse.setTotalElements(theatrePage.getTotalElements());
        theatreResponse.setLastPage(theatrePage.isLast());
        return theatreResponse;
    }

    @Override
    public TheatreDTO getTheatreById(Long theatreId) {
        Theatre theatre=theatreRepository.findById(theatreId)
                .orElseThrow(()-> new ResourceNotFoundException("Theatre","theatreId",theatreId));
        return modelMapper.map(theatre,TheatreDTO.class);
    }

    @Override
    public TheatreDTO getTheatreByName(String theatreName) {
        Theatre theatre=theatreRepository.findByTheatreNameIgnoreCase(theatreName)
                .orElseThrow(()-> new ResourceNotFoundException("Theatre","theatreName",theatreName));
        return modelMapper.map(theatre,TheatreDTO.class);
    }

    @Override
    public TheatreResponse getTheatresByCity(String city,Integer pageNumber, Integer pageSize,String sortBy,String sortOrder) {
        Sort sortByAndOrder=sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                :Sort.by(sortBy).descending();
        Pageable pageDetails= PageRequest.of(pageNumber,pageSize,sortByAndOrder);
        Page<Theatre> theatrePage=theatreRepository.findByCityLikeIgnoreCase(pageDetails,city);

        List<Theatre> theatreList=theatrePage.getContent();
        if(theatreList.isEmpty()){
            throw new APIException("No theatres found");
        }


        List<TheatreDTO> theatres=theatreList.stream().map
                (t->modelMapper.map(t,TheatreDTO.class)).toList();

        TheatreResponse theatreResponse=new TheatreResponse();
        theatreResponse.setContent(theatres);
        theatreResponse.setPageNumber(pageDetails.getPageNumber());
        theatreResponse.setPageSize(pageDetails.getPageSize());
        theatreResponse.setTotalPages(theatrePage.getTotalPages());
        theatreResponse.setTotalElements(theatrePage.getTotalElements());
        theatreResponse.setLastPage(theatrePage.isLast());
        return theatreResponse;
    }

    @Override
    public TheatreResponse getTheatresByState(String state,Integer pageNumber, Integer pageSize,String sortBy,String sortOrder) {
        Sort sortByAndOrder=sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                :Sort.by(sortBy).descending();
        Pageable pageDetails= PageRequest.of(pageNumber,pageSize,sortByAndOrder);
        Page<Theatre> theatrePage=theatreRepository.findByStateLikeIgnoreCase(pageDetails,state);

        List<Theatre> theatreList=theatrePage.getContent();
        if(theatreList.isEmpty()){
            throw new APIException("No theatres found");
        }


        List<TheatreDTO> theatres=theatreList.stream().map
                (t->modelMapper.map(t,TheatreDTO.class)).toList();

        TheatreResponse theatreResponse=new TheatreResponse();
        theatreResponse.setContent(theatres);
        theatreResponse.setPageNumber(pageDetails.getPageNumber());
        theatreResponse.setPageSize(pageDetails.getPageSize());
        theatreResponse.setTotalPages(theatrePage.getTotalPages());
        theatreResponse.setTotalElements(theatrePage.getTotalElements());
        theatreResponse.setLastPage(theatrePage.isLast());
        return theatreResponse;
    }

    @Override
    public TheatreDTO updateTheatre(Long theatreId, TheatreDTO theatreDTO, Long theatreManagerId) {

        Theatre theatreFromDB=theatreRepository.findById(theatreId).orElse(null);
        if(theatreFromDB==null){
            throw new APIException("No theatre found");
        }

        if(theatreFromDB.getManager().getUserId()!=theatreManagerId){
            throw new APIException("You cant modify this theatre");
        }

        theatreFromDB.setTheatreName(theatreDTO.getTheatreName());
        theatreFromDB.setCity(theatreDTO.getCity());
        theatreFromDB.setState(theatreDTO.getState());
        theatreRepository.save(theatreFromDB);
        //resolve whether we can update screen from here or not

        return modelMapper.map(theatreFromDB,TheatreDTO.class);
    }

    @Override
    public TheatreDTO deleteTheatre(Long theatreId, Long theatreManagerId) {
        Theatre theatreFromDB=theatreRepository.findById(theatreId).orElse(null);
        if(theatreFromDB==null){
            throw new APIException("No theatre found");
        }
        if(theatreFromDB.getManager().getUserId()!=theatreManagerId){
            throw new APIException("You cant modify this theatre");
        }
        theatreRepository.deleteById(theatreId);
        return modelMapper.map(theatreFromDB,TheatreDTO.class);
    }
}
