package com.three.recipingeventservicebe.fefsevent.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FirstComeParticipationMetadata {
    private boolean winner;         // 당첨 여부 (선착순 안에 들었는지)
    private int rank;               // 몇 번째 참여자인지
    private boolean isPointGiven;   // 포인트 지급 여부
    private int rewardPoint;        // 받은 포인트 (0이면 미지급)
}
