package com.conference.util;

import lombok.Builder;
import lombok.Data;

/**
 * 01/2024 
 * @author Nagendra
 */

@Data
@Builder
public class Response<T> {

	private String status;
	private T data;
}
