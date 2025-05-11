package com.three.recipingeventservicebe.event.service;

import com.three.recipingeventservicebe.event.domain.Event;
import com.three.recipingeventservicebe.event.domain.EventType;
import com.three.recipingeventservicebe.event.domain.RewardType;
import com.three.recipingeventservicebe.event.dto.CreateEventRequestDto;
import com.three.recipingeventservicebe.event.repository.EventRepository;
import com.three.recipingeventservicebe.fefsevent.domain.EventFirstComeDetails;
import com.three.recipingeventservicebe.fefsevent.domain.Reward;
import com.three.recipingeventservicebe.fefsevent.repository.EventFirstComeDetailsRepository;
import com.three.recipingeventservicebe.global.exception.custom.AccessDeniedException;
import com.three.recipingeventservicebe.global.security.UserDetailsImpl;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j(topic = "EventCommandService")
@Service
@RequiredArgsConstructor
@Transactional
public class EventCommandService {

    private final EventRepository eventRepository;
    private final EventFirstComeDetailsRepository eventFirstComeDetailsRepository;

    public String createEvent(CreateEventRequestDto requestDto, UserDetailsImpl userDetails) {
        if (!userDetails.getRole().equals("ADMIN")) {
            throw new AccessDeniedException("이벤트 작성 권한이 없습니다.");
        }

        Event newEvent = Event.builder()
                .title(requestDto.getTitle())
                .eventType(requestDto.getEventType())
                .triggerType(requestDto.getTriggerType())
                .images(requestDto.getImages())
                .displayStartAt(requestDto.getDisplayStartAt())
                .displayEndAt(requestDto.getDisplayEndAt())
                .activeStartAt(requestDto.getActiveStartAt())
                .activeEndAt(requestDto.getActiveEndAt())
                .createdBy(userDetails.getUserId().toString())
                .createdAt(Instant.now())
                .isDeleted(false)
                .build();

        String eventId = eventRepository.save(newEvent).getId();

        // FCFS 이벤트일 경우 응모 수 제한 정보를 같이 저장
        if (requestDto.getEventType() == EventType.FCFS) {
            EventFirstComeDetails details = new EventFirstComeDetails(
                    eventId,
                    5, // totalWinnerCount 고정값
                    true,
                    requestDto.getActiveStartAt(),
                    new Reward(RewardType.COUPON, 0),
                    Instant.now(),
                    null
            );
            eventFirstComeDetailsRepository.save(details);
        }

        return eventId;
    }
}

