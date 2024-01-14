package com.conference.controler;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import com.conference.entity.BookingData;
import com.conference.exception.RoomBookingException;
import com.conference.service.BookingService;
import com.conference.util.AppProperties;
import com.conference.util.MaintenancePeriodConfig;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = "classpath:data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class BookingControllerTest {

    @Autowired	
    private MockMvc mockMvc;

    @Mock
    private BookingService bookingService;
    
    @Mock
    private AppProperties appProperties;
    
    
    @BeforeEach
    public void setProperties() {
    	lenient().when(appProperties.getMaintenancePeriods()).thenReturn(getMaintenancePeriods());
    }
    
	@Test
    void bookConferenceRoom_ValidBooking_ReturnsBookedRoom() throws Exception {
        BookingData newBooking = createValidBooking();
        lenient().when(bookingService.bookConferenceRoom(any())).thenReturn(newBooking);
        mockMvc.perform(post("/bookings/bookConferenceRoom")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"roomId\": 1, \"startTime\": \"17:00:00\", \"endTime\": \"1:00:00\", \"participants\": 2 }"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.participants").value(2));
    }

    @Test
    void bookConferenceRoom_InvalidRoom_ThrowsException() throws Exception {
    	lenient(). when(bookingService.bookConferenceRoom(any())).thenThrow(new RoomBookingException("Invalid room or exceeding room capacity."));
        mockMvc.perform(post("/bookings/bookConferenceRoom")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"roomId\": 1, \"startTime\": \"14:00:00\"	, \"endTime\": \"15:00:00\", \"participants\": 5 }"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation failed"))
                .andExpect(jsonPath("$.message").value("Invalid room or exceeding room capacity."));
    }

    @Test
    void bookConferenceRoom_InvalidRequestBody_ThrowsException() throws Exception {
        mockMvc.perform(post("/bookings/bookConferenceRoom")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"roomId\": 1, \"participants\": 5 }"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Execution failed"));
                /*.andExpect(jsonPath("$.message").value("Start time cannot be null"));*/
    }

    private List<MaintenancePeriodConfig> getMaintenancePeriods() {
    	List<MaintenancePeriodConfig> maintenancePeriods = new ArrayList<>();
    	MaintenancePeriodConfig maintenancePeriod = new MaintenancePeriodConfig();
    	maintenancePeriod.setStartTime(LocalTime.of(10,00));
    	maintenancePeriod.setEndTime(LocalTime.of(11,00));
    	maintenancePeriods.add(maintenancePeriod);
		return maintenancePeriods;
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