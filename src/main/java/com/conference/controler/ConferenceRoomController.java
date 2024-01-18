package com.conference.controler;

import java.time.LocalTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.conference.dto.ConferenceDetails;
import com.conference.service.ConferenceRoomService;
import com.conference.util.ConferenceConstants;
import com.conference.util.Response;

/**
 * 01/2024 
 * @author Nagendra
 */

@RestController	
@RequestMapping(ConferenceConstants.BASE_PATH+ConferenceConstants.CONFERENCE_ROOMS )
@Validated
public class ConferenceRoomController {
	
	private static final String END_TIME = "endTime";

	private static final String HH_MM = "HH:mm";

	private static final String START_TIME = "startTime";

	private static final Logger logger = LoggerFactory.getLogger(ConferenceRoomController.class);

    @Autowired
    private ConferenceRoomService conferenceRoomService;

    /**
     * User can be able to see meeting rooms available by giving the time range
     * 
     * @param startTime : meeting start time
     * @param endTime : meeting end time
     * @return : list of available conferenceRooms 
     */
    @GetMapping
	public ResponseEntity<Response<List<ConferenceDetails>>> getAvailableConferenceRooms(
			@RequestParam(START_TIME) @DateTimeFormat(pattern = HH_MM) LocalTime startTime,
            @RequestParam(END_TIME) @DateTimeFormat(pattern = HH_MM) LocalTime endTime) {
    	logger.info("Started getAvailableRooms for the time slot {} -{}",startTime,endTime);
    	Response<List<ConferenceDetails>> response = Response.<List<ConferenceDetails>>builder()
    			.status(ConferenceConstants.SUCCESS)
    			.data(conferenceRoomService.getAvailableRooms(startTime, endTime))
    			.build();          
      return  ResponseEntity.status(HttpStatus.OK).body(response);
	}
}
