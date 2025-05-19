package com.three.recipingeventservicebe.event.service;

import com.three.recipingeventservicebe.event.domain.Event;
import com.three.recipingeventservicebe.event.dto.EventDetailResponseDto;
import com.three.recipingeventservicebe.event.dto.EventSummaryResponseDto;
import com.three.recipingeventservicebe.event.repository.EventRepository;
import com.three.recipingeventservicebe.global.exception.custom.EventClosedException;
import java.time.Instant;
import java.util.List;
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

    // 진행 중인 이벤트 요약 목록 조회
    public List<EventSummaryResponseDto> getAllSummaries() {
        return eventRepository.findAll().stream()
                .filter(event -> !event.isDeleted() && event.getDeletedAt() == null)
                .filter(this::isInDisplayPeriod)
                .map(event -> new EventSummaryResponseDto(
                        event.getId(),
                        event.getTitle(),
                        event.getImages().getPreview()
                ))
                .toList();
    }

    // 진행 중인 이벤트 상세 조회
    public EventDetailResponseDto getEventDetail(String eventId) {
        Event event = eventRepository.findById(eventId)
                .filter(this::isInDisplayPeriod)
                .orElseThrow(() -> new EventClosedException("현재 열람할 수 없는 이벤트입니다."));

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

    private boolean isInDisplayPeriod(Event event) {
        Instant now = Instant.now();
        return !now.isBefore(event.getDisplayStartAt()) && !now.isAfter(event.getDisplayEndAt());
    }
}

