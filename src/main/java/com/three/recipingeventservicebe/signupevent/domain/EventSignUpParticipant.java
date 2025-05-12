package com.three.recipingeventservicebe.signupevent.domain;

import jakarta.persistence.Id;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "event_sign_up_participants")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventSignUpParticipant {

    @Id
    private String id; // UUID 등으로 생성

    private String eventId;

    private String userId;

    private SignUpParticipationMetadata metadata;

    private Instant joinedAt; // 참여한 시간
}
