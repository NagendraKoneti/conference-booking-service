package com.conference.dto;

import java.time.LocalTime;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingDetails {

	private Long roomId;
	@NotNull
    private LocalTime startTime;
	@NotNull
    private LocalTime endTime;
	@NotNull
    private int participants;
    	
}
