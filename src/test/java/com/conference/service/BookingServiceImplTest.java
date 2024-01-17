package com.conference.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.conference.dto.BookingDetails;
import com.conference.entity.BookingData;
import com.conference.entity.ConferenceRoomData;
import com.conference.exception.RoomBookingException;
import com.conference.mapper.DataMapper;
import com.conference.repo.BookingRepository;
import com.conference.repo.ConferenceRoomRepository;
import com.conference.util.MaintenancePeriodConfig;

/**
 * 01/2024 
 * @author Nagendra
 */
@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

	private static final String LOGGED_IN_USER = "loggedInUser";

	@Mock
    private BookingRepository bookingRepository;
	
	@Mock
    private ConferenceRoomRepository conferenceRoomRepository;

    @Mock
    private ConferenceRoomServiceImpl conferenceRoomService;

    @Mock
    private MaintenanceService maintenanceService;
    
    @Mock
    private DataMapper dataMapper;

    @InjectMocks
    private BookingServiceImpl bookingService;
    
    @Test
    void bookRoom_ValidBooking_ReturnsBooking() {
        BookingDetails newBooking = createValidBooking();
        //when(conferenceRoomService.getRoomById(anyLong())).thenReturn(createValidRoom());
        when(maintenanceService.isMaintenanceScheduled(any(), any())).thenReturn(false);
        when(conferenceRoomRepository.findByMaxCapacityGreaterThanEqualOrderByMaxCapacityAsc(anyInt())).thenReturn(Collections.singletonList(createValidRoom()));
        when(bookingRepository.save(any())).thenReturn(createBookingData());
        BookingData bookedRoom = bookingService.bookConferenceRoom(newBooking,LOGGED_IN_USER);
        assertNotNull(bookedRoom);
    }

    @Test
    void bookRoom_InvalidRoom_ThrowsException() {
    	BookingDetails newBooking = createValidBooking();
       // when(conferenceRoomService.getRoomById(anyLong())).thenReturn(null);
        assertThrows(RoomBookingException.class, () -> bookingService.bookConferenceRoom(newBooking,LOGGED_IN_USER));
    }

   @Test
    void bookRoom_MaintenanceScheduled_ThrowsException() {
    	BookingDetails newBooking = createValidBooking();
      //  when(conferenceRoomService.getRoomById(anyLong())).thenReturn(createValidRoom());
        when(maintenanceService.isMaintenanceScheduled(any(), any())).thenReturn(true);

        assertThrows(RoomBookingException.class, () -> bookingService.bookConferenceRoom(newBooking,LOGGED_IN_USER));
    }


    private BookingData createBookingData() {
    	BookingData bookingData = new BookingData();
    	bookingData.setId(1l);
    	bookingData.setParticipants(3);
    	bookingData.setStartTime(LocalTime.of(LocalTime.now().getHour()+1,00));
    	bookingData.setEndTime(LocalTime.of(LocalTime.now().getHour()+2,00));
        return bookingData;
    }
    
    private BookingDetails createValidBooking() {
    	BookingDetails bookingData = new BookingDetails();
    	bookingData.setParticipants(3);
    	bookingData.setStartTime(LocalTime.of(LocalTime.now().getHour()+1,00));
    	bookingData.setEndTime(LocalTime.of(LocalTime.now().getHour()+2,00));
        return bookingData;
    }
    
    private ConferenceRoomData createValidRoom() {
        ConferenceRoomData room = new ConferenceRoomData();
        room.setConferenceRoomId(1L);
        room.setMaxCapacity(10);
        return room;
    }
    
    private List<MaintenancePeriodConfig> getMaintenancePeriods() {
    	List<MaintenancePeriodConfig> maintenancePeriods = new ArrayList<>();
    	MaintenancePeriodConfig maintenancePeriod = new MaintenancePeriodConfig();
    	maintenancePeriod.setStartTime(LocalTime.of(10,00));
    	maintenancePeriod.setEndTime(LocalTime.of(11,00));
    	maintenancePeriods.add(maintenancePeriod);
		return maintenancePeriods;
	}
}
