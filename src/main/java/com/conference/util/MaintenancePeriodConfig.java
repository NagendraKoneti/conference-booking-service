package com.conference.util;

import java.time.LocalTime;

import lombok.Data;

@Data
public class MaintenancePeriodConfig {

	private LocalTime startTime;
	private LocalTime endTime;

}
