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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.conference.dto.BookingResponse;
import com.conference.exception.ErrorCodes;
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
	 * Test case : Book conference room with 18 participants. 
	 * Excepted Results : Return 201 Response code with booked conference room details.
	 * 
	 */
	@Test
    void bookConferenceRoom_ValidBooking_ReturnsBookedRoom() throws Exception {
		BookingResponse newBooking = createValidBooking();
        lenient().when(bookingService.bookConferenceRoom(any(),anyString())).thenReturn(newBooking);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/bookings/bookConferenceRoom")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"startTime\": \"16:00:00\", \"endTime\": \"17:00:00\", \"participants\": 18 }")
        		.header("loggedInUser", "nagendra"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data.conferenceRoomName").value("Strive"))
                .andExpect(jsonPath("$.data.conferenceRoomMaxCapacity").value(20))                
                .andExpect(jsonPath("$.data.startTime").value("16:00:00"))
                .andExpect(jsonPath("$.data.endTime").value("17:00:00"))
                .andExpect(jsonPath("$.data.participants").value(18))
                .andExpect(jsonPath("$.data.status").value("Booked"));
    }
	
	/**
	 * Test case    : Book conference room with 5 participants/ No Rooms free to book  
	 * Excepted Results : Return 400 Bad Request with error message as No room available or exceeding room capacity.
	 * 
	 * @throws Exception
	 */
    @Test
    void bookConferenceRoom_NoRoom_Available_Or_ExcedingCapacity_ThrowsException() throws Exception {
    	when(bookingService.bookConferenceRoom(any(),anyString())).thenThrow(new RoomBookingException(ErrorCodes.ALL_ROOMS_BOOKED.name(),ErrorCodes.ALL_ROOMS_BOOKED.getErrorMessage()));
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/bookings/bookConferenceRoom")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"startTime\": \"14:00:00\"	, \"endTime\": \"15:00:00\", \"participants\": 5 }")
                .header("loggedInUser", "nagendra"))
		        .andExpect(status().isOk())
		        .andExpect(jsonPath("$.status").value("error"))
		        .andExpect(jsonPath("$.data.errorCode").value(ErrorCodes.ALL_ROOMS_BOOKED.name()))
		        .andExpect(jsonPath("$.data.errorDetails").value(ErrorCodes.ALL_ROOMS_BOOKED.getErrorMessage()));
    }
    
    /**
	 * Test case    : Book conference room when rooms under maintains period (9 am - 10 am)
	 * Excepted Results : Return 400 Bad Request with error message as No room available or exceeding room capacity.
	 * 
	 * @throws Exception
	 */
    @Test
    void bookConferenceRoom_When_Maintains_Period_ThrowsException() throws Exception {
    	when(bookingService.bookConferenceRoom(any(),anyString())).thenThrow(new RoomBookingException(ErrorCodes.UNDER_MAINTENANCE_EXC.name(),ErrorCodes.UNDER_MAINTENANCE_EXC.getErrorMessage()));
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/bookings/bookConferenceRoom")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"startTime\": \"09:00:00\"	, \"endTime\": \"10:00:00\", \"participants\": 5 }")
                .header("loggedInUser", "nagendra"))
		        .andExpect(status().isOk())
		        .andExpect(jsonPath("$.status").value("error"))
		        .andExpect(jsonPath("$.data.errorCode").value(ErrorCodes.UNDER_MAINTENANCE_EXC.name()))
		        .andExpect(jsonPath("$.data.errorDetails").value(ErrorCodes.UNDER_MAINTENANCE_EXC.getErrorMessage()));
    }
    
    /**
   	 * Test case    : Book conference room when rooms under maintains period (9 am - 10 am)
   	 * Excepted Results : Return 400 Bad Request with error message as No room available or exceeding room capacity.
   	 * 
   	 * @throws Exception
   	 */
       @Test
       void bookConferenceRoom_Afternoon_For_Morning_Slots_ThrowsException() throws Exception {
    	   when(bookingService.bookConferenceRoom(any(),anyString())).thenThrow(new RoomBookingException(ErrorCodes.BOOKING_FOR_FUTURE_SLOTS.name(),ErrorCodes.BOOKING_FOR_FUTURE_SLOTS.getErrorMessage()));
           mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/bookings/bookConferenceRoom")
                   .contentType(MediaType.APPLICATION_JSON)
                   .content("{ \"startTime\": \"09:00:00\"	, \"endTime\": \"10:00:00\", \"participants\": 5 }")
                   .header("loggedInUser", "nagendra"))
			       .andExpect(status().isOk())
			       .andExpect(jsonPath("$.status").value("error"))
			       .andExpect(jsonPath("$.data.errorCode").value(ErrorCodes.BOOKING_FOR_FUTURE_SLOTS.name()))
			       .andExpect(jsonPath("$.data.errorDetails").value(ErrorCodes.BOOKING_FOR_FUTURE_SLOTS.getErrorMessage()));
       }
    
    /**
     * Test case        : Try to book conference room with 10 mints intervals 
     * Excepted Results : Return 400 Bad Request with error as Execution failed its Validation failed for argument
     * @throws Exception
     */
    @Test
    void bookConferenceRoom_With_10Minuts_Intervals_ThrowsException() throws Exception {
    	when(bookingService.bookConferenceRoom(any(),anyString())).thenThrow(new RoomBookingException(ErrorCodes.INCORRECT_BOOKING_INTERVALS.name(),ErrorCodes.INCORRECT_BOOKING_INTERVALS.getErrorMessage()));
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/bookings/bookConferenceRoom")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"startTime\": \"10:10:00\", \"endTime\": \"10:30:00\", \"participants\": 2 }")
                .header("loggedInUser", "nagendra"))
	       		.andExpect(status().isOk())
	       		.andExpect(jsonPath("$.status").value("error"))
	       		.andExpect(jsonPath("$.data.errorCode").value(ErrorCodes.INCORRECT_BOOKING_INTERVALS.name()))
	       		.andExpect(jsonPath("$.data.errorDetails").value(ErrorCodes.INCORRECT_BOOKING_INTERVALS.getErrorMessage()));
    }
    /**
     * Test Scenario    : Try without time slots to book conference room ( Validation failed )
     * Excepted Results : Return 400 Bad Request
     * @throws Exception
     */
    @Test
    void bookConferenceRoom_Without_TimeSloats_ThrowsException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/bookings/bookConferenceRoom")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"participants\": 5 }")
                .header("loggedInUser", "nagendra"))
                .andExpect(status().isBadRequest());               
    }
    
    /**
     * Test Scenario    : Try without participants to book conference room ( Validation failed )
     * Excepted Results : Return 400 Bad Request
     * @throws Exception
     */
    @Test
    void bookConferenceRoom_Without_Participants_ThrowsException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/bookings/bookConferenceRoom")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"startTime\": \"14:00:00\" , \"endTime\": \"15:00:00\" }")
                .header("loggedInUser", "nagendra"))
                .andExpect(status().isBadRequest());
               
    }

    private BookingResponse createValidBooking() {
    	BookingResponse bookingResponse = new BookingResponse();
    	bookingResponse.setStartTime(LocalTime.parse("16:00:00"));
    	bookingResponse.setEndTime(LocalTime.parse("17:00:00"));
    	bookingResponse.setConferenceRoomMaxCapacity(20);
    	bookingResponse.setStatus("Booked");
    	bookingResponse.setConferenceRoomName("Strive");
    	bookingResponse.setParticipants(18);
        return bookingResponse;
    }
}