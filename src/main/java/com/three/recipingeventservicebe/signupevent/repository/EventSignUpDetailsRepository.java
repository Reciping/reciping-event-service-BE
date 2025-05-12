package com.three.recipingeventservicebe.signupevent.repository;

import com.three.recipingeventservicebe.signupevent.domain.EventSignUpDetails;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EventSignUpDetailsRepository extends MongoRepository<EventSignUpDetails, String> {
}
