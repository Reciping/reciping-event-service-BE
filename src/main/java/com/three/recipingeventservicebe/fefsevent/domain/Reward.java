package com.three.recipingeventservicebe.fefsevent.domain;

import com.three.recipingeventservicebe.event.domain.RewardType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Reward {
    private RewardType type; // 예: POINT, COUPON
    private int value;       // 예: 5000 포인트면 5000
}
