package com.conference.util;

public interface ConferenceConstants {

	String BOOKED = "Booked";

	int MINITS_15 = 15;

	int ZERO = 0;

	String BASE_PATH = "/api/v1/";

	String BOOK_CONFERENCE_ROOM_PATH = "/bookConferenceRoom";
	
	String AVAILABLE_PATH = "/available";
	
	String BOOKINGS_PATH = "/bookings";

	String LOGGED_IN_USER = "loggedInUser";

	String UNDER_MAINTENANCE = "The room is under maintenance during the requested time slot.";

	String INCORRECT_BOOKING_INTERVALS = "Booking intervals must be in increments of 15 minutes.";

	String ROOM_ALREADY_BOOKED = "The room is already booked for the requested time slot.";

	String BOOKING_FOR_FUTURE_SLOTS = "Booking can only be done for the current date and future time slots.";

	String LESS_PARTICIPANTS = "Number of participants must be greater than 1.";

	String EXCEEDING_CAPACITY = "Invalid room or exceeding room capacity.";

}
