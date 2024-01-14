package com.conference.service;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.conference.entity.ConferenceRoom;
import com.conference.repo.ConferenceRoomRepository;

@Service
public class ConferenceRoomService {
	
	private static final Logger logger = LoggerFactory.getLogger(ConferenceRoomService.class);
	
    @Autowired
    private ConferenceRoomRepository roomRepository;
    @Autowired
    private BookingService bookingService;

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
		logger.info("Started finding getAvailableRooms slots : {} - {} ",startTime,endTime);
		List<ConferenceRoom> allRooms = roomRepository.findAll();
		logger.info(" Total no of rooms :{} ",allRooms.size());
		List<ConferenceRoom> bookedRooms = allRooms.stream()
				.filter(room -> bookingService.isRoomBooked(room.getId(), startTime, endTime))
				.collect(Collectors.toList());
		logger.info(" Total no of rooms booked for this slot :{} ",bookedRooms.size());
		allRooms.removeAll(bookedRooms);
		
		return allRooms;
	}
	/**
	 * This method will find conference 
	 * @param roomId
	 * @return
	 */
	public ConferenceRoom getRoomById(Long roomId) {
        return roomRepository.findById(roomId).orElse(null);
    }

}