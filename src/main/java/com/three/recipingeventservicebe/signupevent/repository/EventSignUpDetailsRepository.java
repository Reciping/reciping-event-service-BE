package com.three.recipingeventservicebe.signupevent.repository;

import com.three.recipingeventservicebe.signupevent.domain.EventSignUpDetails;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface EventSignUpDetailsRepository extends
        ReactiveMongoRepository<EventSignUpDetails, String> {
}
