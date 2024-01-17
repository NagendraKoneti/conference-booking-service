package com.conference.util;

/**
 * 01/2024
 * @author Nagendra
 */
public interface ConferenceConstants {

	String BOOKED = "Booked";

	int MINITS_15 = 15;

	int ZERO = 0;

	String BASE_PATH = "/api/v1";

	String BOOK_CONFERENCE_ROOM_PATH = "/bookConferenceRoom";
	
	String BOOKINGS_PATH = "/bookings";
	
	String CONFERENCE_ROOMS= "/conference-rooms";

	String LOGGED_IN_USER = "loggedInUser";

	String UNDER_MAINTENANCE = "The room is under maintenance during the requested time slot.";

	String INCORRECT_BOOKING_INTERVALS = "Booking intervals must be in increments of 15 minutes.";

	String ALL_ROOMS_BOOKED = "No room available or exceeding room capacity.";

	String BOOKING_FOR_FUTURE_SLOTS = "Booking can only be done for the current date and future time slots.";

	String LESS_PARTICIPANTS = "Number of participants must be greater than 1.";

	String EXCEEDING_CAPACITY = "Exceeding room capacity.";

}
