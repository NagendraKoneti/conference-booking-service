package com.conference.entity;

import java.time.LocalDateTime;
import java.time.LocalTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 01/2024 
 * @author Nagendra
 */

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingData {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String organizer;
	@ManyToOne
    @JoinColumn(name = "conferenceRoomId")
	private ConferenceRoomData conferenceRoom;
	private String status;
	private int participants;
	private LocalTime startTime;
	private LocalTime endTime;
	private LocalDateTime creationDate;
	private LocalDateTime updateDate;
	
	@PrePersist
    protected void onCreate() {
        creationDate = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updateDate = LocalDateTime.now();
    }
}
