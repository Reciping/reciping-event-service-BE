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
    public void updateButtonActivationTime() {
        Instant today7PM = ZonedDateTime.now(ZoneId.of("Asia/Seoul"))
                .withHour(19).withMinute(0).withSecond(0).withNano(0)
                .toInstant();

        signUpDetailsRepository.findAll()
                .filter(detail -> !Boolean.TRUE.equals(detail.isDeleted()))
                .map(detail -> {
                    detail.setButtonActivatedAt(today7PM);
                    return detail;
                })
                .collectList()
                .flatMapMany(signUpDetailsRepository::saveAll)
                .then()
                .doOnSuccess(unused -> log.info("[스케줄러] SignUp 버튼 시간 갱신 완료: {}", today7PM))
                .subscribe();
    }
}


