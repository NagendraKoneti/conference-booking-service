package com.conference.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.conference.entity.ConferenceRoom;

public interface ConferenceRoomRepository extends JpaRepository<ConferenceRoom, Long> {

}
