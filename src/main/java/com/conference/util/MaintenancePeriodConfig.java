package com.conference.util;

import java.time.LocalTime;

import lombok.Data;

/**
 * 01/2024 
 * @author Nagendra
 */
@Data
public class MaintenancePeriodConfig {

	private LocalTime startTime;
	private LocalTime endTime;

}
