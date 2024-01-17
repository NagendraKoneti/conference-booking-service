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
	private ConferenceRoomServiceImpl roomService;

	@Test
	void getAvailableRooms_ValidRequest_ReturnsAvailableRooms() throws Exception {
		when(roomService.getAvailableRooms(any(), any()))
				.thenReturn(Collections.singletonList(createValidRoom()));

		mockMvc.perform(get("/api/v1/conference-rooms").param("startTime", "13:00")
				.param("endTime", "14:00").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$[0].id").value(1L))
				.andExpect(jsonPath("$[0].maxCapacity").value(10));
	}

	private ConferenceDetails createValidRoom() {
		ConferenceDetails room = new ConferenceDetails();
		room.setId(1L);
		room.setMaxCapacity(10);
		return room;
	}

}
