package com.three.recipingeventservicebe.fefsevent.domain;

import jakarta.persistence.Id;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "event_first_come_details")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EventFirstComeDetails {

    @Id
    private String eventId; // Event._id 와 동일한 값으로 1:1 매핑

    private int totalWinnerCount; // 전체 선착순 발급 가능 수량

    private boolean isCombinable; // 다른 이벤트와 중복 응모 가능 여부

    private Instant buttonActivatedAt; // 버튼 활성화 시간 (프론트에서 버튼 노출 기준)

    private Reward reward; // 리워드 정보 (포인트 or 쿠폰 등)

    private Instant createdAt;
    private Instant modifiedAt;
}
