package com.conference.exception;

/**
 * 01/2024 
 * @author Nagendra
 */
public class RoomBookingException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public RoomBookingException(String message) {
        super(message);
    }
}
