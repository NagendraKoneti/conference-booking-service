package com.conference.dto;

import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *01/2024 
 * @author Nagendra
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponse {

	private String conferenceRoomName;
	private int conferenceRoomMaxCapacity;
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer participants;
    private String status;
	
    	
}
