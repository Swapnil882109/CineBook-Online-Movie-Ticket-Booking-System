package com.moviebooking.project.services;

import com.moviebooking.project.Payload.DTOs.MovieDTO;
import com.moviebooking.project.Payload.Response.MovieResponse;
import com.moviebooking.project.exception.APIException;
import com.moviebooking.project.exception.ResourceNotFoundException;
import com.moviebooking.project.model.Movie;
import com.moviebooking.project.repository.MovieRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class MovieServiceImpl implements MovieService {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    FileService fileService;

    @Value("${project.image}")
    private String path;

    @Override
    public MovieDTO createMovie(MovieDTO movieDTO) {
        Movie movie=modelMapper.map(movieDTO,Movie.class);
        Optional<Movie> movieFromDB=movieRepository.findByMovieName(movie.getMovieName());
        if(movieFromDB.isPresent()){
            throw new APIException("Movie already exists");
        }
        Movie savedMovie=movieRepository.save(movie);
        return modelMapper.map(savedMovie,MovieDTO.class);
    }

    @Override
    public MovieResponse getAllMovies(Integer pageNumber, Integer pageSize,String sortBy,String sortOrder) {
        Sort sortByAndOrder=sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                :Sort.by(sortBy).descending();

        Pageable pageDetails= PageRequest.of(pageNumber,pageSize,sortByAndOrder);
        Page<Movie> moviePage=movieRepository.findAll(pageDetails);
        List<Movie> movies=moviePage.getContent();
        if(movies.size()==0){
            throw new APIException("No movies found");
        }

        List<MovieDTO> movieDTOS=movies.stream().map(m->modelMapper
                .map(m,MovieDTO.class)).toList();

        MovieResponse movieResponse=new MovieResponse();
        movieResponse.setContent(movieDTOS);
        movieResponse.setPageNumber(pageDetails.getPageNumber());
        movieResponse.setPageSize(pageDetails.getPageSize());
        movieResponse.setTotalPages(moviePage.getTotalPages());
        movieResponse.setTotalElements(moviePage.getTotalElements());
        movieResponse.setLastPage(moviePage.isLast());
        return movieResponse;
    }

    @Override
    public MovieDTO getMovieById(Long movieId) {
         Movie movie= movieRepository.findById(movieId)
                 .orElseThrow(()-> new ResourceNotFoundException("Movie","movieId",movieId)
                 );
        return modelMapper.map(movie,MovieDTO.class);
    }


    @Override
    public List<MovieDTO> getMoviesByKeyword(String keyword) {
        List<MovieDTO> movieDTOList=movieRepository
                .findByMovieNameLikeIgnoreCase("%" + keyword + "%")
                .stream()
                .map(movie -> modelMapper.map(movie, MovieDTO.class))
                .toList();
        if(movieDTOList.isEmpty()){
            throw new APIException("No movies found");
        }

        return movieDTOList;
    }



    @Override
    public MovieDTO updateMovie(Long movieId,MovieDTO movieDTO) {
        Movie movieFromDB=movieRepository.findById(movieId)
                .orElseThrow(()-> new ResourceNotFoundException("Movie","movieId",movieId)
        );
        movieFromDB.setMovieName(movieDTO.getMovieName());
        movieFromDB.setMovieDescription(movieDTO.getMovieDescription());
        movieFromDB.setMovieRating(movieDTO.getMovieRating());
        movieFromDB.setMovieImage(movieDTO.getMovieImage());
        movieFromDB.setMovieTags(movieDTO.getMovieTags());
        movieFromDB.setMovieActors(movieDTO.getMovieActors());

        movieRepository.save(movieFromDB);
        return modelMapper.map(movieFromDB,MovieDTO.class);
    }

    @Override
    public MovieDTO deleteMovie(Long id) {
        Movie movie=movieRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Movie","movieId",id));
        movieRepository.delete(movie);
        return modelMapper.map(movie,MovieDTO.class);
    }

    @Override
    public MovieDTO updateMovieImage(Long movieId, MultipartFile image) throws IOException {
        Movie movieFromDB=movieRepository.findById(movieId)
                .orElseThrow(()->new ResourceNotFoundException("Movie","movieId",movieId));

        String fileName=fileService.uploadImage(path,image);

        //updating the new file name to the product
        movieFromDB.setMovieImage(fileName);

        //save updated product
        Movie updatedMovie=movieRepository.save(movieFromDB);

        //return DTO after mapping product to DTO
        return modelMapper.map(updatedMovie,MovieDTO.class);
    }
}
