package com.moviebooking.project.repository;

import com.moviebooking.project.model.Show;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;

@Repository
public interface ShowRepository extends JpaRepository<Show,Long> {
    @Query("""
    SELECT COUNT(s) > 0
    FROM Show s
    WHERE s.screen.screenId = :screenId
      AND s.showDate = :showDate
      AND (s.startTime < :endTime AND s.endTime > :startTime)
""")
    boolean findWhereStartTimeOrEndTimeExists(
            @Param("screenId") Long screenId,
            @Param("showDate") LocalDate showDate,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime
    );


    @Query("SELECT s FROM Show s WHERE s.screen.theatre.theatreId = :theatreId")
    Page<Show> findByTheatreId(@Param("theatreId") Long theatreId, Pageable pageable);

    @Query("SELECT s FROM Show s WHERE s.movie.movieId = :movieId")
    Page<Show> findByMovieId(@Param("movieId") Long movieId, Pageable pageable);

    @Query("""
    SELECT COUNT(s) > 0
    FROM Show s
    WHERE s.screen.screenId = :screenId
      AND s.showDate = :showDate
      AND (s.startTime < :endTime AND s.endTime > :startTime)
      AND (s.showId != :showId)
""")
    boolean findWhereStartTimeOrEndTimeExistsExceptCurrent(@Param("screenId") Long screenId,
                                                           @Param("showId") Long showId,
                                                           @Param("showDate") LocalDate showDate,
                                                           @Param("startTime") LocalTime startTime,
                                                           @Param("endTime") LocalTime endTime);
}
