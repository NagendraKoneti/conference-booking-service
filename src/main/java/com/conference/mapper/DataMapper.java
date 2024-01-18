package com.conference.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.conference.dto.BookingDetails;
import com.conference.dto.BookingResponse;
import com.conference.dto.ConferenceDetails;
import com.conference.entity.BookingData;
import com.conference.entity.ConferenceRoomData;
import com.conference.util.ConferenceConstants;

/**
 * @author Nagendra
 */

@Component
public class DataMapper {

	/**	
	 * Map the repository data to DTO to share with front end.
	 * @param allRooms
	 * @return
	 */
	public List<ConferenceDetails> mapConferenceRoomDetails(List<ConferenceRoomData> allRooms) {
		return allRooms.stream().map(room -> new ConferenceDetails(room.getConferenceRoomId(), room.getName(), room.getMaxCapacity()))
				.collect(Collectors.toList());
	}
	/**
	 * This method will provide the mapping of BookingDetails to BookingData Entity
	 * 
	 * @param bookingDetails : User provided booking details to book conference room
	 * @param loggedInUser : Logged-in user 
	 * @param availableRoom : Available conference room to block for requested user
	 * @return
	 */
	public BookingData mapToBookingDataEntity(BookingDetails bookingDetails, String loggedInUser, ConferenceRoomData availableRoom) {
		  BookingData bookingData = new BookingData();
		  bookingData.setStartTime(bookingDetails.getStartTime());
		  bookingData.setEndTime(bookingDetails.getEndTime());
		  bookingData.setOrganizer(loggedInUser);
		  bookingData.setConferenceRoom(availableRoom);
		  bookingData.setStatus(ConferenceConstants.BOOKED);
		  bookingData.setParticipants(bookingDetails.getParticipants());
		  return bookingData;		
		}
	
	/**
	 * This mapping is to convert the confirmed booking information into dto to share consumer
	 * @param confirmedBooking : confirmed booking information as per the request
	 * @return BookingResponse : return mapped response 
	 */
	public BookingResponse mapBookingDataToBookingResponse(BookingData confirmedBooking) {
		BookingResponse bookingResponse = new BookingResponse();
		bookingResponse.setConferenceRoomMaxCapacity(confirmedBooking.getConferenceRoom().getMaxCapacity());
		bookingResponse.setConferenceRoomName(confirmedBooking.getConferenceRoom().getName());
		bookingResponse.setParticipants(confirmedBooking.getParticipants());
		bookingResponse.setStatus(confirmedBooking.getStatus());
		bookingResponse.setStartTime(confirmedBooking.getStartTime());
		bookingResponse.setEndTime(confirmedBooking.getEndTime());
		return bookingResponse;
	}
}
