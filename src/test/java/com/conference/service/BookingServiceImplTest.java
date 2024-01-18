package com.conference.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.time.LocalTime;
import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.conference.dto.BookingDetails;
import com.conference.dto.BookingResponse;
import com.conference.entity.BookingData;
import com.conference.entity.ConferenceRoomData;
import com.conference.exception.RoomBookingException;
import com.conference.mapper.DataMapper;
import com.conference.repo.BookingRepository;
import com.conference.repo.ConferenceRoomRepository;

/**
 * 01/2024 
 * @author Nagendra
 */
@ExtendWith(MockitoExtension.class)
public class BookingServiceImplTest {

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
    
    /**
     * Test case : User book conference room successfully 
     * 
     * 
     */
    @Test
   public void bookRoom_ValidBooking_ReturnsBooking() {
        BookingDetails newBooking = createValidBooking();
        when(maintenanceService.isMaintenanceScheduled(any(), any())).thenReturn(false);
        when(dataMapper.mapToBookingDataEntity(any(),anyString(),any())).thenReturn(new BookingData());
        when(dataMapper.mapBookingDataToBookingResponse(any())).thenReturn(new BookingResponse());
        when(conferenceRoomRepository.findByMaxCapacityGreaterThanEqualOrderByMaxCapacityAsc(anyInt())).thenReturn(Collections.singletonList(createValidConferenceRoom()));
        when(bookingRepository.save(any())).thenReturn(createBookingData());
        BookingResponse bookedRoom = bookingService.bookConferenceRoom(newBooking,LOGGED_IN_USER);
        assertNotNull(bookedRoom);
    }
    
    /**
     * Test case : User unable to book conference room due to no availability.
     */
    @Test
    public void bookRoom_NoRooms_Available_ThrowsException() {
    	BookingDetails newBooking = createValidBooking();
        assertThrows(RoomBookingException.class, () -> bookingService.bookConferenceRoom(newBooking,LOGGED_IN_USER));
    }
    /**
     * Test case : User unable to book conference room due to participants are not more than 1
     */
    @Test
    public void bookRoom_For_Single_Participants_ThrowsException() {
    	BookingDetails newBooking = createValidBooking();
    	newBooking.setParticipants(1);
        assertThrows(RoomBookingException.class, () -> bookingService.bookConferenceRoom(newBooking,LOGGED_IN_USER));
    }
    
    /**
     * Test case : User unable to book conference room due to participants are more than what we have capacity try with 55
     * 
     */
    @Test
   public void bookRoom_For_55_Participants_ThrowsException() {
        BookingDetails newBooking = createValidBooking();
        newBooking.setParticipants(55);
        when(maintenanceService.isMaintenanceScheduled(any(), any())).thenReturn(false);
        when(conferenceRoomRepository.findByMaxCapacityGreaterThanEqualOrderByMaxCapacityAsc(anyInt())).thenReturn(Collections.emptyList());
        assertThrows(RoomBookingException.class, () -> bookingService.bookConferenceRoom(newBooking,LOGGED_IN_USER));
    }
    
    /**
     * Test case : User unable to book conference room due to Invalid start and end time(End is prior to start time)
     */
    @Test
    public void bookRoom_Invalid_BookData_EndTime_Prior_StartTime_ThrowsException() {
    	BookingDetails newBooking = createValidBooking();
    	newBooking.setStartTime(LocalTime.of(10,00));
    	newBooking.setEndTime(LocalTime.of(9,00));
    	
        assertThrows(RoomBookingException.class, () -> bookingService.bookConferenceRoom(newBooking,LOGGED_IN_USER));
    }
    /**
     * Test case : User Unable to book the conference room due to the schedule is under maintenance scheduled (17-18hr) 
     */
   @Test
   public void bookRoom_MaintenanceScheduled_ThrowsException() {
    	BookingDetails newBooking = createValidBooking();
    	newBooking.setStartTime(LocalTime.of(17,00));
    	newBooking.setEndTime(LocalTime.of(18,00));
        when(maintenanceService.isMaintenanceScheduled(any(), any())).thenReturn(true);
        assertThrows(RoomBookingException.class, () -> bookingService.bookConferenceRoom(newBooking,LOGGED_IN_USER));
    }
   
   /**
    * Test case : User Unable to book the conference room due to the schedule is past time (7-8hr) 
    */
  @Test
  public void bookRoom_PastTime_ThrowsException() {
   	BookingDetails newBooking = createValidBooking();
   	newBooking.setStartTime(LocalTime.of(LocalTime.now().getHour()-1,00));
   	newBooking.setEndTime(LocalTime.of(LocalTime.now().getHour(),00));
       assertThrows(RoomBookingException.class, () -> bookingService.bookConferenceRoom(newBooking,LOGGED_IN_USER));
   }
   
   /**
    * Test case : User Unable to book the conference room due to the invalid Intervals (10-10:10hr) 
    */
   @Test
   public void bookRoom_Invalid_15m_Intervals_ThrowsException() {
   	BookingDetails newBooking = createValidBooking();
   	newBooking.setStartTime(LocalTime.of(10,00));
   	newBooking.setEndTime(LocalTime.of(10,10));
   	
   assertThrows(RoomBookingException.class, () -> bookingService.bookConferenceRoom(newBooking,LOGGED_IN_USER));
   }
   
   /**
    * Test case : Verify the Room no 1 is booked when rooms available then return room details
    *  
    */
   @Test
   public void testIsRoomBooked_When_Room_Available_Then_Return_False() {
	   when(bookingRepository.findByConferenceRoom_ConferenceRoomIdAndEndTimeAfterAndStartTimeBefore(any(),any(),any())).thenReturn(Collections.singletonList(new BookingData()));
	   assertTrue(bookingService.isRoomBooked(1l,LocalTime.now(),LocalTime.of(LocalTime.now().getHour()+1,30)));
   }

   /**
    * Test case : Verify the Room no 1 is booked when room already booked then return empty list 
    *  
    */
   @Test
   public void testIsRoomBooked_When_Room_Available_Then_Return_True() {
	   when(bookingRepository.findByConferenceRoom_ConferenceRoomIdAndEndTimeAfterAndStartTimeBefore(any(),any(),any())).thenReturn(Collections.emptyList());
	   assertFalse(bookingService.isRoomBooked(1l,LocalTime.now(),LocalTime.of(LocalTime.now().getHour()+1,30)));
   }



    private BookingData createBookingData() {
    	BookingData bookingData = new BookingData();
    	bookingData.setId(1l);
    	bookingData.setParticipants(3);
    	bookingData.setStartTime(LocalTime.of(11,00));
    	bookingData.setEndTime(LocalTime.of(12,00));
        return bookingData;
    }
    
    private BookingDetails createValidBooking() {
    	BookingDetails bookingData = new BookingDetails();
    	bookingData.setParticipants(3);
    	bookingData.setStartTime(LocalTime.of(11,00));
    	bookingData.setEndTime(LocalTime.of(12,00));
        return bookingData;
    }
    
    private ConferenceRoomData createValidConferenceRoom() {
        ConferenceRoomData room = new ConferenceRoomData();
        room.setConferenceRoomId(1L);
        room.setMaxCapacity(10);
        return room;
    }
    
}
