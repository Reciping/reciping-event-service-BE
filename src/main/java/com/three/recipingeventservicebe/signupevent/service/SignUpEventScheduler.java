package com.three.recipingeventservicebe.signupevent.service;

import com.three.recipingeventservicebe.signupevent.domain.EventSignUpDetails;
import com.three.recipingeventservicebe.signupevent.repository.EventSignUpDetailsRepository;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j(topic = "SignUpEventScheduler")
@Component
@RequiredArgsConstructor
public class SignUpEventScheduler {

    private final EventSignUpDetailsRepository signUpDetailsRepository;

    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul") // 매일 자정
    public void updateActivationTime() {
        Instant today3PM = ZonedDateTime.now(ZoneId.of("Asia/Seoul"))
                .withHour(15).withMinute(0).withSecond(0).withNano(0)
                .toInstant();

        List<EventSignUpDetails> detailsList = signUpDetailsRepository.findAll().stream()
                .filter(details -> !details.isDeleted())
                .toList();

        for (EventSignUpDetails detail : detailsList) {
            detail.setButtonActivatedAt(today3PM);
        }

        signUpDetailsRepository.saveAll(detailsList);
        log.info("[스케줄러] 회원가입 이벤트 버튼 활성화 시간 갱신 완료 ({}에 활성화)", today3PM);
    }
}
