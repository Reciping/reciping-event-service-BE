package com.three.recipingeventservicebe.event.controller;

import com.three.recipingeventservicebe.common.Response;
import com.three.recipingeventservicebe.event.dto.CreateEventRequestDto;
import com.three.recipingeventservicebe.event.dto.EventDetailResponseDto;
import com.three.recipingeventservicebe.event.dto.EventSummaryResponseDto;
import com.three.recipingeventservicebe.event.service.EventCommandService;
import com.three.recipingeventservicebe.event.service.EventQueryService;
import com.three.recipingeventservicebe.global.security.UserDetailsImpl;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RequestMapping("/api/v1/events")
@RestController
public class EventController {

    private final EventQueryService eventQueryService;
    private final EventCommandService eventCommandService;

    @GetMapping("/{eventId}")
    public Response<EventDetailResponseDto> getEventDetail(@PathVariable String eventId) {
        return Response.ok(eventQueryService.getEventDetail(eventId));
    }

    @GetMapping
    public Response<List<EventSummaryResponseDto>> getAllEventSummaries() {
        return Response.ok(eventQueryService.getAllSummaries());
    }

    @PostMapping
    public Response<String> createEvent(
            @RequestBody @Valid CreateEventRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        String eventId = eventCommandService.createEvent(requestDto, userDetails);
        return Response.ok(eventId);
    }

    @PostMapping("/with-images")
    public Response<String> createEventWithImages(
            @RequestPart("preview") MultipartFile previewImage,
            @RequestPart("main") MultipartFile mainImage,
            @RequestPart("data") @Valid CreateEventRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) throws IOException {
        String eventId = eventCommandService.createEventWithImages(requestDto, previewImage,
                mainImage, userDetails);
        return Response.ok(eventId);
    }
}
