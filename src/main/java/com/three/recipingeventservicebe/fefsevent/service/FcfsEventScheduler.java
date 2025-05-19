package com.three.recipingeventservicebe.fefsevent.service;

import com.three.recipingeventservicebe.event.domain.Event;
import com.three.recipingeventservicebe.event.domain.EventType;
import com.three.recipingeventservicebe.event.repository.EventRepository;
import com.three.recipingeventservicebe.fefsevent.domain.EventFirstComeDetails;
import com.three.recipingeventservicebe.fefsevent.repository.EventFirstComeDetailsRepository;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j(topic = "FcfsEventScheduler")
@Component
@RequiredArgsConstructor
public class FcfsEventScheduler {

    private final EventFirstComeDetailsRepository detailsRepository;

    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul") // 매일 자정에 5시 시간값 갱신
    public void updateButtonActivationTime() {
        Instant today5PM = ZonedDateTime.now(ZoneId.of("Asia/Seoul"))
                .withHour(17).withMinute(0).withSecond(0).withNano(0)
                .toInstant();

        List<EventFirstComeDetails> detailsList = detailsRepository.findAll();

        for (EventFirstComeDetails details : detailsList) {
            details.setButtonActivatedAt(today5PM);
        }

        detailsRepository.saveAll(detailsList);
        log.info("[스케줄러] FCFS 버튼 시간 갱신 완료: {}", today5PM);
    }
}
