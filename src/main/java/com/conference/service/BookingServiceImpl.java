package com.conference.service;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.conference.dto.BookingDetails;
import com.conference.dto.BookingResponse;
import com.conference.entity.BookingData;
import com.conference.entity.ConferenceRoomData;
import com.conference.exception.ErrorCodes;
import com.conference.exception.RoomBookingException;
import com.conference.mapper.DataMapper;
import com.conference.repo.BookingRepository;
import com.conference.repo.ConferenceRoomRepository;
import com.conference.util.ConferenceConstants;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 *01/2024 
 * @author Nagendra
 */
@Service
@AllArgsConstructor
@NoArgsConstructor
public class BookingServiceImpl implements BookingService  {
	
	private static final Logger logger = LoggerFactory.getLogger(BookingServiceImpl.class);
	
	@Autowired
    private BookingRepository bookingRepository;
    
	@Autowired
    private ConferenceRoomRepository conferenceRoomRepo;
    
	@Autowired
	private MaintenanceService maintenanceService;
	
	@Autowired
	private DataMapper dataMapper;
    
    /**
     * This method will book conference room after pass through all controls
     * 	 a) Validate booking time interval (15 minutes interval)
     *   b) Validate room capacity
     *   c) Check if the requested time slot is during maintenance
     *   d) Check if the requested time slot is available (First Come First Serve)
     *   e) Check if booking is for the current date
     *   
     * @param bookingDetails : Booking information requested by customer
     * @param loggedInUser : Logged in user name ( Requested customer identity)
     * @return booking information : Booking confirmation details.
     */
	@Override
    public BookingResponse bookConferenceRoom(BookingDetails bookingDetails, String loggedInUser) {
		logger.info("Booking in process.....");
    	validateBookingForCurrentDate(bookingDetails.getStartTime());
        validateBookingInterval(bookingDetails); 
        validateRoomCapacity(bookingDetails);  
        checkMaintenanceSchedule(bookingDetails); 
        ConferenceRoomData availableRoom = getAvailabileConferenceRoom(bookingDetails);
        logger.info("Booking is confimed`, blockng same in process.....{} ",availableRoom.getName());
        BookingData confirmedBooking =  bookingRepository.save(dataMapper.mapToBookingDataEntity(bookingDetails,loggedInUser,availableRoom));
		return dataMapper.mapBookingDataToBookingResponse(confirmedBooking);
    }

    

	/**
	 * This method has a capable of verifies availability of the room for requested time period
	 * 
	 * @param roomId
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	@Override
    public boolean isRoomBooked(Long roomId, LocalTime startTime, LocalTime endTime) {
		logger.info("Verifying the reqeusted Room is Booked -- in process.....");
        List<BookingData> overlappingBookings = bookingRepository
                .findByConferenceRoom_ConferenceRoomIdAndEndTimeAfterAndStartTimeBefore(roomId, startTime, endTime);
        return !overlappingBookings.isEmpty();
    }
    
    /**
     *  This method checks if there is maintenance scheduled for the specified time slot.
     *  
     * @param bookingDetails
     */
    private void checkMaintenanceSchedule(BookingDetails bookingDetails) {
        if (maintenanceService.isMaintenanceScheduled(bookingDetails.getStartTime(), bookingDetails.getEndTime())) {
            throw new RoomBookingException(ErrorCodes.UNDER_MAINTENANCE_EXC.name() , ErrorCodes.UNDER_MAINTENANCE_EXC.getErrorMessage());
        }
    }
    
    /**
	 * The number of people allowed for booking should be greater than 1 and less
	 * than or equal to the maximum capacity of the conference room.
	 * 
	 * @param bookingDetails
	 */
	private void validateRoomCapacity(BookingDetails bookingDetails) {
		int participants = bookingDetails.getParticipants();
		Optional.of(participants).filter(p -> p > 1)
				.orElseThrow(() -> new RoomBookingException(ErrorCodes.LESS_PARTICIPANTS.name(),(ErrorCodes.LESS_PARTICIPANTS.getErrorMessage())));
	}
    
