package com.conference.service;

import java.time.LocalTime;
import java.util.List;

import com.conference.dto.ConferenceDetails;
import com.conference.entity.ConferenceRoomData;

/**
 * 01/2024 
 * @author Nagendra
 */
public interface ConferenceRoomService {

	/**
	 * This method should return a list of available meeting rooms based on the specified time range. 
	 *  step 1:  It retrieves all conference rooms from the database
	 *  step 2:  It then filters out the rooms that are already booked during the specified time range 
	 *  
	 * @param startTime : meeting start time. 
	 * @param endTime   : meeting end time.
	 * @return List of rooms that are available during the specified time range.
	 */
	List<ConferenceDetails> getAvailableRooms(LocalTime startTime, LocalTime endTime);

	/**
	 * This method will find conference room by id
	 * @param roomId
	 * @return
	 */
	ConferenceRoomData getRoomById(Long roomId);

}
