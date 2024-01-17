package com.conference.repo;

import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.conference.entity.BookingData;

/**
 * 01/2024 
 * @author Nagendra
 */
public interface BookingRepository extends JpaRepository<BookingData, Long> {
    List<BookingData> findByConferenceRoom_ConferenceRoomIdAndEndTimeAfterAndStartTimeBefore(Long roomId, LocalTime start, LocalTime end);
}

