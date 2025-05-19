package com.three.recipingeventservicebe.event.domain;

import jakarta.persistence.Id;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "events")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Event {

    @Id
    private String id;

    private String title;

    private EventType eventType;  // FCFS(First Come-First Served), SIGN_UP 등

    private TriggerType triggerType;  // TIME_BASED, ACTION_BASED 등

    private EventImage images;  // preview + main 이미지 정보

    private Instant displayStartAt;  // 화면에 노출 시작 시간
    private Instant displayEndAt;    // 화면에 노출 종료 시간

    private Instant activeStartAt;   // 실제 이벤트 로직 적용 시작 시간
    private Instant activeEndAt;     // 실제 이벤트 로직 적용 종료 시간

    private String createdBy; // 생성자 아이디
    private Instant createdAt;

    private String modifiedBy; // 수정자 아이디
    private Instant modifiedAt;

    private Instant deletedAt;
    private boolean isDeleted;

    public Instant getActiveStartAt() {
        return activeStartAt;
    }

    public Instant getActiveEndAt() {
        return activeEndAt;
    }
}


