package com.moviebooking.project.repository;


import com.moviebooking.project.model.Theatre;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TheatreRepository extends JpaRepository<Theatre,Long> {

    Page<Theatre> findByCityLikeIgnoreCase(Pageable pageDetails, String city);

    Page<Theatre> findByStateLikeIgnoreCase(Pageable pageDetails, String state);

    Optional<Theatre> findByTheatreNameIgnoreCase(String theatreName);

    @Query("select t from Theatre t where t.theatreName=:theatreName AND t.city=:theatreCity AND t.state=:theatreState AND t.country=:theatreCountry")
    Theatre findWhereTheatreIdCityStateAndCountry(@Param("theatreName") String theatreName,@Param("theatreCity") String theatreCity,
                                                  @Param("theatreState")String theatreState,@Param("theatreCountry") String  theatreCountry);
}