    /**
     * 
     * @param localTime
     */
	@Override
    public void validateBookingForCurrentDate(LocalTime startTime) {
		logger.info("Booking is in process validating requested slots....{} ",startTime.toString());
    	Optional.ofNullable(startTime)
        .ifPresent(currentLocalTime -> {
            if (!currentLocalTime.isAfter(LocalTime.now())) {
                throw new RoomBookingException(ErrorCodes.BOOKING_FOR_FUTURE_SLOTS.name(),ErrorCodes.BOOKING_FOR_FUTURE_SLOTS.getErrorMessage());
            }
        });
    }
    
    /**
     * The checkAvailability method verify for any overlapping bookings for the requested time slot. 
     * If there are overlapping bookings,it throws an RoomBookingException indicating that the room is already booked for the requested time.
     * New bookings are rejected if they conflict with existing bookings.
     * 
     * @param bookingDetails
     * @return 
     */
    private ConferenceRoomData getAvailabileConferenceRoom(BookingDetails bookingDetails) {
    	logger.info("Booking is in process trying to find available rooms....");
    	List<ConferenceRoomData> allRooms = conferenceRoomRepo.findByMaxCapacityGreaterThanEqualOrderByMaxCapacityAsc(bookingDetails.getParticipants());
    	for (ConferenceRoomData conferenceRoomData : allRooms) {
    		 if(!isRoomBooked(conferenceRoomData.getConferenceRoomId(), bookingDetails.getStartTime(), bookingDetails.getEndTime()))
    			 return conferenceRoomData;
    	}
            throw new RoomBookingException(ErrorCodes.ALL_ROOMS_BOOKED.name(),ErrorCodes.ALL_ROOMS_BOOKED.getErrorMessage());
    }
    
    /**
	 * Booking can be done only in intervals of 15 minutes. Examples : 2:00 - 2:15 or 2:00 - 2:30 or 2:00 - 3:00
	 * 
	 * @param bookingDetails
	 */
    private void validateBookingInterval(BookingDetails bookingDetails) {
    	logger.info("Booking is in process validating requested slots Intervals....");
        if (!isValidTimeInterval(bookingDetails.getStartTime(), bookingDetails.getEndTime())) {
            throw new RoomBookingException(ErrorCodes.INCORRECT_BOOKING_INTERVALS.name(),ErrorCodes.INCORRECT_BOOKING_INTERVALS.getErrorMessage());
        }
    }
    /**
     * This method has a capable of verifies the inputed time periods are under 15 mints intervals 
     * 
     * @param startTime
     * @param endTime
     * 
     * @return boolean : true if startTime and endTime are multiple of 15 else false
     */
    private boolean isValidTimeInterval(LocalTime startTime, LocalTime endTime) {
    	logger.info("Booking is in process validating requested slots 3 conditions....");
        if (!isStartTimeBeforeEndTime(startTime, endTime) || !isTimeIn15MinuteIncrements(startTime) || !isTimeIn15MinuteIncrements(endTime)) {
            return false;
        }

        return true;
    }
    /**
     * This method verifies end time should be after start time
     * 
     * @param startTime
     * @param endTime
     * @return boolean : true for end time is after start time else false
     */
    private boolean isStartTimeBeforeEndTime(LocalTime startTime, LocalTime endTime) {
    	logger.info("Booking is in process validating requested slots startTime should be prior to endTime....");
        return startTime.isBefore(endTime);
    }
    /**
     * This method a capable of verifies the 15 minutes intervals on input time
     * @param startTime
     * @return boolean: true if minutes are multiples of 15 else false
     */
    private boolean isTimeIn15MinuteIncrements(LocalTime startTime) {
    	logger.info("Booking is in process validating requested slots should be 15mints intervals....");
        return startTime.getMinute() % ConferenceConstants.MINITS_15 == ConferenceConstants.ZERO;
    }
    
}