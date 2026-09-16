package com.moviebooking.project.repository;

import com.moviebooking.project.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Query("SELECT b FROM Booking b where b.user.userId=:userId")
    List<Booking> findWhereUserId(@Param("userId") Long userId);


}
