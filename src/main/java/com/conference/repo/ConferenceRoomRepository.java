package com.conference.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.conference.entity.ConferenceRoomData;

public interface ConferenceRoomRepository extends JpaRepository<ConferenceRoomData, Long> {

	List<ConferenceRoomData> findByMaxCapacityGreaterThanEqualOrderByMaxCapacityAsc(int participants);

}
