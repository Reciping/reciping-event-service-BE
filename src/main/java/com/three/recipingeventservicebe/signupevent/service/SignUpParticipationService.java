package com.three.recipingeventservicebe.signupevent.service;

import com.three.recipingeventservicebe.global.exception.custom.AlreadyParticipatedException;
import com.three.recipingeventservicebe.global.exception.custom.EventClosedException;
import com.three.recipingeventservicebe.global.exception.custom.EventNotFoundException;
import com.three.recipingeventservicebe.signupevent.domain.EventSignUpDetails;
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
    private final WebClient userClient;
    private final SignUpEventRuleFactory ruleFactory;

    public Mono<Void> participate(String eventId, String userId) {
        // 중복 참여 체크 (동기 DB 체크)
        if (participantRepository.existsByEventIdAndUserId(eventId, userId)) {
            return Mono.error(new AlreadyParticipatedException("이미 참여한 이벤트입니다."));
        }

        // 추천 룰 생성
        SignUpEventRule rule = ruleFactory.createDefaultRule();

        // 가입일 비동기 조회
        return userClient.get()
                .uri("/internal/users/{userId}", userId)
                .retrieve()
                .bodyToMono(UserInfoResponse.class)
                .map(UserInfoResponse::getCreatedAt)
                .flatMap((Instant joinedAt) -> Mono.defer(() -> {
                    if (!rule.isEligible(joinedAt)) {
                        return Mono.<Void>error(new EventClosedException("회원가입 7일 이내만 참여 가능합니다."));
                    }

        EventSignUpDetails details = signUpDetailsRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("이벤트 상세 정보를 찾을 수 없습니다."));

        // 버튼 활성화 시간 확인
        if (Instant.now().isBefore(details.getButtonActivatedAt())) {
            return Mono.error(new EventClosedException("오늘 오후 3시부터 응모 가능합니다."));
        }

        long currentCount = participantRepository.countByEventId(eventId);
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

        participantRepository.save(participant);
                    return Mono.empty();
                }));
    }
}


