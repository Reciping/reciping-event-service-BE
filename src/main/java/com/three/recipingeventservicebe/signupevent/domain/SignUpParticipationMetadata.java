package com.three.recipingeventservicebe.signupevent.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignUpParticipationMetadata {
    private boolean isPointGiven;    // 포인트 지급 완료 여부
    private int rewardPoint;         // 실제 지급된 포인트 금액
}
