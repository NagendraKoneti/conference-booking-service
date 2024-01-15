package com.conference.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "CONFERENCE_ROOM")
public class ConferenceRoomData {

	@Id
	@Column(name = "conference_room_id")
	private Long conferenceRoomId;
	private String name;
	@Column(name = "max_capacity")
	private int maxCapacity;
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
