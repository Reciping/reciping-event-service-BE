package com.three.recipingeventservicebe.signupevent.repository;

import com.three.recipingeventservicebe.signupevent.domain.EventSignUpParticipant;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EventSignUpParticipantRepository extends
        MongoRepository<EventSignUpParticipant, String> {
    boolean existsByEventIdAndUserId(String eventId, String userId);
    long countByEventId(String eventId);
}
