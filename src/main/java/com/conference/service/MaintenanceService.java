package com.conference.service;

import java.time.LocalTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.conference.util.AppProperties;

/**
 * @author Nagendra
 */
@Service
public class MaintenanceService {

	
	@Autowired
	private AppProperties appProperties;
	
    /**
     * This method checks if there is maintenance scheduled for the specified time slot.
     * 
     * @param startTime
     * @param endTime
     * @return
     */
	public boolean isMaintenanceScheduled(LocalTime startTime, LocalTime endTime) {
		return appProperties.getMaintenancePeriods().stream()
				.anyMatch(period -> startTime.isBefore(period.getEndTime()) && endTime.isAfter(period.getStartTime()));
	}

}