package com.three.recipingeventservicebe.fefsevent.repository;

import com.three.recipingeventservicebe.fefsevent.domain.EventFirstComeParticipant;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EventFirstComeParticipantRepository extends
        MongoRepository<EventFirstComeParticipant, String> {
    boolean existsByEventIdAndUserId(String eventId, String userId);
    long countByEventId(String eventId);
}
