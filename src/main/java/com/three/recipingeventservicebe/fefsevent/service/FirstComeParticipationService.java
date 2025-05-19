package com.three.recipingeventservicebe.fefsevent.service;

import com.three.recipingeventservicebe.event.domain.Event;
import com.three.recipingeventservicebe.event.repository.EventRepository;
import com.three.recipingeventservicebe.fefsevent.domain.EventFirstComeDetails;
import com.three.recipingeventservicebe.fefsevent.domain.EventFirstComeParticipant;
import com.three.recipingeventservicebe.fefsevent.domain.FirstComeParticipationMetadata;
import com.three.recipingeventservicebe.fefsevent.repository.EventFirstComeDetailsRepository;
import com.three.recipingeventservicebe.fefsevent.repository.EventFirstComeParticipantRepository;
import com.three.recipingeventservicebe.global.aop.DistributedLock;
import com.three.recipingeventservicebe.global.exception.custom.AlreadyParticipatedException;
import com.three.recipingeventservicebe.global.exception.custom.EventClosedException;
import com.three.recipingeventservicebe.global.exception.custom.EventNotFoundException;
import java.time.Instant;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j(topic = "FirstComeParticipationService")
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FirstComeParticipationService {

    private final EventFirstComeDetailsRepository detailsRepository;
    private final EventFirstComeParticipantRepository participantRepository;
    private final EventRepository eventRepository;

    /**
     * 선착순 이벤트 참여 처리
     * - 분산 락으로 중복 응모 및 임계 구간 보호
     */
    @DistributedLock(prefix = "event:fc", key = "#eventId", waitTime = 5000)
    public void participate(String eventId, String userId) {
        // 중복 참여 여부 확인
        if (participantRepository.existsByEventIdAndUserId(eventId, userId)) {
            throw new AlreadyParticipatedException("이미 참여한 이벤트입니다.");
        }

        // 이벤트 존재 여부 및 참여 가능 시간 확인
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("이벤트를 찾을 수 없습니다."));

        Instant now = Instant.now();
        if (now.isBefore(event.getActiveStartAt()) || now.isAfter(event.getActiveEndAt())) {
            throw new EventClosedException("이벤트 시간이 아닙니다.");
        }

        // 선착순 정보 조회
        EventFirstComeDetails details = detailsRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("상세 정보를 찾을 수 없습니다."));

        // 현재 참여자 수 확인 및 마감 여부 판단
        long currentCount = participantRepository.countByEventId(eventId);
        if (currentCount >= details.getTotalWinnerCount()) {
            throw new EventClosedException("오늘의 선착순 이벤트는 마감되었습니다.");
        }

        // 참여자 데이터 저장
        EventFirstComeParticipant participant = EventFirstComeParticipant.builder()
                .id(UUID.randomUUID().toString())
                .eventId(eventId)
                .userId(userId)
                .joinedAt(now)
                .metadata(FirstComeParticipationMetadata.builder()
                        .winner(currentCount < details.getTotalWinnerCount())
                        .rank((int) currentCount + 1)
                        .isRewardIssued(false)
                        .rewardInfo(details.getReward().getValue())
                        .build())
                .build();

        participantRepository.save(participant);

        log.info("✅ 선착순 이벤트 참여 성공 - userId: {}, eventId: {}, rank: {}", userId, eventId, currentCount + 1);
    }
}
