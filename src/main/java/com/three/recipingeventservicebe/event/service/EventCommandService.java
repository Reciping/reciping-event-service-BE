package com.three.recipingeventservicebe.event.service;

import com.three.recipingeventservicebe.event.domain.Event;
import com.three.recipingeventservicebe.event.domain.EventImage;
import com.three.recipingeventservicebe.event.domain.EventType;
import com.three.recipingeventservicebe.event.domain.ImageInfo;
import com.three.recipingeventservicebe.event.dto.CreateEventRequestDto;
import com.three.recipingeventservicebe.event.repository.EventRepository;
import com.three.recipingeventservicebe.fefsevent.domain.EventFirstComeDetails;
import com.three.recipingeventservicebe.fefsevent.domain.Reward;
import com.three.recipingeventservicebe.fefsevent.repository.EventFirstComeDetailsRepository;
import com.three.recipingeventservicebe.global.exception.custom.AccessDeniedException;
import com.three.recipingeventservicebe.global.s3.FileUploadService;
import com.three.recipingeventservicebe.global.security.UserDetailsImpl;
import java.io.IOException;
import java.time.Instant;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j(topic = "EventCommandService")
@Service
@RequiredArgsConstructor
@Transactional
public class EventCommandService {

    private final EventRepository eventRepository;
    private final EventFirstComeDetailsRepository eventFirstComeDetailsRepository;
    private final FileUploadService fileUploadService;

    public String createEvent(CreateEventRequestDto requestDto, UserDetailsImpl userDetails) {
        if (!userDetails.getRole().equals("ADMIN")) {
            throw new AccessDeniedException("이벤트 작성 권한이 없습니다.");
        }

        Event newEvent = Event.builder()
                .title(requestDto.getTitle())
                .eventType(requestDto.getEventType())
                .triggerType(requestDto.getTriggerType())
                .images(requestDto.getImages())
                .displayStartAt(requestDto.getDisplayStartAt())
                .displayEndAt(requestDto.getDisplayEndAt())
                .activeStartAt(requestDto.getActiveStartAt())
                .activeEndAt(requestDto.getActiveEndAt())
                .createdBy(userDetails.getUserId().toString())
                .createdAt(Instant.now())
                .isDeleted(false)
                .build();

        Event savedEvent = eventRepository.save(newEvent);
        String eventId = savedEvent.getId();

        // FCFS 이벤트일 경우 디테일 정보도 같이 저장
        if (requestDto.getEventType() == EventType.FCFS) {
            EventFirstComeDetails details = new EventFirstComeDetails(
                    eventId,
                    requestDto.getTotalWinnerCount(),
                    requestDto.getAllowDuplicateParticipation(), // Boolean → get으로 접근
                    requestDto.getActiveStartAt(),
                    new Reward(requestDto.getRewardType(), requestDto.getRewardValue()),
                    Instant.now(),
                    null
            );
            eventFirstComeDetailsRepository.save(details);
        }

        return eventId;
    }

    public String createEventWithImages(CreateEventRequestDto requestDto, MultipartFile previewImage, MultipartFile mainImage, UserDetailsImpl userDetails) throws IOException {
        if (!userDetails.getRole().equals("ADMIN")) {
            throw new AccessDeniedException("이벤트 작성 권한이 없습니다.");
        }

        String previewKey = generateKey(previewImage.getOriginalFilename());
        String mainKey = generateKey(mainImage.getOriginalFilename());

        String previewUrl = fileUploadService.upload(previewImage, previewKey);
        String mainUrl = fileUploadService.upload(mainImage, mainKey);

        EventImage imageInfo = new EventImage(
                new ImageInfo(previewImage.getOriginalFilename(), previewKey, previewUrl),
                new ImageInfo(mainImage.getOriginalFilename(), mainKey, mainUrl)
        );

        Event newEvent = Event.builder()
                .title(requestDto.getTitle())
                .eventType(requestDto.getEventType())
                .triggerType(requestDto.getTriggerType())
                .images(imageInfo)
                .displayStartAt(requestDto.getDisplayStartAt())
                .displayEndAt(requestDto.getDisplayEndAt())
                .activeStartAt(requestDto.getActiveStartAt())
                .activeEndAt(requestDto.getActiveEndAt())
                .createdBy(userDetails.getUserId().toString())
                .createdAt(Instant.now())
                .isDeleted(false)
                .build();

        Event savedEvent = eventRepository.save(newEvent);
        String eventId = savedEvent.getId();

        if (requestDto.getEventType() == EventType.FCFS) {
            EventFirstComeDetails details = new EventFirstComeDetails(
                    eventId,
                    requestDto.getTotalWinnerCount(),
                    requestDto.getAllowDuplicateParticipation(),
                    requestDto.getActiveStartAt(),
                    new Reward(requestDto.getRewardType(), requestDto.getRewardValue()),
                    Instant.now(),
                    null
            );
            eventFirstComeDetailsRepository.save(details);
        }

        return eventId;
    }

    private String generateKey(String filename) {
        String uuid = UUID.randomUUID().toString();
        return uuid + "-" + filename.replaceAll("\\s", "");
    }
}

