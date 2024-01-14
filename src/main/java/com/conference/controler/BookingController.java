package com.conference.controler;


import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.conference.dto.BookingDetails;
import com.conference.entity.BookingData;
import com.conference.service.BookingService;

@RestController
@RequestMapping("/bookings")
@Validated
public class BookingController {

    private static final Logger logger = LoggerFactory.getLogger(BookingController.class);

    
    @Autowired
    private BookingService bookingService;

    @PostMapping("/bookConferenceRoom")
    public ResponseEntity<BookingData> bookConferenceRoom(@RequestBody @Valid BookingDetails bookingDetails) {
        BookingData bookedRoom = bookingService.bookConferenceRoom(bookingDetails);
        return new ResponseEntity<>(bookedRoom, HttpStatus.CREATED);
    }

    @GetMapping("/{roomId}")
    public BookingData getBookingsForRoom(@PathVariable Long roomId) {
        return bookingService.getBookingsForRoom(roomId).get();
    }
}