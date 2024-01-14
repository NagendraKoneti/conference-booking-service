package com.conference.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.time.LocalTime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.conference.dto.BookingDetails;
import com.conference.entity.BookingData;
import com.conference.entity.ConferenceRoom;
import com.conference.exception.RoomBookingException;
import com.conference.repo.BookingRepository;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

	private static final String LOGGED_IN_USER = "loggedInUser";

	@Mock
    private BookingRepository bookingRepository;

    @Mock
    private ConferenceRoomService conferenceRoomService;

    @Mock
    private MaintenanceService maintenanceService;

    @InjectMocks
    private BookingService bookingService;
    
    @Test
    void bookRoom_ValidBooking_ReturnsBooking() {
        BookingDetails newBooking = createValidBooking();
        when(conferenceRoomService.getRoomById(anyLong())).thenReturn(createValidRoom());
        when(maintenanceService.isMaintenanceScheduled(any(), any())).thenReturn(false);
        when(bookingRepository.save(any())).thenReturn(createBookingData());
        BookingData bookedRoom = bookingService.bookConferenceRoom(newBooking,LOGGED_IN_USER);
        assertNotNull(bookedRoom);
        assertEquals(newBooking.getRoomId(), bookedRoom.getId());
    }

    @Test
    void bookRoom_InvalidRoom_ThrowsException() {
    	BookingDetails newBooking = createValidBooking();
        when(conferenceRoomService.getRoomById(anyLong())).thenReturn(null);
        assertThrows(RoomBookingException.class, () -> bookingService.bookConferenceRoom(newBooking,LOGGED_IN_USER));
    }

   @Test
    void bookRoom_MaintenanceScheduled_ThrowsException() {
    	BookingDetails newBooking = createValidBooking();
        when(conferenceRoomService.getRoomById(anyLong())).thenReturn(createValidRoom());
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
    	bookingData.setRoomId(1l);
    	bookingData.setParticipants(3);
    	bookingData.setStartTime(LocalTime.of(LocalTime.now().getHour()+1,00));
    	bookingData.setEndTime(LocalTime.of(LocalTime.now().getHour()+2,00));
        return bookingData;
    }
    
    private ConferenceRoom createValidRoom() {
        ConferenceRoom room = new ConferenceRoom();
        room.setId(1L);
        room.setMaxCapacity(10);
        return room;
    }
}
