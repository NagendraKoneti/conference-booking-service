package com.conference.service;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.conference.dto.ConferenceDetails;
import com.conference.entity.ConferenceRoomData;
import com.conference.exception.ErrorCodes;
import com.conference.exception.RoomBookingException;
import com.conference.mapper.DataMapper;
import com.conference.repo.ConferenceRoomRepository;
import com.conference.util.ConferenceConstants;

/**
 * 01/2024 
 * @author Nagendra
 */
@Service
public class ConferenceRoomServiceImpl implements ConferenceRoomService {
	
	private static final Logger logger = LoggerFactory.getLogger(ConferenceRoomServiceImpl.class);
	
    @Autowired
    private ConferenceRoomRepository conferenceRoomRepo;
    @Autowired
    private BookingServiceImpl bookingServiceImpl;
    @Autowired
    private DataMapper dataMapper;
    @Autowired
	private MaintenanceService maintenanceService;

    /**
     * This method should return a list of available meeting rooms based on the specified time range. 
     *  step 1:  It retrieves all conference rooms from the database
     *  step 2:  It then filters out the rooms that are already booked during the specified time range 
     *  
     * @param startTime : meeting start time. 
     * @param endTime   : meeting end time.
     * @return List of rooms that are available during the specified time range.
     */
    @Override
	public List<ConferenceDetails> getAvailableRooms(LocalTime startTime, LocalTime endTime) {
		logger.info("Started finding getAvailableRooms slots : {} - {} ",startTime,endTime);
		validateBookingForCurrentDate(startTime);
		checkMaintenanceSchedule(startTime,endTime);
		List<ConferenceRoomData> allRooms = conferenceRoomRepo.findAll();
		logger.info(" Total no of rooms :{} ",allRooms.size());
		List<ConferenceRoomData> bookedRooms = allRooms.stream()
				.filter(room -> bookingServiceImpl.isRoomBooked(room.getConferenceRoomId(), startTime, endTime))
				.collect(Collectors.toList());
		logger.info(" Total no of rooms booked for this slot :{} ",bookedRooms.size());
		allRooms.removeAll(bookedRooms);
		return dataMapper.mapConferenceRoomDetails(allRooms);
	}
	
	/**
	 * This method will check whether the start time is feature time or past time.  
	 * 
	 * @param startTime
	 */
	private void validateBookingForCurrentDate(LocalTime startTime) {
		bookingServiceImpl.validateBookingForCurrentDate(startTime);
	}

	/**
	 * This method will find conference room by id
	 * @param roomId
	 * @return
	 */
	@Override
	public ConferenceRoomData getRoomById(Long roomId) {
        return conferenceRoomRepo.findById(roomId).orElse(null);
    }
	
	/**
	 * This method has a capable of verifying the maintenance schedule for the given time period.
	 * 
	 * @param startTime
	 * @param endTime
	 */
	private void checkMaintenanceSchedule(LocalTime startTime, LocalTime endTime) {
		if (maintenanceService.isMaintenanceScheduled(startTime, endTime)) {
            throw new RoomBookingException(ErrorCodes.UNDER_MAINTENANCE_EXC.name(),ErrorCodes.UNDER_MAINTENANCE_EXC.getErrorMessage());
        }
	}

}