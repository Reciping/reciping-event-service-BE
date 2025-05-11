package com.three.recipingeventservicebe.event.service;

import com.three.recipingeventservicebe.event.domain.Event;
import com.three.recipingeventservicebe.event.domain.EventType;
import com.three.recipingeventservicebe.event.repository.EventRepository;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j(topic = "EventScheduler")
@Component
@RequiredArgsConstructor
public class EventScheduler {

    private final EventRepository eventRepository;

    @Scheduled(cron = "0 0 13 * * *", zone = "Asia/Seoul") // 매일 오후 1시
    public void activateDailyEvent() {
        log.info("🕐 매일 오후 1시 - 선착순 이벤트 시작 스케줄 실행");

        // 오늘 날짜로 활성화할 이벤트 조회
        List<Event> events = eventRepository.findAll().stream()
                .filter(event -> event.getEventType() == EventType.FCFS)
                .filter(event -> !event.isDeleted())
                .filter(event -> isToday(event.getActiveStartAt())) // 오늘 이벤트만
                .toList();

        for (Event event : events) {
            log.info("✔️ 오늘 활성화된 이벤트: {}", event.getId());
            // 필요한 활성화 로직이 있다면 여기에 작성 (예: 상태변경, 캐시등록 등)
        }
    }

    private boolean isToday(Instant instant) {
        ZonedDateTime seoulTime = instant.atZone(ZoneId.of("Asia/Seoul"));
        LocalDate today = LocalDate.now(ZoneId.of("Asia/Seoul"));
        return seoulTime.toLocalDate().isEqual(today);
    }
}
