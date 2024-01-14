package com.conference.repo;

import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.conference.entity.BookingData;

public interface BookingRepository extends JpaRepository<BookingData, Long> {
    List<BookingData> findByIdAndEndTimeAfterAndStartTimeBefore(Long roomId, LocalTime start, LocalTime end);
}

