package com.conference.service;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.conference.entity.ConferenceRoom;
import com.conference.repo.ConferenceRoomRepository;

@Service
public class ConferenceRoomService {
    @Autowired
    private ConferenceRoomRepository roomRepository;
    @Autowired
    private BookingService bookingService;

    public List<ConferenceRoom> getAllRooms() {
        return roomRepository.findAll();
    }

    public ConferenceRoom getRoomById(Long roomId) {
        return roomRepository.findById(roomId).orElse(null);
    }
    
    /**
     * This method should return a list of available meeting rooms based on the specified time range. 
     *  step 1:  It retrieves all conference rooms from the database
     *  step 2:  It then filters out the rooms that are already booked during the specified time range 
     *  
     * @param startTime : meeting start time. 
     * @param endTime   : meeting end time.
     * @return List of rooms that are available during the specified time range.
     */
    public List<ConferenceRoom> getAvailableRooms(LocalTime startTime, LocalTime endTime) {
        List<ConferenceRoom> allRooms = roomRepository.findAll();

        List<ConferenceRoom> bookedRooms = allRooms.stream()
                .filter(room -> bookingService.isRoomBooked(room.getId(), startTime, endTime))
                .collect(Collectors.toList());

        allRooms.removeAll(bookedRooms);

        return allRooms;
    }

}