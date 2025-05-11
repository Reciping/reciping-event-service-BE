package com.three.recipingeventservicebe.fefsevent.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FirstComeParticipationMetadata {
    private boolean winner;         // 당첨 여부 (선착순 안에 들었는지)
    private int rank;               // 몇 번째 참여자인지
    private boolean isRewardIssued;   // 리워드 지급 여부 (포인트 or 쿠폰)
    private int rewardInfo;        // 리워드 정보 (ex: coupon, point 등)
}
