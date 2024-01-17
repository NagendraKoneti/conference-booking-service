package com.conference.controler;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.conference.entity.BookingData;
import com.conference.exception.RoomBookingException;
import com.conference.service.BookingServiceImpl;

/**
 * 01/2024 
 * @author Nagendra
 */
@WebMvcTest(controllers = { BookingController.class })
public class BookingControllerTest {

    @Autowired	
    private MockMvc mockMvc;

    @MockBean
    private BookingServiceImpl bookingService;
    
	/**
	 * Test case : Book conference room with 5 participants. 
	 * Excepted Results : Return 201 Response code with booked conference room details.
	 * 
	 */
	@Test
    void bookConferenceRoom_ValidBooking_ReturnsBookedRoom() throws Exception {
        BookingData newBooking = createValidBooking();
        lenient().when(bookingService.bookConferenceRoom(any(),anyString())).thenReturn(newBooking);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/bookings/bookConferenceRoom")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"startTime\": \"16:00:00\", \"endTime\": \"17:00:00\", \"participants\": 2 }")
        		.header("loggedInUser", "nagendra"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.participants").value(5));
    }
	
	/**
	 * Test case    : Book conference room with 50 participants. 
	 * Excepted Results : Return 400 Bad Request with error message as No room available or exceeding room capacity.
	 * 
	 * @throws Exception
	 */
    @Test
    void bookConferenceRoom_NoRoom_Available_Or_ExcedingCapacity_ThrowsException() throws Exception {
    	when(bookingService.bookConferenceRoom(any(),anyString())).thenThrow(new RoomBookingException("No room available or exceeding room capacity."));
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/bookings/bookConferenceRoom")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"startTime\": \"14:00:00\"	, \"endTime\": \"15:00:00\", \"participants\": 5 }")
                .header("loggedInUser", "nagendra"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation failed"))
                .andExpect(jsonPath("$.message").value("No room available or exceeding room capacity."));
    }
    
    /**
     * Test case    : Try without time slots to book conference room
     * Excepted Results : Return 400 Bad Request with error as Execution failed its Validation failed for argument
     * @throws Exception
     */
    @Test
    void bookConferenceRoom_MaintenancePeriod_ThrowsException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/bookings/bookConferenceRoom")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"startTime\": \"09:00:00\", \"endTime\": \"09:30:00\", \"participants\": 2 }")
                .header("loggedInUser", "nagendra"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Execution failed"));                
    }
    /**
     * Test Scenario    : Try without time slots to book conference room ( Validation failed )
     * Excepted Results : Return 400 Bad Request with error as Execution failed its Validation failed for argument
     * @throws Exception
     */
    @Test
    void bookConferenceRoom_Without_TimeSloats_ThrowsException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/bookings/bookConferenceRoom")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"participants\": 5 }")
                .header("loggedInUser", "nagendra"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Execution failed"));                
    }
    
    /**
     * Test Scenario    : Try without participants to book conference room ( Validation failed )
     * Excepted Results : Return 400 Bad Request with error as Execution failed its Validation failed for argument
     * @throws Exception
     */
    @Test
    void bookConferenceRoom_Without_Participants_ThrowsException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/bookings/bookConferenceRoom")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"startTime\": \"14:00:00\" , \"endTime\": \"15:00:00\" }")
                .header("loggedInUser", "nagendra"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Execution failed"));                
    }

    private BookingData createValidBooking() {
        BookingData BookingData = new BookingData();
        BookingData.setId(1L);
        BookingData.setStartTime(LocalTime.parse("12:00:00"));
        BookingData.setEndTime(LocalTime.parse("13:00:00"));
        BookingData.setParticipants(5);
        return BookingData;
    }
}