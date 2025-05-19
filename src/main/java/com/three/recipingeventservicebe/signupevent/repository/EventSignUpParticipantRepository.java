package com.three.recipingeventservicebe.signupevent.repository;

import com.three.recipingeventservicebe.signupevent.domain.EventSignUpParticipant;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface EventSignUpParticipantRepository extends
        ReactiveMongoRepository<EventSignUpParticipant, String> {
    Mono<Boolean> existsByEventIdAndUserId(String eventId, String userId);
    Mono<Long> countByEventId(String eventId);
    // Mono<T>는 비동기 단일값 반환, Flux<T>는 비동기 다중값 반환
}

