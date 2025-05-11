package com.three.recipingeventservicebe.event.controller;

import com.three.recipingeventservicebe.common.dto.Response;
import com.three.recipingeventservicebe.event.dto.CreateEventRequestDto;
import com.three.recipingeventservicebe.event.dto.EventDetailResponseDto;
import com.three.recipingeventservicebe.event.service.EventCommandService;
import com.three.recipingeventservicebe.event.service.EventQueryService;
import com.three.recipingeventservicebe.global.security.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping
    public Response<String> createEvent(
            @RequestBody @Valid CreateEventRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        String eventId = eventCommandService.createEvent(requestDto, userDetails);
        return Response.ok(eventId);
    }
}
