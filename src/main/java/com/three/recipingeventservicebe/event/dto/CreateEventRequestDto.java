package com.three.recipingeventservicebe.event.dto;

import com.three.recipingeventservicebe.event.domain.EventImage;
import com.three.recipingeventservicebe.event.domain.EventType;
import com.three.recipingeventservicebe.event.domain.RewardType;
import com.three.recipingeventservicebe.event.domain.TriggerType;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateEventRequestDto {
    private String title;
    private EventType eventType;
    private TriggerType triggerType;
    private EventImage images;
    private Instant displayStartAt;
    private Instant displayEndAt;
    private Instant activeStartAt;
    private Instant activeEndAt;
    
    // FCFS 이벤트 관련 필드
    private Integer totalWinnerCount;
    private Boolean allowDuplicateParticipation;
    private RewardType rewardType;
    private Integer rewardValue;
}
