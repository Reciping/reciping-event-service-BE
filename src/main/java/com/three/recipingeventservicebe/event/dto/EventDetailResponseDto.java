package com.three.recipingeventservicebe.event.dto;

import com.three.recipingeventservicebe.event.domain.EventType;
import com.three.recipingeventservicebe.event.domain.ImageInfo;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EventDetailResponseDto {
    private String id;
    private String title;
    private EventType eventType;
    private ImageInfo mainImage;
    private Instant activeStartAt;
    private Instant activeEndAt;
    private Instant displayStartAt;
    private Instant displayEndAt;
}

