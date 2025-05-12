package com.three.recipingeventservicebe.signupevent.service;

import java.time.Duration;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SignUpEventRule {
    private final int winnerLimit;
    private final int rewardPoint;
    private final int validDays;

    public boolean isEligible(Instant joinedAt) {
        return joinedAt != null &&
                joinedAt.plus(Duration.ofDays(validDays)).isAfter(Instant.now());
    }

    public boolean isWinner(long currentCount) {
        return currentCount < winnerLimit;
    }

    public int getRewardForWinner(boolean isWinner) {
        return isWinner ? rewardPoint : 0;
    }
}

