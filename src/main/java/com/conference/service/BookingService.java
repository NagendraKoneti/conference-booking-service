package com.conference.service;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.conference.dto.BookingDetails;
import com.conference.entity.BookingData;
import com.conference.exception.RoomBookingException;
import com.conference.repo.BookingRepository;

/**
 * @author Nagendra
 */
@Service
public class BookingService {
	

    @Autowired
    private BookingRepository bookingRepository;
    
    @Autowired	
    private ConferenceRoomService conferenceRoomService;
    
	@Autowired
	private MaintenanceService maintenanceService;
    
    /**
     * This method will book conference room after pass through all controls
     * 	 a) Validate booking time interval (15 minutes interval)
     *   b) Validate room capacity
     *   c) Check if the requested time slot is during maintenance
     *   d) Check if the requested time slot is available (First Come First Serve)
     *   e) Check if booking is for the current date
     *   
     * @param bookingDetails
     * @return booking information
     */
    public BookingData bookConferenceRoom(BookingDetails bookingDetails) {
    	validateBookingForCurrentDate(bookingDetails.getStartTime());
        validateBookingInterval(bookingDetails); 
        validateRoomCapacity(bookingDetails);  
        checkMaintenanceSchedule(bookingDetails); 
        checkAvailability(bookingDetails);
        return bookingRepository.save(mapToEntity(bookingDetails));
    }

    private BookingData mapToEntity(BookingDetails bookingDetails) {
	  BookingData bookingData = new BookingData();
	  bookingData.setStartTime(bookingDetails.getStartTime());
	  bookingData.setEndTime(bookingDetails.getEndTime());
	  bookingData.setOrganizer("Nagendra");
	  bookingData.setStatus("Booked");
	  bookingData.setParticipants(bookingDetails.getParticipants());
	  return bookingData;		
	}

	public Optional<BookingData> getBookingsForRoom(Long roomId) {
        return bookingRepository.findById(roomId);
    }
    
	/**
	 * 
	 * @param roomId
	 * @param startTime
	 * @param endTime
	 * @return
	 */
    public boolean isRoomBooked(Long roomId, LocalTime startTime, LocalTime endTime) {
        List<BookingData> overlappingBookings = bookingRepository
                .findByIdAndEndTimeAfterAndStartTimeBefore(roomId, startTime, endTime);

        return !overlappingBookings.isEmpty();
    }
    
    /**
     *  This method checks if there is maintenance scheduled for the specified time slot.
     *  
     * @param bookingDetails
     */
    private void checkMaintenanceSchedule(BookingDetails bookingDetails) {
        if (maintenanceService.isMaintenanceScheduled(bookingDetails.getStartTime(), bookingDetails.getEndTime())) {
            throw new RoomBookingException("The room is under maintenance during the requested time slot.");
        }
    }
    
    /**
     *  The number of people allowed for booking should be greater than 1 and less than or equal to the maximum capacity of the conference room.
     *  
     * @param bookingDetails
     */
	private void validateRoomCapacity(BookingDetails bookingDetails) {
		int participants = bookingDetails.getParticipants();
		Optional.of(participants).filter(p -> p > 1)
				.orElseThrow(() -> new RoomBookingException("Number of participants must be greater than 1."));

		Optional.ofNullable(conferenceRoomService.getRoomById(bookingDetails.getRoomId()))
				.filter(room -> participants <= room.getMaxCapacity())
				.orElseThrow(() -> new RoomBookingException("Invalid room or exceeding room capacity."));

	}
    
    /**
     * 
     * @param localTime
     */
    private void validateBookingForCurrentDate(LocalTime startTime) {
    	Optional.ofNullable(startTime)
        .ifPresent(currentLocalTime -> {
            if (!currentLocalTime.isAfter(LocalTime.now())) {
                throw new RoomBookingException("Booking can only be done for the current date and future time slots.");
            }
        });
    }
    
    /**
     * The checkAvailability method queries the database to find any overlapping bookings for the requested time slot. If there are overlapping bookings, 
     * it throws an RoomBookingException indicating that the room is already booked for the requested time.
     * This approach ensures that bookings are processed on a "First Come First Serve" basis, and new bookings are rejected if they conflict with existing bookings.
     * 
     * @param bookingDetails
     */
    private void checkAvailability(@Valid BookingDetails bookingDetails) {
        Long roomId = bookingDetails.getRoomId();
        List<BookingData> overlappingBookings = bookingRepository
                .findByIdAndEndTimeAfterAndStartTimeBefore(roomId, bookingDetails.getStartTime(), bookingDetails.getEndTime());

        if (!overlappingBookings.isEmpty()) {
            throw new RoomBookingException("The room is already booked for the requested time slot.");
        }
    }
    
    /**
	 * Booking can be done only in intervals of 15 mins. Examples : 2:00 - 2:15 or 2:00 - 2:30 or 2:00 - 3:00
	 * 
	 * @param bookingDetails
	 */
    private void validateBookingInterval(BookingDetails bookingDetails) {
        if (!isValidTimeInterval(bookingDetails.getStartTime(), bookingDetails.getEndTime())) {
            throw new RoomBookingException("Booking intervals must be in increments of 15 minutes.");
        }
    }

    private boolean isValidTimeInterval(LocalTime startTime, LocalTime endTime) {
        if (!isStartTimeBeforeEndTime(startTime, endTime) || !isTimeIn15MinuteIncrements(startTime) || !isTimeIn15MinuteIncrements(endTime)) {
            return false;
        }

        return true;
    }

    private boolean isStartTimeBeforeEndTime(LocalTime startTime, LocalTime endTime) {
        return startTime.isBefore(endTime);
    }

    private boolean isTimeIn15MinuteIncrements(LocalTime startTime) {
        return startTime.getMinute() % 15 == 0;
    }
    
}