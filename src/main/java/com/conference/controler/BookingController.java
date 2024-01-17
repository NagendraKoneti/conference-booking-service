package com.conference.controler;


import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.conference.dto.BookingDetails;
import com.conference.entity.BookingData;
import com.conference.service.BookingService;
import com.conference.util.ConferenceConstants;	

/**
 * 01/2024 
 * @author Nagendra
 */

@RestController
@RequestMapping(ConferenceConstants.BASE_PATH + ConferenceConstants.BOOKINGS_PATH)
@Validated
public class BookingController {

	private static final Logger logger = LoggerFactory.getLogger(BookingController.class);

    @Autowired
    private BookingService bookingService;
    
    /**
     *  User can book the conference by providing start date, end date and no of participants.
     *  
     * @param bookingDetails: user provided booking information to block conference room
     * @param request : RequestHeader requestHeader
     * 
     * @return booking information
     */
    @PostMapping(ConferenceConstants.BOOK_CONFERENCE_ROOM_PATH)
	public ResponseEntity<BookingData> bookConferenceRoom(@RequestBody @Valid BookingDetails bookingDetails,
			@RequestHeader(value = ConferenceConstants.LOGGED_IN_USER, required = false) String loggedInUser) {
    	logger.info("The logged in user is : {}",loggedInUser);
        BookingData bookedRoom = bookingService.bookConferenceRoom(bookingDetails,loggedInUser);
        return new ResponseEntity<>(bookedRoom, HttpStatus.CREATED);
    }

}