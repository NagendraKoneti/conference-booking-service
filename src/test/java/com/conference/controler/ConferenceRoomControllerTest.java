package com.conference.controler;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.conference.dto.ConferenceDetails;
import com.conference.exception.ErrorCodes;
import com.conference.exception.RoomBookingException;
import com.conference.service.ConferenceRoomServiceImpl;	


/**
 * 01/2024 
 * @author Nagendra
 */
@WebMvcTest(controllers = { ConferenceRoomController.class})
public class ConferenceRoomControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ConferenceRoomServiceImpl conferenceRoomService;
	
	/**
	 * Test case : Get available rooms for 14:00 to 15:00 time slot
	 * 
	 * Return the available Rooms list for the requested slot 
	 */
	@Test
	void getAvailableRooms_ValidRequest_Returns_AvailableRooms() throws Exception {
		when(conferenceRoomService.getAvailableRooms(any(), any()))
				.thenReturn(Collections.singletonList(createValidRoom()));

		mockMvc.perform(get("/api/v1/conference-rooms").param("startTime", "13:00")
				.param("endTime", "14:00").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(1)) 
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].name").value("Amaze"))
                .andExpect(jsonPath("$.data[0].maxCapacity").value(2));
	}
	
	/**
	 * Test case : Get available rooms for 13:00 to 14:00 time slot
	 * 
	 * Return the available Rooms list for the requested slot 
	 */
	@Test
	void getAvailableRooms_Maintenance_Returns_NoAvailableRooms() throws Exception {
		when(conferenceRoomService.getAvailableRooms(any(),any())).thenThrow(new RoomBookingException(ErrorCodes.UNDER_MAINTENANCE_EXC.name(),ErrorCodes.UNDER_MAINTENANCE_EXC.getErrorMessage()));
		mockMvc.perform(get("/api/v1/conference-rooms").param("startTime", "13:00")
				.param("endTime", "14:00").contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value("error"))
        .andExpect(jsonPath("$.data.errorCode").value(ErrorCodes.UNDER_MAINTENANCE_EXC.name()))
        .andExpect(jsonPath("$.data.errorDetails").value(ErrorCodes.UNDER_MAINTENANCE_EXC.getErrorMessage()));
	}
	
	/**
	 * Test case : Get available rooms for 13:00 to 14:00 time slot
	 * 
	 * Return the no available Rooms for the requested slot 
	 */
	@Test
	void getAvailableRooms_Returns_NoAvailableRooms() throws Exception {
		when(conferenceRoomService.getAvailableRooms(any(),any())).thenThrow(new RoomBookingException(ErrorCodes.ALL_ROOMS_BOOKED.name(),ErrorCodes.ALL_ROOMS_BOOKED.getErrorMessage()));
		mockMvc.perform(get("/api/v1/conference-rooms").param("startTime", "13:00")
				.param("endTime", "14:00").contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value("error"))
        .andExpect(jsonPath("$.data.errorCode").value(ErrorCodes.ALL_ROOMS_BOOKED.name()))
        .andExpect(jsonPath("$.data.errorDetails").value(ErrorCodes.ALL_ROOMS_BOOKED.getErrorMessage()));
	}
	
	

	private ConferenceDetails createValidRoom() {
		ConferenceDetails room = new ConferenceDetails();
		room.setId(1L);
		room.setName("Amaze");
		room.setMaxCapacity(2);
		return room;
	}

}
