package com.moviebooking.project.repository;

import com.moviebooking.project.model.Screen;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ScreenRepository extends JpaRepository<Screen,Long> {
    @Query("SELECT s FROM Screen s WHERE s.theatre.theatreId = :theatreId")
    Page<Screen> findWhereTheatreId(@Param("theatreId") Long theatreId, Pageable pageable);

    @Query("SELECT s FROM Screen s WHERE s.theatre.theatreId = :theatreId AND s.screenId=:screenId")
    Screen findWhereTheatreId(@Param("theatreId") Long theatreId,@Param("screenId") Long screenId);
}
