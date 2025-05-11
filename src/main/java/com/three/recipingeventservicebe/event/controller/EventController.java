package com.three.recipingeventservicebe.event.controller;

import com.three.recipingeventservicebe.common.dto.Response;
import com.three.recipingeventservicebe.event.dto.EventDetailResponseDto;
import com.three.recipingeventservicebe.event.service.EventQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/events")
@RestController
public class EventController {

    private final EventQueryService eventQueryService;

    @GetMapping("/{eventId}")
    public Response<EventDetailResponseDto> getEventDetail(@PathVariable String eventId) {
        return Response.ok(eventQueryService.getEventDetail(eventId));
    }
}
