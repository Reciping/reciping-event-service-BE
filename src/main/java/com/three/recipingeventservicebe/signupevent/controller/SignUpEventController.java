package com.three.recipingeventservicebe.signupevent.controller;

import com.three.recipingeventservicebe.common.Response;
import com.three.recipingeventservicebe.global.security.UserDetailsImpl;
import com.three.recipingeventservicebe.signupevent.service.SignUpParticipationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/events/{eventId}/participate/sign-up")
public class SignUpEventController {

    private final SignUpParticipationService participationService;

    @PostMapping
    public Mono<Response<String>> participate(
            @PathVariable String eventId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return participationService.participate(eventId, String.valueOf(userDetails.getUserId()))
                .thenReturn(Response.ok("회원가입 이벤트 응모가 완료되었습니다."));
    }
}

