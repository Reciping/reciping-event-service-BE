package com.three.recipingeventservicebe.fefsevent.service;

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

    @DistributedLock(prefix = "event:fc", key = "#eventId.toString()")
    public void participate(String eventId, String userId) {
        // 중복 참여 확인
        if (participantRepository.existsByEventIdAndUserId(eventId, userId)) {
            throw new AlreadyParticipatedException("이벤트를 이미 참여하셨습니다.");
        }

        // 이벤트 상세 정보 조회
        EventFirstComeDetails details = detailsRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("해당 이벤트를 찾을 수 없습니다."));

        // 현재 참여자 수 확인
        long currentCount = participantRepository.countByEventId(eventId);

        // 마감 여부 판단
        if (currentCount >= details.getTotalWinnerCount()) {
            throw new EventClosedException("오늘의 선착순 이벤트는 마갑되었습니다.");
        }

        // 참여 이력 저장
        EventFirstComeParticipant participant = EventFirstComeParticipant.builder()
                .id(UUID.randomUUID().toString())
                .eventId(eventId)
                .userId(userId)
                .joinedAt(Instant.now())
                .metadata(FirstComeParticipationMetadata.builder()
                        .winner(true)
                        .rank((int) currentCount + 1)
                        .isRewardIssued(true) // 쿠폰 발급 완료 여부 의미
                        .rewardInfo(0)
                        .build())
                .build();

        participantRepository.save(participant);
    }

    public boolean hasParticipated(String eventId, String userId) {
        return participantRepository.existsByEventIdAndUserId(eventId, userId);
    }
}