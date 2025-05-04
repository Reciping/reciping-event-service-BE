package com.three.recipingeventservicebe.event.repository;

import com.three.recipingeventservicebe.event.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {

}
