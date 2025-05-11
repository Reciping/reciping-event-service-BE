package com.three.recipingeventservicebe.fefsevent.domain;

import jakarta.persistence.Id;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "event_first_come_participants")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EventFirstComeParticipant {

    @Id
    private String id; // UUID 등으로 생성

    private String eventId;

    private String userId;

    private FirstComeParticipationMetadata metadata;

    private Instant joinedAt;
}
