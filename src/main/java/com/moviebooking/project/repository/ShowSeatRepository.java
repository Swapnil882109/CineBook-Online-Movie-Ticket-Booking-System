package com.moviebooking.project.repository;

import com.moviebooking.project.model.SeatStatus;
import com.moviebooking.project.model.ShowSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShowSeatRepository extends JpaRepository<ShowSeat,Long> {

    Long countByShowShowIdAndSeatStatus(Long showId, SeatStatus seatStatus);
}
