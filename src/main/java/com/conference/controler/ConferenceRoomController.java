package com.conference.controler;

import java.time.LocalTime;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.conference.entity.ConferenceRoom;
import com.conference.service.ConferenceRoomService;


@RestController
@RequestMapping("/conference-rooms")
@Validated
public class ConferenceRoomController {
	
    private static final Logger logger = LoggerFactory.getLogger(ConferenceRoomController.class);


    @Autowired
    private ConferenceRoomService roomService;

    @GetMapping
    public List<ConferenceRoom> getAllConferenceRooms() {
        return roomService.getAllRooms();
    }

    @GetMapping("/{roomId}")
    public ConferenceRoom getConferenceRoomById(@PathVariable Long roomId) {
        return roomService.getRoomById(roomId);
    }
    
    @GetMapping("/available")
    public List<ConferenceRoom> getAvailableRooms(
           @RequestParam("startTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) 
           @NotNull(message = "Start time cannot be null") LocalTime startTime,
            @RequestParam("endTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) 
           @NotNull(message = "End time cannot be null") LocalTime endTime) {
        return roomService.getAvailableRooms(startTime, endTime);
    }
}
