package com.three.recipingeventservicebe.event.service;

import com.three.recipingeventservicebe.event.domain.Event;
import com.three.recipingeventservicebe.event.dto.CreateEventRequestDto;
import com.three.recipingeventservicebe.event.repository.EventRepository;
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

        return eventRepository.save(newEvent).getId();
    }
}
