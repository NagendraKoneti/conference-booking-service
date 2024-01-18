package com.conference.service;

import java.time.LocalTime;

import com.conference.dto.BookingDetails;
import com.conference.dto.BookingResponse;
import com.conference.entity.BookingData;

/**
 * 01/2024 
 * @author Nagendra
 */
public interface BookingService {

	/**
	 * This method will book conference room after pass through all controls
	 * 	 a) Validate booking time interval (15 minutes interval)
	 *   b) Validate room capacity
	 *   c) Check if the requested time slot is during maintenance
	 *   d) Check if the requested time slot is available (First Come First Serve)
	 *   e) Check if booking is for the current date
	 *   
	 * @param bookingDetails
	 * @param loggedInUser 
	 * @return booking information
	 */
	BookingResponse bookConferenceRoom(BookingDetails bookingDetails, String loggedInUser);

	/**
	 * This method has a capable of verifies availability of the room for requested time period
	 * 
	 * @param roomId
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	boolean isRoomBooked(Long roomId, LocalTime startTime, LocalTime endTime);

	/**
	 * Verify the booking is requested for todays time slot
	 * @param localTime
	 */
	void validateBookingForCurrentDate(LocalTime startTime);

}
