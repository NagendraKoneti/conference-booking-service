package com.conference.dto;

import java.time.LocalTime;

import javax.validation.constraints.NotNull;

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
public class BookingDetails {

	@NotNull
    private LocalTime startTime;
	@NotNull
    private LocalTime endTime;
	@NotNull
    private int participants;
    	
}
