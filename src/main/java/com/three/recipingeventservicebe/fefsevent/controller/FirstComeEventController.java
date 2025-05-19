package com.three.recipingeventservicebe.fefsevent.controller;

import com.three.recipingeventservicebe.common.Response;
import com.three.recipingeventservicebe.fefsevent.service.FirstComeParticipationService;
import com.three.recipingeventservicebe.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/events/{eventId}/participate/first-come")
public class FirstComeEventController {

    private final FirstComeParticipationService participationService;

    @PostMapping
    public Response<String> participate(
            @PathVariable String eventId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        participationService.participate(eventId, String.valueOf(userDetails.getUserId()));
        return Response.ok("선착순 이벤트에 응모되었습니다.");
    }
}

