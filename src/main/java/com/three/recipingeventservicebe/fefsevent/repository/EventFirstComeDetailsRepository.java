package com.three.recipingeventservicebe.fefsevent.repository;

import com.three.recipingeventservicebe.fefsevent.domain.EventFirstComeDetails;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EventFirstComeDetailsRepository extends
        MongoRepository<EventFirstComeDetails, String> {
}
