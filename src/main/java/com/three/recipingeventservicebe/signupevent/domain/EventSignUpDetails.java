package com.three.recipingeventservicebe.signupevent.domain;

import jakarta.persistence.Id;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "event_sign_up_details")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EventSignUpDetails {

    @Id
    private String eventId;

    private int rewardPoint; // 지급할 포인트 금액 (ex: 5000)

    private int validPeriodAfterJoin;  // 회원가입 후 몇 일 이내에만 참여 가능 (ex: 7)

    private boolean isCombinable; // 다른 이벤트와 중복 참여 가능 여부

    private Instant createdAt;
    private Instant modifiedAt;
}
