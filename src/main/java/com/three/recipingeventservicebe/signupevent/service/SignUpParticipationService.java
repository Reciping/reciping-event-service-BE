package com.three.recipingeventservicebe.signupevent.service;

import com.three.recipingeventservicebe.event.repository.EventRepository;
import com.three.recipingeventservicebe.global.exception.custom.AlreadyParticipatedException;
import com.three.recipingeventservicebe.global.exception.custom.EventClosedException;
import com.three.recipingeventservicebe.global.exception.custom.EventNotFoundException;
import com.three.recipingeventservicebe.signupevent.domain.EventSignUpParticipant;
import com.three.recipingeventservicebe.signupevent.domain.SignUpParticipationMetadata;
import com.three.recipingeventservicebe.signupevent.dto.UserInfoResponse;
import com.three.recipingeventservicebe.signupevent.repository.EventSignUpDetailsRepository;
import com.three.recipingeventservicebe.signupevent.repository.EventSignUpParticipantRepository;
import java.time.Instant;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j(topic = "SignUpParticipationService")
@Service
@RequiredArgsConstructor
public class SignUpParticipationService {

    private final EventSignUpParticipantRepository participantRepository;
    private final EventSignUpDetailsRepository signUpDetailsRepository;
    private final EventRepository eventRepository;
    private final WebClient userClient;
    private final SignUpEventRuleFactory ruleFactory;

    /**
     * 회원가입 선착순 이벤트 참여 API
     * @param eventId - 이벤트 ID
     * @param userId - 현재 로그인한 회원 ID
     * @return Mono<Void> - 비동기 응답 체인
     */
    public Mono<Void> participate(String eventId, String userId) {
        SignUpEventRule rule = ruleFactory.createDefaultRule();

        return participantRepository.existsByEventIdAndUserId(eventId, userId)
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.error(new AlreadyParticipatedException("이미 참여한 이벤트입니다."));
                    }
                    return userClient.get()
                            .uri("/api/v1/users/{userId}/created-at", userId)
                            .retrieve()
                            .bodyToMono(UserInfoResponse.class)
                            .map(UserInfoResponse::getCreatedAt)
                            .flatMap(joinedAt -> {
                                if (!rule.isEligible(joinedAt)) {
                                    return Mono.error(new EventClosedException("회원가입 7일 이내만 참여 가능합니다."));
                                }
                                return Mono.fromCallable(() -> eventRepository.findById(eventId))
                                        .flatMap(optionalEvent -> optionalEvent.map(Mono::just)
                                                .orElseGet(() -> Mono.error(new EventNotFoundException("이벤트를 찾을 수 없습니다."))))
                                        .flatMap(event -> {
                                            Instant now = Instant.now();
                                            if (now.isBefore(event.getActiveStartAt()) || now.isAfter(event.getActiveEndAt())) {
                                                return Mono.error(new EventClosedException("이벤트 시간이 아닙니다."));
                                            }
                                            return signUpDetailsRepository.findById(eventId)
                                                    .switchIfEmpty(Mono.error(new EventNotFoundException("이벤트 상세 정보를 찾을 수 없습니다.")))
                                                    .flatMap(details -> participantRepository.countByEventId(eventId)
                                                            .flatMap(currentCount -> {
                                                                boolean isWinner = rule.isWinner(currentCount);
                                                                EventSignUpParticipant participant = EventSignUpParticipant.builder()
                                                                        .id(UUID.randomUUID().toString())
                                                                        .eventId(eventId)
                                                                        .userId(userId)
                                                                        .joinedAt(Instant.now())
                                                                        .metadata(new SignUpParticipationMetadata(
                                                                                isWinner,
                                                                                rule.getRewardForWinner(isWinner)
                                                                        ))
                                                                        .build();
                                                                return participantRepository.save(participant).then();
                                                            }));
                                        });
                            });
                });
    }
}