package com.conference.controler;

import java.time.LocalTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.conference.entity.ConferenceRoom;
import com.conference.service.ConferenceRoomService;
import com.conference.util.ConferenceConstants;


@RestController	
@RequestMapping(ConferenceConstants.BASE_PATH + "/conference-rooms")
@Validated
public class ConferenceRoomController {
	
	private static final String END_TIME = "endTime";

	private static final String HH_MM = "HH:mm";

	private static final String START_TIME = "startTime";

	private static final Logger logger = LoggerFactory.getLogger(ConferenceRoomController.class);

    @Autowired
    private ConferenceRoomService roomService;

    
    @GetMapping(ConferenceConstants.AVAILABLE_PATH)
	public List<ConferenceRoom> getAvailableRooms(
			@RequestParam(START_TIME) @DateTimeFormat(pattern = HH_MM) LocalTime startTime,
            @RequestParam(END_TIME) @DateTimeFormat(pattern = HH_MM) LocalTime endTime) {
    	logger.info("Started getAvailableRooms for the time slot {} -{}",startTime,endTime);
		return roomService.getAvailableRooms(startTime, endTime);
	}
}
