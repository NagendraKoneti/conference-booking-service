package com.conference.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 01/2024 
 * @author Nagendra
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConferenceDetails {

	private Long id;
	private String name;
	private int maxCapacity;

}
