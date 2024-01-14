package com.conference.entity;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SloatsData {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long slotId;
	private String conferenceRoomId;
	private String  slotStartTime;
	private String slotEndTime;
	private String status;
	private Timestamp creationDate;
	private Timestamp updateDate;
}
