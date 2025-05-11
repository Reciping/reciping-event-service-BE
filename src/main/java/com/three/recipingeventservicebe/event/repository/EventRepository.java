package com.three.recipingeventservicebe.event.repository;

import com.three.recipingeventservicebe.event.domain.Event;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EventRepository extends MongoRepository<Event, String> {

}
