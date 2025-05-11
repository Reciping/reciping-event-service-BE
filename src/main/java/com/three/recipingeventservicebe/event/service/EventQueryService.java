package com.three.recipingeventservicebe.event.service;

import com.three.recipingeventservicebe.event.domain.Event;
import com.three.recipingeventservicebe.event.dto.EventDetailResponseDto;
import com.three.recipingeventservicebe.event.repository.EventRepository;
import com.three.recipingeventservicebe.global.exception.custom.EventNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j(topic = "EventQueryService")
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventQueryService {

    private final EventRepository eventRepository;

    public EventDetailResponseDto getEventDetail(String eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("해당 이벤트를 찾을 수 없습니다."));

        return new EventDetailResponseDto(
                event.getId(),
                event.getTitle(),
                event.getEventType(),
                event.getImages().getMain(),
                event.getActiveStartAt(),
                event.getActiveEndAt(),
                event.getDisplayStartAt(),
                event.getDisplayEndAt()
        );
    }
}

