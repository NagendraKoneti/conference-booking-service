package com.conference.controler;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalTime;
import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.conference.entity.BookingData;
import com.conference.entity.ConferenceRoom;
import com.conference.service.BookingService;
import com.conference.service.ConferenceRoomService;	

@WebMvcTest(controllers = { ConferenceRoomController.class, BookingController.class })
public class ConferenceRoomControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ConferenceRoomService roomService;

	@MockBean
	private BookingService bookingService;

	@Test
	void getAvailableRooms_ValidRequest_ReturnsAvailableRooms() throws Exception {
		LocalTime startTime = LocalTime.now();
		LocalTime endTime = LocalTime.of(LocalTime.now().getHour()+1,00);
		when(roomService.getAvailableRooms(startTime, endTime))
				.thenReturn(Collections.singletonList(createValidRoom()));

		mockMvc.perform(get("/conference-rooms/available").param("startTime", startTime.toString())
				.param("endTime", endTime.toString()).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$[0].id").value(1L))
				.andExpect(jsonPath("$[0].maxCapacity").value(10));
	}

	@Test
	void bookConferenceRoom_ValidBooking_ReturnsBookedRoom() throws Exception {
		BookingData newBooking = createValidBooking();
		when(bookingService.bookConferenceRoom(any())).thenReturn(newBooking);

		mockMvc.perform(post("/bookings/bookConferenceRoom").contentType(MediaType.APPLICATION_JSON).content(
				"{ \"roomId\": 1, \"startTime\": \"2023-01-01T12:00:00\", \"endTime\": \"2023-01-01T13:00:00\", \"participants\": 5 }"))
				.andExpect(status().isOk()).andExpect(jsonPath("$.id").value(1L))
				.andExpect(jsonPath("$.roomId").value(1L)).andExpect(jsonPath("$.participants").value(5));
	}

	private ConferenceRoom createValidRoom() {
		ConferenceRoom room = new ConferenceRoom();
		room.setId(1L);
		room.setMaxCapacity(10);
		return room;
	}

	private BookingData createValidBooking() {
		BookingData booking = new BookingData();
		booking.setId(1L);
		booking.setId(1L);
		booking.setStartTime(LocalTime.parse("12:00:00"));
		booking.setEndTime(LocalTime.parse("13:00:00"));
		booking.setParticipants(5);
		return booking;
	}
}
