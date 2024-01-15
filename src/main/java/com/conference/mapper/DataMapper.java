package com.conference.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.conference.dto.BookingDetails;
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
}
