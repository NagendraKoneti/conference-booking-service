package com.conference.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.conference.dto.BookingDetails;
import com.conference.dto.BookingResponse;
import com.conference.dto.ConferenceDetails;
import com.conference.entity.BookingData;
import com.conference.entity.ConferenceRoomData;

@ExtendWith(MockitoExtension.class)
public class DataMapperTest {
	
	@InjectMocks
	private DataMapper dataMapper;

	@Test
	public void testMapConferenceRoomDetails() {
		ConferenceRoomData conferenceRoomData = new ConferenceRoomData();
		conferenceRoomData.setConferenceRoomId(1L);
		conferenceRoomData.setName("Amaze");
		conferenceRoomData.setMaxCapacity(10);
		List<ConferenceDetails> conferenceDetailsList = dataMapper.mapConferenceRoomDetails(Collections.singletonList(conferenceRoomData));
		assertNotNull(conferenceDetailsList);
		assertEquals(1, conferenceDetailsList.size());
		assertEquals("Amaze", conferenceDetailsList.get(0).getName());
		assertEquals(1, conferenceDetailsList.get(0).getId());
		assertEquals(10, conferenceDetailsList.get(0).getMaxCapacity());
	}

	@Test
	public void testMapToBookingDataEntity() {
		String loggedInUser = "TestUser";
		BookingDetails bookingDetails = new BookingDetails();		
		bookingDetails.setStartTime(LocalTime.of(10,00));
		bookingDetails.setEndTime(LocalTime.of(11,00));
		bookingDetails.setParticipants(10);
		
		ConferenceRoomData conferenceRoom = new ConferenceRoomData();
		conferenceRoom.setConferenceRoomId(1l);
		conferenceRoom.setMaxCapacity(20);
		conferenceRoom.setName("Amaze");
		conferenceRoom.setCreationDate(LocalDateTime.now());
		
		BookingData bookingData = dataMapper.mapToBookingDataEntity(bookingDetails, loggedInUser, conferenceRoom);
		assertEquals(10, bookingData.getParticipants());
		assertEquals("Amaze", bookingData.getConferenceRoom().getName());
		assertEquals(10, bookingData.getParticipants());
		assertEquals(10, bookingData.getStartTime().getHour());
		assertEquals(11, bookingData.getEndTime().getHour());
	}

	@Test
	public void testMapBookingDataToBookingResponse() {
		ConferenceRoomData conferenceRoom = new ConferenceRoomData();
		conferenceRoom.setConferenceRoomId(1l);
		conferenceRoom.setMaxCapacity(20);
		conferenceRoom.setName("Amaze");
		conferenceRoom.setCreationDate(LocalDateTime.now());
		
		BookingData confirmedBookingInfo = new BookingData();
		confirmedBookingInfo.setCreationDate(LocalDateTime.now());
		confirmedBookingInfo.setStartTime(LocalTime.of(10, 00));
		confirmedBookingInfo.setEndTime(LocalTime.of(11,00));
		confirmedBookingInfo.setParticipants(10);
		confirmedBookingInfo.setStatus("Booked");
		confirmedBookingInfo.setConferenceRoom(conferenceRoom);
		
		BookingResponse bookingResponse = dataMapper.mapBookingDataToBookingResponse(confirmedBookingInfo);
		assertEquals(20, bookingResponse.getConferenceRoomMaxCapacity());
		assertEquals("Amaze", bookingResponse.getConferenceRoomName());
		assertEquals(10, bookingResponse.getParticipants());
		assertEquals("Booked", bookingResponse.getStatus());
		
	}

}
